package com.chinamilitary.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 手机品牌类
 * @author bluestome
 *
 */
public class MobileBrand implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	//品牌名称
	private String name;
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
	private Integer status;
	//创建时间
	private Date createtime = new Date();
	//修改时间
	private Date modifytime = new Date();
}
