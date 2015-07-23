package com.cui.netty_server.nettyutil;

import com.cui.netty_server.nettyutil.msg.IMsg;

public interface IMsgCache {
	public void put(IMsg m);

	public void remove(IMsg m);
}
