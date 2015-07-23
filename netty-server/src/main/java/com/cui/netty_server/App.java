package com.cui.netty_server;

import com.cui.netty_server.socket.TcpServer;

/**
 * 服务启动入口
 * 
 * @author cuipengfei
 *
 */
public class App {
	public static void main(String[] args) {
		TcpServer.getInstance().run();
	}
}
