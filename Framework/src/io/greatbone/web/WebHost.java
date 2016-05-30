package io.greatbone.web;

import io.greatbone.Configurable;
import io.greatbone.Greatbone;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.util.ReferenceCountUtil;
import org.w3c.dom.Element;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetSocketAddress;
import java.util.Base64;

/**
 * A root web folder that may have a hub handler which deals with variable sector folders.
 */
public abstract class WebHost extends WebParentActivity implements ChannelInboundHandler, WebHostMBean, Configurable {

    static final String EMPTY = "";

    static final Base64.Decoder DEC = Base64.getMimeDecoder();

    static final Base64.Encoder ENC = Base64.getMimeEncoder();

    final WebUtility web;

    final Element config;

    final String name;

    final InetSocketAddress address;

    // the server socket channel
    volatile Channel serverchan;

    boolean ssl;

    protected WebHost(WebUtility web, String name) {
        super(null, null);
        this.web = web;
        this.name = name;

        // register as mbean
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName objname = new ObjectName("web:type=Host");
            mbs.registerMBean(this, objname);
        } catch (Exception e) {
        }

        // get address settings from configuration, can be null if no configuration for the host is found
        this.config = Greatbone.getXmlSubElement(web.config, "vhost", name);
        if (config != null) {
            String bind = config.getAttribute("bind");
            int colon = bind.indexOf(':');
            String hostname = colon == -1 ? bind : bind.substring(0, colon);
            int port = colon == -1 ? 80 : Integer.parseInt(bind.substring(colon + 1));
            this.address = new InetSocketAddress(hostname, port);
        } else {
            this.address = null;
        }
    }

    public final String name() {
        return name;
    }

    /**
     * Starts the service with the specified executor. A same executor can be shared among multiple services.
     */
    public void start() throws IOException {
        if (address == null) {
            return;
        }

        final WebHost host = this;
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(Greatbone.BOSS, Greatbone.WORK)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(host);
                            pipeline.addLast(new WebSocketServerCompressionHandler());
                            pipeline.addLast(new WebSocketServerProtocolHandler("", null, true));
                        }
                    })
            ;
            serverchan = b.bind(address).channel();
            serverchan.closeFuture();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        if (address == null) {
            return;
        }
        if (serverchan.isOpen()) {
            serverchan.close();
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean release = true;
        try {
            if (msg instanceof FullHttpRequest) {
                handleRequest((FullHttpRequest) msg);
            } else {
                release = false;
                ctx.fireChannelRead(msg);
            }
        } finally {
            if (release) {
                ReferenceCountUtil.release(msg);
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }

    public void handleRequest(FullHttpRequest exch) throws Exception {

        // handle static resources in a IO thread
        String path = exch.uri();
        String base = path.substring(1);
        int dot = base.lastIndexOf('.');
        if (dot != -1) {
            WebStatic sta = web.getStatic(path);
            handleStatic(sta, exch);
            return;
        }

        try (WebContext wc = new WebContext(this, exch)) {
            // BASIC authentication from client
            authenticate(wc);

            perform(base, wc);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    void authenticate(WebContext wc) {
        String v = wc.authorization();
        if (v == null) {
            return;
        }
        if (!v.startsWith("Basic ")) {

        }

        int len = v.length() - 6;
        byte[] ba = new byte[len];
        for (int i = 0; i < len; i++) { // a quick copy of bytes
            ba[i] = (byte) v.charAt(6 + i);
        }
        byte[] ret = DEC.decode(ba);

        int p = 0;
        while (ret[p++] != (byte) ':') ;
        String username = new String(ret, 0, 0, p); // we use this direct string construction for speed
        String password = new String(ret, 0, p + 1, len - p);
        WebPrincipal prin = acquire(username, password);

        wc.principal = prin;
    }

    protected WebPrincipal acquire(String username, String password) {
        return null;
    }

    void handleStatic(WebStatic sta, FullHttpRequest req) {
        if (sta == null) {
//            exch.setStatusCode(NOT_FOUND);
        } else {
            HttpMethod method = req.method();
            if (method == HttpMethod.GET) {
                String since = req.headers().get("If-Modified-Since");
                if (since != null) {
//                    exch(NOT_MODIFIED);
                } else {
                    // async sending
//                    req.getResponseSender().send(ByteBuffer.wrap(sta.content));
                }
//            } else if (method == HEAD) {
            } else {
//                req.setStatusCode(METHOD_NOT_ALLOWED);
            }
        }
    }

    @Override
    public Element config() {
        return config;
    }

}