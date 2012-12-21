package com.zol.mobile;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.LinkBean;
import com.chinamilitary.bean.MobileBrandTemp;
import com.chinamilitary.bean.ResultBean;
import com.chinamilitary.dao.IMobileBrandTempDAO;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;

/**
 * 中关村手机频道解析类
 * 
 * @author bluestome
 * 
 */
public class MobileParser {
	
	static Log log = LogFactory.getLog(MobileParser.class);

	static String ZOL_URL_PREFIX = "http://detail.zol.com.cn";
	
	final static String ZOL_MOBILE_URL = "http://mobile.zol.com.cn/manu_list.html";
	
	final static Integer D_PARENT_ID = 1634;
	
	static IMobileBrandTempDAO mobileBrandTempDAO = DAOFactory.getInstance().getMobileBrandTempDAO();
	
	static HashMap<String,MobileBrandTemp> BRAND_MAP = new HashMap<String,MobileBrandTemp>();

	static HashMap<String,LinkTag> BRAND_MAP2 = new HashMap<String,LinkTag>();
	
	static String FILE_SAVE_PATH = "D:/zol/mobile/";
	
	static int count = 0;
	
	static MemcacheClient client = MemcacheClient.getInstance();
	
	static StringBuffer CONTENT_BUILDER = new StringBuffer();
	
	public static void main(String args[]){
		
		
		/**
		loopFile(FILE_SAVE_PATH);
		IOUtil.createFile(CONTENT_BUILDER.toString(),"终端属性","txt");
		if(null != CONTENT_BUILDER){
			CONTENT_BUILDER = null;
		}
		log.info(" >> 终端总数:"+count);
		count = 0;
		**/
//		readTerminalInfo("E:\\Workspaces\\5.CMP\\diy_page\\终端属性.txt");
//		readTerminalInfo("终端属性.txt");
		
		sql();
	}
	
