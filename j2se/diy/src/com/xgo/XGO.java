package com.xgo;
import java.text.DecimalFormat;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.math.BigDecimal;
import java.io.BufferedReader;   
import java.io.File;
import java.io.IOException;   
import java.io.InputStream;   
import java.io.InputStreamReader;   
import java.net.Authenticator;   
import java.net.HttpURLConnection;   
import java.net.PasswordAuthentication;   
import java.net.URL;   
import java.net.URLConnection;   
import java.util.Properties; 
import java.util.StringTokenizer;

import org.htmlparser.*;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.*;
import org.htmlparser.util.NodeList;

import com.chinamilitary.bean.Article;
import com.chinamilitary.bean.ImageBean;
import com.chinamilitary.bean.PicfileBean;
import com.chinamilitary.bean.WebsiteBean;
import com.chinamilitary.dao.ArticleDao;
import com.chinamilitary.dao.ImageDao;
import com.chinamilitary.dao.PicFileDao;
import com.chinamilitary.dao.WebSiteDao;
import com.chinamilitary.factory.DAOFactory;
import com.chinamilitary.memcache.MemcacheClient;
import com.chinamilitary.util.CacheUtils;
import com.chinamilitary.util.HttpClientUtils;
import com.chinamilitary.util.IOUtil;
import com.chinamilitary.util.StringUtils;
import com.common.Constants;

public class XGO 
{
	public static final String FCT = "1,A 奥迪,6,A 阿斯顿·马丁,5,A 阿尔法·罗密欧,8,B 本田,2,B 宝马,11,B 别克,9,B 比亚迪,4,B 奔驰,10,B 标致,47,B 北京,3,B 保时捷,7,B 奔腾,18,B 宾利,73,B 布嘉迪,111,B 宝骏,23,C 长城,22,C 长安,25,C 长丰,84,C 昌河,29,D 大众,88,D 帝豪,32,D 东风,30,D 道奇,78,D 东南,91,D 东风风神,27,D 大发,39,F 丰田,40,F 福特,33,F 法拉利,37,F 菲亚特,35,F 飞碟,77,F 福田,96,G GMC　　,100,G 光冈,93,G 广汽,98,G Gumpert,42,H 海马,85,H 华泰,46,H 悍马,101,H 黄海,41,H 哈飞,76,H 红旗,51,J 吉利,53,J 江淮,72,J Jeep,82,J 江南,54,J 捷豹,50,J 金杯,89,J 吉奥,94,J 江铃,55,K 凯迪拉克,56,K 克莱斯勒,105,K 开瑞,90,K 柯尼赛格,87,K 克尔维特,103,K KTM,64,L 铃木,59,L 雷克萨斯,66,L 路虎,57,L 兰博基尼,60,L 雷诺,62,L 力帆,109,L 蓝旗亚,65,L 陆风,63,L 林肯,61,L 莲花,58,L 劳斯莱斯,68,M 马自达,67,M MG,71,M MINI,69,M 玛莎拉蒂,70,M 迈巴赫,107,N 纳智捷,12,O 欧宝,13,O 讴歌,106,P 帕加尼,14,Q 起亚,16,Q 奇瑞,83,Q 全球鹰,15,Q 青年莲花,19,R 日产,17,R 荣威,79,R 瑞麒,26,S 三菱,24,S 斯柯达,21,S 斯巴鲁,31,S 双环,28,S 双龙,74,S Smart,20,S 萨博,81,S 世爵,112,S SPIRRA,110,T Tramontana,36,W 沃尔沃,104,W 五菱,80,W 威麟,102,W 威兹曼,44,X 现代,38,X 雪佛兰,43,X 雪铁龙,99,X 西亚特,34,Y 一汽,86,Y 英伦,45,Y 英菲尼迪,92,Y 野马,48,Z 中华,52,Z 众泰,97,Z 中兴";
	static final String INSERT_CAR_BRAND_SQL = "insert into TBL_CARS_BRAND (d_brand_id,d_brand_name,d_brand_short_name,d_brand_desc) values ({1},'{2}','{3}','{4}');\r\n";
	static final String INSERT_CARS_SQL = "insert into TBL_CARS (d_brand_id,d_cars_id,d_cars_name,d_cars_short_name,d_cars_desc) values ({1},{2},'{3}','{4}','{5}');\r\n";
	static final String INSERT_CARS_YEAR_STYLE_SQL = "insert into tbl_cars_year_style (d_cars_id,d_year_style_id,d_year_style) values ({1},{2},'{3}');\r\n";
	static final String INSERT_CARS_INFO_SQL = "insert into tbl_car_info (d_car_year_style_id,d_car_info_id,d_car_name,d_car_level,d_engine_id,d_transmission_id) values ({1},{2},'{3}',-1,-1,-1);\r\n";
	static final String BRAND_URL = "http://product.xgo.com.cn/price_list/brand_{id}_1.shtml";
	static final String SERIES_URL = "http://www.xgo.com.cn/{id}";
	static final String TMP_FILENAME = "tmp.txt";
	private static final Integer D_PARENT_ID = 1510;
	static final String INSERT_FC_BRAND_SQL  = "insert into tbl_web_site (d_parent_id,d_web_url,d_web_name,d_remarks) values ("+D_PARENT_ID+",'{2}','{3}','{4}');";
	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();
	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();
	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();
	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();
	static String PIC_SAVE_PATH = Constants.FILE_SERVER;
	static MemcacheClient client = MemcacheClient.getInstance();
	static int COUNT = 0 ;
	static final String IMAGE_CACHE_KEY = "XGO_CACHE_KEY_IMAGE";
	static final String ARTICLE_CACHE_KEY = "XGO_CACHE_KEY_ARTICLE";
	public static void main(String[] args) {
//		parserAutoBrandSQL();
//		parserCars();
		
		new Thread(new Runnable(){
			private boolean isRun = true;
			public void run() {
				while(isRun){
					try{
						getCarSerial();
						getArticleImage();
						try{
							imgDownload();
						}catch(Exception e){
							e.printStackTrace();
						}
						isRun = false;
						System.exit(-1);
						//休眠半小时
						Thread.sleep(80000000);
					}catch(Exception e){
						e.printStackTrace();
						isRun = false;
					}
				}
			}
		 }).start();
		
	}

