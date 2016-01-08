package cn.ivan.spider;


import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.ivan.spider.domain.Page;
import cn.ivan.spider.download.Downloadable;
import cn.ivan.spider.download.HttpClientDownable;
import cn.ivan.spider.process.JDProcessable;
import cn.ivan.spider.process.Processable;
import cn.ivan.spider.reposity.QueueRepository;
import cn.ivan.spider.reposity.RedisRepository;
import cn.ivan.spider.reposity.Repository;
import cn.ivan.spider.store.ConsoleStoreable;
import cn.ivan.spider.store.HBaseStoreable;
import cn.ivan.spider.store.Storeable;
import cn.ivan.spider.utils.Config;
import cn.ivan.spider.utils.SleepUtils;

public class Spider {
	Logger logger = LoggerFactory.getLogger(Spider.class);
	Downloadable downloadable = new HttpClientDownable();
	Processable processable;
	Storeable storeable = new ConsoleStoreable();
	Repository repository = new QueueRepository();
	/**
	 * 创建一个大小为5的线程池
	 */
	ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(Config.THREADNUM);
	public Spider() {
		String connectString = "192.168.57.133:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
		
		client.start();
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			String ip = localHost.getHostAddress();
			String forPath = client.create()
					.creatingParentsIfNeeded()//如果父节点不存在，则创建
					.withMode(CreateMode.EPHEMERAL)//指定创建的节点类型，设置为临时节点
					.forPath("/spider/"+ip);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void start(){
		check();
		logger.info("开始启动爬虫。。。");
		while(true){
			final String url = repository.poll();
			if(StringUtils.isNotBlank(url)){
				newFixedThreadPool.execute(new Runnable() {
					public void run() {
						Page page = Spider.this.download(url);
						List<String> urls = page.getUrls();
						Spider.this.process(page);
						for (String nextUrl : urls) {
							if (nextUrl.startsWith("http://list.jd.com/")) {
								repository.addHigh(nextUrl);
							} else {
								repository.add(nextUrl);
							}
						}
						//列表页面才进行存储
						if (urls == null || urls.size() == 0) {
							Spider.this.store(page);
						}
						SleepUtils.sleep(Config.MILLION_1);
					}
				});
			}else{
				logger.info("没有url，休息一会吧~~~~");
				SleepUtils.sleep(Config.MILLION_5);
			}
		}
	}
	/**
	 * 配置检查
	 */
	private void check() {
		if(processable==null){
			String error_message = "没有配置解析类！";
			logger.error(error_message);
			throw new RuntimeException(error_message);
		}
		
		logger.info("===================================================");
		logger.info("下载功能的实现类为：{}",downloadable.getClass().getName());
		logger.info("解析功能的实现类为：{}",processable.getClass().getName());
		logger.info("存储功能的实现类为：{}",storeable.getClass().getName());
		logger.info("url队列功能的实现类为：{}",repository.getClass().getName());
		logger.info("===================================================");
	}
	public Storeable getStoreable() {
		return storeable;
	}

	public void setStoreable(Storeable storeable) {
		this.storeable = storeable;
	}
	public void setDownloadable(Downloadable downloadable) {
		this.downloadable = downloadable;
	}

	/**
	 * 下载页面
	 * 
	 * @param url
	 * @return
	 */
	public Page download(String url) {
		Page page = this.downloadable.download(url);
		page.setUrl(url);
		return page;
	}

	/**
	 * 解析页面
	 * 
	 * @param page
	 */
	public void process(Page page) {

		this.processable.process(page);
	}

	public Processable getProcessable() {
		return processable;
	}

	public void setProcessable(Processable processable) {
		this.processable = processable;
	}

	public Repository getRepository() {
		return repository;
	}
	public void setRepository(Repository repository) {
		this.repository = repository;
	}
	public Downloadable getDownloadable() {
		return downloadable;
	}

	public void store(Page page) {
		this.storeable.store(page);
	}
	
	public void setSeedUrl(String url){
		this.repository.addHigh(url);
	}
	public static void main(String[] args) {
		Spider spider = new Spider();
		String url = "http://list.jd.com/list.html?cat=9987,653,655";
		spider.setProcessable(new JDProcessable());
		spider.setStoreable(new HBaseStoreable());
		spider.setRepository(new RedisRepository());
//		spider.setDownloadable(new HttpClientDownable());
//		spider.setProcessable(new JDProcessable());
//		spider.setStoreable(new ConsoleStoreable());
//		spider.setRepository(new RedisRepository());
		spider.setSeedUrl(url);
		spider.start();
	}
}
