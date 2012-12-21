package com.p163;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;

import com.parser.tag.LITag;
import com.parser.tag.ULTag;

/**
 * 普通图片解析器
 * @author Bluestome.Zhang
 *
 */
public class NormalParser {

	/**
	 * 执行普通图片解析
	 * @param url
	 * @return String[]
	 * 0: 小图 100*77
	 * 1: 文字说明
	 * 2: 大图
	 * 3: 小图 155*103
	 */
	public static String[][] execute(String url){
		String[][] result = null;
		Parser p1 = null;
		Parser p2 = null;
		Parser p3 = null;
		try{
			PrototypicalNodeFactory factory = new PrototypicalNodeFactory ();  
			factory.registerTag(new ULTag());
			factory.registerTag(new LITag());
			factory.registerTag(new ITag());
			
			p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("GB2312");
			p1.setNodeFactory(factory);
			
			NodeFilter filter = new NodeClassFilter(CompositeTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("id","photoList"));
			if(null != list && list.size() > 0){
				CompositeTag comp = (CompositeTag)list.elementAt(0);
				p2 = new Parser();
				p2.setInputHTML(comp.toHtml());
				p2.setNodeFactory(factory);
				filter = new NodeClassFilter(LITag.class);
				NodeList list2 = p2.extractAllNodesThatMatch(filter);
				if(null != list2 && list2.size() > 0){
					result = new String[list2.size()][4];
					for(int i=0;i<list2.size();i++){
						LITag li = (LITag)list2.elementAt(i);
						/**
						<li>
						*小图100x75
						<a href="#p=6QLQOSFE00AN0001" hidefocus="true">
						<img src="http://img4.cache.netease.com/photo/0001/2011-01-18/100x75_6QLQOSFE00AN0001.jpg" alt="" />
						</a>
						<h2></h2>
						*图片描述
						<p>1月18日凌晨，消防人员在火灾现场楼道里勘查火情。</p>
						*大图
						<i title="img">http://img3.cache.netease.com/photo/0001/2011-01-18/900x600_6QLQOSFE00AN0001.jpg</i>
						*小图 155x103
						<i title="timg">http://img3.cache.netease.com/photo/0001/2011-01-18/t_6QLQOSFE00AN0001.jpg</i>
						</li>
						**/
						String content = li.toHtml();
						
						//小图1 100x75
						p3 = new Parser();
						p3.setInputHTML(content);
						NodeFilter tFilter = new NodeClassFilter(ImageTag.class);
						NodeList tList = p3.extractAllNodesThatMatch(tFilter);
						if(null != tList && tList.size() > 0){
//							System.out.println("小图1_100x75:"+tList.toHtml()+"|"+tList.size());
							ImageTag t = (ImageTag)tList.elementAt(0);
							result[i][0] = t.getImageURL();
						}
						
						//说明
						p3 = new Parser();
						p3.setInputHTML(content);
						tFilter = new TagNameFilter("p");
						tList = p3.extractAllNodesThatMatch(tFilter);
						if(null != tList && tList.size() > 0){
//							System.out.println("文字说明:"+tList.toHtml()+"|"+tList.size());
							Tag t = (Tag)tList.elementAt(0);
							result[i][1] = t.toPlainTextString().trim();
						}
						
						//大图
						p3 = new Parser();
						p3.setInputHTML(content);
						p3.setNodeFactory(factory);
						tFilter = new TagNameFilter("i");
						tList = p3.extractAllNodesThatMatch(tFilter).extractAllNodesThatMatch(new HasAttributeFilter("title","img"));
						if(null != tList && tList.size() > 0){
//							System.out.println("大图:"+tList.toHtml()+"|"+tList.size());
							ITag itag = (ITag)tList.elementAt(0);
							result[i][2] = itag.toPlainTextString().trim();
						}
						
						//小图2 155*103
						p3 = new Parser();
						p3.setInputHTML(content);
						p3.setNodeFactory(factory);
						tFilter = new TagNameFilter("i");
						tList = p3.extractAllNodesThatMatch(tFilter).extractAllNodesThatMatch(new HasAttributeFilter("title","timg"));
						if(null != tList && tList.size() > 0){
//							System.out.println("小图2_155*103:"+tList.toHtml()+"|"+tList.size());
							ITag itag = (ITag)tList.elementAt(0);
							result[i][3] = itag.toPlainTextString().trim();
						}
					}
				}
				
			}
		}catch(Exception e){
			
		}finally{
			if(null != p2)
				p2 = null;
			if(null != p1)
				p1 = null;
		}
		return result;
	}
	
	public static void main(String args[]){
		String[][] result = NormalParser.execute("http://news.163.com/photoview/00AN0001/12768.html");
		System.out.println("\t> 长度："+result.length);
		for(String[] imgs:result){
			System.out.println("\t 小图1:"+imgs[0]);
			System.out.println("\t 文字说明:"+imgs[1]);
			System.out.println("\t 大图:"+imgs[2]);
			System.out.println("\t 小图2:"+imgs[3]);
			System.out.println("\r\n");
		}
	}
}

class ITag extends CompositeTag{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String[] mIds = new String[] { "I" };

	public String[] getIds() {
		return (mIds);
	}

	public String[] getEnders() {
		return (mIds);
	}

}
