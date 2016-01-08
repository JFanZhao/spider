package cn.ivan.spider.utils;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {
	public static String START_URL = "cn.spider.start_url";
	public static String HIGHKEY = "spider.todo.high";
	public static String LOWKEY = "spider.todo.low";
	
	JedisPool jedisPool = null;
	/**
	 * 构造方法  初始化redis配置信息
	 */
	public RedisUtils() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(10);
		poolConfig.setMaxTotal(100);
		poolConfig.setMaxWaitMillis(10000);
		poolConfig.setTestOnBorrow(true);
		jedisPool = new JedisPool(poolConfig, "192.168.57.133", 6379);
	}
	/**
	 * 模拟redis的lrange方法
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> lrange(String key,int start,int end){
		Jedis resource = jedisPool.getResource();
		List<String> lrange = resource.lrange(key, start, end);
		jedisPool.returnResourceObject(resource);
		return lrange;
	}
	
	public void add(String lowKey, String url){
		Jedis resource = jedisPool.getResource();
		Long lpush = resource.lpush(lowKey, url);
		jedisPool.returnResourceObject(resource);
		
	}
	
	public String poll(String key){
		Jedis resource = jedisPool.getResource();
		String result = resource.rpop(key);
		jedisPool.returnResourceObject(resource);
		return result;
	}
}
