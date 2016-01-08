package cn.ivan.spider.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.HTablePool;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;


public class HBaseUtils {
	/**
	 * 表名称
	 */
	public static String TABLE_NAME = "spider";
	/**
	 * 列簇1 商品信息
	 */
	public static String COLUMNFAMILY_1 = "goodinfo";
	/**
	 * 列簇1中的列
	 */
	public static String COLUMNFAMILY_1_PRICE = "price";
	public static String COLUMNFAMILY_1_PIC_URL = "pic_url";
	public static String COLUMNFAMILY_1_DATA_URL = "data_url";
	public static String COLUMNFAMILY_1_TITLE = "title";
	
	/**
	 * 列簇2 商品参数信息
	 * 
	 */
	public static String COLUMNFAMILY_2 = "spec";
	public static String COLUMNFAMILY_2_PARAM = "param";
	
	HBaseAdmin admin = null;
	Configuration conf = null;
	
	/**
	 * 构造函数 加载配置
	 */
	public HBaseUtils() {
		// TODO Auto-generated constructor stub
		conf = new Configuration();
		conf.set("hbase.zookeeper.quorum", "192.168.57.133:2181");
		conf.set("hbase.rootdir", "hdfs://192.168.57.133:9000/hbase");
		try {
			admin = new HBaseAdmin(conf);
		} catch (MasterNotRunningException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZooKeeperConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws Exception {
		HBaseUtils hbase = new HBaseUtils();
		//创建一张表
//		hbase.createTable("stu","cf");
//		//查询所有表名
//		hbase.getALLTable();
//		//往表中添加一条记录
//		hbase.addOneRecord("stu","key1","cf","name","zhangsan");
//		hbase.addOneRecord("stu","key1","cf","age","24");
//		//查询一条记录
//		hbase.getKey("stu","key1");
//		//获取表的所有数据
//		hbase.getALLData("stu");
//		//删除一条记录
//		hbase.deleteOneRecord("stu","key1");
//		//删除表
//		hbase.deleteTable("stu");
		//scan过滤器的使用
//		hbase.getScanData("stu","cf","age");
		//rowFilter的使用
		//84138413_20130313145955
	}
	/**
	 * rowFilter 的使用
	 * @param tableName
	 * @param reg
	 * @throws Exception 
	 */
	public void getRowFilter(String tableName,String reg) throws Exception{
		HTable hTable = new HTable(conf, tableName);
		Scan scan = new Scan();
		RowFilter rowFilter = new RowFilter(CompareOp.NOT_EQUAL, new RegexStringComparator(reg));
		scan.setFilter(rowFilter);
		ResultScanner scanner = hTable.getScanner(scan);
		for (Result result : scanner) {
			System.out.println(new String(result.getRow()));
		}
	}
	/**
	 * 扫描数据
	 * @param tableName
	 * @param family
	 * @param qualifier
	 * @throws Exception 
	 */
	public void getScanData(String tableName,String family, String qualifier) throws Exception{
		HTable hTable = new HTable(conf, tableName);
		Scan scan = new Scan();
		scan.addColumn(family.getBytes(), qualifier.getBytes());
		ResultScanner scanner = hTable.getScanner(scan);
		for (Result result : scanner) {
			if (result.raw().length == 0) {
				System.out.println(tableName + " 表数据为空！");
			} else {
				for (KeyValue kv : result.raw()) {
					System.out.println(new String(kv.getKey()) + "\t" + new String(kv.getValue()));
				}
			}
		}
	}
	/**
	 * 删除表
	 * @param tableName
	 * @throws Exception 
	 */
	public void deleteTable(String tableName){
		try {
			if(admin.tableExists(tableName)){
				admin.disableTable(tableName);
				admin.deleteTable(tableName);
				System.out.println(tableName + "表删除成功！");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 删除一条记录
	 * @param tableName
	 * @param rowKey
	 */
	public void deleteOneRecord(String tableName,String rowKey){
		HTablePool hTablePool = new HTablePool(conf, 1000);
		HTableInterface table = hTablePool.getTable(tableName);
		Delete delete = new Delete(rowKey.getBytes());
		try {
			table.delete(delete);
			System.out.println(rowKey + "记录删除成功！");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(rowKey + "记录删除失败！");
		}
	}
	/**
	 * 获取表中所有数据
	 * @param tableName
	 * @throws Exception 
	 */
	public void getALLData(String tableName){
		try {
			HTable hTable = new HTable(conf, tableName);
			Scan scan = new Scan();
			ResultScanner scanner = hTable.getScanner(scan);
			for (Result result : scanner) {
				if (result.raw().length == 0) {
					System.out.println(tableName + " 表数据为空！");
				} else {
					for (KeyValue kv : result.raw()) {
						System.out.println(new String(kv.getKey()) + "\t" + new String(kv.getValue()));
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 添加一条数据
	 * @param tableName
	 * @param row
	 * @param columnFamily
	 * @param column
	 * @param data
	 * @throws IOException
	 */
	public void put(String tableName, String row, String columnFamily, String column, String data) throws IOException {
		HTablePool hTablePool = new HTablePool(conf, 1000);
		HTableInterface table = hTablePool.getTable(tableName);
		Put p1 = new Put(Bytes.toBytes(row));
		p1.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(data));
		table.put(p1);
		System.out.println("put'" + row + "'," + columnFamily + ":" + column + "','" + data + "'");
	}

	/**
	 * 查询所有表名
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getALLTable() throws Exception {
		ArrayList<String> tables = new ArrayList<String>();
		if (admin != null) {
			HTableDescriptor[] listTables = admin.listTables();
			if (listTables.length > 0) {
				for (HTableDescriptor tableDesc : listTables) {
					tables.add(tableDesc.getNameAsString());
					System.out.println(tableDesc.getNameAsString());
				}
			}
		}
		return tables;
	}

	/**
	 * 创建一张表
	 * 
	 * @param tableName
	 * @param column
	 * @throws Exception
	 */
	public void createTable(String tableName, String column) throws Exception {
		if (admin.tableExists(tableName)) {
			System.out.println(tableName + "表已经存在！");
		} else {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			tableDesc.addFamily(new HColumnDescriptor(column.getBytes()));
			admin.createTable(tableDesc);
			System.out.println(tableName + "表创建成功！");
		}
	}
}
