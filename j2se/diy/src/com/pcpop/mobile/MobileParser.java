package com.pcpop.mobile;

import java.util.HashMap;
import java.util.Iterator;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.MobileBrandTemp;
import com.chinamilitary.dao.IMobileBrandTempDAO;
import com.chinamilitary.factory.DAOFactory;

/**
 * 泡泡网手机频道数据解析
 * @author bluestome
 *
 */
public class MobileParser {

	final static String PCPOP_MOBILE_BRAND_URL = "http://product.pcpop.com/Mobile_Brand.html";
	
	static HashMap<String,MobileBrandTemp> BRAND_MAP = new HashMap<String,MobileBrandTemp>();
	
	static IMobileBrandTempDAO mobileBrandTempDAO = DAOFactory.getInstance().getMobileBrandTempDAO();
	
	final static Integer D_PARENT_ID = 1635;
	
	public static void main(String args[]){
		try{
			BRAND_MAP.clear();
			enableBrand();
			disableBrand();
			System.out.println(" >> 品牌数量:"+BRAND_MAP.size());
			Iterator it = BRAND_MAP.keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				MobileBrandTemp brand = (MobileBrandTemp)BRAND_MAP.get(key);
				if(null != brand){
					brand.setWebid(D_PARENT_ID);
					System.out.println(" >>name:"+brand.getName());
					System.out.println(" >>url:"+brand.getUrl());
					if(null != brand.getIcon()){
						System.out.println(" >>icon:"+brand.getIcon());
					}
					System.out.println(" >> status:"+brand.getStatus());
					System.out.println();
					try{
						int result = mobileBrandTempDAO.add(brand);
						System.out.println(" >> name:"+brand.getName());
						System.out.println(" >> result:"+result);
						System.out.println();
					}catch(Exception e){
						e.printStackTrace();
					}
					/**
					**/
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 在售品牌
	 *
	 */
	static void enableBrand() throws Exception{
		Parser parser = null;
		Parser p2 = null;
		MobileBrandTemp brand = null;
		try{
			parser = new Parser();
			parser.setURL(PCPOP_MOBILE_BRAND_URL);
			parser.setEncoding("GB2312");
			NodeFilter fileter = new NodeClassFilter(CompositeTag.class);
			NodeList list = parser.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "list5"));
			if(null != list && list.size() > 0){
				String content = list.toHtml();
				if(null != content && !"".equals(content)){
					p2 = new Parser();
					p2.setInputHTML(content);
					p2.setEncoding("GB2312");
					NodeFilter linkFilter = new NodeClassFilter(Div.class);
					NodeList list2 = p2.extractAllNodesThatMatch(linkFilter);
					for(int i=0;i<list2.size();i++){
						Div div = (Div)list2.elementAt(i);
						int childCount = div.getChildCount();
						if(childCount == 2){
							brand = new MobileBrandTemp();
							if(div.getChild(0) instanceof LinkTag){
								LinkTag link = (LinkTag)div.getChild(0);
//								System.out.println(link.getLinkText()+"\t|"+link.getLink());
								brand.setUrl(link.getLink());
								brand.setName(link.getLinkText());
								if(!BRAND_MAP.containsKey(link.getLink())){
									BRAND_MAP.put(link.getLink(), brand);
								}
							}
							/**
							if(div.getChild(1) instanceof Span){
								Span span = (Span)div.getChild(1);
								String text = span.getStringText();
								if(text.lastIndexOf("(") != -1 && text.lastIndexOf(")") != -1){
									int start = text.lastIndexOf("(");
									int end = text.lastIndexOf(")");
									String count = text.substring(start+1,end);
									System.out.println(" >> count:"+count);
								}
							}
							**/
//							System.out.println();
						}
					}
//					System.out.println(" >> "+list2.size());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != parser)
				parser = null;
		}
	}
	
	/**
	 * 停售品牌
	 *
	 */
	static void disableBrand(){
		Parser parser = null;
		Parser p2 = null;
		MobileBrandTemp brand = null;
		try{
			parser = new Parser();
			parser.setURL(PCPOP_MOBILE_BRAND_URL);
			parser.setEncoding("GB2312");
			NodeFilter fileter = new NodeClassFilter(CompositeTag.class);
			NodeList list = parser.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "list5 list5_1"));
			if(null != list && list.size() > 0){
				String content = list.toHtml();
				if(null != content && !"".equals(content)){
					p2 = new Parser();
					p2.setInputHTML(content);
					p2.setEncoding("GB2312");
					NodeList list2 = p2.extractAllNodesThatMatch(new TagNameFilter("li"));
					for(int i=0;i<list2.size();i++){
						CompositeTag div = (CompositeTag)list2.elementAt(i);
						int childCount = div.getChildCount();
						if(childCount == 2){
							brand = new MobileBrandTemp();
							brand.setStatus(0);
							if(div.getChild(0) instanceof LinkTag){
								LinkTag link = (LinkTag)div.getChild(0);
//								System.out.println(link.getLinkText()+"\t|"+link.getLink());
								brand.setUrl(link.getLink());
								brand.setName(link.getLinkText());
								if(!BRAND_MAP.containsKey(link.getLink())){
									BRAND_MAP.put(link.getLink(), brand);
								}
							}
							
							/**
							if(div.getChild(1) instanceof Span){
								Span span = (Span)div.getChild(1);
								String text = span.getStringText();
								if(text.lastIndexOf("(") != -1 && text.lastIndexOf(")") != -1){
									int start = text.lastIndexOf("(");
									int end = text.lastIndexOf(")");
									String count = text.substring(start+1,end);
									System.out.println(" >> count:"+count);
								}
							}
							**/
						}
					}
//					System.out.println(" >> "+list2.size());
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != parser)
				parser = null;
		}
	}
}
