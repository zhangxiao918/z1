package com.autohome;
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

public class CarHomeParser 
{
	public static final String FCT = "35,A　阿斯顿·马丁,33,A　奥迪,15,B　宝马,40,B　保时捷,27,B　北汽,36,B　奔驰,95,B　奔腾,14,B　本田,75,B　比亚迪,13,B　标致,38,B　别克,39,B　宾利,37,B　布嘉迪,79,C　昌河,76,C　长安,77,C　长城,78,C　长丰,111,C　川汽野马,1,D　大众,41,D　道奇,105,D　帝豪,32,D　东风,113,D　东风风神,81,D　东南,42,F　法拉利,11,F　菲亚特,3,F　丰田,8,F　福特,96,F　福田,112,G　GMC,82,G　广汽,24,H　哈飞,86,H　海马,43,H　悍马,91,H　红旗,87,H　华泰,97,H　黄海,46,J　Jeep吉普,108,J　吉奥,104,J　吉利全球鹰,84,J　江淮,119,J　江铃,44,J　捷豹,83,J　金杯,101,K　开瑞,47,K　凯迪拉克,100,K　柯尼赛格,9,K　克莱斯勒,48,L　兰博基尼,54,L　劳斯莱斯,52,L　雷克萨斯,10,L　雷诺,80,L　力帆,50,L　莲花,89,L　莲花汽车,51,L　林肯,53,L　铃木,88,L　陆风,49,L　路虎,20,M　MG,56,M　MINI,58,M　马自达,57,M　玛莎拉蒂,55,M　迈巴赫,60,O　讴歌,59,O　欧宝,26,Q　奇瑞,62,Q　起亚,63,R　日产,19,R　荣威,103,R　瑞麒,45,S　smart,64,S　萨博,68,S　三菱,66,S　世爵,90,S　双环,69,S　双龙,65,S　斯巴鲁,67,S　斯柯达,102,W　威麟,99,W　威兹曼,70,W　沃尔沃,114,W　五菱汽车,7,X　夏利,12,X　现代,71,X　雪佛兰,72,X　雪铁龙,110,Y　一汽,73,Y　英菲尼迪,106,Y　英伦,93,Y　永源,22,Z　中华,74,Z　中兴,94,Z　众泰";
	static final String TMP_1 = "br['35']='266,A　阿斯顿马丁DB9,582,A　阿斯顿马丁DBS,923,R　Rapide,822,V　V12 Vantage'";
	static final String INSERT_CAR_BRAND_SQL = "insert into TBL_CARS_BRAND (d_brand_id,d_brand_name,d_brand_short_name,d_brand_desc) values ({1},'{2}','{3}','{4}');\r\n";
	static final String INSERT_CARS_SQL = "insert into TBL_CARS (d_brand_id,d_cars_id,d_cars_name,d_cars_short_name,d_cars_desc) values ({1},{2},'{3}','{4}','{5}');\r\n";
	static final String INSERT_CARS_YEAR_STYLE_SQL = "insert into tbl_cars_year_style (d_cars_id,d_year_style_id,d_year_style) values ({1},{2},'{3}');\r\n";
	static final String INSERT_CARS_INFO_SQL = "insert into tbl_car_info (d_car_year_style_id,d_car_info_id,d_car_name,d_car_level,d_engine_id,d_transmission_id) values ({1},{2},'{3}',-1,-1,-1);\r\n";
	static final String BRAND_URL = "http://car.autohome.com.cn/pic/brand-{id}.html";
	static final String SERIES_URL = "http://car.autohome.com.cn/pic/series/{id}.html";
	static final String INSERT_FC_BRAND_SQL  = "insert into tbl_web_site (d_parent_id,d_web_url,d_web_name,d_remarks) values (1500,'{2}','{3}','{4}');";
	private static final Integer D_PARENT_ID = 1500;
	static ArticleDao articleDao = DAOFactory.getInstance().getArticleDao();

	static WebSiteDao webSiteDao = DAOFactory.getInstance().getWebSiteDao();

	static ImageDao imageDao = DAOFactory.getInstance().getImageDao();

	static PicFileDao picFiledao = DAOFactory.getInstance().getPicFileDao();
	
	static String PIC_SAVE_PATH = Constants.FILE_SERVER;
	
	static MemcacheClient client = MemcacheClient.getInstance();
	
	static int COUNT = 0 ;
	
