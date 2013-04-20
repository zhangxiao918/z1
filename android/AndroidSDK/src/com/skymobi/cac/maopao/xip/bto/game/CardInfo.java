package com.skymobi.cac.maopao.xip.bto.game;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class CardInfo implements ByteBean {

    @ByteField(index = 0, bytes = 1)
    private int seatId;

    @ByteField(index = 1, bytes = 1)
    private int num;

    @ByteField(index = 2, length = 1)
    private byte[] cards;

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public byte[] getCards() {
        if (cards == null) {
            cards = new byte[0];
        }
        return cards;
    }

    public void setCards(byte[] cards) {
        this.cards = cards;
        this.num = (cards == null) ? 0 : cards.length;
    }

    public int getNum() {
        return num;
    }
}