	static void method1(){
		brandList();
		Iterator it = BRAND_MAP2.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			try {
				LinkTag lt = BRAND_MAP2.get(key);
				ResultBean result = hasPaging(key,"class","pagebar");
				if (result.isBool()) {
					Iterator it2 = result.getMap().keySet().iterator();
					int pageNum = 1;
					while (it2.hasNext()) {
						String key2 = (String) it2.next();
						LinkBean link = result.getMap().get(key2);
//						items(link.getLink());
						boolean b = savePage(lt.getLinkText(), link.getLink(), pageNum);
						log.info(link.getLink()+",保存:"+b);
						pageNum++;
						Thread.sleep(1000);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		BRAND_MAP2.clear();
	}
	
	static void list(){
		try{
			List<MobileBrandTemp> list= mobileBrandTempDAO.findByWebId(D_PARENT_ID);
			for(MobileBrandTemp brand:list){
				String url = getPriceList(brand.getUrl());
				log.info(" >> brand["+brand.getName()+"].list:"+url);
			}
			brandList();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static String getPriceList(String url) throws Exception {
		String target = null;
		Parser parser = null;
		Parser p2 = null;
		try{
			parser = new Parser();
			parser.setURL(url);
			parser.setEncoding("GB2312");
			NodeList list = parser.extractAllNodesThatMatch(new TagNameFilter("ul"))
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "brand_nav white clearfix"));
			if (null != list && list.size() > 0) {
				String content = list.toHtml();
				p2 = new Parser();
				p2.setInputHTML(content);
				p2.setEncoding("GB2312");
				NodeFilter fileter = new NodeClassFilter(LinkTag.class);
				NodeList list2 = p2.extractAllNodesThatMatch(fileter);
				if(null != list2 && list2.size() > 0){
					LinkTag link = (LinkTag)list2.elementAt(0);
					target = link.getLink();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != parser)
				parser = null;
			if(null != p2)
				p2 = null;
		}
		return target;
	}
	
	static void add(){
		BRAND_MAP.clear();
		brandWithIcon();
		brandWithoutIcon();
		log.info(" >> 品牌数量:"+BRAND_MAP.size());
		Iterator it = BRAND_MAP.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			MobileBrandTemp brand = (MobileBrandTemp)BRAND_MAP.get(key);
			if(null != brand){
				brand.setWebid(D_PARENT_ID);
				log.info(" >>name:"+brand.getName());
				log.info(" >>url:"+brand.getUrl());
				if(null != brand.getIcon()){
					log.info(" >>icon:"+brand.getIcon());
				}
				try{
					int result = mobileBrandTempDAO.add(brand);
					log.info(" >> result:"+result);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 有图表的品牌
	 * 
	 */
	static void brandWithIcon() {
		Parser parser = null;
		MobileBrandTemp brand = null;
		try {
			parser = new Parser();
			parser.setURL(ZOL_MOBILE_URL);
			parser.setEncoding("GB2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "blI"));
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Div div = (Div) list.elementAt(i);
					int childCount = div.getChildCount();
					if (childCount == 5) {
						if (div.getChild(1) instanceof LinkTag) {
							brand = new MobileBrandTemp();
							LinkTag link = (LinkTag) div.getChild(1);
							brand.setUrl(link.getLink());
							if(link.getChild(0) instanceof ImageTag){
								ImageTag image = (ImageTag)link.getChild(0);
								brand.setIcon(image.getImageURL());
								brand.setName(image.getAttribute("alt"));
								BRAND_MAP.put(link.getLink(), brand);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != parser)
				parser = null;
		}
	}

	/**
	 * 有图表的品牌
	 * 
	 */
	static void brandWithoutIcon() {
		Parser parser = null;
		MobileBrandTemp brand = null;
		try {
			parser = new Parser();
			parser.setURL(ZOL_MOBILE_URL);
			parser.setEncoding("GB2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "blCon2"));
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Div div = (Div) list.elementAt(i);
					if (div.getChild(0) instanceof LinkTag) {
						brand = new MobileBrandTemp();
						LinkTag link = (LinkTag) div.getChild(0);
						brand.setName(link.getLinkText());
						brand.setUrl(link.getLink());
						if(!BRAND_MAP.containsKey(link.getLink())){
							BRAND_MAP.put(link.getLink(), brand);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != parser)
				parser = null;
		}
	}
	
	static void brandList(){
		Parser parser = null;
		Parser p2 = null;
		try {
			parser = new Parser();
			parser.setURL("http://detail.zol.com.cn/category/57.html");
			parser.setEncoding("GB2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList list = parser.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("id", "manulist"));
			if (null != list && list.size() > 0) {
				Div div = (Div)list.elementAt(0);
				p2 = new Parser();
				p2.setInputHTML(div.toHtml());
				p2.setEncoding("GB2312");
				NodeFilter fileter2 = new NodeClassFilter(LinkTag.class);
				NodeList list2 = p2.extractAllNodesThatMatch(fileter2);
				for(int i=0;i<list2.size();i++){
					LinkTag link = (LinkTag)list2.elementAt(i);
					if(null != link.getLinkText() && !"".equals(link.getLinkText())){
						log.info(" >> "+ZOL_URL_PREFIX+link.getLink()+"|"+link.getLinkText());
						BRAND_MAP2.put(ZOL_URL_PREFIX+link.getLink(), link);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != parser)
				parser = null;
		}
	}
	
	/**
	 * 获取分类下的分页信息
	 * 
	 * @param url
	 * @param attribute
	 * @param value
	 * @return
	 * @throws Exception
	 */
	static ResultBean hasPaging(String url, String cls, String value)
			throws Exception {
		boolean b = false;
		ResultBean result = new ResultBean();
		Parser parser = new Parser();
		parser.setURL(url);
		int len = 1;
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(Div.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(new HasAttributeFilter(cls, value));
		if (list != null && list.size() > 0) {
			Parser p2 = new Parser();
			p2.setInputHTML(list.toHtml());

			NodeFilter filter2 = new NodeClassFilter(LinkTag.class);
			NodeList list2 = p2.extractAllNodesThatMatch(filter2);
			if (null != list && list2.size() > 0) {
				String tmp = null;
				LinkBean l1 = null;
				if(list2.size() == 6){
					LinkTag tl = (LinkTag)list2.elementAt(4);
//					log.info(" >> 获取分页数量:"+tl.toPlainTextString()+"|"+tl.getLink());
					try{
						len = Integer.parseInt(tl.toPlainTextString());
					}catch(NumberFormatException e){
						e.printStackTrace();
					}
					for(int i=1;i<len+1;i++){
						String tu = url.replace("1.", i+".");
						if (!url.startsWith("http://")) {
							tmp = ZOL_URL_PREFIX + tu;
						} else {
							tmp = tu;
						}
						if(null != tmp && !"".equals(tmp)){
							l1 = new LinkBean();
							l1.setLink(tmp);
							l1.setTitle(i+"");
							result.put(tmp, l1);
						}
					}
				}else{
					for (int i = 0; i < list2.size(); i++) {
						l1 = new LinkBean();
						LinkTag link2 = (LinkTag) list2.elementAt(i);
						if (!link2.getLink().startsWith("http://")) {
							tmp = ZOL_URL_PREFIX + link2.getLink();
						} else {
							tmp = link2.getLink();
						}
						tmp = tmp.replace("&amp;", "&");
						l1.setLink(tmp);
						l1.setTitle(link2.toPlainTextString());
						result.put(tmp, l1);
					}
				}
			}else{
				LinkBean l1 = new LinkBean();;
				l1.setLink(url);
				l1.setTitle(url);
				result.put(url, l1);
				result.setBool(true);
			}
			if (null != p2)
				p2 = null;
		} 
		LinkBean l1 = new LinkBean();;
		l1.setLink(url);
		l1.setTitle(url);
		result.put(url, l1);
		result.setBool(true);
		return result;
	}
	
	static void items(String url){
		Parser p1 = null;
		Parser p2 = null;
		try{
			p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("GB2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "item"));
			if(null != list && list.size() > 0){
				count += list.size();
				log.info(" >> 终端数量:"+list.size());
			}
		}catch(Exception e){
			
		}finally{
			if(null != p1){
				p1 = null;
			}
			if(null != p2){
				p2 = null;
			}
		}
	}

	static void items(String brand,String content){
		Parser p1 = null;
		Parser p2 = null;
		try{
			p1 = new Parser();
			p1.setInputHTML(content);
			p1.setEncoding("GB2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "item"));
			if(null != list && list.size() > 0){
//				log.info(" >> 品牌【"+brand+"】.终端数量:"+list.size());
				for(int i=0;i<list.size();i++){
					Div div = (Div)list.elementAt(i);
//					log.info(" >> "+div.toHtml());
					String hstype = getHstype(div.toHtml());
//					log.info(" \t\t>> 机型:"+hstype);
					String paramUrl = getParameterPage(div.toHtml());
//					log.info(" \t\t>> 参数URL:"+paramUrl);
					if(null != paramUrl){
						int[] paramset = parameterSet(paramUrl);
						log.info(" >> 品牌["+brand+"],机型["+hstype.replace(brand.replace("手机", ""), "")+"],终端属性：(GPRS["+(paramset[0] == 1?"支持":"不支持")+"],WAP["+(paramset[1] == 1?"支持":"不支持")+"],智能手机["+(paramset[2] == 1?"支持":"不支持")+"],操作系统["+(paramset[3] == 1?"支持":"不支持")+"],WIFI["+(paramset[4] == 1?"支持":"不支持")+"])");
						CONTENT_BUILDER.append(brand).append(",").append(hstype.replace(brand, "")).append(",").append(paramset[0]).append(",").append(paramset[1]).append(",").append(paramset[2]).append(",").append(paramset[3]).append(",").append(paramset[4]).append("\r\n");
						count++;
					}
				}
			}
		}catch(Exception e){
			log.error(e);
		}finally{
			if(null != p1){
				p1 = null;
			}
			if(null != p2){
				p2 = null;
			}
		}
	}
	
	static String getHstype(String content){
		String type = null;
		Parser p1 = null;
		Parser p2 = null;
		try{
			p1 = new Parser();
			p1.setInputHTML(content);
			p1.setEncoding("GB2312");
			
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "intro"));
			if(null != list && list.size() > 0){
				p2 = new Parser();
				p2.setInputHTML(list.toHtml());
				p2.setEncoding("GB2312");
				NodeFilter fileter2 = new NodeClassFilter(LinkTag.class);
				list = p2.extractAllNodesThatMatch(fileter2);
				if(null != list && list.size() > 0){
					LinkTag link = (LinkTag)list.elementAt(0);
					if(null != link.getLinkText() && !"".equals(link.getLinkText())){
						type = link.getLinkText();
					}
				}
			}
		}catch(Exception e){
			log.error(e);
		}finally{
			if(null != p1)
				p1 = null;
			if(null != p2)
				p2 = null;
		}
		return type;
	}
	
	static String getParameterPage(String content){
		String type = null;
		Parser p1 = null;
		Parser p2 = null;
		try{
			p1 = new Parser();
			p1.setInputHTML(content);
			p1.setEncoding("GB2312");
			
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "intro"));
			if(null != list && list.size() > 0){
				p2 = new Parser();
				p2.setInputHTML(list.toHtml());
				p2.setEncoding("GB2312");
				NodeFilter fileter2 = new NodeClassFilter(LinkTag.class);
				list = p2.extractAllNodesThatMatch(fileter2);
				if(null != list && list.size() > 0){
					for(int i=0;i<list.size();i++){
						LinkTag link = (LinkTag)list.elementAt(i);
						if(null != link.getLinkText() && !"".equals(link.getLinkText()) && "更多参数>>".equals(link.getLinkText())){
							if(link.getLink().startsWith("http://")){
								type = link.getLink();
							}else{
								type = ZOL_URL_PREFIX+link.getLink();
							}
							break;
						}
					}
				}
			}
		}catch(Exception e){
			log.error(e);
		}finally{
			if(null != p1)
				p1 = null;
			if(null != p2)
				p2 = null;
		}
		return type;
	}
	
	/**
	 * 保存文件
	 * @param type 品牌名称
	 * @param url 地址
	 * @param pageNum 页数
	 * @return
	 */
	public static boolean savePage(String type,String url,int pageNum){
		String filePath = FILE_SAVE_PATH+type+"\\"+type+"_"+pageNum+".html";
		try{
			File file = new File(filePath);
			if(file.exists()){
				return false;
			}
			String content = HttpClientUtils.getResponseBody(url,"GB2312");
			if(file.length() == content.length()){
				return false;
			}
			if(content.length() == 11033){
				return false;
			}
			if(null != content && !"".equals(content)){
				if(!file.exists()){
					file.getParentFile().mkdir();
				}
				IOUtil.createFile(content, file.getParentFile().getAbsolutePath(),file.getName());
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	
	static void loopFile(String path){
		File file = null;
		try{
			file = new File(path);
			File[] flist = file.listFiles();
			for(File s:flist){
				if(s.isDirectory()){
					loopFile(s.getAbsolutePath());
				}else{
					String type = s.getParentFile().getName();
					String content = IOUtil.readFile(s.getAbsolutePath(), "GBK", "html");
					items(type,content);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param url
	 * @return 1:GPRS 2: 3: 4: 5:
	 *		  1.GPRS  数据业务： 查找是否有GPRS字眼
	 *		  2.WAP   WAP浏览器：查找是否有WAP字眼
	 *		  3.智能手机  手机类型： 查找是否存在智能手机字眼.
	 *		  4.操作系统  操作系统： 查找操作系统名称.
	 *		  5.WIFI      WLAN功能：查找是否有WIFI.
	 */
	static int[] parameterSet(String  url){
		int[] params = {0,0,0,0,0};
		Parser p1 = null;
		Parser p2 = null;
		try{
			p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("GB2312");
			
			NodeFilter fileter = new NodeClassFilter(TableTag.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("id", "param-tb"));
			if(null != list && list.size() > 0){
				p2 = new Parser();
				p2.setInputHTML(list.toHtml());
				p2.setEncoding("GB2312");
				NodeFilter fileter2 = new NodeClassFilter(TableRow.class);
				list = p2.extractAllNodesThatMatch(fileter2);
				if(null != list && list.size() > 0){
					for(int i=0;i<list.size();i++){
						TableRow ct = (TableRow)list.elementAt(i);
						TableColumn[] tcs = ct.getColumns();
						int len = tcs.length;
						String content = null; 
						if(len == 2){
							TableColumn tc = tcs[0];
							if(tc.toPlainTextString().equals("数据业务")){
								content = tcs[1].toPlainTextString().replace("纠错", "");
								if(content.contains("GPRS")){
//									log.info(" >> 数据业务:"+content);
									params[0] = 1;
									
								}
							}
							if(tc.toPlainTextString().equals("WAP浏览器")){
								content = tcs[1].toPlainTextString().replace("纠错", "");
								if(content.contains("WAP")){
//									log.info(" >> WAP浏览器:"+content);
									params[1] = 1;
								}
							}
							if(tc.toPlainTextString().equals("手机类型")){
								content = tcs[1].toPlainTextString().replace("纠错", "");
								if(content.contains("智能手机")){
//									log.info(" >> 手机类型:"+content);
									params[2] = 1;
								}
							}
							if(tc.toPlainTextString().equals("操作系统")){
								content = tcs[1].toPlainTextString().replace("纠错", "");
//								log.info(" >> 操作系统:"+tcs[1].toPlainTextString());
								params[3] = 1;
							}
							if(tc.toPlainTextString().equals("WLAN功能")){
								content = tcs[1].toPlainTextString().replace("纠错", "");
								if(content.contains("WIFI")){
//									log.info(" >> WLAN功能:"+content);
									params[4] = 1;
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			log.error(e);
		}finally{
			if(null != p1)
				p1 = null;
			if(null != p2)
				p2 = null;
		}
		return params;
	}
	
	static void readTerminalInfo(String name){
		File file = null;
		int id = 1;
		StringBuffer sb = new StringBuffer();
		try{
			file = new File(System.getProperty("user.dir")+File.separator+name);
			String content = IOUtil.readFile(file.getAbsolutePath(), "GBK", "txt");
			String[] rows = content.split("\r\n");
			sb.append("INSERT INTO `tbl_terminal_property` (`d_id`, `d_hsman`, `d_hstype`, `d_gprs`, `d_wap`, `d_smartphone`, `d_os`, `d_wifi`) values ");
			for(String row:rows){
				String[] contents = row.split(",");
//				log.info(">> 厂商:"+contents[0]);
//				log.info(">> 机型:"+contents[1]);
//				log.info(">> GPRS:"+contents[2]);
//				log.info(">> WAP:"+contents[3]);
//				log.info(">> 是否智能手机:"+contents[4]);
//				log.info(">> 操作系统:"+contents[5]);
//				log.info(">> WIFI:"+contents[6]);
//				log.info("");
				sb.append("(").append(id).append(",'").append(contents[0].replace("'", "")).append("','").append(contents[1].replace("'", "")).append("',").append(contents[2]).append(",").append(contents[3]).append(",");
				sb.append(contents[4]).append(",").append(contents[5]).append(",").append(contents[6]).append(")");
				if(id<rows.length){
					sb.append(",\r\n");
				}
				id++;
			}
			sb.append(";");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != file){
				file = null;
			}
			log.info(sb.toString());
			IOUtil.createFile(sb.toString(), "terminal_property", ".sql");
		}
	}
	
	static void sql(){
		StringBuffer sb = new StringBuffer();
		sb.append("\t").append("create table tbl_bussiness_{id}(\r\n");
		sb.append("\t\t").append("d_id  int(4) not null AUTO_INCREMENT,\r\n");
		sb.append("\t\t").append("d_phonenum varchar(11) not null,\r\n");
		sb.append("\t\t").append("d_hsman varchar(32),\r\n");
		sb.append("\t\t").append("d_hstype varchar(32),\r\n");
		sb.append("\t\t").append("d_imei varchar(16),\r\n");
		sb.append("\t\t").append("d_support int(4) default 0,\r\n");
		sb.append("\t\t").append("d_support_type int(4) default 0,\r\n");
		sb.append("\t\t").append("PRIMARY KEY (d_id)\r\n");
		sb.append("\t").append(");");
		for(int i=1;i<17;i++){
			String sql = sb.toString().replace("{id}", i+"");
			log.info(sql);
			log.info("\r\n");
		}
	}
}

/**
 * 品牌BEAN
 * @author bluestome
 *
 */
class Brand{
	String name;
	String url;
	String icon;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
