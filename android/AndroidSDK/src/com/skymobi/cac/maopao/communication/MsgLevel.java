package com.skymobi.cac.maopao.communication;

import java.util.HashMap;
import java.util.Map;

public enum MsgLevel {
	
	NORMAL(1),
	WARN(2),
	ERROR(3),
	FATAL(4);
	
	private final static Map<Integer, MsgLevel> ID_MAP;
	
	static{
		ID_MAP = new HashMap<Integer, MsgLevel>();
		for(MsgLevel level:MsgLevel.values()){
			ID_MAP.put(level.getId(), level);
		}
	}
	
	private int id;
	
	private MsgLevel(int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static MsgLevel getLevelById(int id){
		return ID_MAP.get(id);
	}
	
}