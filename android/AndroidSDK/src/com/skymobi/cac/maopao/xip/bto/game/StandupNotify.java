package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_STANDUP_NOTIFY)
public class StandupNotify extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int seatId;

    @ByteField(index = 1, bytes = 1)
    private byte infoLen;

    @ByteField(index = 2, length = 1)
    private String info;

    public byte getInfoLen() {
        return infoLen;
    }

    public void setInfoLen(byte infoLen) {
        this.infoLen = infoLen;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
        this.infoLen = (byte) ((info == null) ? 0 : (info.length() * 2));
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

}
