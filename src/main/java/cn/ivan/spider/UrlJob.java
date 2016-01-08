package cn.ivan.spider;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.ivan.spider.utils.RedisUtils;
/**
 * 将种子url 添加到redis的队列中
 * @author Hades
 *
 */
public class UrlJob implements Job {
	RedisUtils redisUtils = new RedisUtils();
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		List<String> list = redisUtils.lrange(RedisUtils.START_URL, 0, -1);
		for (String url : list) {
			redisUtils.add(RedisUtils.HIGHKEY, url);
		}
	}

}
