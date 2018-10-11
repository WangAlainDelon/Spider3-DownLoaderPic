package com.wx.downloadpicture3;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
public class PicPageProsessor implements PageProcessor {
	private final static Site site=Site.me()
			.setRetryTimes(3)
			.setSleepTime(1000)
			.addHeader("Connection", "keep-alive")
			.addHeader("Cache-Control", "max-age=0")
			.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
	private static ChromeDriverService chromeservier;
	private static int count=0;
	private static WebDriver getWebDriver() throws IOException
	{
		System.setProperty("webdriver.chrome.driver","D:/Program Files (x86)/Google/Chrome/Application/chrome.exe");
		chromeservier = new ChromeDriverService.Builder().usingDriverExecutable(new File("D:/tools/chromedriver_win32/chromedriver.exe")) .usingAnyFreePort().build();
		chromeservier.start();
		return new RemoteWebDriver(chromeservier.getUrl(), DesiredCapabilities.chrome());
		
	}
	public void process(Page page) {
		
		if(page.getUrl().regex("https://pic\\.sogou\\.com/pics\\?query=%C0%AD%C8%F8&di=2&_asf=pic\\.sogou.com&w=05009900&sut=8455&sst0=1539229825463").match())
		{
			System.out.println("到达爬取的页面");
			try {
				WebDriver driver=getWebDriver();
				driver.get("https://pic.sogou.com/pics?query=%C0%AD%C8%F8&di=2&_asf=pic.sogou.com&w=05009900&sut=8455&sst0=1539229825463");
				Thread.sleep(3000);
				//逐渐滚动浏览器窗口，令ajax逐渐加载
				String js;
				for(int i=0;i<=1;i++)
				{	 				 
					 js = "var q=document.documentElement.scrollTop=" + 500 * i;
					 //先将driver转换为可执行js的接口JavascriptExecutor
					 ((JavascriptExecutor)driver).executeScript(js);
					 Thread.sleep(1000);
					 //利用chromedriver提取页面的信息,主要是提取图片a标签的属性值，然后添加到爬取队列中
					 //不需要定义Set,队列有自动的去重功能
					 List<WebElement> findElements = driver.findElements(By.xpath("//div[@id='imgid']/ul/li/a[2]"));
					 for (int j = 0; j < findElements.size(); j++) {
						 String href = findElements.get(j).getAttribute("href");
						 page.addTargetRequest(href);
					}
				}
				//关闭浏览器
				driver.close();
				driver.quit();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
		
		//否则到达图片下载页面
		else if(page.getUrl().regex("https://pic\\.sogou\\.com/d\\?query=%C0%AD%C8%F8&mode=1&did=\\d+").match())
		{
			System.out.println("=========================到达下载图片数据的页面====================");
			//发现单张的图片也是动态加载，所以只能用chromedriver来解决了
//			Html html = page.getHtml();
//			System.out.println(html);
//			Selectable xpath = html.xpath("//a[@id='imageBox']/img/@src");
			String string = page.getUrl().toString();
			
			try {
				WebDriver driver=getWebDriver();
				driver.get(string);
				Thread.sleep(3000);
				WebElement findElement = driver.findElement(By.xpath("//a[@id='imageBox']/img"));
				if(findElement!=null)
				{
					String text = findElement.getAttribute("src");
					System.out.println(text+"=========");
					URL url;
					InputStream inputStream;
					ByteArrayOutputStream byteArrayOutputStream;
					FileOutputStream outputstream;
					try {
						url = new URL(text);
						URLConnection conn = url.openConnection();
						//读图片的操作是先把图片到入流读到内存缓存，再从内存缓存中读到磁盘
						inputStream = conn.getInputStream();
						byteArrayOutputStream=new ByteArrayOutputStream();
						byte[] buff=new byte[1024];
						int len=0;
						while((len=inputStream.read(buff))!=-1)
						{
							byteArrayOutputStream.write(buff, 0, len);
						}
						File file=new File("D://image//"+count+".jpg");
						count++;
						outputstream=new FileOutputStream(file);
						outputstream.write(byteArrayOutputStream.toByteArray());
						inputStream.close();
						byteArrayOutputStream.close();
						outputstream.close();
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
					driver.close();
					driver.quit();
				}		
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
							
		}		
				
	}
	public Site getSite() {		
		return site;
	}	
}
