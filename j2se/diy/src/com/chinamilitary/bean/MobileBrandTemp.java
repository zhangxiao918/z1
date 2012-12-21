package com.chinamilitary.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 手机品牌类
 * @author bluestome
 *
 */
public class MobileBrandTemp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	//品牌名称
	private String name;
	//所属网站
	private Integer webid;
	//地址
	private String url;
	//品牌类型
	private Integer type;
	//品牌代码
	private String code;
	//品牌来源【国内，海外】
	private String from;
	//品牌图标
	private String icon;
	//品牌备注
	private String remarks;
	//品牌状态 【在售，停售】
	private Integer status = 1;
	//创建时间
	private Date createtime = new Date();
	//修改时间
	private Date modifytime = new Date();
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getModifytime() {
		return modifytime;
	}
	public void setModifytime(Date modifytime) {
		this.modifytime = modifytime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getWebid() {
		return webid;
	}
	public void setWebid(Integer webid) {
		this.webid = webid;
	}
	
	
}
