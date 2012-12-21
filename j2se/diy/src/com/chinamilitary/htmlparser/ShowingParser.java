package com.chinamilitary.htmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.ResultBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;

public class ShowingParser {

	private static String URL = "http://www.showimg.com/";

	private static final String TUKU_URL = "http://www.showimg.com/htm/";

	private static List<LinkBean> LINKLIST = new ArrayList<LinkBean>();

	private static List<Article> ARTICLELIST = new ArrayList<Article>();

	private static HashMap<String, LinkBean> LINKHASH = new HashMap<String, LinkBean>();

	private static int COUNT = 0;

	/**
	 * 获取链接
	 * 
	 * @throws Exception
	 */
	static void getLink() throws Exception {
		Parser p1 = new Parser();
		p1.setURL(URL);

		NodeFilter filter = new NodeClassFilter(LinkTag.class);
		NodeList list = p1.extractAllNodesThatMatch(filter);
		LinkBean bean = null;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				LinkTag link = (LinkTag) list.elementAt(i);
				if (link.getLink().startsWith(TUKU_URL)) {
					if (link.getLink().equals(URL)) {
						getLink(link.getLink());
					}
					bean = new LinkBean();
					bean.setLink(link.getLink());
					String name = StringUtils
							.illageString(link.getLinkText() == null ? (link
									.getAttribute("title") == null ? "无话题"
									: link.getAttribute("title")) : link
									.getLinkText());
					if (name.indexOf("“") != -1 && name.indexOf("”") != -1) {
						name = name.replaceAll("“", "");
						name = name.replace("”", "");
					}
					// 判断连接中是否存在创建文件夹时的非法字符
					if (name.indexOf("\"") != -1 && name.indexOf("\"") != -1) {
						name = name.replace("\"", "");
					}
					bean.setName(name);
					if (name.length() > 0 && name.length() < 3) {
						LINKHASH.put(bean.getName(), bean);
					}
				}

			}
		}
		p1 = null;
	}

	/**
	 * 根据URL获取需要的链接
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void getLink(String url) throws Exception {
		Parser p1 = new Parser();
		p1.setURL(url);

		NodeFilter filter = new NodeClassFilter(LinkTag.class);
		NodeList list = p1.extractAllNodesThatMatch(filter);
		LinkBean bean = null;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				LinkTag link = (LinkTag) list.elementAt(i);
				if (link.getLink().startsWith(TUKU_URL)) {
					bean = new LinkBean();
					bean.setLink(link.getLink());
					String name = StringUtils
							.illageString(link.getAttribute("alt") == null ? (link
									.getLinkText() == null ? "无话题" : link
									.getLinkText())
									: link.getAttribute("title"));
					if (name.indexOf("“") != -1 && name.indexOf("”") != -1) {
						name = name.replaceAll("“", "");
						name = name.replace("”", "");
					}
					// 判断连接中是否存在创建文件夹时的非法字符
					if (name.indexOf("\"") != -1 && name.indexOf("\"") != -1) {
						name = name.replace("\"", "");
					}
					bean.setName(name);
					if (name.length() > 0 && name.length() < 3) {
						LINKHASH.put(bean.getName(), bean);
					}
				}
			}
		}
		p1 = null;
	}

	/**
	 * 解析页面数据
	 * 
	 * @param url
	 * @throws Exception
	 */
	static void test(WebsiteBean bean) throws Exception {
		Parser p1 = new Parser();
		p1.setURL(bean.getUrl());

		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = p1.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "damulu-left2"));
		if (list != null && list.size() > 0) {
			ResultBean result = pageing(bean.getUrl(), "id", "damulu-page");
			if (result.isBool()) {
				System.out.println("需要解析分页:" + result.getList().size());
				List<LinkBean> linkList = result.getList();
				if (linkList != null && linkList.size() > 0) {
					for (LinkBean linkBean : linkList) {
						parserLink(linkBean, bean.getId());
					}
				}
			} else {
				System.out.println("不需要解析分页");
			}

		}
		p1 = null;
	}

	static void parserLink(LinkBean linkBean, Integer webId) throws Exception {
		Parser p1 = new Parser();
		p1.setURL(linkBean.getLink());

		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = p1.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("class", "damulu-left2"));
		if (list != null && list.size() > 0) {
			Parser parser = new Parser();
			parser.setInputHTML(list.toHtml());
			NodeFilter filter1 = new NodeClassFilter(LinkTag.class);
			NodeList list1 = parser.extractAllNodesThatMatch(filter1);
			if (list1 != null && list1.size() > 0) {
				Article article = null;
				for (int i = 0; i < list1.size(); i++) {
					LinkTag link = (LinkTag) list1.elementAt(i);
					if (link.getLinkText() != null
							&& !link.getLinkText().equalsIgnoreCase("")) {
						article = new Article();
						article.setWebId(webId);
						article.setArticleUrl(URL
								+ link.getLink().replace("../", "/"));
						article.setTitle(StringUtils.illageString(link
								.getLinkText()));
						article.setText("NED"); // No Execute Download
						int result = DAOFactory.getInstance().getArticleDao()
								.insert(article);
						if (result > 0) {
							System.out.println("添加" + link.getLinkText()
									+ ",成功");
							COUNT++;
						} else {
							System.out
									.println("添加" + link.getLinkText() + "失败");
						}
					}
					// NodeList list4 = link.getChildren();
					// if(list4 != null && list4.size() > 0){
					// for(int k=0;k<list4.size();k++){
					// if(list4.elementAt(k) instanceof ImageTag){
					// ImageTag tag = (ImageTag)list4.elementAt(k);
					// System.out.println("图片名称:"+(tag.getAttribute("alt") ==
					// null ? "无名称" : tag.getAttribute("alt")));
					// System.out.println("图片URL:"+tag.getImageURL());
					// }
					// if(list4.elementAt(k) instanceof TextNode){
					// TextNode tag = (TextNode)list4.elementAt(k);
					// System.out.println("TextNode："+tag.getText());
					// }
					// System.out.println("\n***********************************");
					// }
					// }
				}
			}
		}

	}

	/**
	 * 是否有分页
	 * 
	 * @param content
	 * @param attribute
	 * @param value
	 * @return
	 * @throws Exception
	 */
	static ResultBean pageing(String content, String attribute, String value)
			throws Exception {
		ResultBean result = new ResultBean();
		Parser p1 = new Parser();
		p1.setURL(content);
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = p1.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter(attribute, value));
		if (list != null && list.size() > 0) {
			result.setBool(true);
			Parser p2 = new Parser();
			p2.setInputHTML(list.toHtml());

			NodeFilter filter1 = new NodeClassFilter(SelectTag.class);
			NodeList list2 = p2.extractAllNodesThatMatch(filter1);

			if (list2 != null && list2.size() > 0) {
				for (int i = 0; i < list2.size(); i++) {
					SelectTag select = (SelectTag) list2.elementAt(i);
					NodeList list3 = select.getChildren();
					if (list3 != null && list3.size() > 0) {
						for (int j = 0; j < list3.size(); j++) {
							LinkBean bean = new LinkBean();
							OptionTag node = (OptionTag) list3.elementAt(j);
							bean.setLink(TUKU_URL + node.getValue());
							result.add(bean);
						}
					}
				}
			}
		} else {
			result.setBool(false);
		}

		return result;

	}

	static void clearHash() {
		if (LINKHASH.size() > 0) {
			LINKHASH.clear();
		}
	}

	static void clearList() {
		if (LINKLIST.size() > 0) {
			LINKLIST.clear();
		}
		if (ARTICLELIST.size() > 0)
			ARTICLELIST.clear();
	}

	/**
	 * 初始化数据库中Showing链接方法
	 */
	static void initDB() {
		try {
			getLink();
			Set<String> key = LINKHASH.keySet();
			Iterator<String> it = key.iterator();
			int count = 0;
			WebsiteBean webSite = null;
			while (it.hasNext()) {
				LinkBean bean = (LinkBean) LINKHASH.get(it.next());
				System.out.println("链接名称：" + bean.getName() + "\t链接名称："
						+ bean.getLink());
				webSite = new WebsiteBean();
				webSite.setParentId(36);
				webSite.setName(StringUtils.illageString(bean.getName()));
				webSite.setUrl(bean.getLink());
				boolean b = DAOFactory.getInstance().getWebSiteDao().insert(
						webSite);
				if (b) {
					System.out.println("链接名称:" + bean.getName() + "添加成功");
				} else {
					System.out.println("链接名称:" + bean.getName() + "添加失败");
				}
				count++;
			}
			System.out.println("count:" + count);
			clearHash();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		WebSiteDao dao = DAOFactory.getInstance().getWebSiteDao();
		try {
			List<WebsiteBean> list = dao.findByParentId(36);
			if (list != null && list.size() > 0) {
				for (WebsiteBean bean : list) {
					System.out.println("链接名称：" + bean.getName());
					test(bean);
				}
				IOUtil.createFile("一共添加记录到数据库" + COUNT);
			}
			COUNT = 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
