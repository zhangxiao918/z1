package com.chinamilitary.test;

import java.util.List;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;

public class Test {
	
	public void test() throws Exception{
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String url = "http://tuku.military.china.com/military//pic/2009-12-23/8b939fd7-e8ea-4eb4-b818-3ca85a2f0dd3.jpg";
//		String fileName = url.substring(url.lastIndexOf("/")+1,url.length());
//		fileName = fileName.replace(".", "_s.");
//		System.out.println("fileName:"+fileName);
		
		try{
			ImageDao dao = DAOFactory.getInstance().getImageDao();
//			ImageBean bean = dao.findByLink("NED", 47);
			 MemcacheClient client = MemcacheClient.getInstance();
			WebSiteDao webDAO = DAOFactory.getInstance().getWebSiteDao();
			ArticleDao  articleDao = DAOFactory.getInstance().getArticleDao();
			PicFileDao picFileDao = DAOFactory.getInstance().getPicFileDao();
			List<WebsiteBean>  webList = webDAO.findAll();
			if(null != webList && webList.size() > 0){
				//添加webList
//				client.add("127.0.0.1:11211:WEBSITE:LIST_BY_WEBID:", webList);
				for(WebsiteBean wbean:webList){
					List<Article>  articleList = articleDao.findByWebId(wbean.getId());
					if(null != articleList && articleList.size() > 0){
						//添加articleList
						client.add("127.0.0.1:11211:ARTICLE:LIST_BY_WEBID:"+wbean.getId(), articleList);
						for(Article article:articleList){
							List<ImageBean> imageList = dao.findImage(article.getId());
							if(null != imageList && imageList.size() > 0){
								//添加到缓存
								client.add("127.0.0.1:11211:IMAGE:IMAGE_LIST_BY_ARTICLE:"+article.getId(), imageList);
								for(ImageBean imageBean:imageList){
									client.add("127.0.0.1:11211:IMAGE:IMAGE_BY_ID:"+imageBean.getId(), imageBean);
								}
							}
							
							List<PicfileBean> picList = picFileDao.findByArticleId(article.getId());
							if(null != picList && picList.size() > 0){
								client.add("127.0.0.1:11211:PICFILE:PICFILE_LIST_BY_ARTICLE:"+article.getId(), picList);
								for(PicfileBean picfileBean:picList){
									client.add("127.0.0.1:11211:PICFILE:PICFILE_BY_ID:"+picfileBean.getId(), picfileBean);
								}
							}
						}
						client.add("127.0.0.1:11211:WEBSITE:WEBSITE_BY_ID:"+wbean.getId(), wbean);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
