package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 不同意被其他玩家查看牌请求（闪避技能）
 * 
 * @author Terry.Fang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_ANTI_SHOW_PLAYER_CARD_REQ)
public class AntiShowPlayerCardReq extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int isAgreed;

    public int getIsAgreed() {
        return isAgreed;
    }

    /**
     * 
     * @param isAgreed 1表示挡，0表示不挡
     */
    public void setIsAgreed(int isAgreed) {
        this.isAgreed = isAgreed;
    }

}
