package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


/**
 * 被玩家换牌时收到的通知（奇袭技能）
 * 
 * @author Terry.Fang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_ANTI_SWITCH_PLAYER_CARD_NOTIFY)
public class AntiSwitchPlayerCardNotify extends XipBody {

	 @ByteField(index = 1, bytes = 1, description = "牌")
	    private int cards;

	    @ByteField(index = 2, bytes = 1, description = "发起看牌的位置号")
	    private int fromSeatId;

	    public int getCards() {
	        return cards;
	    }

	    public void setCards(byte cards) {
	        this.cards = cards;
	    }

	    public int getFromSeatId() {
	        return fromSeatId;
	    }

	    public void setFromSeatId(int fromSeatId) {
	        this.fromSeatId = fromSeatId;
	    }

}
