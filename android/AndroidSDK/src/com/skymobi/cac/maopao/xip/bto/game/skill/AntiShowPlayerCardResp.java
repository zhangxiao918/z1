package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


/**
 * 不同意被其他玩家查看牌请求的回复（闪避技能）
 * 
 * @author Terry.Fang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_ANTI_SHOW_PLAYER_CARD_RESP)
public class AntiShowPlayerCardResp extends XipResp {

}
