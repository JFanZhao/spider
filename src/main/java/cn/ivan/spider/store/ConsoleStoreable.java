package cn.ivan.spider.store;

import cn.ivan.spider.domain.Page;

public class ConsoleStoreable implements Storeable{

	public void store(Page page) {
		System.out.println(page.getUrl()+"--"+page.getValues().get("price"));
	}

}
