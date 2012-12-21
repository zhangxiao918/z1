package org.bluestome.pcs.bean;

import java.util.Date;

public class Article implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7180170746381422192L;
	private Integer id;
	private Integer webId;
	private String articleUrl;
	private String acticleRealUrl;
	private String acticleXmlUrl;
	private String title;
	private String text;
	private String intro;
	private Date createTime;
	
	public Article(){
	}
	
	public Article(Integer webId,String articleUrl){
		this.webId = webId;
		this.articleUrl = articleUrl;
	}

	public String getArticleUrl() {
		return articleUrl;
	}

	public void setArticleUrl(String articleUrl) {
		this.articleUrl = articleUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getWebId() {
		return webId;
	}

	public void setWebId(Integer webId) {
		this.webId = webId;
	}

	public String getActicleRealUrl() {
		return acticleRealUrl;
	}

	public void setActicleRealUrl(String acticleRealUrl) {
		this.acticleRealUrl = acticleRealUrl;
	}

	public String getActicleXmlUrl() {
		return acticleXmlUrl;
	}

	public void setActicleXmlUrl(String acticleXmlUrl) {
		this.acticleXmlUrl = acticleXmlUrl;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	@Override
	public String toString(){
		String body = null;
		body = (this.getId() == null?"NID":this.getId())+"|"+this.getWebId()+"|"+this.getTitle()+"|"+this.getArticleUrl();
		return body;
	}
	
}
