package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


/**
 * 被其他玩家查看牌时收到的通知（奇袭技能）
 * 
 * @author Terry.Fang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_ANTI_SHOW_PLAYER_CARD_NOTIFY)
public class AntiShowPlayerCardNotify extends XipBody {


    @ByteField(index = 0, bytes = 1, description = "发起看牌的位置号")
    private int fromSeatId;

    public int getFromSeatId() {
        return fromSeatId;
    }

    public void setFromSeatId(int fromSeatId) {
        this.fromSeatId = fromSeatId;
    }
}
