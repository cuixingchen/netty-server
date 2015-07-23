package com.cui.netty_server.socket.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.netty_server.msg.AbsMsg;
import com.cui.netty_server.msg.MessageID;
import com.cui.netty_server.nettyutil.handler.IHandler;

/**
 * handler工厂
 * 
 * @author cuipengfei
 *
 */
public class HandlerFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(HandlerFactory.class);

	public static IHandler getHandler(AbsMsg m) {
		int msgid=m.getHead().getMsg_id();
//		if (logger.isDebugEnabled()) {
//			logger.debug("handler工厂产生消息:" + Integer.toHexString(msgid)); //$NON-NLS-1$
//		}
		IHandler h = null;
		switch (msgid) {
		/** 链路类 **/
		case MessageID.UP_CONNECT_REQ:
			h = new Handler2001();
			break;

		default:
			break;
		}
		return h;
	}
}
