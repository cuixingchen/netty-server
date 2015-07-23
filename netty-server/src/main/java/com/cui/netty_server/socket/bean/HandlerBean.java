package com.cui.netty_server.socket.bean;

import io.netty.channel.ChannelHandlerContext;

import com.hdsx.taxi.upa.nettyutil.msg.IMsg;

/**
 * handler消息封装bean
 * 
 * @author cuipengfei
 *
 */
public class HandlerBean {
	private IMsg m;
	private ChannelHandlerContext channel;

	public IMsg getM() {
		return m;
	}

	public void setM(IMsg m) {
		this.m = m;
	}

	public ChannelHandlerContext getChannel() {
		return channel;
	}

	public void setChannel(ChannelHandlerContext channel) {
		this.channel = channel;
	}

}
