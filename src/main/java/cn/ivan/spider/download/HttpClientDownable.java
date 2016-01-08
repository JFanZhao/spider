package cn.ivan.spider.download;

import cn.ivan.spider.domain.Page;
import cn.ivan.spider.utils.PageUtils;

public class HttpClientDownable implements Downloadable{

	public Page download(String url) {
		// TODO Auto-generated method stub
		Page page = new Page();
		page.setUrl(url);
		page.setContent(PageUtils.getContent(url));
		return page;
	}

}
