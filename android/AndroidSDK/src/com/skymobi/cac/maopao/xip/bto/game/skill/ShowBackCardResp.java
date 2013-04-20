package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_SHOW_BACK_CARD_RESP)
public class ShowBackCardResp extends XipResp {

    @ByteField(index = 3, bytes = 1, description = "底牌数量，默认为2")
    private int num;

    @ByteField(index = 4, length = 3, description = "底牌")
    private byte[] cards;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

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
}
