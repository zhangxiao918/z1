package com.edmunds;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.HtmlPage;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.StringUtils;
import com.common.Constants;

public class EdmundsParser {
	
	
	private static final String PIC_SAVE_PATH = Constants.FILE_SERVER;//"d:\\share\\zhuoku\\";//"d:\\share\\zhuoku\\";

	static final Integer D_PARENT_ID = 1550;
	
	private static final String URL = "http://www.edmunds.com/";

	static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	static List<Article> ARTICLELIST = new ArrayList<Article>();

	static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	static MemcacheClient client = MemcacheClient.getInstance();

	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();
	
	static int COUNT = 0 ;

	
	public static void main(String args[]){
		try{
//			catalog(URL);
			getCarSerial();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取第一级分类链接
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void catalog(String url) throws Exception { // WebsiteBean bean
		Parser parser = new Parser();
		parser.setURL(url);
		parser.setResource("E:\\2.WS\\1.PRODUCT\\diy_page\\src\\com\\edmunds\\index.html");
		NodeFilter fileter = new NodeClassFilter(SelectTag.class);
		NodeList list = parser.extractAllNodesThatMatch(fileter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "hp-makes")); //id makes_list_module

		if (null != list && list.size() > 0) {
			SelectTag table = (SelectTag) list.elementAt(0);
			
			OptionTag[] options = table.getOptionTags();
			WebsiteBean tmp = null;
			for(OptionTag op:options){
				if(null != op.getValue() && !"".equals(op.getValue())){
					tmp = new WebsiteBean();
					tmp.setName(op.getOptionText().trim());
					if (!op.getValue().startsWith("http://")) {
						tmp.setUrl(URL + op.getValue());
					} else {
						tmp.setUrl(op.getValue());
					}
					if(null == client.get(tmp.getUrl())){
						tmp.setParentId(D_PARENT_ID);
						boolean b = webSiteDao.insert(tmp);
						if (b) {
							client.add(tmp.getUrl(), tmp.getUrl());
							System.out.println("成功");
						} else {
							System.out.println("失败");
						}
						System.out.println(" >> "+tmp.getName()+"|"+tmp.getUrl());
					}else{
						System.err.println(" >> 已添加过该站点！");
					}
				}
			}
		}
		if (null != parser)
			parser = null;
	}
	
	public static void getCarSerial(){
		COUNT = 0;
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
			for(WebsiteBean bean:list){
				getCarSerial2(bean);
				break;
			}
			System.out.println(" >> COUNT:"+COUNT);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getCarSerial2(WebsiteBean web){
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(web.getId());
			if(list.size() > 0){
				for (WebsiteBean bean : list) {
					getCarSerial2(bean);
				}
			}else{
				//获取车型信息
				Parser p1 = new Parser();
				p1.setURL(web.getUrl());
				p1.setEncoding("UTF8");
				NodeFilter fileter = new NodeClassFilter(Div.class);
				NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
							new HasAttributeFilter("id", "vcards"));
				if(null != nodeList && nodeList.size() > 0){
					Div div = (Div)nodeList.elementAt(0);
					System.out.println(div.toHtml());
					System.out.println();
				}
				
				if(null != p1)
					p1 = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
