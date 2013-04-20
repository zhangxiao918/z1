package com.skymobi.cac.maopao.communication.aidl;

import com.skymobi.cac.maopao.passport.api.CurrentUser;
import com.skymobi.cac.maopao.communication.aidl.IMsgCallback;


interface IMsgService{
	void send(in byte[] bytes, in boolean encrypt, in boolean seq);
	byte[] getEncryptKey();
	boolean loginOk();
	void registerCallback(IMsgCallback cb);
    void unregisterCallback(IMsgCallback cb);
}