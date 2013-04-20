package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


/**
 * 请求和其他玩家换牌（神偷技能）
 * 
 * @author Terry.Fang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_SWITCH_PLAYER_CARD_REQ)
public class SwitchPlayerCardReq extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int otherSeatId;

    @ByteField(index = 1, bytes = 1, description = "换出去的牌")
    private int switchOutCard;

    public int getOtherSeatId() {
        return otherSeatId;
    }

    public void setOtherSeatId(int otherSeatId) {
        this.otherSeatId = otherSeatId;
    }

    public int getSwitchOutCard() {
        return switchOutCard;
    }

    public void setSwitchOutCard(int switchOutCard) {
        this.switchOutCard = switchOutCard;
    }

}
