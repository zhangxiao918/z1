package com.skymobi.cac.maopao.communication.domains;

import com.skymobi.cac.maopao.xip.XipBody;

public class RepeatMsgTask {

	private int interval; //重复间隔，按秒计
	
	private byte[] msg;
	
	private long lastRunTime; //上次运行的时间

	private String desc;
	
	public RepeatMsgTask(){
		
	}
	
	public RepeatMsgTask(byte[] msg, int interval, String desc){
		this.msg = msg;
		this.interval = interval;
		this.desc = desc;
	}
	
	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public byte[] getMsg() {
		return msg;
	}

	public void setMsg(byte[] msg) {
		this.msg = msg;
	}
	
	/**
	 * 根据上次运行的时间戳判断是否可以发送
	 * @return
	 */
	public boolean shouldSend(){
		if(interval <1 || msg == null){
			return false;
		}
		long currentMills =  System.currentTimeMillis();
		long time = currentMills - lastRunTime;
		if(time > (interval * 1000)){
			lastRunTime = currentMills;
			return true;
		}
		return false;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
