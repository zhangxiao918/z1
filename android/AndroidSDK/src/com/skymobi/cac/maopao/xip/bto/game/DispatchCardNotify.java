package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;


@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_DISPATCH_CARD_NOTIFY)
public class DispatchCardNotify extends XipBody {

    @ByteField(index = 0, bytes = 1)
    private int num;

    @ByteField(index = 1, length = 0)
    private byte[] cards;

    public byte[] getCards() {
        if (cards == null) {
            cards = new byte[0];
        }
        return cards;
    }

    public void setCards(byte[] cards) {
        this.cards = cards;
        this.setNum(getCards().length);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

}
