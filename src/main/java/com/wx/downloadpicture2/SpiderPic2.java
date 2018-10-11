package com.wx.downloadpicture2;
import us.codecraft.webmagic.Spider;
public class SpiderPic2 {
    //爬取鼠标滚动加载的数据，找到了后台传输数据的地址，所以就是爬取分页的数据
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//费明细页面得知，最多以图片为关键字检索得到图片最多49页，偏移量为48
		Spider spider = Spider.create(new PicPageProsessor());
		for(int i=0;i<49;i++)
		{		  
		  spider.addUrl("https://pic.sogou.com/pics?query=%CD%BC%C6%AC&st=255&mode=255&start="+i*48+"&reqType=ajax&reqFrom=result&tn=0")
				.thread(5)
				.run();
		}
	}
}
