package cn.ivan.spider;

import static org.junit.Assert.*;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

public class CuratorTest {
	@Test
	public void test() throws Exception {
		//获取curator客户端
		String connectString = "192.168.57.133:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
		
		client.start();
		String forPath = client.create()
				.creatingParentsIfNeeded()//如果父节点不存在，则创建
				.withMode(CreateMode.EPHEMERAL)//指定创建的节点类型，设置为临时节点
				.forPath("/spider/192.168.57.1");
		System.out.println(forPath);
		
	}
}
