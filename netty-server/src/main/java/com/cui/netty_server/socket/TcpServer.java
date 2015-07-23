package com.cui.netty_server.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.netty_server.thread.CheckClientThread;
import com.cui.netty_server.thread.CheckTemClientThread;
import com.cui.netty_server.thread.ParseMsgThreadManager;
import com.cui.netty_server.util.TcpPropertiesUtil;

/**
 * tcp服务端
 * 
 * @author cuipengfei
 *
 */
public class TcpServer extends Thread {

	private static TcpServer tcpServer;

	public static TcpServer getInstance() {
		if (tcpServer == null) {
			createTcpServer();
		}
		return tcpServer;
	}

	private static synchronized void createTcpServer() {
		if (tcpServer == null) {
			tcpServer = new TcpServer();
		}
	}

	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(TcpServer.class);

	int hostport; // 服务器端口
	LogLevel loglevel;

	@Override
	public void run() {
		// 初始化服务器
		init();

		// 启动临时客户端管理(秒)
		CheckTemClientThread.getInstance().run(
				Integer.parseInt(TcpPropertiesUtil.getProperties().getProperty(
						"tcp.temclientdelay")),
				Integer.parseInt(TcpPropertiesUtil.getProperties().getProperty(
						"tcp.temclientdelay")));

		// 启动消息处理
		ParseMsgThreadManager.getInstance().run();

		//启动客户端管理
		CheckClientThread.getInstance().run(
				Integer.parseInt(TcpPropertiesUtil.getProperties().getProperty(
						"tcp.clientdelay")),
				Integer.parseInt(TcpPropertiesUtil.getProperties().getProperty(
						"tcp.clientdelay")));
	}

	/**
	 * 初始化
	 */
	private void init() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		Properties p = TcpPropertiesUtil.getProperties();
		try {
			this.hostport = Integer.parseInt(p.getProperty("tcp.port"));
			String loglevelname = p.getProperty("tcp.loglevel").toUpperCase();
			loglevel = LogLevel.valueOf(loglevelname);
			ServerBootstrap b = new ServerBootstrap(); // (2)
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class) // (3)
					.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
								@Override
								public void initChannel(SocketChannel ch)
										throws Exception {
									ch.pipeline().addLast(new TcpCodec(),

									new TcpServerHandler());
								}
							}).option(ChannelOption.SO_BACKLOG, 128) // (5)
					.childOption(ChannelOption.SO_KEEPALIVE, true); // (6)

			// Bind and start to accept incoming connections.
			ChannelFuture f;
			f = b.bind(hostport).sync();

			// Wait until the server socket is closed.
			// In this example, this does not happen, but you can do that to
			// gracefully
			// shut down your server.
			// f.channel().closeFuture().sync();
		} catch (InterruptedException e) {

		} finally {
			// workerGroup.shutdownGracefully();
			// bossGroup.shutdownGracefully();
		}
		logger.info("服务端初始化完成");

	}

}
