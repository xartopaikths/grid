package io.greatbone.web;

import io.greatbone.grid.GridData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

    // standard HTTP date format
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    static {
        TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");
        DATE_FORMAT.setTimeZone(GMT_ZONE);
    }

    final WebHost host;

    final ChannelHandlerContext chctx;

    final FullHttpRequest req;

    DefaultFullHttpResponse resp;

    // request headers
    final HttpHeaders requesth;

    WebPrincipal principal;

    Z zone;

    WebService<Z> service;

    // converted request content: form-data, json-deserilized and input stream
    Object content;

    WebContext(WebHost host, ChannelHandlerContext chctx, FullHttpRequest req) {
        this.host = host;
        this.chctx = chctx;
        this.req = req;
        this.requesth = req.headers();
    }

    public HttpMethod method() {
        return req.method();
    }

    public WebService service() {
        return service;
    }

    public Z zone() {
        return zone;
    }

    String authorization() {
        return requesth.get(HttpHeaderNames.AUTHORIZATION);
    }

    public String hstring(String name) {
        return requesth.get(name);
    }

    public int hint(String header) {
        String v = requesth.get(header);
        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
        }
        return 0;
    }

    public Date hdate(String name) {
        String v = requesth.get(name);
        if (v != null) {
            try {
                return DATE_FORMAT.parse(v);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    //
    // REQUEST CONTENT

    public <T extends GridData> T content(T obj) {
        return null;
    }


    public WebContext $obj(GridData dat) {

        return this;
    }

    public WebContext $obj(GridData dat, int flags) {

        return this;
    }

    public WebContext $obj(List<GridData> data) {

        return this;
    }

    public WebContext $obj(List<GridData> datalst, int flags) {

        return this;
    }

    public void sendNotFound() throws IOException {
    }

    PooledByteBufAllocator pool;


    public void sendOK(ByteBuf buf) throws IOException {
        HttpHeaders headers = new DefaultHttpHeaders();
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf, headers, new DefaultHttpHeaders());
        chctx.writeAndFlush(resp);
    }

    public void sendOK(WebPrint print) throws IOException {

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
    }

}
