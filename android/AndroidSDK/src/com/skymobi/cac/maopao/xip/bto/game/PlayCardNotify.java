package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;



@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PLAY_CARD_NOTIFY)
public class PlayCardNotify extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private long seatId;

    @ByteField(index = 1, bytes = 1)
    private int type;

    @ByteField(index = 2, bytes = 1)
    private int num;

    @ByteField(index = 3, length = 2)
    private byte[] pcard;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public long getSeatId() {
        return seatId;
    }

    public void setSeatId(long seatId) {
        this.seatId = seatId;
    }

}
