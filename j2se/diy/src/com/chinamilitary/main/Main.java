package com.chinamilitary.main;

import java.util.List;

import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.threadpool.ThreadPoolManager;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
		try{
			List<WebsiteBean> list = dao.findByParentId(36);
			
			int count = list.size();
			
			ThreadPoolManager theadPool = new ThreadPoolManager(count);
			
			for(WebsiteBean bean:list){
				theadPool.process(bean.getId());
			}
			
//			theadPool.process();
//			System.out.println("根目录"+dao.findByParentId(1).size());
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
