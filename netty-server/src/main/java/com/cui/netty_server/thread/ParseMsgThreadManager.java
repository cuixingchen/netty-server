package com.cui.netty_server.thread;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cui.netty_server.socket.bean.MsgQueue;
import com.cui.netty_server.socket.bean.ReciPackBean;
import com.cui.netty_server.util.TcpPropertiesUtil;

/**
 * 线程池（处理消息）管理
 * 
 * @author cuipengfei
 *
 */
public class ParseMsgThreadManager extends AbsThread {

	private static final Logger logger = LoggerFactory
			.getLogger(ParseMsgThreadManager.class);

	static ParseMsgThreadManager obj;

	public static ParseMsgThreadManager getInstance() {
		if (obj == null)
			obj = new ParseMsgThreadManager();
		return obj;
	}

	private ThreadPoolExecutor threadPool;

	public ParseMsgThreadManager() {
		int corePoolSize = Integer.parseInt(TcpPropertiesUtil.getProperties()
				.getProperty("ParseCorePoolSize"));
		int maximunPoolSize = Integer.parseInt(TcpPropertiesUtil.getProperties()
				.getProperty("ParseMaximumPoolSize"));
		int keepAliveTime = Integer.parseInt(TcpPropertiesUtil.getProperties()
				.getProperty("ParseKeepAliveTime"));
		int queueSize=Integer.parseInt(TcpPropertiesUtil.getProperties()
				.getProperty("ParseQueueSize"));
		threadPool = new ThreadPoolExecutor(corePoolSize, maximunPoolSize,
				keepAliveTime, TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(queueSize),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	@Override
	public void runThread(long delay, long period) {

		new Thread(new ParseThreadManage()).start();
		logger.info("服务器消息处理启动完成");

	}

	class ParseThreadManage implements Runnable {

		@Override
		public void run() {
			while (true) {
				ReciPackBean rpb = null;
				try {
					rpb = MsgQueue.getRecqueue().take();
					threadPool.execute(new ParseMsgThread(rpb));
				} catch (Exception e) {
					logger.error("消息解析管理线程运行异常", e);
				}
			}

		}

	}

}
