package com.xcar;

import java.io.*;
import java.util.*;

import org.htmlparser.filters.*;
import org.htmlparser.*;
import org.htmlparser.util.*;
import org.htmlparser.tags.*;

import com.chinamilitary.util.IOUtil;

public class XCarParser
{
	static final String GOOGLE_URL = "http://www.google.com.hk";

	static final String AUTO_PIC_URL = "http://car.autohome.com.cn/pic/series/362.html";

	static final String SAVE_DIR = "D:\\fileserver\\auto\\xcar\\";
	
	static final String LIST_NAME = "list.txt";

	static final int OFFSIZE = 50;
	
	/**
	 * ���URL��ȡ����
	 *@param url ҳ���ַ
	 *@return String ����
	 */
	public static String getContent(String url){
		Parser p1 = new Parser();
		String content = null;
		try{
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList list = p1.extractAllNodesThatMatch(fileter);
			if(null != list && list.size() > 0){
				content = list.toHtml();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1){
				p1 = null;
			}
		}
		return content;
	}

	
	/**
	 * ���URL��ȡͼƬҳ���ַ
	 * @param url
	 */
	public static List<String> getPictureListPage(String url){
		Parser p1 = new Parser();
		List<String> list = new ArrayList<String>();
		try{
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "OptionDesc_2"));;
			if(null != nodeList && nodeList.size() > 0){
				for(int i=0;i<nodeList.size();i++){
					Div div = (Div)nodeList.elementAt(i);
					int childCount = div.getChildCount();
					if(childCount > 0){
						Node node = div.getChild(0);
						if(node instanceof LinkTag){
							LinkTag link = (LinkTag)node;
//							System.out.println(" >> "+link.getLinkText()+"\t"+link.getLink());
							list.add(link.getLink());
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1){
				p1 = null;
			}
		}
		return list;
	}


		/**
	 * ���URL��ȡͼƬҳ���ַ
	 * @param url
	 */
	public static List<LinkTag> getPictureListPageLinkTag(String url){
		Parser p1 = new Parser();
		List<LinkTag> list = new ArrayList<LinkTag>();
		try{
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "OptionDesc_2"));;
			if(null != nodeList && nodeList.size() > 0){
				for(int i=0;i<nodeList.size();i++){
					Div div = (Div)nodeList.elementAt(i);
					int childCount = div.getChildCount();
					if(childCount > 0){
						Node node = div.getChild(0);
						if(node instanceof LinkTag){
							LinkTag link = (LinkTag)node;
//							System.out.println(" >> "+link.getLinkText()+"\t"+link.getLink());
							list.add(link);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1){
				p1 = null;
			}
		}
		return list;
	}

	public static List<String> getPictureLinkPage(String url){
		Parser p1 = new Parser();
		List<String> list = new ArrayList<String>();
		try{
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(Span.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "fr"));
			if(null != nodeList && nodeList.size() > 0){
				for(int i=0;i<nodeList.size();i++){
					Span div = (Span)nodeList.elementAt(i);
//					System.out.println( " >> url["+url+"]:" + div.getStringText());
					int count = 0;
					try{
						if(null != div.getStringText() && !"".equals(div.getStringText())){
							count = Integer.valueOf(div.getStringText());
						}
					}catch(NumberFormatException e){
						System.err.println(e);
					}

					int start = url.lastIndexOf(".");
					String pre = url.substring(0,start);
					String end = url.substring(start);
					
					int num = count/60;
					if(count%60 > 0){
						num ++;
					}
					list.add(url);
					for(int k=0;k<num;k++){
						if(k == 0){
							continue;
						}
						list.add(pre+"-p"+k+end);
//						System.out.println(" >> "+pre+"-p"+k+end);
					}

					/**
					int childCount = div.getChildCount();
					if(childCount > 0){
						Node node = div.getChild(0);
						if(node instanceof LinkTag){
							LinkTag link = (LinkTag)node;
							System.out.println(" >> link:"+link.getLink());
							list.add(link.getLink());
						}
					}
					**/
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1){
				p1 = null;
			}
		}
		return list;
	}

	/**
	 * ��ȡͼƬ��ͼ��ַ���Լ�����ͼ��ַ
	 * ��¼�б?������
	 * ���顾0�� ���õ�ַ
	 * ���顾1�� ��ͼҳ���ַ
	 �� ���顾2�� ͼƬ����ͼ��ַ
	 */
	public static List<String[]> getPictureLink(String url){
		Parser p1 = new Parser();
		List<String[]> list = new ArrayList<String[]>();
		try{
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "r_img"));
			if(null != nodeList && nodeList.size() > 0){
				String picUrl ="";
				String imgSrc = "";
				for(int i=0;i<nodeList.size();i++){
					String[] values = new String[3];
					Div div = (Div)nodeList.elementAt(i);
					int count = div.getChildCount();
					if(count == 2){
						Div thumb = (Div)div.getChild(0);
						int count2 = thumb.getChildCount();
						if(count2 == 1){
							Node tmp = thumb.getChild(0);
							if(tmp instanceof LinkTag){
								LinkTag link = (LinkTag)tmp;
								//ͼƬ��ͼҳ��
								picUrl = link.getLink();
								/**
								 *��ȡͼƬ����ͼ��ַ
								 */
								int count3 = link.getChildCount();
								if( count3 > 0 ){
									Node tmp2 = link.getChild(0);
									if(tmp2 instanceof ImageTag){
										ImageTag img = (ImageTag)tmp2;
										if(null != img.getImageURL() && !"".equals(img.getImageURL())){
											imgSrc = img.getImageURL();
										}
									}
								}
								values[0] = url;
								values[1] = picUrl;
								values[2] = imgSrc;
							}
						}
						list.add(values);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1){
				p1 = null;
			}
		}

		return list;
	}

	/**
	 * ��ȡ��ͼͼƬ��ַ
	 */
	public static String getBigImageSrc(String url){
		Parser p1 = new Parser();
		String src = null;
		try{
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(ImageTag.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("id", "img"));
			if(nodeList.size() > 0 ){
				ImageTag img = (ImageTag)nodeList.elementAt(0);
				if(null != img.getImageURL() && !"".equals(img.getImageURL())){
					src = img.getImageURL();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(p1 != null){
				p1 = null;
			}
		}
		return src;
	}

	/**
	 * ���URL��ȡ�ļ���
	 */
	public static String getName(String url){
		String name = "";
		int start = url.lastIndexOf("/");
		if(start != -1){
			name = url.substring(start+1);
		}
		return name;
	}

	public static String[] getUrls(String content){
		String[] result = null;
		if(null != content && !"".equals(content)){
			result = content.split("\r\n");
		}
		return result;
	}

	/**
	 * ��ȡ��ҳ�еı���
	 */
	public static String getTitle(String url){
		String tmp = null;
		Parser p1 = new Parser();
		try{
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(TitleTag.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter);
			if(nodeList.size() > 0 ){
				TitleTag title = (TitleTag)nodeList.elementAt(0);
				if(null != title && !"".equals(title.getTitle())){
					tmp = title.getTitle();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(p1 != null){
				p1 = null;
			}
		}

		return tmp;
	}

	/**
	 * ������⣬��ȡ��ϵ������Ϣ
	 * title:���µ�A8L|�µ�A8L����-ͼƬ��_�µ�_��������
	 */
	public static String dealWithTitle(String title){
		String tmp = null;

		try{
			int start = title.lastIndexOf("��");
			int end = title.lastIndexOf("|");
			/**
			System.out.println( " >> substring:" + title.substring(3,end));
			String[] tmps = title.split("_");
			if(null != tmps){
				for(String t:tmps){
					System.out.println( " >> dealWithTitle:" + t);
				}
			}
			**/
			tmp = title.substring(start+1,end);
		}catch(Exception e){
			tmp = title;
			e.printStackTrace();
		}
		
		return tmp;
	}

	/** 
	 * ��URL�л�ȡID
	 * http://car.autohome.com.cn/pic/series/547.html
	 */
	public static String getIdFromURL(String url){
		String id = null;
		try{
			int start = url.lastIndexOf("/")+1;
			int end = url.lastIndexOf(".");
//			System.out.println(url.substring(start));
			id = url.substring(start);
		}catch(Exception e){
			id = "-99";		
		}
		return id;
	}

	public static void main(String args[]){}


}