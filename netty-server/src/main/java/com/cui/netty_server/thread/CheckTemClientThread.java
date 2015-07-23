package com.cui.netty_server.thread;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.netty_server.socket.ClientManager;

/**
 * 检查未登陆的客户端
 * 
 * @author cuipengfei
 *
 */
public class CheckTemClientThread extends AbsThread {

	private static final Logger logger = LoggerFactory
			.getLogger(CheckTemClientThread.class);

	static CheckTemClientThread obj;

	public static CheckTemClientThread getInstance() {
		if (obj == null)
			obj = new CheckTemClientThread();
		return obj;
	}

	private Timer timer = new Timer();

	@Override
	public void runThread(long delay, long period) {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				try{
					ClientManager.checkTempClient();
				}catch(Exception e){
					logger.error("检查临时客户端异常：",e);
				}
			}
		}, delay * 1000, period * 1000);

	}

}
