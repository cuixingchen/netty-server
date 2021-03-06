package com.cui.netty_server.thread;

/**
 * 线程抽象类
 * 
 * @author cuipengfei
 * 
 */
public abstract class AbsThread {

	public boolean isRun = false;

	public void run() {
		run(0, 0);
	}

	public void run(long delay, long period) {
		if (isRun)
			return;
		isRun = true;
		runThread(delay, period);
	}

	public abstract void runThread(long delay, long period);

}
