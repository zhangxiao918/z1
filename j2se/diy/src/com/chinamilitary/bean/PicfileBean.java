package com.chinamilitary.bean;

import java.io.Serializable;
import java.util.Date;

public class PicfileBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3651555464532842680L;
	private Integer id;
	private Integer articleId;
	private Integer imageId;
	private String url;
	private String name;
	private String smallName;
	private String title;
	private String desc;
	private Date createTime;
	
	public PicfileBean(){
		
	}
	
	public PicfileBean(Integer articleId,Integer imageId,String url,String name,String desc,String title){
		this.articleId = articleId;
		this.imageId = imageId;
		this.url = url;
		this.name = name;
		this.title = title;
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Integer getImageId() {
		return imageId;
	}

	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSmallName() {
		return smallName;
	}

	public void setSmallName(String smallName) {
		this.smallName = smallName;
	}

	
}
