package com.cui.netty_server.socket.bean;

import io.netty.channel.ChannelHandlerContext;

/**
 * 封装handler类
 * 
 * @author cuipengfei
 *
 */
public class ReciPackBean {

	private byte[] msgbytes;
	private ChannelHandlerContext channel;

	public ChannelHandlerContext getChannel() {
		return channel;
	}

	public void setChannel(ChannelHandlerContext channel) {
		this.channel = channel;
	}

	public byte[] getMsgbytes() {
		return msgbytes;
	}

	public void setMsgbytes(byte[] msgbytes) {
		this.msgbytes = msgbytes;
	}

}
