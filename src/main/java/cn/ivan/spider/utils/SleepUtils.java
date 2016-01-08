package cn.ivan.spider.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SleepUtils {
	static Logger logger = LoggerFactory.getLogger(SleepUtils.class);
	
	public static void sleep(long millons){
		try {
			Thread.currentThread().sleep(millons);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("线程休息失败~~~");
		}
	}
}
