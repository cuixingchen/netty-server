package com.cui.netty_server.socket.handler;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.netty_server.msg.UP_CONNECT_REQ;
import com.cui.netty_server.msg.UP_CONNECT_RSP;
import com.cui.netty_server.nettyutil.handler.IHandler;
import com.cui.netty_server.nettyutil.msg.IMsg;
import com.cui.netty_server.socket.ClientManager;
import com.cui.netty_server.socket.bean.TcpUser;
import com.cui.netty_server.util.Md5Util;
import com.cui.netty_server.util.TcpPropertiesUtil;

/**
 * 链路登陆请求handler
 * 
 * @author cuipengfei
 *
 */
public class Handler2001 implements IHandler {

	Logger logger = LoggerFactory.getLogger(Handler2001.class);

	// 0x00：成功
	// 0x01：IP地址不正确
	// 0x02：接入码不正确
	// 0x03：用户没有注册
	// 0x04：密码错误
	// 0x05：资源紧张，稍后再连接（已经占用）
	// 0x06：其他

	@Override
	public void doHandle(IMsg m, ChannelHandlerContext ctx) {
		if (m instanceof UP_CONNECT_REQ) {
			UP_CONNECT_REQ msg = (UP_CONNECT_REQ) m;
			UP_CONNECT_RSP answer = new UP_CONNECT_RSP();
			// 获取TcpUser数据
			TcpUser user = null;

			if (user != null) {

				if ("".equals(user.getMsg_gnsscenterid())
						|| user.getMsg_gnsscenterid() != msg.getHead()
								.getMsg_gnsscenterid()) {
					answer.setResult((byte) 0x02);
					ClientManager.sendAnswerData(answer, msg, ctx);
					return;
				}
				InetSocketAddress address = (InetSocketAddress) ctx
						.channel().remoteAddress();
				InetAddress inetAdd = address.getAddress();
				if ("".equals(user.getIp()) || !inetAdd.getHostAddress().equals(user.getIp())) {
					answer.setResult((byte) 0x01);
					ClientManager.sendAnswerData(answer, msg, ctx);
					return;
				}

				if (user.getUserid() != msg.getUserid()) {
					answer.setResult((byte) 0x03);
					ClientManager.sendAnswerData(answer, msg, ctx);
					return;
				}
				String pw = user.getPassword() + msg.getConnecttime();

				if ("".equals(pw)
						|| !Md5Util.getMD5Str(pw)
								.equalsIgnoreCase(msg.getMac())) {
					answer.setResult((byte) 0x04);
					ClientManager.sendAnswerData(answer, msg, ctx);
					return;
				}
			} else {
				logger.error("接入码错误：" + msg.getHead().getMsg_gnsscenterid()
						+ "登陆失败");
				answer.setResult((byte) 0x02);
				ClientManager.sendAnswerData(answer, msg, ctx);
				return;
			}
			answer.setResult((byte) 0x00);
			ClientManager.sendAnswerData(answer, msg, ctx);
			ClientManager.addClient(ctx, user);
		} else {
			logger.error("2001链路请求消息强转失败");
		}

	}

}
