package io.greatbone.web;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A web request/response exchange.
 *
 * @param <Z> the resolved working zone (if any)
 */
public class WebContext<Z extends WebZone> implements AutoCloseable {

    static final Charset UTF_8 = Charset.forName("UTF-8");

    static final int MAX_BODY = 64 * 1024;

    static final HttpHeaders EMPTY = new DefaultHttpHeaders();

    static final PooledByteBufAllocator POOL = new PooledByteBufAllocator();

    // standard HTTP date format
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    static {
        TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");
        DATE_FORMAT.setTimeZone(GMT_ZONE);
    }

    final ChannelHandlerContext chctx;

    HttpVersion version;

    // request
    final HttpMethod method;
    final String uri;
    final String path;
    final FormData query; // can be null
    final HttpHeaders reqheaders;
    final ByteBuf reqcontent;
    Object reqbody; // parsed request content

    // response
    HttpResponseStatus status;
    HttpHeaders respheaders;
    ByteBuf respcontent;

    // the authenticated principal
    WebPrincipal principal;

    // the resolved web zone, can be null
    Z zone;

    // the currently targetd service
    WebService<Z> service;

    // the currently targeted action
    WebAction action;

    WebContext(ChannelHandlerContext chctx, FullHttpRequest req) {
        this.chctx = chctx;

        this.version = req.protocolVersion();
        this.method = req.method();

        // parse the uri
        final String uri = this.uri = req.uri();
        int quest = uri.indexOf('?');
        this.path = (quest == -1) ? uri : uri.substring(0, quest);
        this.query = (quest == -1) ? null : new FormData(uri.substring(quest + 1));

        this.reqheaders = req.headers();
        this.reqcontent = req.content();

        this.status = HttpResponseStatus.OK;
        this.respheaders = new DefaultHttpHeaders();

    }

    public final WebService service() {
        return service;
    }

    public final WebAction action() {
        return action;
    }

    public final Z zone() {
        return zone;
    }

    public final WebPrincipal principal() {
        return principal;
    }

    public final HttpMethod method() {
        return method;
    }

    public final String getUri() {
        return uri;
    }

    public final String getPath() {
        return path;
    }

    public final FormData getQuery() {
        return query;
    }

    public final HttpHeaders getRequestHeaders() {
        return reqheaders;
    }

    public Date hdate(String name) {
        String v = reqheaders.get(name);
        if (v != null) {
            try {
                return DATE_FORMAT.parse(v);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <C> C getRequestContent(Class<C> contentClass) {
        if (contentClass == FormData.class) {
            if (reqbody == null) {
                reqbody = new FormData(reqcontent);
            }
            if (reqbody instanceof FormData) {
                return (C) reqbody;
            }
        } else if (contentClass == ByteBuf.class) {
            return (C) reqcontent;
        }
        return null;
    }

    //
    // RESPONSE

    public void setStatus(HttpResponseStatus v) {
        this.status = v;
    }

    public final void setResponseContent(WebView view) {

        // render output content
        view.render();

        ByteBuf buf = view.buf;
        respheaders.set(HttpHeaderNames.CONTENT_TYPE, view.ctype());
        respheaders.set(HttpHeaderNames.CONTENT_LENGTH, buf == null ? 0 : buf.readableBytes());

        if (respcontent != null) {
            respcontent.release();
        }
        this.respcontent = view.buf;
    }

    @Override
    public void close() throws IOException {
        // send out the repsonse
        if (respcontent == null) {
            respcontent = Unpooled.buffer(0);
        }

        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, respcontent, respheaders, EMPTY);
        chctx.writeAndFlush(resp);
    }

}
