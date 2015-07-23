package com.cui.netty_server.socket.bean;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 消息队列
 * 
 * @author cuipengfei
 *
 */
public class MsgQueue {

	/**
	 * 接收数据队列
	 */
	private static LinkedBlockingQueue<ReciPackBean> rec_queue = new LinkedBlockingQueue<ReciPackBean>();


	public static LinkedBlockingQueue<ReciPackBean> getRecqueue() {
		return rec_queue;
	}

}
