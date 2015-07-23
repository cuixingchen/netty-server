package com.cui.netty_server.nettyutil.msg;

public interface IMsgHead {
	byte[] tobytes();

	boolean frombytes(byte[] b);
}
