package com.skymobi.cac.maopao;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import com.skymobi.cac.maopao.communication.aidl.IMsgService;
import com.skymobi.cac.maopao.communication.router.IMsgRouter;
import com.skymobi.cac.maopao.communication.router.MsgRouter;
import com.skymobi.cac.maopao.communication.service.WanMsgService;
import com.skymobi.cac.maopao.xip.bto.user.PlayerBaseInfoBTO;

public class GameApplication extends Application{
	
	private final static String TAG = "GameApplication";
	
	private IMsgRouter mMsgRouter;
	private IMsgService mMsgService;
	private PlayerBaseInfoBTO playerInfo;

	@Override
	public void onCreate(){
		super.onCreate();
	}
	
	public void setMsgRouter(IMsgRouter msgRouter) {
		if(mMsgRouter != null){
			mMsgRouter.shutDown();
			mMsgRouter = null;
		}
		mMsgRouter = msgRouter;
	}
	
	public IMsgRouter getMsgRouter() {
		return mMsgRouter;
	}

	public void fullExitApplication(){
		try{
			unBindMsgService();
			stopServices();
		}catch(IllegalArgumentException e){}
		System.exit(0);
	}
	
	public void buildMsgRouter(Context applicationContext){
		unBindMsgService();
		stopMessageService();
		
		setMsgRouter(new MsgRouter());
		buildRemoteRouter(WanMsgService.class);
	}
	
	private void buildRemoteRouter(Class<WanMsgService> msgServiceClass){
		Intent intent = new Intent(this, msgServiceClass);
		bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);
	}
	
	private void stopServices(){
		if(mMsgService != null){
			unbindService(mServiceConn);
			mMsgService = null;
		}
		stopMessageService();
		Log.d(TAG, "stop msg service");
	}
	
	private void stopMessageService(){
		Intent intent = new Intent(this.getApplicationContext(), WanMsgService.class);
		stopService(intent);
	}
	
	private void unBindMsgService(){
		if(this.mMsgRouter != null){
			this.mMsgRouter.shutDown();
		}
	}
	
	private ServiceConnection mServiceConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mMsgService = IMsgService.Stub.asInterface(service);
			Log.d(TAG, "on msg service connected!");
			if (mMsgService != null) {
				if(mMsgRouter == null){
					mMsgRouter = new MsgRouter();
				}
				mMsgRouter.setMsgService(mMsgService, true);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "disconnect from msg service");
			mMsgService = null;
		}
	};

	public PlayerBaseInfoBTO getPlayerInfo() {
		return playerInfo;
	}

	public void setPlayerInfo(PlayerBaseInfoBTO playerInfo) {
		this.playerInfo = playerInfo;
	}
}

