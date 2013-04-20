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

	public static RegisterReq getRegisterReq(String username, String passwd, String passwd2, String nickname, int gender){
		RegisterReq req = new RegisterReq();
		req.setAccountName(username);
		req.setPwd(passwd);
		req.setConfirmPwd(passwd2);
		req.setNickName(nickname);
		req.setGender(gender);
		req.addHeader(
				0x2, 
				BasisMessageConstants.SMOD_ID_CAC_ACCOUNT, 
				BasisMessageConstants.MSG_FLAG_ENCRYPT_TEMP_UA 
				);
		
		return req;
	}
	
	public static QuickRegisterReq getQuickRegisterReq(){
		QuickRegisterReq req = new QuickRegisterReq();
		req.addHeader(
				0x2, 
				BasisMessageConstants.SMOD_ID_CAC_ACCOUNT, 
				BasisMessageConstants.MSG_FLAG_ENCRYPT_TEMP_UA 
				);
		
		return req;
	}
	
	public static ThirdLoginReq getThirdLoginReq(String openid, int openPlatformType, String nickname){
		ThirdLoginReq req = new ThirdLoginReq();
		req.setOpenId(openid);
		req.setOpenPlatformType(openPlatformType);
		req.setNickName(nickname);
		req.addHeader(
				0x2, 
				BasisMessageConstants.SMOD_ID_CAC_ACCOUNT, 
				BasisMessageConstants.MSG_FLAG_ENCRYPT_TEMP_UA 
				);
		
		return req;
	}
	
	public static UserReportReq getUserReportReq(int type, String title, String content){
		UserReportReq req = new UserReportReq();
		req.setType((byte)type);
		req.setTitle(title);
		req.setContent(content);
		req.addHeader(
				0x2, 
				BasisMessageConstants.SMOD_ID_CAC_BASIS, 
				BasisMessageConstants.MSG_FLAG_ENCRYPT_TEMP_UA 
				);
		
		return req;
	}
}
