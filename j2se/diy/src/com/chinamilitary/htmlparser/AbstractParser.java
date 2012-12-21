package com.chinamilitary.htmlparser;

import java.util.HashMap;

import com.chinamilitary.bean.LinkBean;

public interface AbstractParser {

	public abstract HashMap<String,LinkBean> getLink(String url,String encoding) throws Exception;
	
	public abstract HashMap<String,LinkBean> getLinkByTag(String url,String attribute,String value) throws Exception;
	
	public abstract HashMap<String,LinkBean> getLinkByTag(String url,String attribute,Class cls,String value) throws Exception;
	
	public abstract HashMap<String,LinkBean> getDiv(String url,String encoding) throws Exception;
	
	public abstract HashMap<String,LinkBean> getByTag(String url,Class cls,String attribute,String value);
	
}
