package com.sina.json;

import java.util.ArrayList;


public class WeiboRespJson {

	private String code;

	private String msg;

	private ArrayList<String> data;

	public String getCode() {
		return code.trim();
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg.trim();
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ArrayList<String> getData() {
		return data;
	}

	public void setData(ArrayList<String> data) {
		this.data = data;
	}

}
