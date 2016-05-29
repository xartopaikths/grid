package io.greatbone.web;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * Created with IntelliJ IDEA.
 * User: Michael
 * Date: 5/29/16
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ABC extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
