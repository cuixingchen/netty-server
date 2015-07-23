package com.cui.netty_server.thread;

import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.pattern.Converter;

import com.cui.netty_server.nettyutil.handler.IHandler;
import com.cui.netty_server.socket.ClientManager;
import com.cui.netty_server.socket.bean.Client;
import com.cui.netty_server.socket.bean.ReciPackBean;

/**
 * 处理消息线程
 * 
 * @author cuipengfei
 *
 */
public class ParseMsgThread extends Thread {

	private static final Logger logger = LoggerFactory
			.getLogger(ParseMsgThread.class);

	private ReciPackBean rpb;

	public ParseMsgThread(ReciPackBean rpb) {
		this.rpb = rpb;
	}

	@Override
	public void run() {
		try {
			// 转码
			rpb.setMsgbytes(decode(rpb.getMsgbytes()));

			// 消息头解析
			MsgHeader head = headFromBytes(rpb.getMsgbytes());
			if (head == null) {
				return;
			}

			int msgid = head.getMsg_id();
			// 登陆消息处理
			if (msgid == MessageID.UP_CONNECT_REQ) {
				// 判断是否加密
				if (head.getEncrypt_flag() == 1) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("msg_gnsscenterid", head.getMsg_gnsscenterid());
					List<TcpUser> list = TcpUserServiceImpl.getInstance()
							.getByProperty(map);
					if (list != null && list.size() == 1) {
						TcpUser user = list.get(0);
						head.setIA1(user.getIA1());
						head.setIC1(user.getIC1());
						head.setM1(user.getM1());
					} else {
						head.setMsg_sn(Converter.bigBytes2Unsigned32Long(
								rpb.getMsgbytes(), 4));
						UP_CONNECT_RSP answer = new UP_CONNECT_RSP();
						answer.setResult((byte) 0x02);
						ClientManager.sendAnswerData(answer, head,
								rpb.getChannel());
						logger.info("接入码" + head.getMsg_gnsscenterid()
								+ "异常：数据库接入码重复或者无该接入码");
						return;
					}
				}
			} else {// 其它消息处理
				Client client = ClientManager.getClient(rpb.getChannel());
				if (client == null || !client.isLogin()) {
					logger.info("客户端没有登陆，请先登录后再发送消息");
					ClientManager.removeTemClient(rpb.getChannel());
					ChannelHandlerContext channel = null;
					channel=rpb.getChannel();
					if (channel.channel().isOpen()) {
						logger.info("客户端没有登陆，发送业务数据，主动断开连接，连接name："
								+ ClientManager.getClientId(channel));
						channel.close();
					}
					channel=null;
					return;
				}
				ClientManager.setClientLastTime(rpb.getChannel(), client);
				if (head.getEncrypt_flag() == 1) {
					head.setIA1(client.getIA1());
					head.setIC1(client.getIC1());
					head.setM1(client.getM1());
				}
			}
			// 生成消息后产生handler
			AbsMsg msg = MsgFactory.genMsg(head, rpb.getMsgbytes());
			if (msg == null) {
				logger.error(Integer.toHexString(head.getMsg_id()) + "消息不存在");
				return;
			}
			// 交给对应handler处理
			IHandler handler = HandlerFactory.getHandler(msg);
			if (handler != null) {
				handler.doHandle(msg, rpb.getChannel());
			}
		} catch (Exception e) {
			logger.error("接受消息队列处理数据错误", e);
		}
	}

	/**
	 * 消息头解析
	 * 
	 * @return
	 */
	private MsgHeader headFromBytes(byte[] b) {
		ByteBuffer buffer1 = ByteBuffer.wrap(b);
		byte[] head_body = new byte[b.length - 2];
		buffer1.position(0);
		buffer1.get(head_body);

		byte[] crc = new byte[2];
		buffer1.position(b.length - 2);
		buffer1.get(crc);

		byte[] crc_check = CRC16_CUI.getCRCCRC16_CCITT(head_body);

		if (Converter.bigBytes2Unsigned16Int(crc, 0) != Converter
				.bigBytes2Unsigned16Int(crc_check, 0)) {
			logger.info(Converter.bigBytes2Unsigned16Int(crc, 0) + "  "
					+ Converter.bigBytes2Unsigned16Int(crc_check, 0));
			logger.error("消息crc校验码不正确");
			return null;// crc校验码有误
		}

		MsgHeader head = new MsgHeader();
		if (!head.frombytes(head_body))
			return null;// 消息头属性解析有误
		return head;
	}

	/**
	 * 解码转义
	 * 
	 * @param b
	 * @return
	 */
	private byte[] decode(byte[] b) {
		ByteBuffer buffer = ByteBuffer.allocate(10*1024*1024);
		ByteBuffer buffer1 = ByteBuffer.wrap(b);
		buffer.position(0);
		while (buffer1.remaining() > 0) {

			byte d = buffer1.get();
			if (d == 0x5a) {
				byte e = buffer1.get();
				if (e == 0x02)
					buffer.put((byte) 0x5a);
				else if (e == 0x01)
					buffer.put((byte) 0x5b);
			} else if (d == 0x5e) {
				byte e = buffer1.get();
				if (e == 0x02)
					buffer.put((byte) 0x5e);
				else if (e == 0x01)
					buffer.put((byte) 0x5d);
			} else {
				buffer.put(d);
			}
		}

		byte[] result = new byte[buffer.position()];
		buffer.position(0);
		buffer.get(result);
		return result;
	}

}
