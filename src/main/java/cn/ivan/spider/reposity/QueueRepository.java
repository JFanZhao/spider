package cn.ivan.spider.reposity;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * 
 * @author ivan Email:seeHades@163.com
 *
 * @date 2016年1月7日 上午12:45:11
 * 
 * @desc 优先级队列url 仓库
 */
public class QueueRepository implements Repository{
	/**
	 * 高优先级队列
	 */
	Queue<String> highQueue = new ConcurrentLinkedQueue<String>();
	/**
	 * 低优先级队列
	 */
	Queue<String> lowQueue = new ConcurrentLinkedQueue<String>();
	/**
	 * 从url仓库中取url，先从高优先级队列中取，如果高优先级队列中没有url，则从低优先级队列中取
	 */
	public String poll() {
		String url = highQueue.poll();
		if(url==null){
			url = lowQueue.poll();
		}
		return url;
	}
	/**
	 * 向高优先级队列中存入url
	 */
	public void addHigh(String nextUrl) {
		highQueue.add(nextUrl);
	}
	/**
	 * 向低优先级队列中存入url
	 */
	public void add(String nextUrl) {
		lowQueue.add(nextUrl);
	}

}
