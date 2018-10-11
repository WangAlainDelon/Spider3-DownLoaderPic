package com.wx.downloadpicture3;

import us.codecraft.webmagic.Spider;

public class SpiderPic3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
         //模拟鼠标滚动来加载数据
		Spider.create(new PicPageProsessor())
		.addUrl("https://pic.sogou.com/pics?query=%C0%AD%C8%F8&di=2&_asf=pic.sogou.com&w=05009900&sut=8455&sst0=1539229825463")
		.thread(5)
		.run();
	}
}
