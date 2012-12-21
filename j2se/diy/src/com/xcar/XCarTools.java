package com.xcar;
import java.io.*;
import java.util.*;

import org.htmlparser.filters.*;
import org.htmlparser.*;
import org.htmlparser.util.*;
import org.htmlparser.tags.*;

import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;

public class XCarTools
{
	static final String GOOGLE_URL = "http://www.google.com.hk";

	static final String AUTO_PIC_URL = "http://car.autohome.com.cn/pic/series/362.html";

	static final String XCAR_IMAGE_URL_PREFIX = "http://newcar.xcar.com.cn/";

	static final String SAVE_DIR = "I:\\fileserver\\auto\\xcar\\image\\";

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
	 * @demo http://newcar.xcar.com.cn/7/photo.htm
	 * @param url
	 */
	public static List<LinkTag> getPictureListPageLinkTag(String url){
		Parser p1 = new Parser();
		List<LinkTag> list = new ArrayList<LinkTag>();
		try{
			p1.setURL(url+"/photo.htm");
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(LinkTag.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "t_0915s_t3more"));;
			if(null != nodeList && nodeList.size() > 0){
				for(int i=0;i<nodeList.size();i++){
					LinkTag link = (LinkTag)nodeList.elementAt(i);
					list.add(link);
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
	 * ͨ�����ú�URL4��ȡ����
	 */
	public static String getContent(String referer,String url){
		String content = null;
		byte[] bs = null;
		try{
			bs = HttpClientUtils.getResponseBodyAsByte(referer,
			 null,url);
			if(null != bs && bs.length > 0){
				content = new String(bs);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return content;
	}

	public static List<String> getPictureLinkPage(String url){
		Parser p1 = new Parser();
		List<String> list = new ArrayList<String>();
		HashMap<String,String> urlMap = new HashMap<String,String>();
		try{
			System.out.println(" >> "+url);
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(CompositeTag.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "np_num"));
			System.out.println(" >> "+nodeList.size());
			if(null != nodeList && nodeList.size() > 0){
				if(nodeList.size() == 1){
					CompositeTag tag = (CompositeTag)nodeList.elementAt(0);
					int count = tag.getChildCount();
					if(count > 0){
						for(int i=0;i<count;i++){
							Node tmp = tag.getChild(i);
							if(tmp instanceof LinkTag){
								LinkTag lt = (LinkTag)tmp;
								urlMap.put(lt.getLink(),lt.getLink());
							}
						}
					}else{
						urlMap.put(url,url);
					}
				}
			}else{
				urlMap.put(url,url);
			}
			Iterator it = urlMap.keySet().iterator();
			while(it.hasNext()){
				String key = (String)it.next();
				System.out.println("key:"+key);
				list.add(key);
			}
			/**
			**/
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
		Parser p1 = null;
		Parser p2 = null;
		List<String[]> list = new ArrayList<String[]>();
		String picUrl ="";
		String imgSrc = "";
		try{
			p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(Div.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "np_r_p3"));
			if(null != nodeList && nodeList.size() > 0){
				p2 = new Parser();
				p2.setInputHTML(nodeList.toHtml());
				p2.setEncoding("gb2312");
				NodeFilter fileter2 = new NodeClassFilter(LinkTag.class);
				NodeList nodeList2 = p2.extractAllNodesThatMatch(fileter2);
				if(null != nodeList2 && nodeList2.size() > 0){
					for(int i=0;i<nodeList2.size();i++){
						String[] values = new String[3];
						LinkTag link = (LinkTag)nodeList2.elementAt(i);
						int count3 = link.getChildCount();
						if( count3 > 0 ){
							Node tmp2 = link.getChild(0);
							if(tmp2 instanceof ImageTag){
								ImageTag img = (ImageTag)tmp2;
								if(null != img.getImageURL() && !"".equals(img.getImageURL())){
									imgSrc = img.getImageURL();
									picUrl = XCAR_IMAGE_URL_PREFIX+link.getLink();
									/**
									System.out.println(" >> url:"+url);
									System.out.println(" >> imgSrc:"+imgSrc);
									System.out.println(" >> picUrl:"+picUrl);
									System.out.println("\r\n");
									**/
									values[0] = url;
									values[1] = picUrl;
									values[2] = imgSrc;
									list.add(values);
								}
							}
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
	 * ��ȡ��ͼͼƬ��ַ
	 */
	public static String getBigImageSrc(String url){
		Parser p1 = new Parser();
		String src = null;
		try{
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(LinkTag.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "btn1"));
			if(nodeList.size() > 0 ){
				LinkTag link = (LinkTag)nodeList.elementAt(0);
				if(null != link.getLink() && !"".equals(link.getLink())){
					src = link.getLink();
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

	public static void main(String args[]){
		String urlContent = IOUtil.readFile("XCAR_CARDS_URL_02.txt");
		if(null == urlContent && "".equals(urlContent)){
			return;
		}
		String[] urls = getUrls(urlContent);
		if(null == urls){
			System.out.println(" >> ����URLʧ�ܣ�");
			return;
		}

		Thread save = new Thread(new RequestRecordThread(),"recordSavenew");
		save.start();

		int webid = 1;
		for(String tmpUrl:urls){
			List<LinkTag> listLinkTag = getPictureListPageLinkTag(tmpUrl);
			StringBuffer content = new StringBuffer();
			int part = 1;
			for(LinkTag link:listLinkTag){
				List<String> pageList = getPictureLinkPage(link.getLink());
				for(String tmp2Url:pageList){
					List<String[]> tmp = getPictureLink(tmp2Url);
					int subid = 1;
					System.out.println(">> webid:"+webid+"\tsubid:"+subid);
					for(String[] s2:tmp){
						//����ͼ
						 byte[] value2 = HttpClientUtils.getResponseBodyAsByte(s2[0],null,s2[2]);
						 if(null!= value2 && value2.length > 0){
							 String fileName = getName(s2[2]);
							 if(!"".equals(fileName)){
								 try{
									String filePath1 = SAVE_DIR+webid+"\\"+part+"\\"+fileName;
									File tmpFile = new File(filePath1);
									if(tmpFile.exists() && true){
										System.out.println(" >>\t small img "+filePath1+" exists");
										continue;
									}else{
										ResourceQueneInsert.getInstance().setElement(new FileContent(value2,filePath1));
									}
								 }catch(Exception e){
									e.printStackTrace();
									System.err.println(" >> ERROR.MESSAGE:"+e.getMessage());
								 }
							 }
						 }

						 //��ͼ
						 String bigImgSrc = getBigImageSrc(s2[1]);
						 byte[] value3 = HttpClientUtils.getResponseBodyAsByte(s2[1],null,bigImgSrc);
						 if(null != value3 && value3.length > 0){
							 String fileName = getName(bigImgSrc);
							 if(!"".equals(fileName)){
								 try{
									String filePath1 = SAVE_DIR+webid+"\\big\\"+part+"\\"+fileName;
									File tmpFile = new File(filePath1);
									if(tmpFile.exists() && true){
										System.out.println(" >>\t big img "+filePath1+" exists");
										continue;
									}else{
										ResourceQueneInsert.getInstance().setElement(new FileContent(value3,filePath1));
									}
								 }catch(Exception e){
									e.printStackTrace();
									System.err.println(" >> ERROR.MESSAGE:"+e.getMessage());
								 }
							 }
						 }
						 subid++; 
					}
				}
				part++;
			}
			webid ++;
		}
	}
	
	/**
	 * �����ļ�
	 */
	static void createFolder(int webid,List<LinkTag> listLinkTag){
		int part = 1;
		for(LinkTag link:listLinkTag){
			try{
				String filePath1 = SAVE_DIR+webid+"\\"+part+"_"+link.getAttribute("title")+"\\";
				File tmpFile = new File(filePath1);
				if(!tmpFile.exists()){
					System.out.println(" >> dir["+tmpFile.getAbsolutePath()+"] is not exists,create it");
					tmpFile.mkdirs();
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			try{
				String filePath1 = SAVE_DIR+webid+"\\big\\"+part+"_"+link.getAttribute("title")+"\\";
				File tmpFile = new File(filePath1);
				if(!tmpFile.exists()){
					System.out.println(" >> dir["+tmpFile.getAbsolutePath()+"] is not exists,create it");
					tmpFile.mkdirs();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			part ++;
		}
	}


	public static String[] getUrls(String content){
		String[] result = null;
		if(null != content && !"".equals(content)){
			result = content.split("\r\n");
		}
		return result;
	}
}