	static final String IMAGE_CACHE_KEY = "AUTO_HOME_CACHE_KEY_IMAGE";
	
	static final String ARTICLE_CACHE_KEY = "AUTO_HOME_CACHE_KEY_ARTICLE";
	
	public static void main(String[] args) {
//		parserAutoBrandSQL();
//		parserCars();
//		parserCarModelYear();
//		parserCarModel();
//		System.out.println(" >>\t"+fileSize(123l));
		
		
//		patch();
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
		
//		patch();
	}
	
	/**
	 * 更新图片数量与实际网站数量不符合的文章状态为NND[NO NEED DOWNLOAD]
	 *
	 */
	static void patch(){
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(D_PARENT_ID);
			for(WebsiteBean bean:list){
				patch2(bean);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	static void patch2(WebsiteBean web){
		try{
			List<WebsiteBean> list = webSiteDao.findByParentId(web.getId());
			if(list.size() > 0){
				for(WebsiteBean bean:list){
					patch2(bean);
				}
			}else{
				List<Article> alist = articleDao.findByWebId(web.getId(), "NED");
				for(Article art:alist){
					int ic = imageDao.getCount("select count(*) from tbl_image where d_article_id = "+art.getId());
					if(ic > 0 ){
						int count = 0;
						try{
							count = Integer.parseInt(art.getIntro());
							System.out.println(" >> "+art.getTitle()+".Count:" + count);
							if(count > 0){
								art.setText("NND");
								if(!articleDao.update(art)){
									System.err.println(" >> 更新 "+art.getId()+"|"+art.getTitle()+"| 为NND状态失败");
								}
							}
						}catch(Exception e){
							continue;
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
					Thread.sleep(10);
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
								icon = img.getImgUrl();
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
				Thread.sleep(10);
				/**
				**/
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
				List<LinkTag> ll = getPictureListPage(web.getUrl());
				Article art = null;
				if(null != ll && ll.size() > 0){
					for(LinkTag u:ll){
						art = new Article();
						if(null != u.getLinkText() && !"".equals(u.getLinkText())){
							if(null != client.get(ARTICLE_CACHE_KEY+u.getLink())){
								continue;
							}
							int end = u.getLinkText().lastIndexOf("(");
							//共有68图片
							String name = u.getLinkText().substring(0,end);
							
							int start = u.getLinkText().lastIndexOf("有");
							int end2 = u.getLinkText().lastIndexOf("图");
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
//							if(webSiteDao.update(web)){
//								System.out.println(" >> 更新网站["+web.getName()+"|"+web.getUrl()+"]最后时间["+lastModify+"]成功!");
//							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 *获取页面内容
	 *
	 */
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
	
	/**
	 *文件大小转换
	 */
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

	/**
	 * 解析品牌数据
	 */
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
			/**
//			tmp = tmp.replace("{1}",value);
			**/
			url = url.replace("{id}",value);
//			String[] txt = text.split("　");
//			if(txt.length == 2){
//				tmp = tmp.replace("{2}",txt[1]);
//				tmp = tmp.replace("{3}",txt[0]);
//			}
//			tmp = tmp.replace("{4}",text);
			tmp = tmp.replace("{2}",url).replace("{3}",text).replace("{4}",value);
			System.out.println(" >> tmp:"+tmp);
			sb.append(tmp).append("\r\n");
		}
		IOUtil.createFile(sb.toString(),"BRAND_URL_0426");
	}

	/**
	 *解析品牌下的车系数据
	 */
	public static void parserCars(){
		StringBuffer sb = new StringBuffer();
		String content = IOUtil.readFile("E:\\2.WS\\1.PRODUCT\\diy_page\\src\\com\\autohome\\br_0426.txt","GBK","txt");
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

	/**
	 *解析车系下的车型数据
	 */
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

	/**
	 *解析车系下的车型年代
	 */
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

	/**
	 * 车系解析方法
	 */
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
	
	/**
	 * 车型解析方法
	 */
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


	/**
	 * 车型解析方法
	 */
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

	/**
	 * 获取父ID
	 */
	public static String getPaserId(String key){
		String parId = "";
		int start = key.indexOf("[");
		int end = key.indexOf("]");
		if(start != -1 && end != -1){
			parId = key.substring(start+1,end);
		}
		return parId;
	}

	/**
	 * 获取车系信息
	 */
	public static List<String[]> getCarsInfo(String arg0){
		List<String[]> list = new ArrayList<String[]>();
		String[] fcts = arg0.split(",");
		int length  = fcts.length;
		for(int i=0;i<length;i+=2){
			String[] infos = new String[3];
			String value = fcts[i];
			String text = fcts[i+1];
			infos[0] = value;
			String[] txt = text.split("　");
			if(txt.length == 2){
				if(null != txt[0] && !"".equals(txt[0])){
					infos[1] = txt[0];
				}
				if(null != txt[1] && !"".equals(txt[1])){
					infos[2] = txt[1];
				}
			}
			list.add(infos);
		}
		return list;
	}

	/**
	 * 获取车年代款信息
	 */
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

	/**
	 * 获取车型信息
	 */
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
	 * 获取视角图片
	 * @param url
	 * @return
	 */
	public static List<LinkTag> getPictureListPage(String url){
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
					int count = 0;
					try{
						if(null != div.getStringText() && !"".equals(div.getStringText())){
							count = Integer.valueOf(div.getStringText());
						}
					}catch(NumberFormatException e){
						System.err.println(e);
					}
					System.out.println(" >> count:"+count);
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
	 * 获取分页图片
	 * @param web
	 * @return
	 */
	public static Map<String,Object>  getPictureLinkPage2(WebsiteBean web){
		Map<String,Object> map = new HashMap<String,Object>();
		int count = 0;
		String remark = web.getRemarks();
		try{
			int end = remark.lastIndexOf("张");
			if(end != -1){
				count = Integer.parseInt(remark.substring(end));
			}else{
				count = Integer.parseInt(remark);
			}
			int num = count/60;
			if(count%60 > 0){
				num ++;
			}
			int start = web.getUrl().lastIndexOf(".");
			String pre = web.getUrl().substring(0,start);
			String end2 = web.getUrl().substring(start);
			for(int k=0;k<num;k++){
				if(k == 0){
					continue;
				}
				String tmp = pre+"-p"+k+end2;
				System.out.println(" >> tmp:"+tmp);
				map.put(tmp,tmp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
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
			int num = count/72;
			if(count%60 > 0){
				num ++;
			}
			int start = web.getArticleUrl().lastIndexOf(".");
			String pre = web.getArticleUrl().substring(0,start);
			String end2 = web.getArticleUrl().substring(start);
			for(int k=0;k<num+1;k++){
				if(k == 0){
					continue;
				}
				String tmp = pre+"-p"+k+end2;
				map.put(tmp,tmp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 获取图片大图地址，以及缩略图地址
	 * 记录列表泛型数组
	 * 数组【0】 引用地址
	 * 数组【1】 大图页面地址
	 * 数组【2】 图片缩略图地址
	 * 数组【3】 标题
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
				String title = "";
				for(int i=0;i<nodeList.size();i++){
					String[] values = new String[4];
					Div div = (Div)nodeList.elementAt(i);
					int count = div.getChildCount();
					if(count == 2){
						Div thumb = (Div)div.getChild(0);
						int count2 = thumb.getChildCount();
						if(count2 == 1){
							Node tmp = thumb.getChild(0);
							if(tmp instanceof LinkTag){
								LinkTag link = (LinkTag)tmp;
								//图片大图页面
								picUrl = link.getLink();
								/**
								 *获取图片缩略图地址
								 */
								int count3 = link.getChildCount();
								if( count3 > 0 ){
									Node tmp2 = link.getChild(0);
									if(tmp2 instanceof ImageTag){
										ImageTag img = (ImageTag)tmp2;
										if(null != img.getImageURL() && !"".equals(img.getImageURL())){
											imgSrc = img.getImageURL();
										}
										title = img.getAttribute("alt");
									}
								}
								values[0] = url;
								values[1] = picUrl;
								values[2] = imgSrc;
								values[3] = title;
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
	 * 获取大图图片地址
	 */
	public static String getBigImageSrc(String url){
		Parser p1 = new Parser();
		String src = null;
		String content = null;
		try{
			content = HttpClientUtils.getResponseBody(url);
			if(null == content){
				return src;
			}
//			p1.setURL(url);
			p1.setInputHTML(content);
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

/**
 * 车系类
 */
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
 * 车型基本信息
 */
class CarsModel
{
	//所属车系ID
	private String parentId = "-1";

	//当前车型ID
	private String id = "-1";
	
	//车型名称
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
	//所属车系ID
	private String parentId = "-1";

	//年款ID
	private String id;

	//年贷款说明
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
