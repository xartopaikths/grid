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

    final HttpVersion version;

    // request
    final HttpMethod method;
    final String uri;
    final String path;
    final FormData query; // can be null
    final HttpHeaders inheaders;
    final ByteBuf inbuf;
    transient Object inbody; // parsed request content

    // response
    HttpResponseStatus status;
    HttpHeaders outheaders;
    ByteBuf outbuf;
    transient Object outbody; // content for output

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

        this.inheaders = req.headers();
        this.inbuf = req.content();

        this.status = HttpResponseStatus.OK;
        this.outheaders = new DefaultHttpHeaders();

    }

    public WebService service() {
        return service;
    }

    public WebAction action() {
        return action;
    }

    public Z zone() {
        return zone;
    }

    public WebPrincipal principal() {
        return principal;
    }

    public HttpMethod method() {
        return method;
    }

    public String uri() {
        return uri;
    }

    public String path() {
        return path;
    }

    public FormData query() {
        return query;
    }

    public HttpHeaders inheaders() {
        return inheaders;
    }

    public Date hdate(String name) {
        String v = inheaders.get(name);
        if (v != null) {
            try {
                return DATE_FORMAT.parse(v);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    public ByteBuf getInBuf() {
        return inbuf;
    }

    public FormData getInFormData() {
        if (inbody == null) {
            inbody = new FormData(inbuf);
        }
        if (inbody instanceof FormData) {
            return (FormData) inbody;
        }
        return null;
    }

    public Object getInJson() {
        return inbuf;
    }


    //
    // RESPONSE

    public void setStatus(HttpResponseStatus v) {
        this.status = v;
    }

    public HttpHeaders outheaders() {
        return outheaders;
    }

    public void setOut(WebView view) {

        // render output content
        view.render();

        ByteBuf buf = view.buf;
        outheaders.set(HttpHeaderNames.CONTENT_TYPE, view.ctype());
        outheaders.set(HttpHeaderNames.CONTENT_LENGTH, buf == null ? 0 : buf.readableBytes());

        if (outbuf != null) {
            outbuf.release();
        }
        this.outbuf = view.buf;
    }

    public void setOut(ByteBuf buf) {

    }

    @Override
    public void close() throws IOException {
        // send out the repsonse
        if (outbuf == null) {
            outbuf = Unpooled.buffer(0);
        }

        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, outbuf, outheaders, EMPTY);
        chctx.writeAndFlush(resp);
    }

}
