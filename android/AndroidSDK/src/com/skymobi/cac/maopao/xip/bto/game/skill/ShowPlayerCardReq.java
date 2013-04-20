package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 请求查看其他玩家牌（奇袭技能）
 * @author Terry.Fang
 *
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_SHOW_PLAYER_CARD_REQ)
public class ShowPlayerCardReq extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int otherSeatId;

    public int getOtherSeatId() {
        return otherSeatId;
    }

    public void setOtherSeatId(int otherSeatId) {
        this.otherSeatId = otherSeatId;
    }

}
