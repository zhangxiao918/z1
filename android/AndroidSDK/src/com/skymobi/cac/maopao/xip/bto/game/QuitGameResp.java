package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_QUIT_GAME_RESP)
public class QuitGameResp extends XipResp {
	// 根据ERROR_CODE判断，为0允许退出，不为0则不允许退出。
}
