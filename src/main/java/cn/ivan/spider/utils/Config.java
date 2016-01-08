package cn.ivan.spider.utils;


/**
 * 把项目中可能会发生变化的参数的值都提取到这个类中，
 * 注意:在这我直接把值在这个类中写死了，实际中这些参数的值是需要读取配置文件或者从数据库读取的
 * 如果读取配置文件，可以使用properties类
 * @author Administrator
 *
 */
public class Config {
	public static long MILLION_1 = 1000;
	public static long MILLION_5 = 5000;
	
	public static int THREADNUM = 5;
}
