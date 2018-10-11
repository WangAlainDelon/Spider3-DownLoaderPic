package com.wx.downloadpicture;

import java.net.URL;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;

public class SpiderPic {

	//https://blog.csdn.net/qixinbruce/article/details/54883233
	//找到了动态加载的链接和规律，但是先用自动化测试工具试试,这里没有实现采集动态加载数据的功能
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassLoader classloder=SpiderPic.class.getClassLoader(); 
		URL resource = classloder.getResource("config.ini");
		String path = resource.getPath();
		System.out.println(path);
		System.setProperty("selenuim_config",path);
		Spider spider=Spider.create(new PicPageProsessor());
		spider.setDownloader(new SeleniumDownloader("D:/tools/chromedriver_win32/chromedriver.exe"));
		spider.addUrl("https://pic.sogou.com/pics?query=%CD%BC%C6%AC&p=40230500&st=255&mode=255")
		.thread(5).run();
		
	}

}
