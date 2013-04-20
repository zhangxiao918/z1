package com.skymobi.cac.maopao.xip.bto.basis;

public enum UserStatus {
	
	USER_STATUS_GETOUT(0, "USER_STATUS_GETOUT"),
	USER_STATUS_FREE(1, "USER_STATUS_FREE"),
	USER_STATUS_SIT(2, "USER_STATUS_SIT"),
	USER_STATUS_READY(3, "USER_STATUS_READY"),
	USER_STATUS_PLAYING(4, "USER_STATUS_PLAYING");
	
	public static UserStatus getUserStatus(int value) {
		UserStatus ret = null;
		for(UserStatus status : UserStatus.values()){
			if(status.getValue() == value){
				ret = status;
				break;
			}
		}
		return ret;
	}
	
	private int value;
	
	private String description;

	private UserStatus(int value, String description) {
		this.value = value;
		this.description = description;
	}

	public int getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}
}
