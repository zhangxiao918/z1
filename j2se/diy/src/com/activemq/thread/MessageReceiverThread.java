package com.activemq.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.activemq.impl.MessageReceiver;

public class MessageReceiverThread extends Thread {
	
	static Log logger = LogFactory.getLog(MessageReceiverThread.class);
	private MessageReceiver messageReceiver;
	private boolean isRun = true;
	int i=1;
	//休眠时间，从配置文件中读取
	private long sleeptime = 1000;

	
	public MessageReceiverThread() {
		logger.info(" >> MessageReceiverThread GO!");
	}

	public void run() {
		try{
			while(isRun){
				logger.info(" >> MessageReceiverThread.i:"+i);
				execute();
			}
		}catch(Exception e){
			logger.error(e);
			isRun = false;
			try {
				Thread.sleep(sleeptime);
				isRun = true;
			} catch (InterruptedException e1) {
				logger.error(e);
			}
		}
	}

	public void execute(){
		int result = messageReceiver.receiverMsg();
		logger.info(" >> result:"+result);
		if(result > 0){
			i++;
		}
	}
	public final MessageReceiver getMessageReceiver() {
		return messageReceiver;
	}

	public final void setMessageReceiver(MessageReceiver messageReceiver) {
		this.messageReceiver = messageReceiver;
	}

	public final boolean isRun() {
		return isRun;
	}

	public final void setRun(boolean isRun) {
		this.isRun = isRun;
	}

	public final long getSleeptime() {
		return sleeptime;
	}

	public final void setSleeptime(long sleeptime) {
		this.sleeptime = sleeptime;
	}

	
}
