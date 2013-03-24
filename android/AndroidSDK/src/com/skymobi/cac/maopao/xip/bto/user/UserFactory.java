package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.xip.BasisMessageConstants;

public class UserFactory {

	public static LoginReq getLoginReq(String username, String passwd){
		LoginReq req = new LoginReq();
		req.setAccountName(username);
		req.setPwd(passwd);
		req.setAccountType(0);
		req.addHeader(
				0x2, 
				BasisMessageConstants.SMOD_ID_CAC_ACCOUNT, 
				BasisMessageConstants.MSG_FLAG_ENCRYPT_TEMP_UA 
				);
		
		return req;
	}

}