	/**
	 * 获取文章下的图片
	 *
	 */
	private static void getArticleImage() {
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
			for(WebsiteBean bean:list){
				getArticleImage2(bean);
				Thread.sleep(100);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	static void imgDownload() throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for(WebsiteBean website:webList){
			imgDownload2(website);
			Thread.sleep(100);
		}
	}
	
	static void imgDownload2(WebsiteBean website) throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(website.getId());
		if(webList.size() > 0){
			System.out.println(" >> "+website.getName()+"|"+website.getUrl()+"|"+webList.size());
			for(WebsiteBean web:webList){
				imgDownload2(web);
				Thread.sleep(100);
			}
		}else{
			System.out.println(website.getId()+"|"+website.getName()+"|"+website.getUrl());
//			List<Article> artList = articleDao.findByWebId(website.getId(), "FD");
			List<Article> artList = articleDao.findByWebId(website.getId());
			System.out.println("文章数量:"+artList.size());
			for(Article article:artList){
				List<ImageBean> list = imageDao.findImage(article.getId());
				System.out.println(">> 图片数量:["+article.getTitle()+"["+article.getId()+"]]"+list.size()+"\n**************************");
				if(list.size() == 0){
					article.setText("NED");
					if(articleDao.update(article)){
						System.out.println(">> 更新图片记录数据为0的文章成功");
					}
				}else{
					for(ImageBean bean : list) {
						if(bean.getStatus() == -1){
							if(download(bean)){
								System.out.println(">> 更新图片对象["+bean.getId()+"]成功");
							}
						}
					}
				}
			}
		}
	}
	static boolean download(ImageBean imgBean) {
		PicfileBean bean = null;
		bean = new PicfileBean();
		String s_fileName = imgBean.getImgUrl().substring(
				imgBean.getImgUrl().lastIndexOf("/") + 1,
				imgBean.getImgUrl().length());
		String fileName = imgBean.getHttpUrl().substring(
				imgBean.getHttpUrl().lastIndexOf("/") + 1,
				imgBean.getHttpUrl().length());
		String length = "0";
		try {
			if(imgBean.getHttpUrl().toLowerCase().endsWith("htm")){
				return false;
			}
			byte[] small = null;
			small = HttpClientUtils.getResponseBodyAsByte(imgBean.getImgUrl(), null, imgBean.getImgUrl());
			if(null == small)
				return false;
			//小图
			if (client.get(CacheUtils.getShowImgKey(PIC_SAVE_PATH
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ s_fileName)) == null) {
				IOUtil.createFile(small, File.separator+PIC_SAVE_PATH
						+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
						+ imgBean.getArticleId() + File.separator
						+ s_fileName);
			}
			byte[] big = null;
			big = HttpClientUtils.getResponseBodyAsByte(imgBean.getCommentshowurl(), null, imgBean.getHttpUrl());
			if(null == big)
				return false;
			length = String.valueOf(big.length);
			if(length.equalsIgnoreCase("20261")){
				return false;
			}
			//大图
			if (client.get(CacheUtils.getBigPicFileKey(PIC_SAVE_PATH
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ fileName)) == null) {
				IOUtil.createFile(big, File.separator+PIC_SAVE_PATH
						+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
						+ imgBean.getArticleId() + File.separator
						+ fileName);
			}
			bean.setArticleId(imgBean.getArticleId());
			bean.setImageId(imgBean.getId());
			bean.setTitle(imgBean.getTitle());
			bean.setSmallName(File.separator
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ s_fileName);
			bean.setName(File.separator
					+ StringUtils.gerDir(String.valueOf(imgBean.getArticleId()))
					+ imgBean.getArticleId() + File.separator
					+ fileName);
			bean.setUrl(PIC_SAVE_PATH);
			try {
				imgBean.setStatus(1);
				imgBean.setLink("FD");
				imgBean.setFileSize(Long.valueOf(length));
				if(imageDao.update(imgBean)){
					boolean b = picFiledao.insert(bean);
					if (b) {
						client.add(CacheUtils.getBigPicFileKey(bean.getUrl()
								+ bean.getName()), bean);
						client.add(CacheUtils.getSmallPicFileKey(bean.getUrl()
								+ bean.getSmallName()), bean);
					} else {
						return false;
					}
				}
			} catch (Exception e) {
				System.out.println("数据库异常");
				e.printStackTrace();
				return false;
			}
		} catch (IOException e) {
			System.out.println("网络连接，文件IO异常");
			return false;
		}
		return true;
	}
	/**
	 * 获取大图图片地址
	 */
	public static String getBigImageSrc(String url){
		Parser p1 = null;
		String src = null;
		String content = null;
		try{
			content = HttpClientUtils.getResponseBody(url);
			if(null == content || "".equals(content)){
				return src;
			}
			p1 = new Parser();
			p1.setInputHTML(content);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(LinkTag.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("id", "pro_photo_6"));
			if(nodeList.size() > 0 ){
				LinkTag tag = (LinkTag)nodeList.elementAt(0);
				if(tag.getChildCount() > 0){
					Node tmp2 = tag.getChild(1);
					if(tmp2 instanceof ImageTag){
						ImageTag img = (ImageTag)tmp2;
						if(null != img.getImageURL() && !"".equals(img.getImageURL())){
							src = img.getImageURL();
						}
					}
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
	 * 获取文章下的图片
	 *
	 */
	private static void getArticleImage2(WebsiteBean web) {
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(web.getId());
			if(list.size() > 0){
				for (WebsiteBean bean : list) {
					getArticleImage2(bean);
					Thread.sleep(100);
				}
			}else{
				List<Article> alist = articleDao.findByWebId(web.getId(), "NED");
				
				System.out.println(" >> "+web.getName()+"|"+web.getUrl()+"|"+web.getParentId()+"|"+alist.size());
				for(Article art:alist){
					Map<String, Object> map = getPictureLinkPage2(art);
					Iterator it = map.keySet().iterator();
					int i = 0;
					String icon = null;
					while(it.hasNext()){
						String key = (String)it.next();
						List<String[]> ilist = getPictureLink(key);
						ImageBean img = null;
						for(String[] str:ilist){
							img = new ImageBean();
							String bg = getBigImageSrc(str[1]);
							if(null != bg && !"".equals(bg)){
								if(null != client.get(IMAGE_CACHE_KEY+bg)){
									continue;
								}
								img.setImgUrl(str[2]);
								img.setHttpUrl(bg);
								img.setCommentsuburl(str[0]);
								img.setCommentshowurl(str[0]);
								img.setArticleId(art.getId());
								img.setTitle(str[3]);
								img.setName(str[3]);
								icon = img.getImgUrl();
//								System.out.println(" >> title:"+ img.getTitle());
//								System.out.println(" >> img:"+ img.getImgUrl());
//								System.out.println(" >> http:"+ img.getHttpUrl());
								int result = imageDao.insert(img);
								if(result > 0){
									i++;
									client.add(IMAGE_CACHE_KEY+bg, img);
								}else{
									System.err.println(" >> 添加图片["+img.getArticleId()+"|"+img.getTitle()+"|"+bg+"]失败!");
								}
							}else{
								System.err.println(" >> 获取图片大图["+str[1]+"]失败!");
							}
						}
					}
					int count = Integer.parseInt(art.getIntro());
					if(i == count){
						art.setText("FD");
						if(null != icon){
							art.setActicleXmlUrl(icon);
						}
						if(articleDao.update(art)){
							System.out.println(" >> 更新文章["+art.getId()+"]成功!");
							icon = null;
						}
					}else{
						int imc = imageDao.findImage(art.getId()).size();
						if(count == imc){
							art.setText("FD");
							if(null != icon){
								art.setActicleXmlUrl(icon);
							}
							if(articleDao.update(art)){
								System.out.println(" >> 更新文章["+art.getId()+"]成功!");
								icon = null;
							}
						}
					}
				}
				Thread.sleep(100);
				/**
				**/
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getCarSerial(){
		COUNT = 0;
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
			for(WebsiteBean bean:list){
				getCarSerial2(bean);
			}
			System.out.println(" >> COUNT:"+COUNT);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void getCarSerial2(WebsiteBean web){
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(web.getId());
			if(list.size() > 0){
				for (WebsiteBean bean : list) {
					getCarSerial2(bean);
				}
			}else{
				
				String lastModify = HttpClientUtils.getLastModifiedByUrl(web.getUrl());
				if(null != web.getLastModifyTime() && !"".equals(web.getLastModifyTime()) && web.getLastModifyTime().equals(lastModify)){
					return;
				}

				//获取车型下视角图片
				List<LinkTag> ll = getPictureListPageLinkTag(web.getUrl());
				Article art = null;
				for(LinkTag u:ll){
					art = new Article();
					//
					if(null != u.getLinkText() && !"".equals(u.getLinkText())){
						if(null != client.get(ARTICLE_CACHE_KEY+u.getLink())){
							continue;
						}
						int end = u.getLinkText().lastIndexOf("(");
						//共有68图片
						String name = u.getLinkText().substring(0,end);
						int start = u.getLinkText().lastIndexOf("(");
						int end2 = u.getLinkText().lastIndexOf(")");
						int end3 = u.getLinkText().lastIndexOf("张");
						if(end3 != -1){
							String count = u.getLinkText().substring(start+1,end3);
							System.out.println(" >> ["+u.getLinkText()+"]count:"+count);
							art.setIntro(count);
						}else{
							String count = u.getLinkText().substring(start+1,end2);
							System.out.println(" >> ["+u.getLinkText()+"]count:"+count);
							art.setIntro(count);
						}
						art.setTitle(name);
					}else{
						art.setTitle(u.getLinkText());
					}
					art.setArticleUrl(u.getLink());
					art.setWebId(web.getId());
					int result = articleDao.insert(art);
					if(result > 0){
						client.add(ARTICLE_CACHE_KEY+art.getArticleUrl(), art);
						System.out.println(" >> 添加"+art.getWebId()+"|"+result+"|"+art.getArticleUrl()+"|"+art.getIntro() + "成功");
					}else{
						System.err.println(" >> 添加"+art.getWebId()+"|"+result+"|"+art.getArticleUrl()+"|"+art.getIntro() + "失败");
					}
				}
				
				if(lastModify != null && !"".equals(lastModify) ){
					if(null == web.getLastModifyTime() || "".equals(web.getLastModifyTime()) || !web.getLastModifyTime().equals(lastModify)){
						web.setLastModifyTime(lastModify);
						if(webSiteDao.update(web)){
							System.out.println(" >> 更新网站["+web.getName()+"|"+web.getUrl()+"]最后时间["+lastModify+"]成功!");
						}
					}
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据URL获取图片页面地址
	 * @demo http://newcar.xcar.com.cn/7/photo.htm
	 * @param url
	 */
	public static List<LinkTag> getPictureListPageLinkTag(String url){
		Parser p1 = new Parser();
		List<LinkTag> list = new ArrayList<LinkTag>();
		try{
			p1.setURL(url+"/pic.html");
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(CompositeTag.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "mphoto_tit"));;
			if(null != nodeList && nodeList.size() > 0){
				for(int i=0;i<nodeList.size();i++){
					CompositeTag pos = (CompositeTag)nodeList.elementAt(i);
					if(pos.getChildCount() == 2){
						LinkTag link = (LinkTag)pos.getChild(1);
						list.add(link);
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
	
	static void analiyBrand(String content){
		StringBuffer buffer = new StringBuffer();
		if(null == content || "".equals(content)){
			return;
		}
		String[] subs = content.split(";");
		for(String sub:subs){
			String[] tmps = sub.split("=");
			if(tmps.length == 2){
				buffer.append(tmps[1]);
			}
			buffer.append(",");
		}
		IOUtil.createFile(buffer.toString().replace("\"",""),"XGO_BRAND");
	}
	
	static void analiyCars(String content){
		StringBuffer buffer = new StringBuffer();
		if(null == content || "".equals(content)){
			return;
		}
		String[] subs = content.split(";");
		for(String sub:subs){
			buffer.append(sub.replace("@",","));
			buffer.append("\r\n");
		}
		IOUtil.createFile(buffer.toString().replace("\"","'"),"xgo_ps_att");
	}

	static void analiyCarsModel(String content){
		StringBuffer buffer = new StringBuffer();
		if(null == content || "".equals(content)){
			return;
		}
		String[] subs = content.split(";");
		for(String sub:subs){
			buffer.append(sub.replace("@",","));
			buffer.append("\r\n");
		}
		IOUtil.createFile(buffer.toString().replace("\"","'"),"xgo_car_model");
	}

	static void analiyCarsYears(String content){
		StringBuffer buffer = new StringBuffer();
		if(null == content || "".equals(content)){
			return;
		}
		String[] subs = content.split(";");
		for(String sub:subs){
			buffer.append(sub.replace("\"","'"));
			buffer.append("\r\n");
		}
		IOUtil.createFile(buffer.toString(),"xgo_car_years");
	}

	public static void analiyFile(){
		String content = null;
		try{
			content = IOUtil.readFile(TMP_FILENAME);
			String[] subs = content.split("\r\n");
			System.out.println( " >> length:" + subs.length);
			int i = 0;
			for(String sub:subs){
				System.out.println(" >> i:"+i);
				switch(i){
					case 0:
//						analiyBrand(sub);
						break;
					case 1:
//						analiyCars(sub);
						break;
					case 2:
						analiyCarsModel(sub);
						break;
					case 3:
						analiyCarsYears(sub);
						break;
				}
				i ++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return;
	}

    public static String getContent(String strUrl) {   
        try {   
  
            URL url = new URL(strUrl);   
            BufferedReader br = new BufferedReader(new InputStreamReader(url   
                    .openStream(),"UTF-8"));   
            String s = "";   
            StringBuffer sb = new StringBuffer("");   
            while ((s = br.readLine()) != null) {   
                sb.append(s + "\r\n");   
            }   
            br.close();   
            return sb.toString();   
        } catch (Exception e) {   
            return "error open url:" + strUrl;   
        }   
    } 
	
	public static String fileSize(Long size){
		   DecimalFormat df = new DecimalFormat("###.##");
		   float f;
		   if (size < 1024 * 1024) {
			f = (float) ((float) size / (float) 1024);
			return (df.format(new Float(f).doubleValue())+"KB");
		   } else {
			f = (float) ((float) size / (float) (1024 * 1024));
			return (df.format(new Float(f).doubleValue())+"MB");
		   }  
	}

	public static void parserAutoBrandSQL(){
		String[] fcts = FCT.split(",");
		int length  = fcts.length;
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<length;i+=2){
			String url = BRAND_URL;
			String tmp = INSERT_FC_BRAND_SQL;
			String value = fcts[i];
			String text = fcts[i+1];
			System.out.println(" >> value="+value+"|text="+text);
			url = url.replace("{id}",value);
			tmp = tmp.replace("{2}",url).replace("{3}",text).replace("{4}",value);
			System.out.println(" >> tmp:"+tmp);
			sb.append(tmp).append("\r\n");
		}
		IOUtil.createFile(sb.toString(),"BRAND_URL_0428");
	}

	public static void parserCars(){
		StringBuffer sb = new StringBuffer();
		String content = IOUtil.readFile("E:\\2.WS\\1.PRODUCT\\diy_page\\src\\com\\xgo\\xgo_ps_att.txt","GBK","txt");
//		System.out.println(" >> "+content);
		content = content.replace("'","").replace(";","");
		String[] res = content.split("\r\n");
		int i = 0;
		for(String re:res){
			List<Cars> list = carsParser(re);
			if(null != list && list.size() > 0){
				WebsiteBean web = null;
				for(Cars car:list){
					if(null != car){
						String tmp = SERIES_URL;
						web = getWebsite(car.getParentId());
						if(null != web){
//							System.out.println(" >> "+web.getName()+"|"+web.getUrl()+"|"+web.getRemarks());
//							System.out.println(" >> id："+car.getId());
//							System.out.println(" >> name:"+car.getName());
//							System.out.println(" >> parentId:"+car.getParentId());
							
							tmp = tmp.replace("{id}",car.getId());
							
							WebsiteBean tweb = web;
							tweb.setParentId(web.getId());
							tweb.setName(car.getName());
							tweb.setUrl(tmp);
							tweb.setRemarks(car.getId());
							
//							System.out.println(" >> 添加:"+tweb.getId()+"|"+tweb.getParentId()+"|"+tweb.getName()+"|"+tweb.getUrl()+"|"+tweb.getRemarks()+" 成功!");
							try{
								if(webSiteDao.insert(tweb)){
									i++;
									System.out.println(" >> 添加:"+tweb.getParentId()+"|"+tweb.getId()+"|"+tweb.getName()+"|"+tweb.getUrl()+"|"+tweb.getRemarks()+" 成功!");
								}
							}catch(Exception e){
								e.printStackTrace();
							}
							
						}
						/**
						String tmp = INSERT_CARS_SQL;
						tmp = tmp.replace("{1}",car.getParentId()).replace("{2}",car.getId()).replace("{3}",car.getName()).replace("{4}",car.getShortName()).replace("{5}",car.getShortName()+" "+car.getName());
						**/
//						System.out.println(" >> URL:"+tmp);
						sb.append(tmp).append("\r");
					}
				}
//				System.out.println(" >> num:"+list.size());
			}
		}
		System.out.println(" >> i:"+i);
		i = 0;
//		IOUtil.createFile(sb.toString(),"CARDS_URL");
//		System.out.println(" >> sb: "+sb.toString());
		
	}

	public static void parserCarModel(){
		StringBuffer sb = new StringBuffer();
		String content = IOUtil.readFile("spl.txt");
		content = content.replace("'","").replace(";","");
		String[] res = content.split("\r\n");
		for(String re:res){
			System.out.println(" >>\t"+re);
			/**
			List<CarsModel> list = carsModelParser(re);
			if(null != list && list.size() > 0){
				for(CarsModel model:list){
					if(null != model){
						String tmp = INSERT_CARS_INFO_SQL;
						tmp = tmp.replace("{1}",model.getParentId()).replace("{2}",model.getId()).replace("{3}",model.getModelName());
						sb.append(tmp).append("\r");
						System.out.println("\r\n");
					}
				}
			}
		**/
		}
//		System.out.println(" >> :"+sb.toString());
//		IOUtil.createFile(sb.toString(),"CAR_MODELS");
	}

	public static void parserCarModelYear(){
		StringBuffer sb = new StringBuffer();
		String content = IOUtil.readFile("spec_year_name.txt");
		content = content.replace("'","").replace(";","");
		String[] res = content.split("\r\n");
		for(String re:res){
			List<CarsYear> list = carsModelYearParser(re);
			if(null != list && list.size() > 0){
				for(CarsYear year:list){
					if(null != year){
						String tmp = INSERT_CARS_YEAR_STYLE_SQL;
						tmp = tmp.replace("{1}",year.getParentId()).replace("{2}",year.getId()).replace("{3}",year.getYears());
						sb.append(tmp).append("\r");
					}
				}
			}
//			System.out.println(" >> sb.toString():"+sb.toString());
		}
//		IOUtil.createFile(sb.toString(),"CARDS_YEAR_STYLE");
	}

	public static List<Cars> carsParser(String content){
		List<Cars> list = new ArrayList<Cars>();
		try{
			String[] res = content.split("=");
			if(res.length == 2){
				String key = res[0];
				String value = res[1];
				List<String[]> infos = getCarsInfo(value);
				Cars car = null;
				for(String[] info:infos){
					car = new Cars();
					car.setParentId(getPaserId(key));
					car.setId(info[0]);
					car.setShortName(info[1]);
					car.setName(info[2]);
					list.add(car);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
	public static List<CarsYear> carsModelYearParser(String content){
		List<CarsYear> list = new ArrayList<CarsYear>();
		try{
			String[] res = content.split("=");
			if(res.length == 2){
				String key = res[0];
				String value = res[1];
				List<String[]> infos = getCarsYearsInfo(value);
				CarsYear year = null;
				for(String[] info:infos){
					year = new CarsYear ();
					year.setParentId(getPaserId(key));
					year.setId(info[0]);
					year.setYears(info[1]);
					list.add(year);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}


	public static List<CarsModel> carsModelParser(String content){
		List<CarsModel> list = new ArrayList<CarsModel>();
		try{
			String[] res = content.split("=");
			if(res.length == 2){
				String key = res[0];
				String value = res[1];
				List<String[]> infos = getCarsModelInfo(value);
				CarsModel model = null;
				for(String[] info:infos){
					model = new CarsModel ();
					model.setParentId(getPaserId(key));
					model.setId(info[0]);
					model.setModelName(info[1]);
					list.add(model);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public static String getPaserId(String key){
		String parId = "";
		int start = key.indexOf("[");
		int end = key.indexOf("]");
		if(start != -1 && end != -1){
			parId = key.substring(start+1,end);
		}
		return parId.trim();
	}

	public static List<String[]> getCarsInfo(String arg0){
		List<String[]> list = new ArrayList<String[]>();
		String[] fcts = arg0.split(",");
		int length  = fcts.length;
		for(int i=0;i<length;i+=2){
			String[] infos = new String[3];
			String value = fcts[i];
			String text = fcts[i+1];
			infos[0] = value;
			infos[1] = text;
			infos[2] = text;
			list.add(infos);
		}
		return list;
	}

	public static List<String[]> getCarsYearsInfo(String arg0){
		List<String[]> list = new ArrayList<String[]>();
		String[] fcts = arg0.split(",");
		int length  = fcts.length;
		for(int i=0;i<length;i+=2){
			String[] infos = new String[2];
			String value = fcts[i];
			String text = fcts[i+1];
			infos[0] = value;
			int start = text.indexOf(" ");
			if(start != -1){
				if(null != text.substring(0,start) && !"".equals(text.substring(0,start))){
					infos[1] = text.substring(0,start);
				}
				if(null != text.substring(start,text.length()) && !"".equals(text.substring(start,text.length()))){
					infos[2] = text.substring(start,text.length());
				}
			}else{
				infos[1] = text;
			}
			list.add(infos);
		}
		return list;
	}

	public static List<String[]> getCarsModelInfo(String arg0){
		List<String[]> list = new ArrayList<String[]>();
		String[] fcts = arg0.split(",");
		int length  = fcts.length;
		for(int i=0;i<length;i+=2){
			String[] infos = new String[2];
			String value = fcts[i];
			String text = fcts[i+1];
			infos[0] = value;
			infos[1] = text;
			/**
			int start = text.indexOf(" ");
			if(start != -1){
				if(null != text.substring(0,start) && !"".equals(text.substring(0,start))){
					infos[1] = text.substring(0,start);
				}
				if(null != text.substring(start,text.length()) && !"".equals(text.substring(start,text.length()))){
					infos[2] = text.substring(start,text.length());
				}
			}
			**/
			list.add(infos);
		}
		return list;
	}

	/**
	 * 获取图片大图地址，以及缩略图地址
	 * 记录列表泛型数组
	 * 数组【0】 引用地址
	 * 数组【1】 大图页面地址
	 × 数组【2】 图片缩略图地址
	 */
	public static List<String[]> getPictureLink(String url){
		Parser p1 = null;
		Parser p2 = null;
		List<String[]> list = new ArrayList<String[]>();
		String picUrl ="";
		String imgSrc = "";
		String title = "";
		try{
			p1 = new Parser();
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(CompositeTag.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "e5"));
			if(null != nodeList && nodeList.size() > 0){
				p2 = new Parser();
				p2.setInputHTML(nodeList.toHtml());
				p2.setEncoding("gb2312");
				NodeFilter fileter2 = new NodeClassFilter(LinkTag.class);
				NodeList nodeList2 = p2.extractAllNodesThatMatch(fileter2);
				if(null != nodeList2 && nodeList2.size() > 0){
					for(int i=0;i<nodeList2.size();i++){
						String[] values = new String[4];
						LinkTag link = (LinkTag)nodeList2.elementAt(i);
						int count3 = link.getChildCount();
						if( count3 > 0 ){
							Node tmp2 = link.getChild(0);
							if(tmp2 instanceof ImageTag){
								ImageTag img = (ImageTag)tmp2;
								if(null != img.getImageURL() && !"".equals(img.getImageURL())){
									imgSrc = img.getImageURL();
									picUrl = link.getLink();
									title = img.getAttribute("title");
									values[0] = url;
									values[1] = picUrl;
									values[2] = imgSrc;
									values[3] = title;
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
	 * 获取分页图片
	 * @param web
	 * @return
	 */
	public static Map<String,Object>  getPictureLinkPage2(Article web){
		Map<String,Object> map = new HashMap<String,Object>();
		int count = 0;
		String remark = web.getIntro();
		try{
			int end = remark.lastIndexOf("张");
			if(end != -1){
				count = Integer.parseInt(remark.substring(end));
			}else{
				count = Integer.parseInt(remark);
			}
			int num = count/25;
			if(count%25 > 0){
				num ++;
			}
			int start = web.getArticleUrl().lastIndexOf("_");
			String pre = web.getArticleUrl().substring(0,start);
			String end2 = web.getArticleUrl().substring(web.getArticleUrl().lastIndexOf("."));
			for(int k=0;k<num+1;k++){
				if(k == 0){
					continue;
				}
				String tmp = pre+"_"+k+end2;
				map.put(tmp,tmp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}

	static WebsiteBean getWebsite(String id){
		WebsiteBean web = null;
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
			for(WebsiteBean bean:list){
				if(bean.getRemarks().equals(id)){
					web = bean;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return web;
	}
}

class Cars
{
	private String parentId = "-1";
	private String id = "-1";
	private String shortName = "default";
	private String name = "default";

	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getParentId(){
		return parentId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setShortName(String shortName){
		this.shortName = shortName;
	}

	public String getShortName(){
		return shortName;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}
}

class CarsModel
{
	private String parentId = "-1";

	private String id = "-1";
	
	private String modelName = "none";

	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getParentId(){
		return parentId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setModelName(String modelName){
		this.modelName = modelName;
	}

	public String getModelName(){
		return modelName;
	}
}

class CarsYear
{
	private String parentId = "-1";

	private String id;

	private String years; 

	public void setParentId(String parentId){
		this.parentId = parentId;
	}

	public String getParentId(){
		return parentId;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setYears(String years){
		this.years = years;
	}

	public String getYears(){
		return years;
	}
}
