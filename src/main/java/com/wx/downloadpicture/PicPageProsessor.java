package com.wx.downloadpicture;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;


import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class PicPageProsessor implements PageProcessor {

	private final static Site site=new Site()
                                       .setRetryTimes(3)
                                       .setSleepTime(1000)
                                       .addHeader("Connection", "keep-alive")
                                       .addHeader("Cache-Control", "max-age=0")
                                       .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
	
	public void process(Page page) {		
		if(page.getUrl().regex("https://pic\\.sogou\\.com/pics\\?query=%CD%BC%C6%AC&p=40230500&st=255&mode=255").match())
		{
			System.out.println("匹配到链接");
			//抽取图片的地址用流来读取图片
			List<String> links = page.getHtml().regex("https://img\\d+\\.sogoucdn\\.com/app/a/\\S+\\.jpg").all();
			String urlstr;
			InputStream inputStream;
			ByteArrayOutputStream outputStream;
			int count=0;
			for (String string : links) {
				//可以拿到链接访问数据下载图片
				try {
					String[] split = string.split("\"");
					urlstr=split[0];
					System.out.println(urlstr);
					URL url=new URL(urlstr);
					URLConnection openConnection = url.openConnection();
					//先把文件读到缓冲区，再从缓冲区写出去
					inputStream = openConnection.getInputStream();
					outputStream=new ByteArrayOutputStream();
					byte[] b=new byte[1024];
					int len=0;
					while((len=inputStream.read(b))!=-1)
					{
						outputStream.write(b, 0, len);
					}
					//把缓冲区的数据保存到本地
					File file=new File("D:/image/"+count+".jpg");
					count++;
					FileOutputStream fileoutputstream=new FileOutputStream(file);
					fileoutputstream.write(outputStream.toByteArray());
					//关闭流
					inputStream.close();
					outputStream.close();
					fileoutputstream.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
	        
		}
	}

	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

}
