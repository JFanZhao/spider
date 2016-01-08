package cn.ivan.spider;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 * url调度类
 * 负责每天定时向url仓库中添加种子url
 * 这样可以保证爬虫每天都抓取一次所有数据
 * @author Administrator
 *
 */
public class UrlManager {
	public static void main(String[] args) {
		try {
			//获取一个默认调度器
			Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
			//开启调度器
			defaultScheduler.start();
			String simpleName = UrlJob.class.getSimpleName();
			//具体要调度的任务
			JobDetail jobDetail = new JobDetail(simpleName, Scheduler.DEFAULT_GROUP, UrlJob.class);
			//说明什么时候执行任务，每天凌晨一点执行任务一次
			Trigger trigger = new CronTrigger(simpleName, Scheduler.DEFAULT_GROUP, "00 00 01 * * ?");
			defaultScheduler.scheduleJob(trigger);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
