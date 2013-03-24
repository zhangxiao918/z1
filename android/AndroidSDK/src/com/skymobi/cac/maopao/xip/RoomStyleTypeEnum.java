package com.skymobi.cac.maopao.xip;

public enum RoomStyleTypeEnum {
	
	TRAINNING_ROOM(1, "训练场"),
	GOLD_ROOM(2, "金豆场");
	
	private int code;
	private String name;
	
	private RoomStyleTypeEnum(int code, String name){
		this.code = code;
		this.name = name;
	}

	public int getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	
	
}
