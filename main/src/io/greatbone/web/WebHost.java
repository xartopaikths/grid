package io.greatbone.web;

import io.greatbone.Configurable;
import io.greatbone.Greatbone;
import io.greatbone.util.Roll;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.ReferenceCountUtil;
import org.w3c.dom.Element;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.net.InetSocketAddress;
import java.util.Base64;

/**
 * A web virtual host that listens on a certain socket address as normally associated to an internet subdomain.
 * <p/>
 * Being with its own handling actions, a host can have a number of sub services and a hub service.
 */
public abstract class WebHost extends WebService implements ChannelInboundHandler, WebHostMBean, WebParent, Configurable {

    static final Base64.Decoder DEC = Base64.getMimeDecoder();

    static final Base64.Encoder ENC = Base64.getMimeEncoder();

    final WebUtility web;

    final Element config;

    final String name;

    final InetSocketAddress address;

    // the server socket channel
    volatile Channel serverchan;

    boolean ssl;

    Roll<String, WebService> subs;

    WebServiceHub hub;

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

    public <S extends WebService> void addSub(String key, Class<S> serviceClass, Authorizer authorizer) {
        try {
            Constructor<S> ctor = serviceClass.getConstructor(WebHost.class, WebParent.class);
            S sub = ctor.newInstance(host, this);
            sub.key = key;
            sub.authorizer = authorizer;
            if (subs == null) {
                this.subs = new Roll<>(8);
            }
            subs.put(key, sub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <S extends WebServiceHub<Z>, Z extends WebZone> void setHub(Class<S> hubClass, Authorizer<Z> authorizer) {
        try {
            Constructor<S> ctor = hubClass.getConstructor(WebHost.class);
            S hub = ctor.newInstance(this);
            hub.key = key;
            hub.authorizer = authorizer;
            this.hub = hub;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public WebService subordinate(String key, WebContext wc) {

        return null;
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
                    .channel(Greatbone.LINUX ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(32 * 1024, 64 * 1024))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            pipeline.addLast(new WebSocketServerProtocolHandler("", null, true));
                            pipeline.addLast(host);
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
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        boolean release = true;
        try {
            if (msg instanceof FullHttpRequest) {
                handle(ctx, (FullHttpRequest) msg);
            } else if (msg instanceof WebSocketFrame) {
                WebSocketFrame f = (WebSocketFrame) msg;
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
        ctx.fireChannelReadComplete();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelWritabilityChanged();
    }

    @Override
    @Deprecated
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    }

    @SuppressWarnings("unchecked")
    final void handle(ChannelHandlerContext chctx, FullHttpRequest req) throws Exception {
        try (final WebContext wc = new WebContext(chctx, req)) {

            String path = wc.path();
            int dot = path.lastIndexOf('.');
            if (dot != -1) {
                handleStatic(chctx, req);
                return;
            }

            // authentication of the client
            authenticate(wc);

            String base = path.substring(1);
            int slash = base.indexOf("/");
            if (slash == -1) { // without a slash then handle by this host
                perform(base, wc);
            } else { // sub-perform
                String key = base.substring(0, slash);
                if (key.startsWith("-")) { // hub
                    if (hub == null) { // hub unavailable
                        wc.setStatus(HttpResponseStatus.NOT_FOUND);
                        return;
                    }
                    WebZone zone = hub.resolve(key.substring(1));
                    if (zone == null) {
                        wc.setStatus(HttpResponseStatus.NOT_FOUND);
                        return;
                    }
                    wc.zone = zone;
                    hub.perform(base.substring(slash + 1), wc);

                } else {
                    WebService sub = subs.get(key);
                    if (sub == null) { // sub unavailable
                        wc.setStatus(HttpResponseStatus.NOT_FOUND);
                        return;
                    }
                    sub.perform(base.substring(slash + 1), wc);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("deprecation")
    final void authenticate(WebContext wc) {
        String v = wc.inheaders().get(HttpHeaderNames.AUTHORIZATION);
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
        WebPrincipal prin = fetch(username, password);

        wc.principal = prin;
    }

    protected WebPrincipal fetch(String username, String password) {
        return null;
    }

    static final HttpHeaders EMPTY = new DefaultHttpHeaders();

    final void handleStatic(ChannelHandlerContext ctx, FullHttpRequest req) {
        String path = req.uri();
        WebStatic sta = web.getStatic(path);
        if (sta == null) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND));
        } else {
            HttpMethod method = req.method();
            if (method == HttpMethod.GET) {
                String since = req.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE);
                if (since != null) {
                    ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_MODIFIED));
                } else {
                    // async sending
                    ByteBuf buf = Unpooled.wrappedBuffer(sta.content);
                    HttpHeaders headers = new DefaultHttpHeaders();
                    headers.set(HttpHeaderNames.CONTENT_TYPE, sta.ctype());
                    headers.set(HttpHeaderNames.CONTENT_LENGTH, sta.length());
                    ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, buf, headers, EMPTY));
                }
            } else if (method == HttpMethod.HEAD) {
            } else {
                ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED));
            }
        }
    }

    final void send(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse resp = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        ctx.writeAndFlush(resp);

    }

    @Override
    public Element config() {
        return config;
    }

}
