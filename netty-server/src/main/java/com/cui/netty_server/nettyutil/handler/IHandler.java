package com.cui.netty_server.nettyutil.handler;

import io.netty.channel.ChannelHandlerContext;

import com.cui.netty_server.nettyutil.msg.IMsg;

public interface IHandler {

	void doHandle(IMsg m, ChannelHandlerContext ctx);

}
