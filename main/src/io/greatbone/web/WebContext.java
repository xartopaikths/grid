package io.greatbone.web;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
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
    final HttpHeaders rqheaders;
    final ByteBuf rqcontent;
    Object rqbody; // parsed request content

    // response
    HttpResponseStatus status;
    HttpHeaders rpheaders;
    ByteBuf rpcontent;
    HttpHeaders trailingHeader;

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


        this.rqheaders = req.headers();
        this.rqcontent = req.content();

        this.status = HttpResponseStatus.OK;
        this.rpcontent = POOL.buffer();
        this.rpheaders = new DefaultHttpHeaders();


    }

    public final WebService service() {
        return service;
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

    public final String uri() {
        return uri;
    }

    public final String path() {
        return path;
    }

    public final FormData query() {
        return query;
    }

    public final HttpHeaders rqheaders() {
        return rqheaders;
    }

    public Date hdate(String name) {
        String v = rqheaders.get(name);
        if (v != null) {
            try {
                return DATE_FORMAT.parse(v);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    public FormData getForm() {
        if (rqbody == null) {
            rqbody = new FormData(rqcontent);
        }
        if (rqbody instanceof FormData) {
            return (FormData) rqbody;
        }
        return null;
    }

    public Object getJson() {
        return null;
    }

    public ByteBuf rqbody() {
        return rqcontent;
    }

    //
    // RESPONSE

    public void status(HttpResponseStatus v) {
        this.status = v;
    }

    public void header(CharSequence name, String v) {

    }

    public final void print(WebView print) {
        print.print();

        ByteBuf buf = print.buf;
        rpheaders.set(HttpHeaderNames.CONTENT_TYPE, print.ctype());
        rpheaders.set(HttpHeaderNames.CONTENT_LENGTH, buf == null ? 0 : buf.readableBytes());

        this.rpcontent = print.buf;
    }

    public void sendNotFound() throws IOException {
    }

    PooledByteBufAllocator pool;

    public void sendOK(WebView print) throws IOException {

        // print content to the out
        print.print();

        HttpHeaders headers = new DefaultHttpHeaders();
        headers.set(HttpHeaderNames.CONTENT_TYPE, print.ctype());
        ByteBuf buf = print.buf;
        headers.set(HttpHeaderNames.CONTENT_LENGTH, buf == null ? 0 : buf.readableBytes());

        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf, headers, new DefaultHttpHeaders());
        chctx.writeAndFlush(resp);
    }

    @Override
    public void close() throws IOException {
        // send out the repsonse
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, rpcontent, rpheaders, EMPTY);
        chctx.writeAndFlush(resp);
    }

}
