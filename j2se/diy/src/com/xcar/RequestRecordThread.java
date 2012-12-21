package com.xcar;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.chinamilitary.util.IOUtil;

public class RequestRecordThread extends Thread {
	
	private static Log logger = LogFactory.getLog(RequestRecordThread.class);
	
	private int count = 0;
	
	private int count2 = 0;
	
	private boolean isRunning = true;
	
	private long sleeptime = 0;
	
	public RequestRecordThread(){
		System.out.println("��ȡ��������¼�߳���!!!");
		sleeptime = Integer.valueOf(10000);
	}
	
	
	public synchronized void run() {
		try {
			while (isRunning) {
				count = ResourceQueneInsert.getInstance().getQuene().size();
				if(count > 0){
					executeInsert();
				}else{
					pause();
				}
			}
		} catch (Exception e) {
			System.err.println("RecordThread.run.exception:"+e.getMessage());
			try{
				ResourceQueneInsert.getInstance().getQuene().clear();
				pause();
			}catch(Exception ex){
				System.err.println(" >> pause exception:"+ex);
			}
		}
	}
	
	public void pause() throws Exception{
		isRunning = false;
		Thread.sleep(sleeptime);
		isRunning = true;
	}

	/**
	 * ��ȡ�����е�����¼
	 * @throws Exception
	 */
	public synchronized void executeInsert() throws Exception {
		Object obj = ResourceQueneInsert.getInstance().getElement();
		if(obj instanceof FileContent){
			FileContent content = (FileContent)obj;
			if(null != content){
				IOUtil.createFile(content.getContent(), content.getSavePath());
				System.out.println(" >> save file success:"+content.getSavePath());
				Thread.sleep(100);
			}
		}
	}

	public int getCount() {
		return count;
	}


	public void setCount(int count) {
		this.count = count;
	}


	public boolean isRunning() {
		return isRunning;
	}


	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}


	public long getSleeptime() {
		return sleeptime;
	}


	public void setSleeptime(long sleeptime) {
		this.sleeptime = sleeptime;
	}

	
}