package cn.ivan.spider.reposity;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import cn.ivan.spider.utils.DomainUtils;

/**
 * 
 * @author ivan Email:seeHades@163.com
 *
 * @date 2016年1月7日 上午12:49:20
 * 
 * @desc 使用对列 随机获取某一个电商网站的url
 */

public class RandomQueueRepository implements Repository{
	/**
	 * Map<String,Queue<String>> hashMap
	 * @desc map对象存储各个电商网站的url队列，主要用在分布式爬虫中，但是使用队列会存在url仓库共享问题，后面是用redis解决
	 * key用电商网站的域名，value是一个队列
	 */
	Map<String,Queue<String>> hashMap = new HashMap<String, Queue<String>>();
	Random random = new Random();
	/** 
	* @Title: poll 
	* @Description: 随机获取一个url
	* @return  
	* @throws 
	*/
	public String poll() {
		String[] keyArray = hashMap.keySet().toArray(new String[0]);
		int nextInt = random.nextInt(keyArray.length);
		String key = keyArray[nextInt];//获取随机站点域名
		Queue<String> queue = hashMap.get(key);
		return queue.poll();
	}
	
	/** 
	* @Title: addHigh 
	* @Description: 向队列仓库中添加url，在添加之前先截取url中的顶级域名，当作key值
	* @param nextUrl  
	* @throws 
	*/
	public void addHigh(String nextUrl) {
		String topDomain = DomainUtils.getTopDomain(nextUrl);
		Queue<String> queue = hashMap.get(topDomain);
		if(queue==null){
			queue = new ConcurrentLinkedQueue<String>();
			hashMap.put(topDomain, queue);
		}
		queue.add(nextUrl);
	}

	public void add(String nextUrl) {
		addHigh(nextUrl);
	}

}
