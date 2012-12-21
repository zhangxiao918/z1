package com.autohome.json;

/**
 * 回复 使用的JSON对象
 * @author Bluestome.Zhang
 *
 */
public class ReplyJson {

	private Boolean succeed;

	private String errMsg;

	private String errMap;

	private String topic;

	private Integer topicId;

	private String bbs;

	private Integer bbsid;

	private Integer loginId;

	private String currUser;

	private String loginName;

	private String content;

	private Integer newFloor;

	private String clientIP;

	private Integer addMoney;

	private Integer replyId;

	private String redirect;

	private Integer redirectPageIndex;

	private String alertMsg;

	public Integer getAddMoney() {
		return addMoney;
	}

	public void setAddMoney(Integer addMoney) {
		this.addMoney = addMoney;
	}

	public String getAlertMsg() {
		return alertMsg;
	}

	public void setAlertMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public String getBbs() {
		return bbs;
	}

	public void setBbs(String bbs) {
		this.bbs = bbs;
	}

	public Integer getBbsid() {
		return bbsid;
	}

	public void setBbsid(Integer bbsid) {
		this.bbsid = bbsid;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCurrUser() {
		return currUser;
	}

	public void setCurrUser(String currUser) {
		this.currUser = currUser;
	}

	public String getErrMap() {
		return errMap;
	}

	public void setErrMap(String errMap) {
		this.errMap = errMap;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Integer getLoginId() {
		return loginId;
	}

	public void setLoginId(Integer loginId) {
		this.loginId = loginId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Integer getNewFloor() {
		return newFloor;
	}

	public void setNewFloor(Integer newFloor) {
		this.newFloor = newFloor;
	}

	public String getRedirect() {
		return redirect;
	}

	public void setRedirect(String redirect) {
		this.redirect = redirect;
	}

	public Integer getRedirectPageIndex() {
		return redirectPageIndex;
	}

	public void setRedirectPageIndex(Integer redirectPageIndex) {
		this.redirectPageIndex = redirectPageIndex;
	}

	public Integer getReplyId() {
		return replyId;
	}

	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Integer getTopicId() {
		return topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	public Boolean getSucceed() {
		return succeed;
	}

	public void setSucceed(Boolean succeed) {
		this.succeed = succeed;
	}

	
}
