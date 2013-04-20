package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


/**
 * 是否同意被玩家换牌请求（护主技能）
 * 
 * @author Terry.Fang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_ANTI_SWITCH_PLAYER_CARD_REQ)
public class AntiSwitchPlayerCardReq extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int isAgreed;

    public int getIsAgreed() {
        return isAgreed;
    }

    public void setIsAgreed(int isAgreed) {
        this.isAgreed = isAgreed;
    }

}
