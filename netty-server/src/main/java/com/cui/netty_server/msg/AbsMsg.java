package com.cui.netty_server.msg;

import com.cui.netty_server.nettyutil.msg.IMsg;

/**
 * 消息
 * 
 * @author cuipengfei
 *
 */
public abstract class AbsMsg implements IMsg {

	@Override
	public byte[] toBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean fromBytes(byte[] b) {
		// TODO Auto-generated method stub
		return false;
	}

}
