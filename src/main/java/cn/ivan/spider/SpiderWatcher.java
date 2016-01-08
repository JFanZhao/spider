package cn.ivan.spider;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Spider 使用Curator监控爬虫节点的生命周期
 * @author Hades
 *
 */
public class SpiderWatcher implements Watcher{
	private CuratorFramework client;
	List<String> children ;
	public SpiderWatcher() {
		String connectString = "192.168.57.133:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);
		client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
		client.start();
		//指定需要监控的父节点
		try {
			//表示给spider节点注册一个监视器
			children = client.getChildren().usingWatcher(this).forPath("/spider");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * 当监视器发现监控的父节点下面的子节点发生变化的时候，会调用这个方法
	 * 
	 * 注意：watcher监视器是单次有效的，注册一次，只能使用一次，需要想要一直使用，则需要重复注册
	 */
	public void process(WatchedEvent arg0) {
		// TODO Auto-generated method stub
		try {
			List<String> newChildren = client.getChildren().usingWatcher(this).forPath("/spider");
			for (String node :children) {
				if(!newChildren.contains(node)){
					System.out.println(node+"节点消失了~~~");
					//TODO-- 在这需要给管理员发送邮件或者短信提醒
					/**
					 * 发邮件使用javamail
					 * 
					 * 
					 * 发短信使用 云片网
					 */
				}
			}
			for (String node : newChildren) {
				if(!children.contains(node)){
					System.out.println("新增节点:"+node);
				}
			}
			
			children = newChildren;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		SpiderWatcher spiderWatcher = new SpiderWatcher();
		spiderWatcher.start();
	}
	
	/**
	 * 让当前进程一直运行
	 */
	private void start() {
		// TODO Auto-generated method stub
		while(true){
			;
		}
	}

}
