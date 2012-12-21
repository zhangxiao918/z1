package com.chinamilitary.htmlparser;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.util.NodeList;

public class GoogleTranslateParser {

	private static final String GOOGLE_TRANSLATE = "http://translate.google.cn/";
	
	public static void getSrcSelectValue() throws Exception{
		Parser parser = new Parser();
		parser.setURL(GOOGLE_TRANSLATE);
		
		// 获取指定ID的DIV内容
		NodeFilter filter = new NodeClassFilter(SelectTag.class);
		NodeList list = parser.extractAllNodesThatMatch(filter)
				.extractAllNodesThatMatch(
						new HasAttributeFilter("id", "old_sl"));
		
		if(list != null && list.size() > 0){
			Node  node = list.elementAt(0);
			if(node instanceof SelectTag){
				SelectTag select  = (SelectTag)node;
				OptionTag[] options = select.getOptionTags();
				if(options != null && options.length > 0){
					for(int i=0;i<options.length;i++){
						System.out.println("OptionText:"+options[i].getOptionText());
						System.out.println("Value:"+options[i].getValue());
					}
				}
			}
//			System.out.println(list.toHtml());
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			getSrcSelectValue();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
