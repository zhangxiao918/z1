
	
package com.skymobi.cac.maopao.communication.aidl;

import com.skymobi.cac.maopao.xip.AccessHeader;

interface IMsgCallback{

	void receive(int msgCode, out AccessHeader header, out byte[] bytes);
	void onMsg(int level, String detail);
	void onTerminate();
}