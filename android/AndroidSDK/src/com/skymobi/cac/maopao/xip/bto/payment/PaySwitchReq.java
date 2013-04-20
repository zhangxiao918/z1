
package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_BILL_PAY_SWITCH_REQ)
public class PaySwitchReq extends XipBody {

    @ByteField(index = 0, bytes = 2, description = "游戏ID，斗地主1")
    private short gameId;

    @ByteField(index = 1, bytes = 4, description = "渠道ID")
    private int enterId;

    public short getGameId() {
        return gameId;
    }

    public void setGameId(short gameId) {
        this.gameId = gameId;
    }

    public int getEnterId() {
        return enterId;
    }

    public void setEnterId(int enterId) {
        this.enterId = enterId;
    }

}
