package com;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

/**
 * @ClassName: LinkParser
 * @Description: TODO
 * @author Bluestome.Zhang
 * @date 2012-10-22 下午02:42:42
 */
public class LinkParser {

    public final static String DIR = "d:\\link.html";
    
    public void process(){
        Parser p1 = null;
        try{
            p1 = new Parser();
            p1.setURL(DIR);
            
            NodeFilter fileter = new NodeClassFilter(LinkTag.class);
            NodeList list = p1.extractAllNodesThatMatch(fileter);
            if(null != list && list.size() > 0){
                for(int i=0;i<list.size();i++){
                    LinkTag link = (LinkTag)list.elementAt(i);
                    System.out.println(link.getLink());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]){
        LinkParser parser = new LinkParser();
        parser.process();
    }
}
