package com.skymobi.cac.maopao.xip;

import java.util.HashMap;
import java.util.Map;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
import com.skymobi.cac.maopao.xip.bto.basis.EnterRoomStyleReq;
import com.skymobi.cac.maopao.xip.bto.basis.GetGameInfoReq;
import com.skymobi.cac.maopao.xip.bto.basis.GetGameInfoResp;
import com.skymobi.cac.maopao.xip.bto.game.AddPlayerNotify;
import com.skymobi.cac.maopao.xip.bto.game.BidFinishNotify;
import com.skymobi.cac.maopao.xip.bto.game.BidNotify;
import com.skymobi.cac.maopao.xip.bto.game.BidReq;
import com.skymobi.cac.maopao.xip.bto.game.BidResp;
import com.skymobi.cac.maopao.xip.bto.game.DispatchCardNotify;
import com.skymobi.cac.maopao.xip.bto.game.PlayCardReq;
import com.skymobi.cac.maopao.xip.bto.game.QuitGameReq;
import com.skymobi.cac.maopao.xip.bto.game.QuitGameResp;
import com.skymobi.cac.maopao.xip.bto.game.RaiseHandNotify;
import com.skymobi.cac.maopao.xip.bto.game.StartBidNotify;
import com.skymobi.cac.maopao.xip.bto.lobby.LaunchGameNotify;
import com.skymobi.cac.maopao.xip.bto.user.LoginReq;
import com.skymobi.cac.maopao.xip.bto.user.LoginResp;


public class XipBeanLoader {

	public static Map<Integer, Class<?>> msgCodeXipMap 
			= new HashMap<Integer, Class<?>>();

	public static void add2Map(Class<?> cls) {
		if (XipBody.class.isAssignableFrom(cls)) {
			XipSignal attr = cls.getAnnotation(XipSignal.class);
			if (null != attr) {
				msgCodeXipMap.put(attr.messageCode(), cls);
			}
		}
	}

	static {
		// 登录与注册
		add2Map(LoginReq.class);
		add2Map(LoginResp.class);
		
		// Basis模块
		add2Map(GetGameInfoReq.class);
		add2Map(GetGameInfoResp.class);
		add2Map(EnterRoomStyleReq.class);
		
		// 打牌游戏模块
		add2Map(LaunchGameNotify.class);
		
		add2Map(QuitGameReq.class);
		add2Map(QuitGameResp.class);
		
		add2Map(AddPlayerNotify.class);
		add2Map(RaiseHandNotify.class);
		
		add2Map(DispatchCardNotify.class);
		
		add2Map(StartBidNotify.class);
		add2Map(BidNotify.class);
		add2Map(BidFinishNotify.class);
		add2Map(BidReq.class);
		add2Map(BidResp.class);
		
		add2Map(PlayCardReq.class);
		
	}
}
