package com.chinamilitary.test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.util.HttpClientUtils;

public class TestHttpClient {
	
	static final String URL1 = "http://www.showimg.com/tabulation.php?mid=6775";
	
	public static HttpClient httpclient = new HttpClient();
	
	public static GetMethod getMethod = null;
	
	public static PostMethod postMethod = null;
	
	static ArticleDao  articleDao = DAOFactory.getInstance().getArticleDao();
	
	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();
	
	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();
	
	static HashMap<String,String> LINK_NOT_OK = new HashMap<String,String>();
	
	static void test1(String url) {
		try {
			getMethod = new GetMethod(url);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
			// 执行getMethod
			int statusCode = httpclient.executeMethod(getMethod);
			System.out.println("URL:"+url+",状态码:"+statusCode);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("Method failed: "+ getMethod.getStatusLine());
				System.err.println("URL: "+ url);
				System.out.println();
			}
			getMethod.releaseConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		try{
//			List<Article> list = articleDao.findByWebId(1);
//			for(Article bean:list){
//				test1(bean.getArticleUrl());
//			}
//			boolean isTrue = urlValidation("http://bizhi.zhuoku.com/wall/jie/20070203/0721/001.jpg");
//			System.out.println("页面是否合法:"+isTrue);
			
//			getResponseHeaders("http://image.tuku.china.com/tuku.military.china.com/military//pic/2010-08-09/f003abea-7bd8-478b-b9f8-a11b59d6a0e0.jpg");
//			getResponseHeaders("http://bizhi.zhuoku.com/wall/jie/20070203/0721/001.jpg");
//			getResponseHeaders("https://api.twitter.com/oauth/authorize?oauth_token=oMjOck4z2kwxrXHis9VbJdUbgoh2Ez6UVaS6as5U3KU");
//			boolean isTrue = urlValidation("https://api.twitter.com/oauth/authorize?oauth_token=oMjOck4z2kwxrXHis9VbJdUbgoh2Ez6UVaS6as5U3KU");
//			System.out.println("页面是否合法:"+isTrue);
			
//			pitchImage();
//			getImgInfoFromResponse();
			
			
//			Iterator it = LINK_NOT_OK.keySet().iterator();
//			System.out.println("无效链接如下\n");
//			while(it.hasNext()){
//				String key = (String)it.next();
//				System.out.println("URL["+key+"] \t状态码["+LINK_NOT_OK.get(key)+"]");
//			}
			
			postapplist();
			
			LINK_NOT_OK.clear();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean urlValidation(String url){
		try {
			getMethod = new GetMethod(url);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
			// 执行getMethod
//			getMethod.addRequestHeader("Referer", "http://www.bizhizhan.com/junshikeji/junshikeji-26-8806_2.html");
//			getMethod.addRequestHeader("Referer", "http://www.zhuoku.com/zhuomianbizhi/computer-pcother/20070203130513(1).htm");
			int statusCode = httpclient.executeMethod(getMethod);
			if (statusCode != HttpStatus.SC_OK) {
				System.err.println("地址["+url+"],状态码["+statusCode+"]");
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			getMethod.releaseConnection();
		}
		return true;
	}
	
	static void getResponseHeaders(String url){
		long start = System.currentTimeMillis();
		try{
			getMethod = new GetMethod(url);
			getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
			getMethod.addRequestHeader("Referer", "http://www.zhuoku.com/zhuomianbizhi/computer-pcother/20070203130513(1).htm");
			// 执行getMethod
			int statusCode = httpclient.executeMethod(getMethod);
			if(statusCode == HttpStatus.SC_OK){
				String length = getMethod.getResponseHeader("Content-Length").getValue();
				String type = getMethod.getResponseHeader("Content-Type").getValue();
				long end = System.currentTimeMillis();
				System.out.println("响应体类型:"+type+"\t响应头长度:"+length + "\t耗时:"+(end-start));
				Header[] headers = getMethod.getResponseHeaders();
				for(Header header:headers){
					System.out.println(header.getName()+":"+header.getValue());
//					System.out.println("Value:"+header.getValue());
				}
			}else{
				LINK_NOT_OK.put(url, String.valueOf(statusCode));
			}
			getMethod.releaseConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void getImgInfoFromResponse(){
		try{
			List<ImageBean> list = imageDao.findImage(36, "FD");
			for(ImageBean img:list){
				getResponseHeaders(img.getHttpUrl());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void pitchImage(){
		try{
			List<WebsiteBean> webSites = webSiteDao.findAll();
			for(WebsiteBean website:webSites){
				List<ImageBean> imgList = imageDao.findImage(website.getId());
				for(ImageBean imgBean:imgList){
					long start = System.currentTimeMillis();
					String length = HttpClientUtils.getHttpHeaderResponse(imgBean.getHttpUrl(),"Content-Length");
					if(null != length){
						imgBean.setFileSize(Long.valueOf(length));
						imgBean.setStatus(1);
						if(imageDao.update(imgBean)){
							long end = System.currentTimeMillis();
							System.out.println("更新["+imgBean.getTitle()+"|"+imgBean.getId()+"]成功,耗时:"+(end-start));
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	static void postapplist(){
		try{
			postMethod  = new PostMethod("http://218.213.81.103:8080/cache.do?saction=sync");
	        NameValuePair tableId = new NameValuePair("tableId","5");
	        NameValuePair keyArray = new NameValuePair("keys","1555,1556,1557,1558");
	        postMethod.setRequestBody(new NameValuePair[]{tableId,keyArray});
	        int stats = httpclient.executeMethod(postMethod);
	        if(stats == 200){
	        	byte[] result = postMethod.getResponseBody();
	        	System.out.println("返回结果"+new String(result));
	        }
	        System.out.println(stats);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			postMethod.releaseConnection();
		}
	}
}
