package com.algorithm;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;

import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.util.HttpClientUtils;

public class URLMatchTest {

	private List<String> urlList = new ArrayList<String>();
	private List<String> resultList = new ArrayList<String>();
	String[] stringarray = {};
	static String URL = "http://www.zhuoku.com/zhuomianbizhi/movie-oumei/20111006134002.htm";
    static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
	static Integer lastTotal = 0;
	@Before
	public void init(){
		urlList.add("http://www.9ku.com/");
		urlList.add("http://sc.chinaz.com/");
		urlList.add("http://www.zhuoku.com/");
		urlList.add("http://www.zhuoku.com/new/");
		urlList.add("http://desk.zhuoku.com/zhuomianzhuti/533");
		urlList.add("http://www.zhuoku.com/zhuomianbizhi/");
		urlList.add("http://www.zhuoku.com/zhuomianbizhi/movie-oumei/");
	}
	
	@After
	public void destory(){
		if(null != urlList && urlList.size() > 0){
			urlList.clear();
			urlList = null;
		}
		
		if(null != resultList && resultList.size() > 0){
			resultList.clear();
			resultList = null;
		}
		
	}
	
	public void run(){
		int i=0;
		for(String url:urlList){
			if(URL.startsWith(url)){
				resultList.add(url);
			}
		}
		
		
		
		if(null != resultList && resultList.size() > 0){
			stringarray = new String[resultList.size()];
			for(int j=0;j<resultList.size();j++){
				stringarray[j] = resultList.get(j);
			}
			
			sort(stringarray);
			for(String tmp:stringarray){
				System.out.println(tmp);
			}
		}
	}
	
	public static void main(String args[]){
		//17963
	    exec.scheduleWithFixedDelay(new Runnable(){
	    	public void run() {
	    		try{
					long start = System.currentTimeMillis();
					byte[] body = HttpClientUtils
							.getResponseBodyAsByte(
									"http://yc.jxcdc.cn/default.aspx",
									"ASPSESSIONIDCABADTTC=EIFDIDLADCPBMEGLINAJJMBP; CNZZDATA1544465=cnzz_eid=66762208-1344928683-http%253A%252F%252Fyc.jxcdc.cn%252F&ntime=1344928683&cnzz_a=7&retime=1344929753213&sin=&ltime=1344929753213&rtime=0",
									"http://count.knowsky.com/count1/count.asp?id=54956&sx=1&ys=9");
					if(null == body){
						System.out.println("\t内容为空!");
					}
					System.out.println("\t>> 耗时:"
							+ (System.currentTimeMillis() - start));
					try {
						String ct = new String(body,"GBK");
						int s = ct.lastIndexOf("累计访问：");
						int e = ct.lastIndexOf("&#10;");
						if(s != 0 && e != 0){
							String total = ct.substring(s+"累计访问：".length(),e);
							Integer tmpTotal = 0;
							try{
								tmpTotal = Integer.parseInt(total);
							}catch(Exception e2){
								System.err.println("字符串转成数字失败!");
							}
							int offset = tmpTotal-lastTotal; 
							if(offset > 1){
								System.out.println("有其他人访问，访问数量为:"+offset);
							}
							lastTotal = tmpTotal;
						}
						System.out.println("累计访问量为:"+lastTotal);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
	    		}catch(Exception e){
	    			e.printStackTrace();
	    		}
			}
	    }, 5*1000L,5*1000L, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 字符长度从大到小排序
	 * @param s1
	 */
	void sort(String[] s1){
		String tmp;
		for(int i=0;i<s1.length;i++){
			for(int j=0;j<s1.length-i-1;j++){
				if(s1[j].length() < s1[j+1].length() ){
					tmp = s1[j];
					s1[j] = s1[j+1];
					s1[j+1] = tmp;
				}
			}
		}
	}
	
	
	void sort(List<WebsiteBean> wlist){
		WebsiteBean tmp;
		for(int i=0;i<wlist.size();i++){
			for(int j=0;j<wlist.size()-i-1;j++){
				WebsiteBean w1 = wlist.get(j);
				WebsiteBean w2 = wlist.get(j+1);
				if(null != w1 && null != w2){
					if(w1.getUrl().length() < w2.getUrl().length()){
						tmp = w1;
						w1 = w2;
						w2 = tmp;
					}
				}
			}
		}
	}

}
