package com.cui.netty_server.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.netty_server.socket.bean.MsgQueue;
import com.cui.netty_server.socket.bean.ReciPackBean;

/**
 * tcp handler
 * 
 * @author cuipengfei
 *
 */
public class TcpServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(TcpServerHandler.class);

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
		try {
			if (msg instanceof byte[]) {
				final byte[] msgbytes = (byte[]) msg;
				ReciPackBean rpb = new ReciPackBean();
				rpb.setChannel(ctx);
				rpb.setMsgbytes(msgbytes);
				try {
					MsgQueue.getRecqueue().put(rpb);
				} catch (Exception e) {
					logger.error("主handler---接收消息队列存储消息失败", e);
				}
			} else {
				logger.error("主handler---消息解码有误，请检查！！");
			}

		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

		logger.info("-------------临时客户端建立连接--------------");
		ClientManager.addTemClient(ctx);

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub

		InetSocketAddress address = (InetSocketAddress) ctx.channel()
				.remoteAddress();
		InetAddress inetAdd = address.getAddress();
		logger.info("客户端断开连接：" + ctx.name() + ",IP:" + inetAdd.getHostAddress()
				+ ",port:" + address.getPort());
		// 记录日志
		ClientManager.removeClient(ctx);
		ClientManager.removeTemClient(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.

		logger.error("主handle---rexceptionCaught异常", cause);
		ctx.close();
		ctx = null;
	}

}
