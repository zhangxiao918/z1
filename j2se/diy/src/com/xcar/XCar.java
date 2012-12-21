package com.xcar;

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

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
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

public class XCar 
{
	public static final String FCT = "78,A 阿尔法·罗米欧,56,A 阿斯顿马丁,1,A 奥迪,52,B 保时捷,109,B 宝骏,2,B 宝马,88,B 北汽,3,B 奔驰,105,B 奔腾,17,B 本田,27,B 比亚迪,5,B 标致,13,B 别克,57,B 宾利,58,B 布加迪,28,C 昌河,29,C 长安,30,C 长城,32,D 大发,4,D 大众,11,D 道奇,95,D 帝豪,33,D 东风,90,D 东风风神,34,D 东南,59,F 法拉利,6,F 菲亚特,18,F 丰田,10,F 福特,76,F 福田,104,G GMC,103,G 广汽,31,G 广汽长丰,37,H 哈飞,38,H 海马,68,H 悍马,106,H 红旗,42,H 华普,80,H 华泰,91,H 黄海汽车,77,J JEEP吉普,92,J 吉奥汽车,26,J 吉利,44,J 江淮,79,J 江铃,60,J 捷豹,39,J 金杯,99,K 开瑞,69,K 凯迪拉克,12,K 克莱斯勒,61,L 兰博基尼,62,L 劳斯莱斯,71,L 雷克萨斯,63,L 雷诺,40,L 力帆,24,L 莲花,108,L 莲花汽车,70,L 林肯,19,L 铃木,65,L 路虎,45,L 陆风,54,M MINI,66,M 玛莎拉蒂,14,M 马自达,53,M 迈巴赫,15,M MG,55,O 欧宝,72,O 讴歌,84,P 帕加尼,25,Q 奇瑞,22,Q 起亚,96,Q 全球鹰,20,R 日产,46,R 荣威,97,R 瑞麒,83,S Smart,67,S 萨博,110,S SPIRRA,21,S 三菱,87,S 世爵,47,S 双环,75,S 双龙,73,S 斯巴鲁,7,S 斯柯达,98,W 威麟,9,W 沃尔沃,82,W 五菱,86,X 西亚特,107,X 夏利,23,X 现代,16,X 雪佛兰,8,X 雪铁龙,50,Y 一汽,81,Y 依维柯,74,Y 英菲尼迪,94,Y 英伦汽车,35,Y 永源,41,Z 中华,93,Z 中兴汽车,51,Z 众泰";
	static final String TMP_1 = "br['35']='266,A　阿斯顿马丁DB9,582,A　阿斯顿马丁DBS,923,R　Rapide,822,V　V12 Vantage'";
	static final String INSERT_CAR_BRAND_SQL = "insert into TBL_CARS_BRAND (d_brand_id,d_brand_name,d_brand_short_name,d_brand_desc) values ({1},'{2}','{3}','{4}');\r\n";
	static final String INSERT_CARS_SQL = "insert into TBL_CARS (d_brand_id,d_cars_id,d_cars_name,d_cars_short_name,d_cars_desc) values ({1},{2},'{3}','{4}','{5}');\r\n";
	static final String INSERT_CARS_YEAR_STYLE_SQL = "insert into tbl_cars_year_style (d_cars_id,d_year_style_id,d_year_style) values ({1},{2},'{3}');\r\n";
	static final String INSERT_CARS_INFO_SQL = "insert into tbl_car_info (d_car_year_style_id,d_car_info_id,d_car_name,d_car_level,d_engine_id,d_transmission_id) values ({1},{2},'{3}',-1,-1,-1);\r\n";
	static final String BRAND_URL = "http://newcar.xcar.com.cn/price/b{id}/";
	static final String SERIES_URL = "http://newcar.xcar.com.cn/{id}";
	static final String INSERT_FC_BRAND_SQL  = "insert into tbl_web_site (d_parent_id,d_web_url,d_web_name,d_remarks) values (7974,'{2}','{3}','{4}');";
	static final String XCAR_IMAGE_URL_PREFIX = "http://newcar.xcar.com.cn/";

