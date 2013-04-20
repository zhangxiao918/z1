package com.hifreshday.android.thirdpart.user.oAuth2;

import android.content.Context;
import android.content.Intent;

import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

public final class OAuthQQ {
	
	private static OAuthQQ mInstance = null;
	private OAuthV2 mOAuth;
	public static int OAUTH_QQ_REQUESTCODE = 1001;
	public static final String QQ_RedirectUri = "http://www.tencent.com/zh-cn/index.shtml";
	public static final String QQ_APP_KEY = "801115505";
	public static final String QQ_APP_SECRET = "be1dd1410434a9f7d5a2586bab7a6829";
	
	private OAuthQQ(){
		
	}
	
	public static OAuthQQ getInstance() {
        if (null == mInstance) {
        	mInstance = new OAuthQQ();
        }
        return mInstance;
    }
	
	public Intent GetOAuthQQContext(Context srcCon){
		mOAuth=new OAuthV2(OAuthQQ.QQ_RedirectUri);
		mOAuth.setClientId(OAuthQQ.QQ_APP_KEY);
		mOAuth.setClientSecret(OAuthQQ.QQ_APP_SECRET);
		
		Intent intent = new Intent(srcCon, OAuthV2AuthorizeWebView.class);
	       intent.putExtra("oauth", mOAuth);
	   
	       return intent;
	};
}
