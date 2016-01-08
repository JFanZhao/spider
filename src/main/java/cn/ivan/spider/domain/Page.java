package cn.ivan.spider.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储页面的原始数据
 * @author Hades
 *
 */
public class Page {
	/**
	 * 保存商品页面内容
	 */
	private String content;
	/**
	 * 商品url
	 */
	private String url;
	/**
	 * 
	 * 商品基本信息
	 */
	
	private Map<String,String> values =new HashMap<String, String>();
	
	/**
	 * 保存页面中解析出来的url
	 */
	private List<String> urls = new ArrayList<String>();
	
	
	
	private String goodId;
	
	public void addUrl(String url){
		this.urls.add(url);
	}
	public List<String> getUrls() {
		return urls;
	}
	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	public String getGoodId() {
		return goodId;
	}
	public void setGoodId(String goodId) {
		this.goodId = goodId;
	}
	public Map<String, String> getValues() {
		return values;
	}
	public void addField(String key,String value){
		values.put(key, value);
	}
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
