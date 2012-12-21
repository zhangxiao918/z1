package com.chinamilitary.util;

public class CacheUtils {

	private final static String CACHE_KEY = "PICSERVER";
	
	private final static String CACHE_ARTICLE = "127.0.0.1"+Constants.CACHE_KEY_SEPARATOR2+"11211"+Constants.CACHE_KEY_SEPARATOR+"ARTICLE";
	
	private final static String CACHE_IMAGE = "127.0.0.1"+Constants.CACHE_KEY_SEPARATOR2+"11211"+Constants.CACHE_KEY_SEPARATOR+"IMAGE";
	
	private final static String CACHE_SMALL_IMG_DOWNLOAD_FLAG = "SMALL_IMG_DOWNLOAD_TRUE";
	
	private final static String CACHE_BIG_IMG_DOWNLOAD_FLAG = "BIG_IMG_DOWNLOAD_TRUE";
	
	private final static String CACHE_WEBLIST_PARENT = "WEBLIST_BY_PARENT_ID";
	
	public static String getHTMLKey(Object obj){
		return CACHE_KEY + Constants.CACHE_KEY_SEPARATOR+"html" 
		+ Constants.CACHE_KEY_SEPARATOR+ obj;
	}
	
	public static String getQwqqKey(Object obj){
		return CACHE_KEY + Constants.CACHE_KEY_SEPARATOR+"qwqq" 
		+ Constants.CACHE_KEY_SEPARATOR+ obj;
	}
	
	public static String getShowImgKey(Object obj){
		return CACHE_KEY + Constants.CACHE_KEY_SEPARATOR+"showimgarticle" + Constants.CACHE_KEY_SEPARATOR+ obj;
	}
	
	public static String getImgKey(Object obj){
		return CACHE_KEY + Constants.CACHE_KEY_SEPARATOR+"image" + Constants.CACHE_KEY_SEPARATOR+ obj;
	}

	public static String getBigPicFileKey(Object obj){
		return CACHE_KEY + Constants.CACHE_KEY_SEPARATOR+"bigpicfile" + Constants.CACHE_KEY_SEPARATOR+ obj;
	}
	
	public static String getSmallPicFileKey(Object obj){
		return CACHE_KEY + Constants.CACHE_KEY_SEPARATOR+"smallpicfile" + Constants.CACHE_KEY_SEPARATOR+ obj;
	}
	
	public static String getPicFileKey(Object imgId,Object articleId){
		return CACHE_KEY + Constants.CACHE_KEY_SEPARATOR+"picfile" + Constants.CACHE_KEY_SEPARATOR+ imgId + Constants.CACHE_KEY_SEPARATOR + articleId;
	}
	
	public static String getArticleKey(Object obj){
		return CACHE_ARTICLE + Constants.CACHE_KEY_SEPARATOR2 + obj;
	}
	
	public static String getImageKey(Object obj){
		return CACHE_IMAGE + Constants.CACHE_KEY_SEPARATOR2 + obj;
	}
	
	public static String getDownloadSmallImageKey(Object obj){
		return CACHE_SMALL_IMG_DOWNLOAD_FLAG+ Constants.CACHE_KEY_SEPARATOR2 + obj;
	}
	
	public static String getDownloadBigImageKey(Object obj){
		return CACHE_BIG_IMG_DOWNLOAD_FLAG+ Constants.CACHE_KEY_SEPARATOR2 + obj;
	}
	
	public static String getWebListByParentID(Object obj){
		return  CACHE_WEBLIST_PARENT + Constants.CACHE_KEY_SEPARATOR2 + obj;
	}
}
