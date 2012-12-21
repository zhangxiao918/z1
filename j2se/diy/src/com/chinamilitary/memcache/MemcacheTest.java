package com.chinamilitary.memcache;

import java.util.Date;

import com.chinamilitary.util.CacheUtils;

public class MemcacheTest extends Thread{

	
	private int count = 0;
	private static MemcacheClient client = new MemcacheClient();
	
	public MemcacheTest(){
		System.out.println("启动MEMCACHE");
//		client.add("http://tuku.military.china.com/military/html/2009-12-17/134438.htm", "http://tuku.military.china.com/military/html/2009-12-17/134438.htm");
	}
	
	public void run(){
		try{
			while(true){
				String key = String.valueOf(System.currentTimeMillis());
				
				Object obj = client.get(key);
//				Object obj = client.get("drulechao:460_1_skytest_m900_240x320_1000:1");
				if(null != obj){
					System.out.println("缓存不为空");
				}else{
					System.out.println("缓存为空");
					client.add(key, "http://tuku.military.china.com/military/html/2009-12-17/134438.htm");
				}
				Thread.sleep(10);
				
//				if(client.get("abc") == null){
//					client.add("abc", "cde");
//				}
				
				
//				client.remove("abc");
				
				count ++;
			}
		}catch(Exception e){
			
		}
	}
	
	public static void main(String args[]){
		Thread th = new Thread(new MemcacheTest());
		th.start();
//		String info = (String)client.get(CacheUtils.getHTMLKey(123));
//		System.out.println("info:"+info);
	}
}
