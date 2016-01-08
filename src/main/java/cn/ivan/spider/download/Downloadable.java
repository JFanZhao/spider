package cn.ivan.spider.download;

import cn.ivan.spider.domain.Page;

public interface Downloadable {
	public Page download(String url);
}
