package com.wx.downloadpicture2;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class PicPageProsessor implements PageProcessor {
    private final static Site site=Site
    		.me()
    		.setRetryTimes(3)
    		.setSleepTime(1000)
    		.addHeader("Connection", "keep-alive")
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
    private static int count=0;
	public void process(Page page) {
		//请求的是json数据现在要解析json，提取图片的链接，再请求图片的链接下载图片
		//page.getRawText()返回的就是json格式的字符串
		String josn=page.getRawText();
		//这里有些数据是smallThumbUrl属性下，有些图片在pic_url下
		List<String> picurlList1 = new JsonPathSelector("$.items[*].smallThumbUrl").selectList(josn);
		List<String> picurlList2 = new JsonPathSelector("$.items[*].pic_url").selectList(josn);
		//现在将两个集合去重合并
		Set<String> setlist=new HashSet<String>();
		setlist.addAll(picurlList1);
		setlist.addAll(picurlList2);
	
		InputStream inputstream;
		ByteArrayOutputStream byteArrayOutputStream;
		
		//String regex = "https://img\\d+\\.sogoucdn\\.com/app/a/\\S+\\.jpg";
		for (String string : setlist) {
			if(string.endsWith(".jpg"))
			{				
				//拿到图片的url就可以下载了				
				try {
					System.out.println(string);
					URL url = new URL(string);
					HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
					//如果返回成功才抓取图片
					if(httpconn.getResponseCode()==200)
					{
						System.out.println(httpconn.getResponseCode());
						inputstream = httpconn.getInputStream();
						byteArrayOutputStream=new ByteArrayOutputStream();					
						byte[] buff=new byte[1024];
						int len=0;
						//把数据读到缓冲区
					    while((len=inputstream.read(buff))!=-1)
					    {
					    	byteArrayOutputStream.write(buff, 0, len);
					    }
						//从缓冲区把数据读出来
					    File file=new File("D://image//"+count+".jpg");
					    count++;
						FileOutputStream outputstream=new FileOutputStream(file);
						outputstream.write(byteArrayOutputStream.toByteArray());
						//关闭流
						inputstream.close();
						byteArrayOutputStream.close();
						outputstream.close();
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
				
				
			}
		}
	}
	public Site getSite() {		
		return site;
	}
}
