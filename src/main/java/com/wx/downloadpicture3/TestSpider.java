package com.wx.downloadpicture3;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

public class TestSpider implements PageProcessor {

	private final static Site site=Site.me()
			.setRetryTimes(3)
			.setSleepTime(1000)
			.addHeader("Connection", "keep-alive")
			.addHeader("Cache-Control", "max-age=0")
			.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
	public static void main(String[] args) {
		// TODO Auto-generated method stub
         Spider.create(new TestSpider())
         .addUrl("https://pic.sogou.com/d?query=%C0%AD%C8%F8&mode=1&did=1#did0")
         .thread(5).run();
	}
 
	public void process(Page page) {
		// TODO Auto-generated method stub
		String string = page.getUrl().toString();
		System.out.println(string);
	}

	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
