package io.greatbone.web;

import io.greatbone.grid.GridData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * A web request/response exchange.
 *
 * @param <Z> the resolved working zone (if any)
 */
public class WebContext<Z extends WebZone> implements AutoCloseable {

    static final Charset UTF_8 = Charset.forName("UTF-8");

    static final int MAX_BODY = 64 * 1024;

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
    Map<String, List<String>> query;
    final HttpHeaders hsreq;
    final ByteBuf icontent;
    Map<String, List<String>> form;

    // response
    HttpResponseStatus status;
    HttpHeaders oheaders;
    ByteBuf ocontent;
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
        this.uri = req.uri();
        this.hsreq = req.headers();
        this.icontent = req.content();

        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        query = decoder.parameters();

        status = HttpResponseStatus.OK;

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

    public final String Uri() {
        return uri;
    }

    public final HttpHeaders hsreq() {
        return hsreq;
    }

    public String hstring(String name) {
        return hsreq.get(name);
    }

    public int hint(String header) {
        String v = hsreq.get(header);
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public Date hdate(String name) {
        String v = hsreq.get(name);
        if (v != null) {
            try {
                return DATE_FORMAT.parse(v);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    public String querystr(String name) {
        if (query == null) {
            QueryStringDecoder decoder = new QueryStringDecoder(uri);
            query = decoder.parameters();
        }
        List<String> v = query.get(name);
        if (v != null) {
            return v.get(0);
        }
        return null;
    }

    public List<String> querystrs(String name) {
        return query.get(name);
    }

    public String getParameter(String name) {
        if (form == null) {
            QueryStringDecoder decoder = new QueryStringDecoder("?" + icontent.toString(CharsetUtil.UTF_8));
            form = decoder.parameters();
        }
        return null;
    }

    public void json() {
        JsonObjectDecoder d = new JsonObjectDecoder();
    }

    //
    // REQUEST CONTENT

    public <T extends GridData> T content(T obj) {
        return null;
    }


    public void status(HttpResponseStatus v) {
        this.status = v;
    }

    public void header(CharSequence name, String v) {

    }

    public final void print(WebView print) {
        print.print();

        ByteBuf buf = print.buf;
        oheaders.set(HttpHeaderNames.CONTENT_TYPE, print.ctype());
        oheaders.set(HttpHeaderNames.CONTENT_LENGTH, buf == null ? 0 : buf.readableBytes());

        this.ocontent = print.buf;
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
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, ocontent, oheaders, new DefaultHttpHeaders());
        chctx.writeAndFlush(resp);
    }

}
