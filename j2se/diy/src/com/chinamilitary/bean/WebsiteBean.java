package com.chinamilitary.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WebsiteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9138376143415042891L;
	private Integer id;
	private Integer parentId;
	private Integer type;
	private String url;
	private String name;
	private Date createtme;
	private Date modifytime;
	private Integer status = 1;
	private String remarks = null;
	private String lastModifyTime = null;
	private List<WebsiteBean> child = new ArrayList<WebsiteBean>();
	
	public WebsiteBean(){
	}

	public Date getCreatetme() {
		return createtme;
	}

	public void setCreatetme(Date createtme) {
		this.createtme = createtme;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getModifytime() {
		return modifytime;
	}

	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}

	public List<WebsiteBean> getChild() {
		return child;
	}

	public void setChild(List<WebsiteBean> child) {
		this.child = child;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public  void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
	
}
