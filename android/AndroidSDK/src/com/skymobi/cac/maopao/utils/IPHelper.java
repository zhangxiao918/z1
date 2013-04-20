package com.skymobi.cac.maopao.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.util.Log;

public abstract class IPHelper {
	
	protected String TAG = "IPHelper";
	
	protected abstract int getPort();
	
	protected abstract String getDomain();
	
	protected abstract String[] getOptionalIps();
	
	protected final static int PING_TIMEOUT = 500;// 500ms
	
	protected String mIp;
	
	
	
	protected long pingIp(String ip){
		Socket socket = new Socket();
		try {
			long start = System.currentTimeMillis();
			InetAddress address = InetAddress.getByName(ip);
			InetSocketAddress remoteAddr = new InetSocketAddress(address, getPort());
			socket.connect(remoteAddr, PING_TIMEOUT);
			long cost = System.currentTimeMillis() - start;
			Log.d(TAG, "ip:"+ip+" cost:"+cost);
			return cost;
		} catch (Exception e1) {
			e1.printStackTrace();
		} 
		finally{
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return PING_TIMEOUT;
	}
	
	protected String getIpByDomain(){
		if(mIp != null){
			return mIp;
		}
		mIp = getOptionalIps()[getOptionalIps().length - 1];
		try {
			InetAddress address = InetAddress.getByName(getDomain());
			mIp = address.getHostAddress();
		} catch (UnknownHostException e) {
			Log.d(TAG, "fail to get ip address from domain");
			long cost = PING_TIMEOUT;
			for(String ip: getOptionalIps()){
				long tempCost = pingIp(ip);
				if(cost >= tempCost){
					mIp = ip;
					cost = tempCost;
				}
			}
		}
		return mIp;
		
	}
	
	public static String getLocalIpAddress() {  
        String ipaddress="";
       
	    try {  
	        for (Enumeration<NetworkInterface> en = NetworkInterface  
	                .getNetworkInterfaces(); en.hasMoreElements();) {  
	            NetworkInterface intf = en.nextElement();  
	            for (Enumeration<InetAddress> enumIpAddr = intf  
	                    .getInetAddresses(); enumIpAddr.hasMoreElements();) {  
	                InetAddress inetAddress = enumIpAddr.nextElement();  
	                if (!inetAddress.isLoopbackAddress()) {  
	                        ipaddress=inetAddress.getHostAddress().toString();  
	                        break;
	                }  
	            }
	        }  
	    } catch (SocketException ex) {  
	        Log.e("WifiPreference IpAddress", ex.toString());  
	    }  
	    return ipaddress;
    }
	
}
