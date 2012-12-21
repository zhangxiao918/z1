package com.numberingplans;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.OptionTag;
import org.htmlparser.tags.SelectTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;

import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;

public class IndexParser {
	
	static final String INDEX_URL = "http://www.numberingplans.com/";
	static final String URL = "http://www.numberingplans.com/?page=plans&sub=imeinr&alpha_2_input=44";
	static String URL_TMPLATE = "http://www.numberingplans.com/?page=plans&sub=imeinr&alpha_2_input={type}";
	static MemcacheClient client = MemcacheClient.getInstance();
	static String SAVE_PATH = "d:\\numberingplans\\";
	
	static HashMap<String,String[]> TAC_MAP = new HashMap<String,String[]>();
	static int TMP_COUNT = 0;
	public static void main(String args[]){
//		test1();
//		getKindsTac();
		prepare();
//		loopFile(SAVE_PATH);
//		System.out.println(" >> size:"+TAC_MAP.size());
//		TAC_MAP.clear();
//		System.out.println(" >> count:"+TMP_COUNT);
	}
	
	public static void prepare(){
		HashMap<String,String> map = new HashMap<String,String>();
		try{
			/**
			getKindsTacFromLocal();
			**/
			map = getKindsTacFromLocal();
			Iterator it = map.keySet().iterator();
			while(it.hasNext()){
				int page = 1;
				String key = (String)it.next();
				System.out.println("key:"+key);
				//key.equals("35") || 
				if(key.equals("99") || key.equals("98") || key.equals("97") || key.equals("44") || key.equals("49") || key.equals("45") || key.equals("91") || 
				   key.equals("01") || key.equals("10") || key.equals("30") || key.equals("31") || key.equals("33") || key.equals("51") || key.equals("52")){
					continue;
				}
				if(null != key && !"".equals(key)){
					if(null == client.get(key)){
						String value = map.get(key);
						String count = getPageCount(value);
						page = Integer.valueOf(count);
						boolean isOk = false;
						for(int i=1;i<page+1;i++){
							String url = value+"&current_page="+i;
							if(null == client.get(url)){
								System.out.println(">> now processing:"+url);
								boolean b = savePage(key,url,i);
								if(b){
									client.add(url, url);
								}
								Thread.sleep(25000);
							}
						}
						client.add(key, value);
					}
				}
				Thread.sleep(1000);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			map.clear();
		}
	}
	
	public static void process(String type,String url,int pageNum){
		Parser p1 = null;
		Parser p2 = null;
		String count = "0";
		StringBuffer sb = new StringBuffer();
		try{
			p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("UTF-8");
			
			NodeFilter filter = new NodeClassFilter(TableTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("id","AutoNumber1"));
			if(null != list && list.size() > 0){
				int size = list.size();
				TableTag table = (TableTag)list.elementAt(size-1);
				
//				System.out.println(" >> "+table.toHtml());
				if(null != table){
					int rowc = table.getRowCount();
					for(int j=0;j<rowc;j++){
						if(j<2){
							continue;
						}
						TableRow row = table.getRow(j);
						TableColumn[] col = row.getColumns();
						System.out.println(" >> col.length:"+col.length);
						System.out.println(" >> row.html:"+row.toHtml());
						if(col.length == 3){
							sb.append(">> Type Allocation Code:"+col[0].toPlainTextString()).append("\r\n");
							sb.append(">> Mobile phone manufacturer:"+col[1].toPlainTextString()).append("\r\n");
							sb.append(">> Mobile phone brand and model"+col[2].toPlainTextString()).append("\r\n");
							sb.append("\r\n");
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			IOUtil.createFile(sb.toString(), type+"_"+pageNum);
			if(null != p1)
				p1 = null;
		}
	}
	
	public static void process(String content){
		Parser p1 = null;
		StringBuffer sb = new StringBuffer();
		String[] tacs = new String[3];
		try{
			p1 = new Parser();
			p1.setInputHTML(content);
			p1.setEncoding("UTF-8");
			
			NodeFilter filter = new NodeClassFilter(TableTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("id","AutoNumber1"));
			if(null != list && list.size() > 0){
				int size = list.size();
				TableTag table = (TableTag)list.elementAt(size-1);
				
//				System.out.println(" >> "+table.toHtml());
				if(null != table){
					int rowc = table.getRowCount();
					for(int j=0;j<rowc;j++){
						if(j<2){
							continue;
						}
						TableRow row = table.getRow(j);
						TableColumn[] col = row.getColumns();
						if(col.length == 3){
							tacs[0] = col[0].toPlainTextString().trim();
							tacs[1] = col[1].toPlainTextString().trim();
							tacs[2] = col[2].toPlainTextString().trim();
//							sb.append(">> Type Allocation Code:"+tacs[0]).append("\r\n");
//							sb.append(">> Mobile phone manufacturer:"+tacs[1]).append("\r\n");
//							sb.append(">> Mobile phone brand and model:"+tacs[2]).append("\r\n");
//							sb.append("\r\n");
							TAC_MAP.put(tacs[0], tacs);
							TMP_COUNT++;
						}
					}
				}
			}
		}catch(Exception e){
			System.err.println(e);
		}finally{
			if(null != p1)
				p1 = null;
		}
		System.out.println(sb.toString());
	}
	
	public static boolean savePage(String type,String url,int pageNum){
		String filePath = SAVE_PATH+type+"\\"+type+"_"+pageNum+".html";
		try{
			File file = new File(filePath);
			if(file.exists()){
				System.err.println(" >> 文件["+file.getAbsolutePath()+"]已存在");
				return false;
			}
			if(null == client.get(url)){
				String content = HttpClientUtils.getResponseBody(url);
				System.out.println(" >> length:"+content.length());
				if(file.length() == content.length()){
					System.err.println(" >> 文件一样，不需要更新");
					return false;
				}
				if(content.length() == 11033){
					return false;
				}
				if(null != content && !"".equals(content)){
					if(!file.exists()){
						file.getParentFile().mkdir();
					}
					IOUtil.createFile(content, SAVE_PATH+type, type+"_"+pageNum+".html");
					client.add(url, new NumberingPlansFile(url,content.length(),file));
				}
				return true;
			}else{
				Object obj = client.get(url);
				NumberingPlansFile npf = (NumberingPlansFile)obj;
				String content = HttpClientUtils.getResponseBody(url);
				if(npf.getLength() == content.length()){
					return false;
				}
				if(content.length() == 11033){
					return false;
				}
				if(null != content && !"".equals(content)){
					if(!npf.getFile().exists()){
						npf.getFile().getParentFile().mkdir();
					}
					IOUtil.createFile(content, SAVE_PATH+type, type+"_"+pageNum+".html");
					client.remove(url);
					client.add(url, new NumberingPlansFile(url,content.length(),npf.getFile()));
					return true;
				}
			}
		}catch(Exception e){
			return false;
		}
		return false;
	}
	
	public static void test1(){
		Parser p1 = null;
		Parser p2 = null;
		String count = "0";
		try{
			p1 = new Parser();
			p1.setURL(URL);
			p1.setEncoding("UTF-8");
			
			NodeFilter filter = new NodeClassFilter(CompositeTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("style","border-left-style:none; border-left-width:medium; border-right-style:none; border-right-width:medium; border-bottom-style:none; border-bottom-width:medium; border-top-style:solid; border-top-width:1"));
			if(null != list && list.size() > 0){
//				System.out.println(" >> html:"+list.toHtml());
				p2 = new Parser();
				p2.setInputHTML(list.toHtml());
				p2.setEncoding(p1.getEncoding());
				
				filter = new NodeClassFilter(LinkTag.class);
				list = p2.extractAllNodesThatMatch(filter);
				
				if(null != list && list.size() > 0){
					LinkTag link = (LinkTag)list.elementAt(list.size()-1);
					if(null != link){
						String url = link.getLink();
						String[] params = url.split("&");
						if(params.length > 0){
							String[] values = params[params.length-1].split("=");
//							System.out.println(" >> values[0]:"+values[0]);
//							System.out.println(" >> values[1]:"+values[1]);
							count = values[1];
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p1)
				p1 = null;
		}
		System.out.println(" >> count:"+count);
	}
	
	/**
	 * 获取TAC类别页面
	 * @return
	 */
	public static HashMap<String,String> getKindsTacFromRemote(){
		Parser p1 = null;
		Parser p2 = null;
		HashMap<String,String> map = new HashMap<String,String>();
		try{
			p1 = new Parser();
			p1.setURL(URL);
			p1.setEncoding("UTF-8");
			
			NodeFilter filter = new NodeClassFilter(SelectTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("name","alpha_2_input"));
			if(null != list && list.size() > 0){
				if(list.size() == 1){
					SelectTag tag = (SelectTag)list.elementAt(0);
					OptionTag[] options = tag.getOptionTags();
					for(OptionTag opt:options){
						String url = URL_TMPLATE;
						url = url.replace("{type}",opt.getValue());
						map.put(opt.getValue(), url);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p2)
				p2 = null;
			if(null != p1)
				p1 = null;
		}
		return map;
	}
	
	/**
	 * 获取TAC类别页面
	 * @return
	 */
	public static HashMap<String,String> getKindsTacFromLocal(){
		Parser p1 = null;
		Parser p2 = null;
		HashMap<String,String> map = new HashMap<String,String>();
		try{
			String content = IOUtil.readFile(System.getProperty("user.dir")+File.separator+"NUMBERINGPLANS.txt", "GBK", "txt");
			if(null != content && !content.equals("")){
//				System.out.println(" >> content:"+content);
				String[] rows = content.split("\r\n");
				for(String row:rows){
					String[] t = row.split("\t\t");
//					System.out.println("\t\t[0]:"+t[0]);
//					System.out.println("\t\t[1]:"+t[1]);
//					System.out.println("\r\n");
					map.put(t[0],t[1]);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(null != p2)
				p2 = null;
			if(null != p1)
				p1 = null;
		}
		return map;
	}
	
	public static String getPageCount(String url){
		Parser p1 = null;
		Parser p2 = null;
		String count = "1";
		try{
			p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("UTF-8");
			
			NodeFilter filter = new NodeClassFilter(CompositeTag.class);
			NodeList list = p1.extractAllNodesThatMatch(filter).extractAllNodesThatMatch(new HasAttributeFilter("style","border-left-style:none; border-left-width:medium; border-right-style:none; border-right-width:medium; border-bottom-style:none; border-bottom-width:medium; border-top-style:solid; border-top-width:1"));
			if(null != list && list.size() > 0){
				p2 = new Parser();
				p2.setInputHTML(list.toHtml());
				p2.setEncoding(p1.getEncoding());
				
				filter = new NodeClassFilter(LinkTag.class);
				list = p2.extractAllNodesThatMatch(filter);
				
				if(null != list && list.size() > 0){
					LinkTag link = (LinkTag)list.elementAt(list.size()-1);
					if(null != link){
						String turl = link.getLink();
						String[] params = turl.split("&");
						if(params.length > 0){
							String[] values = params[params.length-1].split("=");
							count = values[1];
						}
					}
				}
			}
		}catch(Exception e){
			System.err.println(e);
		}finally{
			if(null != p2)
				p2 = null;
			if(null != p1)
				p1 = null;
		}
		System.out.println(" >> count:"+count);
		return count;
	}
	
	/**
	 * 获取下载的HTML文件内容，此方法为递归方法
	 * @param filePath
	 */
	public static void loopFile(String filePath){
		File file = null;
		try{
			file = new File(filePath);
			if(file.isDirectory()){
				for(File sub:file.listFiles()){
					loopFile(sub.getAbsolutePath());
				}
			}else{
				String content = IOUtil.readFile(file.getAbsolutePath(), "GBK", "html");
				if(null != content && !"".equals(content)){
					process(content);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

/**
 * 网站文件对象
 * @author bluestome
 *
 */
class NumberingPlansFile implements java.io.Serializable {
	
	private String url = null;
	
	private int length = 0;
	
	private File file = null;
	
	public NumberingPlansFile(String url,int length,File file){
		this.url = url;
		this.length = length;
		this.file = file;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
}