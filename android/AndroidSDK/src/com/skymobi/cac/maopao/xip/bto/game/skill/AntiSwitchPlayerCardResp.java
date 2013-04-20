package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 是否同意被玩家换牌请求的回复（护主技能）
 * 
 * @author Terry.Fang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_ANTI_SWITCH_PLAYER_CARD_RESP)
public class AntiSwitchPlayerCardResp extends XipBody {

    @ByteField(index = 3, bytes = 1, description = "换出去的牌")
    private int switchOutCard;

    @ByteField(index = 4, bytes = 1, description = "换进来的牌")
    private int switchInCard;

    public int getSwitchOutCard() {
        return switchOutCard;
    }

    public void setSwitchOutCard(int switchOutCard) {
        this.switchOutCard = switchOutCard;
    }

    public int getSwitchInCard() {
        return switchInCard;
    }

    public void setSwitchInCard(int switchInCard) {
        this.switchInCard = switchInCard;
    }

}
