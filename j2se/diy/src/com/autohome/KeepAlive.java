package com.autohome;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.chinamilitary.util.DateUtils;

public class KeepAlive {
	
	String FORUM_URL = "http://club.autohome.com.cn/bbs/forum-c-2001-1.html";

	private void keepAlive(){
		URL url = null;
		HttpURLConnection connection = null;
		OutputStream out = null;
		InputStream in = null;
		ByteArrayOutputStream byteArray = null;
		try{
			url = new URL(FORUM_URL);
			connection = (HttpURLConnection)url.openConnection();
			connection.addRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
			connection.addRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			connection.addRequestProperty("Accept", "*/*");
			connection.addRequestProperty("Origin", "http://club.autohome.com.cn");
			connection.addRequestProperty("Connection", "Keep-Alive:600");
			connection.addRequestProperty("Referer", FORUM_URL);
			connection.addRequestProperty("Cookie", "ASP.NET_SessionId=i5rklwmtposzhh55egotym55; isLoginedWeb=T");
			connection.addRequestProperty("Cache-Control", "no-cache");
			connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.63 Safari/535.7");
			connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			connection.addRequestProperty("Cookie", "__utma=37235314.1856846134.1321246603.1323769135.1323772004.40; area=330199; sessionip=115.238.91.226; UP=30; CKVER=1; samemappingcookie=m; UN=%e6%9b%be%e7%bb%8fde%e6%b5%81%e6%98%9f%e9%9b%a8; UID=4212192; UC=2001; MISUSER=2; CKVER=1; Roleid=0; isCarOwnerCamp=1; bbsOfMaster=; picture=userheaders/2012/6/13/300500d0-c220-487b-af1e-c109cd251b23_b30422f7-1475-4086-9d06-7060b5e5497b_120X120_545X800_22X1_138X138.jpg; pcpopclub=74A96643E4093218E0C894DE59BB26867805001F132A69E43148C00EEF0F442A4CD7E5D16513441D16E6547DFD0418FE6D3CAE74251FEDC996782D332205DC0F046E705C501C389AC1D62785E17032201565324A14C4E602EB9FD8CE2D82283E6C71AB5C002248455D6443513F1A843549A722D62E760EB6E6F6B82479885432F2C9F002EE3A8E97708BA0BA815C7A0F1A684815B556E89A20647A31A152CBFA4069CBF5EA29368870FFB4D1815C87F6DA6A9639816F76B9A346CDF5877B224465D8F864; ssoUN=186*****690|4212192|3; Hm_lvt_90ad5679753bd2b5dec95c4eb965145d=1340602656708; cookieKnow=1; sessionid=ef1989f3-d75e-45a4-ae9a-178bcdc5b599; smsso=oDA2TQEmTgY7YqU4qYbsCGIS52JmdH-bxuAf66uvrEE7XYCsbFK8jg; historybbsName2=c-2001%7C%E5%93%88%E5%BC%97M2%2Cc-2476%7C%E4%BC%98%E4%BC%98%2Cc-2002%7C%E8%B5%B7%E4%BA%9AMagentis%2Cc-2003%7C%E6%96%AF%E6%9F%AF%E8%BE%BERoomster%20; __utma=1.1484792975.1322543017.1341971128.1341974893.279; __utmb=1.0.10.1341974893; __utmc=1; __utmz=1.1341827251.272.30.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=Android%20%E6%93%8D%E6%8E%A7%E6%B1%BD%E8%BD%A6; Hm_lvt_a14e5f772a500c6bc3f506e86c3567c2=1341976175989; Hm_lpvt_a14e5f772a500c6bc3f506e86c3567c2=1341976175989; sessionprovince=36; sessioncity=538; sessionvid=DE23DFE2-95ED-13CB-E3BB-17FB55789DFA");
			connection.setConnectTimeout(15*1000);
			connection.connect();
			
			
			int code = connection.getResponseCode();
			switch(code){
				case 200:
					in = connection.getInputStream();
					byteArray = new ByteArrayOutputStream();
					int ch;
					while((ch = in.read()) != -1){
						byteArray.write(ch);
					}
					byteArray.flush();
					break;
				default:
					System.err.println(connection.getResponseCode()+":"+connection.getResponseMessage());
					break;
			}
			
			if(null != out){
				out.close();
			}
			if(null != in){
				in.close(); 
			}
		}catch(Exception e){
			if(null != byteArray){
				try {
					byteArray.close();
				} catch (IOException e1) {
					System.err.println(e1);
				}
			}
			e.printStackTrace();
		}
	}
	
	public void doKeepAlive(){
		ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor();
		schedule.scheduleWithFixedDelay(new Runnable(){
			public void run(){
				System.out.println(DateUtils.getNow());
				keepAlive();
			}
		}, 5000L, 10*60*1000L, TimeUnit.MILLISECONDS);
	}
	
	public static void main(String args[]){
		KeepAlive keepAlive = new KeepAlive();
		keepAlive.doKeepAlive();
	}
}
