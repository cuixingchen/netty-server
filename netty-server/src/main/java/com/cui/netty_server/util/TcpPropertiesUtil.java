package com.cui.netty_server.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tcp配置文件
 * 
 * @author cuipengfei
 *
 */
public class TcpPropertiesUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(TcpPropertiesUtil.class);

	private static Properties p;

	public static Properties getProperties() {
		if (p == null) {
			p = new Properties();
			try {
				p.load(TcpPropertiesUtil.class
						.getResourceAsStream("/tcp.properties"));
			} catch (IOException e) {
				logger.error("配置文件tcp.properties初始化错误：" + e.getMessage());
			}
		}
		return p;
	}
}
