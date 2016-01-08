package cn.ivan.spider.store;

import java.util.Map;

import cn.ivan.spider.domain.Page;
import cn.ivan.spider.utils.HBaseUtils;
import cn.ivan.spider.utils.RedisUtils;

public class HBaseStoreable implements Storeable{
	 HBaseUtils hbBaseUtils = new HBaseUtils(); 
	 RedisUtils redisUtils = new RedisUtils();
	public void store(Page page) {
		String rowKey = page.getGoodId();
		Map<String, String> values = page.getValues();
		try {
			hbBaseUtils.put(HBaseUtils.TABLE_NAME, rowKey, HBaseUtils.COLUMNFAMILY_1, HBaseUtils.COLUMNFAMILY_1_DATA_URL, page.getUrl());
			hbBaseUtils.put(HBaseUtils.TABLE_NAME, rowKey, HBaseUtils.COLUMNFAMILY_1, HBaseUtils.COLUMNFAMILY_1_PIC_URL, values.get("pic_url"));
			hbBaseUtils.put(HBaseUtils.TABLE_NAME, rowKey, HBaseUtils.COLUMNFAMILY_1, HBaseUtils.COLUMNFAMILY_1_PRICE, values.get("price"));
			hbBaseUtils.put(HBaseUtils.TABLE_NAME, rowKey, HBaseUtils.COLUMNFAMILY_1, HBaseUtils.COLUMNFAMILY_1_TITLE, values.get("title"));
			hbBaseUtils.put(HBaseUtils.TABLE_NAME, rowKey, HBaseUtils.COLUMNFAMILY_2, HBaseUtils.COLUMNFAMILY_2_PARAM, values.get("spec"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		redisUtils.add("solr_index", rowKey);
	}

}
