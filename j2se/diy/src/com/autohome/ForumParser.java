package com.autohome;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.PrototypicalNodeFactory;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.visitors.NodeVisitor;

import com.autohome.tag.DDTag;
import com.autohome.tag.DLTag;
import com.autohome.tag.DTTag;

/**
 * 论坛文章解析
 * @author Bluestome.Zhang
 *
 */
public class ForumParser {

	public void parser(){
		Parser p1 = null;
		try{
			
			PrototypicalNodeFactory factory = new PrototypicalNodeFactory ();  
			factory.registerTag(new DLTag());
			factory.registerTag(new DDTag());
			factory.registerTag(new DTTag());
			
			p1 = new Parser();
			p1.setURL("http://club.autohome.com.cn/bbs/forum-c-2001-1.html");
			//需要将自定义的TAG注册到解析工厂中
			p1.setNodeFactory(factory);
			NodeFilter fileter = new NodeClassFilter(DLTag.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter)
					.extractAllNodesThatMatch(
							new HasAttributeFilter("class", "list_dl "));
			if(null != list && list.size() > 0){
				int c=0;
				do{
					DLTag dl = (DLTag)list.elementAt(c);
					
					System.out.println(dl.getAttribute("lang"));
					System.out.println(dl.getChildCount());
					System.out.println("\r\n");
					int cc = 0;
					do{
						//获取子节点中的数据
						System.out.println(cc+"|"+dl.getChild(cc));
						cc++;
					}while(cc < dl.getChildCount());
					c++;
				}while(c<list.size());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1){
				p1 = null;
			}
		}
	}
	
	public static void main(String args[]){
		ForumParser p2 = new ForumParser();
		p2.parser();
	}
}
