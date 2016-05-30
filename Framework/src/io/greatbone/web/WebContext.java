package io.greatbone.web;

import io.greatbone.Out;
import io.greatbone.Printer;
import io.greatbone.grid.GridData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
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
 */
public class WebContext implements Out<WebContext>, AutoCloseable {

    static final Charset UTF_8 = Charset.forName("UTF-8");

    static final int MAX_BODY = 64 * 1024;

    static final int OUT_INITIAL = 4 * 1024;

    // standard HTTP date format
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    static {
        TimeZone GMT_ZONE = TimeZone.getTimeZone("GMT");
        DATE_FORMAT.setTimeZone(GMT_ZONE);
    }

    // possible chars for representing a number
    static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    static final char MINUS = '-';

    static final short[] SHORTS = {10, 100, 1000, 10000};

    static final int[] INTS = {10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 1000000000};

    static final long[] LONGS = {10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000, 1000000000, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 100000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L};

    final WebHostActivity host;

    // the underlying exchange impl
    final FullHttpRequest request;

    final FullHttpResponse response;

    // request headers
    final HttpHeaders requesth;

    WebPrincipal principal;

    // response headers
    HttpHeaders responseh;

    String space;

    WebActivity control;

    String resource;

    // converted request content: form-data, json-deserilized and input stream
    Object content;

    // the underlying blocking I/O output buffer
    ByteBuf out;

    WebContext(WebHostActivity host, FullHttpRequest request) {
        this.host = host;
        this.request = request;
        this.requesth = request.headers();
        this.response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    }

    public HttpMethod method() {
        return request.method();
    }

    public String space() {
        return space;
    }

    public WebActivity control() {
        return control;
    }

    public String resource() {
        return resource;
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


    //
    // OUTPUT FUNCTIONS

    void write(char c) throws IOException {
        if (out == null) {
            out = PooledByteBufAllocator.DEFAULT.buffer();
        }

        // UTF-8 encoding but without surrogate support
        if (c < 0x80) {
            // have at most seven bits
            out.writeByte((byte) c);
        } else if (c < 0x800) {
            // 2 text, 11 bits
            out.writeByte((byte) (0xc0 | (c >> 6)));
            out.writeByte((byte) (0x80 | (c & 0x3f)));
        } else {
            // 3 text, 16 bits
            out.writeByte((byte) (0xe0 | ((c >> 12))));
            out.writeByte((byte) (0x80 | ((c >> 6) & 0x3f)));
            out.writeByte((byte) (0x80 | (c & 0x3f)));
        }
    }

    public WebContext $(CharSequence v) throws IOException {
        if (v != null) {
            for (int i = 0; i < v.length(); i++) {
                write(v.charAt(i));
            }
        }
        return this;
    }

    @Override
    public WebContext $(boolean v) throws IOException {
        return this;
    }

    public WebContext $(short v) throws IOException {
        int x = v;
        if (v < 0) {
            write(MINUS);
            x = -v;
        }
        boolean begun = false;
        for (int i = SHORTS.length - 1; i >= 0; i--) {
            short base = SHORTS[i];
            int q = x / base;
            x = x % base;
            if (q != 0 || begun) {
                write(DIGITS[q]);
                begun = true;
            }
        }
        write(DIGITS[x]); // last reminder
        return this;
    }

    public WebContext $(int v) throws IOException {
        int x = v;
        if (v < 0) {
            write(MINUS);
            x = -v;
        }
        boolean begun = false;
        for (int i = INTS.length - 1; i >= 0; i--) {
            int base = INTS[i];
            int q = x / base;
            x = x % base;
            if (q != 0 || begun) {
                write(DIGITS[q]);
                begun = true;
            }
        }
        write(DIGITS[x]); // last reminder
        return this;
    }

    public WebContext $(long v) throws IOException {
        long x = v;
        if (v < 0) {
            write(MINUS);
            x = -v;
        }
        boolean begun = false;
        for (int i = LONGS.length - 1; i >= 0; i--) {
            long base = LONGS[i];
            int q = (int) (x / base);
            x = x % base;
            if (q != 0 || begun) {
                write(DIGITS[q]);
                begun = true;
            }
        }
        write(DIGITS[(int) x]); // last reminder
        return this;
    }

    public WebContext $pair(String name, String value) throws IOException {
        write('"');
        $(name);
        write('"');
        write(':');
        if (value == null) {
            $("null");
        } else {
            write('"');
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                switch (c) {
                    case '"':
                        write('\\');
                        write('"');
                        break;
                    case '\\':
                        write('\\');
                        write('\\');
                        break;
                    case '/':
                        write('\\');
                        write('/');
                        break;
                    case '\b':
                        write('\\');
                        write('b');
                        break;
                    case '\f':
                        write('\\');
                        write('f');
                        break;
                    case '\n':
                        write('\\');
                        write('n');
                        break;
                    case '\r':
                        write('\\');
                        write('r');
                        break;
                    case '\t':
                        write('\\');
                        write('t');
                        break;
                    default:
                        write(c);
                }
            }
            write('"');
        }
        return this;
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

    public void sendOK(Printer printer) throws IOException {
//        request.setStatusCode(200);
//        if (printer instanceof WebView) {
//            WebView view = (WebView) printer;
//            view.wc = this;
//            responseh.put(CONTENT_TYPE, view.ctype());
//        } else {
//            responseh.put(CONTENT_TYPE, "text/plain");
//        }
//
        // print out content
        printer.print(this);

    }

    @Override
    public void close() throws IOException {
    }

}
