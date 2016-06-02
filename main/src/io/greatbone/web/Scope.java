package io.greatbone.web;

import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * A space is a virtual folder that may be dynamically resolved.
 */
public interface Scope {

    String id();

    List<ChannelHandlerContext> contexts();

}
