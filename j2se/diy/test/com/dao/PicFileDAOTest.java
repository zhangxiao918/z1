package com.dao;

import java.io.File;
import java.util.List;

import org.junit.*;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.util.StringUtils;
import com.utils.FileUtils;

public class PicFileDAOTest {

	PicFileDao picFiledao = null;
	WebSiteDao webSiteDao = null;
	ArticleDao articleDao = null;
	List<PicfileBean> list = null;
	List<WebsiteBean> webList = null;
	List<Article> articleList = null;
	final static String FILE_SERVER = "D:\\fileserver\\imageTest\\";
	@Before
	public void init(){
		picFiledao = DAOFactory.getInstance().getPicFileDao();
		webSiteDao = DAOFactory.getInstance().getWebSiteDao();
		articleDao = DAOFactory.getInstance().getArticleDao();
	}
	
	@After
	public void destory(){
		if(null != picFiledao)
			picFiledao = null;
		if(null != webSiteDao)
			webSiteDao = null;
		if(null != list)
			list.clear();
		if(null != webList)
			webList.clear();
		if(null != articleList)
			articleList = null;
		System.gc();
		System.out.println("gc");
	}
	
	public void find() throws Exception{
		list = picFiledao.findAll();
		webList = webSiteDao.findByParentId(600);
		Assert.assertNotNull(list);
		System.out.println("总数:"+list.size());
	}
	
	public void findByid() throws Exception{
		
	}
	
	@Test
	public void update() throws Exception{
		webList = webSiteDao.findByParentId(600);
		PicfileBean bean = picFiledao.findById(321321);
		Assert.assertNotNull(bean);
		System.out.println("name:"+bean.getUrl()+bean.getName());
		int start = bean.getName().lastIndexOf(File.separator)+1;
		int smnStart = bean.getSmallName().lastIndexOf(File.separator)+1;
		String prx = StringUtils.gerDir(String.valueOf(bean.getArticleId()));
		String fileName = prx+bean.getArticleId()+File.separator+bean.getName().substring(start);
		String smallFileName = prx+bean.getArticleId()+File.separator+bean.getSmallName().substring(smnStart);
		String source = bean.getUrl() + bean.getName();
		String target = FILE_SERVER+fileName;
		String smgTarget = FILE_SERVER + smallFileName;
		bean.setName(fileName);
		
		System.out.println("source:"+source);
		System.out.println("target:"+target);
		System.out.println("smgTarget:"+smgTarget);
		if(FileUtils.copyFile(source, target)){
			System.out.println(">> 大图成功!!!");
		}
		
		if(FileUtils.copyFile(bean.getUrl()+bean.getSmallName(), smgTarget)){
			System.out.println(">> 小图成功!!!");
		}else{
//			if(FileUtils.deleteFile(target)){
				System.out.println(">> 生成小图失败,删除大图");
//			}
		}
		
		
//		System.out.println("name:"+bean.getName().substring(0, bean.getName().lastIndexOf(File.separator)));
//		System.out.println("smallname:"+bean.getSmallName());
	}
}
