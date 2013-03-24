package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_BID_FINISH_NOTIFY)
public class BidFinishNotify extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private long seatId;

    @ByteField(index = 1, bytes = 1, description = "叫的分值")
    private int bidVal;

    @ByteField(index = 2, bytes = 1)
    private int num;

    @ByteField(index = 3, length = 2)
    private byte[] cards;

    public int getBidVal() {
        return bidVal;
    }

    public void setBidVal(int bidVal) {
        this.bidVal = bidVal;
    }

    public byte[] getCards() {
        if (cards == null) {
            cards = new byte[0];
        }
        return cards;
    }

    public long getSeatId() {
        return seatId;
    }

    public void setSeatId(long seatId) {
        this.seatId = seatId;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setCards(byte[] cards) {
        this.cards = cards;
        this.num = getCards().length;
    }

    public int getNum() {
        return num;
    }
}
