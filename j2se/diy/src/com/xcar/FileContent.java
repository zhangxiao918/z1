package com.xcar;

public class FileContent 
{
	private byte[] content;
	private String savePath;

	public FileContent(byte[] content,String savePath){
		this.content = content;
		this.savePath = savePath;
	}

	public void setContent(byte[] content){
		this.content = content;
	}

	public byte[] getContent(){
		return content;
	}

	public void setSavePath(String savePath){
		this.savePath = savePath;
	}

	public String getSavePath(){
		return savePath;
	}
}