	private static final Integer D_PARENT_ID = 7974;
	
	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();
	
	static int COUNT = 0 ;
	
	static String PIC_SAVE_PATH = Constants.FILE_SERVER;
	
	static final String IMAGE_CACHE_KEY = "XCAR_CACHE_KEY_IMAGE";
	
	static final String ARTICLE_CACHE_KEY = "XCAR_CACHE_KEY_ARTICLE";
	
	static MemcacheClient client = MemcacheClient.getInstance();
	
	public static void main(String[] args) {
//		parserAutoBrandSQL();
//		parserCars();
//		parserCarModelYear();
//		parserCarModel();
		
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
						Thread.sleep(160000000);
					}catch(Exception e){
						e.printStackTrace();
						isRun = false;
					}
				}
			}
		 }).start();
		
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
				List<LinkTag> ll = getPictureListPageLinkTag(web); //getPictureListPage(web.getUrl())
				Article art = null;
				for(LinkTag u:ll){
					art = new Article();
					if(null != u.getLinkText() && !"".equals(u.getLinkText())){
						String name = u.getAttribute("title");
						int end = u.getLinkText().lastIndexOf("(");
						
						//共有68图片
						if(end != -1){
							name = u.getLinkText().substring(0,end);
						}
						
						art.setTitle(name);
					}else{
						art.setTitle(u.getLinkText());
					}
					art.setArticleUrl(u.getLink());
					art.setWebId(web.getId());
					art.setIntro("0");
					int result = articleDao.insert(art);
					if(result > 0){
						System.out.println(" >> 添加"+art.getWebId()+"|"+art.getTitle()+"|"+result+"|"+art.getArticleUrl()+"|"+art.getIntro() + "成功");
					}else{
						System.err.println(" >> 添加"+art.getWebId()+"|"+art.getTitle()+"|"+result+"|"+art.getArticleUrl()+"|"+art.getIntro() + "失败");
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
				for(Article art:alist){
					Map<String, Object> map = getPictureLinkPage2(art);
					Iterator it = map.keySet().iterator();
					int i = 0;
					String icon = null;
					String tmp = art.getArticleUrl();
					Integer imgCount = 0;
					while(it.hasNext()){
						String key = (String)it.next();
						List<String[]> ilist = getPictureLink(key,tmp);
						ImageBean img = null;
						for(String[] str:ilist){
							img = new ImageBean();
							String bg = getBigImageSrc(str[1]);
							if(null == bg || "".equals(bg)){
								continue;
							}
							if(null != client.get(IMAGE_CACHE_KEY+bg)){
								continue;
							}
							img.setImgUrl(str[2]);
							img.setHttpUrl(bg);
							img.setCommentsuburl(str[0]);
							img.setCommentshowurl(str[0]);
							img.setArticleId(art.getId());
							img.setTitle(str[3]);
							icon = img.getImgUrl();
							int result = imageDao.insert(img);
							System.out.println(" >> getArticleImage2.insert.img:"+result);
							if(result > 0){
								client.add(IMAGE_CACHE_KEY+bg, img);
								i++;
							}else{
								System.err.println(" >> 添加图片["+img.getArticleId()+"|"+img.getTitle()+"|"+bg+"]失败!");
							}
						}
						tmp = key;
						imgCount = (Integer)map.get(key);
						art.setIntro(String.valueOf(imgCount));
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
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void imgDownload() throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(D_PARENT_ID);
		for(WebsiteBean website:webList){
			imgDownload2(website);
		}
	}
	
	static void imgDownload2(WebsiteBean website) throws Exception {
		List<WebsiteBean> webList = webSiteDao.findByParentId(website.getId());
		if(webList.size() > 0){
			System.out.println(" >> "+website.getName()+"|"+website.getUrl()+"|"+webList.size());
			for(WebsiteBean web:webList){
				imgDownload2(web);
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
	private static Map<String, Object> getPictureLinkPage2(Article web) {
		Map<String,Object> map = new HashMap<String,Object>();
		Integer count = 0;
		Parser p1 = null;
		try{
			p1 = new Parser();
			String url = web.getArticleUrl(); //.replace("/s", "/s-").replace("_","-")
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(Span.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "bn"));
			if(null != nodeList && nodeList.size() > 0){
				Span span = (Span)nodeList.elementAt(0);
				int child = span.getChildCount();
				if(child > 0){
					String tmp = span.getChild(2).toHtml();
					int start = tmp.lastIndexOf("(");
					int end  = tmp.lastIndexOf("张");
					if(start != -1 && end != -1){
						count = Integer.parseInt(tmp.substring(start+1,end));
						System.out.println(" >> count:"+count);
						int num = count/30;
						if(count%30 > 0){
							num ++;
						}
						for(int i=1;i<num+1;i++){
							String pn = web.getArticleUrl()+"?pn="+i;
							System.out.println(" >> pn:"+pn);
							map.put(pn, count);
						}
					}
				}
			}
		}catch(Exception e){
			System.err.println( " >> " + web.getArticleUrl());
			e.printStackTrace();
		}finally{
			if(null != p1)
				p1 = null;
		}
		return map;
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
//			String tmp = INSERT_CAR_BRAND_SQL;
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
		IOUtil.createFile(sb.toString(),"BRAND_URL_0426");
	}

	public static void parserCars(){
//		StringBuffer sb = new StringBuffer();
		String content = IOUtil.readFile("E:\\2.WS\\1.PRODUCT\\diy_page\\src\\com\\xcar\\xcar_ps_arr.txt","GBK","txt");
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
							tmp = tmp.replace("{id}",car.getId());
							WebsiteBean tweb = web;
							tweb.setParentId(web.getId());
							tweb.setName(car.getName());
							tweb.setUrl(tmp);
							tweb.setRemarks(car.getId());
							
							System.out.println(" >> 添加:"+tweb.getId()+"|"+tweb.getParentId()+"|"+tweb.getName()+"|"+tweb.getUrl()+"|"+tweb.getRemarks()+" 成功!");
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
//						sb.append(tmp).append("\r");
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
		}
//		IOUtil.createFile(sb.toString(),"CARDS_YEAR_STYLE");
	}

	public static List<Cars> carsParser(String content){
		List<Cars> list = new ArrayList<Cars>();
		try{
			String[] res = content.split("]=");
//			System.out.println(" >> xcar_ps_arr.length:"+res.length);
			if(res.length == 2){
				String key = res[0];
				String value = res[1];
//				System.out.println(" >> key["+key+"] = "+value);
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
		if(start != -1){
			parId = key.substring(start+1);
		}
		return parId;
	}

	public static List<String[]> getCarsInfo(String arg0){
		List<String[]> list = new ArrayList<String[]>();
//		arg0 = arg0.replace("== ","");
		String[] fcts = arg0.split(",");
		int length  = fcts.length;
		for(int i=0;i<length;i+=2){
			String[] infos = new String[3];
			String value = fcts[i];
			String text = fcts[i+1];
//			System.out.println("\t\t\t value["+value+"]:"+text);
			infos[0] = value;
			infos[1] = text;
			infos[2] = text;
			/**
			String[] txt = text.split("��");
			if(txt.length == 2){
				if(null != txt[0] && !"".equals(txt[0])){
					infos[1] = txt[0];
				}
				if(null != txt[1] && !"".equals(txt[1])){
					infos[2] = txt[1];
				}
			}
			**/
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
	 * 根据URL获取内容
	 *@param url 页面地址
	 *@return String 内容
	 */
//	public static String getContent(String url){
//		Parser p1 = new Parser();
//		String content = null;
//		try{
//			p1.setURL(url);
//			p1.setEncoding("gb2312");
//			NodeFilter fileter = new NodeClassFilter(Div.class);
//			NodeList list = p1.extractAllNodesThatMatch(fileter);
//			if(null != list && list.size() > 0){
//				content = list.toHtml();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}finally{
//			if(null != p1){
//				p1 = null;
//			}
//		}
//		return content;
//	}

	
	/**
	 * 根据URL获取图片页面地址
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
	 * 根据URL获取图片页面地址
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
	 * 根据URL获取图片页面地址
	 * @demo http://newcar.xcar.com.cn/7/photo.htm
	 * @param url
	 */
	public static List<LinkTag> getPictureListPageLinkTag(WebsiteBean web){
		Parser p1 = new Parser();
		List<LinkTag> list = new ArrayList<LinkTag>();
		try{
			p1.setURL(web.getUrl()+"/photo.htm");
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
	 * 通过引用和URL来获取内容
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
	 * 获取图片大图地址，以及缩略图地址
	 * 记录列表泛型数组
	 * 数组【0】 引用地址
	 * 数组【1】 大图页面地址
	 × 数组【2】 图片缩略图地址
	 */
	public static List<String[]> getPictureLink(String url,String reffer){
		Parser p1 = null;
		Parser p2 = null;
		List<String[]> list = new ArrayList<String[]>();
		String picUrl ="";
		String imgSrc = "";
		String title = "";
		String body = null;
		byte[] bs = null;
		try{
			bs = HttpClientUtils.getResponseBodyAsByte(reffer, null, url);
			if(null != bs){
				body = new String(bs,"GBK");
			}
			if(null == body && "".equals(body)){
				return list;
			}
			p1 = new Parser();
			p1.setInputHTML(body);
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
						String[] values = new String[4];
						LinkTag link = (LinkTag)nodeList2.elementAt(i);
						int count3 = link.getChildCount();
						if( count3 > 0 ){
							Node tmp2 = link.getChild(0);
							if(tmp2 instanceof ImageTag){
								ImageTag img = (ImageTag)tmp2;
								if(null != img.getImageURL() && !"".equals(img.getImageURL())){
									imgSrc = img.getImageURL();
									picUrl = XCAR_IMAGE_URL_PREFIX+link.getLink();
									title = img.getAttribute("alt");
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
	 * 获取大图图片地址
	 */
	public static String getBigImageSrc(String url){
		Parser p1 = new Parser();
		String src = null;
		Parser p2 = new Parser();
		try{
			p1.setURL(url);
			p1.setEncoding("gb2312");
			NodeFilter fileter = new NodeClassFilter(LinkTag.class);
			NodeList nodeList = p1.extractAllNodesThatMatch(fileter).extractAllNodesThatMatch(
						new HasAttributeFilter("class", "btn1"));
			if(nodeList.size() > 0 ){
				LinkTag link = (LinkTag)nodeList.elementAt(0);
				if(null != link.getLink() && !"".equals(link.getLink())){
					link.getLink();
					p2.setURL(link.getLink());
					p2.setEncoding("gb2312");
					NodeFilter fileter2 = new NodeClassFilter(ImageTag.class);
					NodeList nodeList2 = p2.extractAllNodesThatMatch(fileter2).extractAllNodesThatMatch(
								new HasAttributeFilter("id", "bigPhoto"));
					if(null != nodeList2 && nodeList2.size() > 0){
						ImageTag img = (ImageTag)nodeList2.elementAt(0);
						if(null != img.getImageURL()){
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
			if(null != p2)
				p2 = null;
		}
		return src;
	}

	/**
	 * 根据URL获取文件名
	 */
	public static String getName(String url){
		String name = "";
		int start = url.lastIndexOf("/");
		if(start != -1){
			name = url.substring(start+1);
		}
		return name;
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

/**
 *
 * ���ͻ���Ϣ
 */
class CarsModel
{
	//����ϵID
	private String parentId = "-1";

	//��ǰ����ID
	private String id = "-1";
	
	//�������
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
	//����ϵID
	private String parentId = "-1";

	//���ID
	private String id;

	//����˵��
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
