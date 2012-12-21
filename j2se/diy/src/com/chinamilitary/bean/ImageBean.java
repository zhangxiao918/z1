package com.chinamilitary.bean;

import java.util.Date;

public class ImageBean implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8163799487276569871L;
	private Integer id;
	private Integer articleId;
	private String title;
	private String name;
	private String imgUrl;
	private String httpUrl;
	private Integer orderId;
	private String time;
	private String intro;
	private String commentsuburl;
	private String commentshowurl;
	private String link;
	private Integer status;
	private Long fileSize;
	private Date createtime;
	private String referer;
	
	public ImageBean(){
	}

	public String getCommentshowurl() {
		return commentshowurl;
	}

	public void setCommentshowurl(String commentshowurl) {
		this.commentshowurl = commentshowurl;
	}

	public String getCommentsuburl() {
		return commentsuburl;
	}

	public void setCommentsuburl(String commentsuburl) {
		this.commentsuburl = commentsuburl;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Integer getArticleId() {
		return articleId;
	}

	public void setArticleId(Integer articleId) {
		this.articleId = articleId;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}
	
	
	
	
}
