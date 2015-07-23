package com.cui.netty_server.msg;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.netty_server.util.Encrypt;

/**
 * 消息工厂
 * 
 * @author cuipengfei
 *
 */
public class MsgFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(MsgFactory.class);

	/**
	 * 根据二进制文件生成消息对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static AbsMsg genMsg(MsgHeader head, byte[] msgbytes) {
		ByteBuffer bf = ByteBuffer.wrap(msgbytes);
		byte[] body = new byte[msgbytes.length - head.getHeadLen() - 2];
		bf.position(head.getHeadLen());
		bf.get(body);
		if (head.getEncrypt_flag() == 1) {
			body = Encrypt.encryptUtil(head.getEncrypt_key(), body,
					body.length, head.getM1(), head.getIA1(), head.getIC1());
		}
		AbsMsg m = null;
		int msg_id = head.getMsg_id();
		switch (msg_id) {

		/** 链路类 **/
		case MessageID.UP_CONNECT_REQ:
			m = new UP_CONNECT_REQ();
			break;
		default:
			break;
		}
		if (m != null) {
			m.head = head;
		}
		m.bodyfromBytes(body);
		logger.debug("接收到数据解码后："+m.toString());

		return m;
	}
}
