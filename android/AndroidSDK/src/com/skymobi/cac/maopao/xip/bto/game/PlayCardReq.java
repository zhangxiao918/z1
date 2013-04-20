package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PLAY_CARD_REQ)
public class PlayCardReq extends XipBody {

    @ByteField(index = 1, bytes = 1)
    private int num;

    @ByteField(index = 2, length = 1)
    private byte[] pcard;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public byte[] getPcard() {
        if (pcard == null) {
            this.pcard = new byte[0];
        }
        return pcard;
    }

    public void setPcard(byte[] pcard) {
        this.pcard = pcard;
        this.setNum(getPcard().length);
    }

}
