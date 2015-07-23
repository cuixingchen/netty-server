package com.cui.netty_server.nettyutil.msg;

/**
 * 消息接口
 * 
 * @author cuipengfei
 *
 */
public interface IMsg {
	byte[] toBytes();

	boolean fromBytes(byte[] b);
}
