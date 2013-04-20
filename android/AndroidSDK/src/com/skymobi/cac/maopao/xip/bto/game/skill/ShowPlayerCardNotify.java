package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;
/**
 * 请求查看其他玩家牌回复（奇袭技能）
 * @author Terry.Fang
 *
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_SHOW_PLAYER_CARD_NOTIFY)
public class ShowPlayerCardNotify extends XipBody {

    @ByteField(index = 0, bytes = 1, description = "错误代码")
    private int errorCode;

    @ByteField(index = 1, bytes = 1, description = "提示消息长度")
    private int errorMsgLen;

    @ByteField(index = 2, length = 1, description = "提示消息内容")
    private String errorMessage;

    @ByteField(index = 3, bytes = 1, description = "查看其他玩家牌数量，默认为2")
    private int num;

    @ByteField(index = 4, length = 3, description = "牌")
    private byte[] cards;
    
    @ByteField(index = 5, bytes = 1, description = "发起看牌的位置号")
    private int fromSeatId;
    
    @ByteField(index = 6, bytes = 1, description = "被看牌的位置号")
    private int toSeartId;

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

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.errorMsgLen = (errorMessage == null) ? (byte) 0 : (byte) (errorMessage.length() * 2);
    }

    public int getErrorMsgLen() {
        return errorMsgLen;
    }

	public int getFromSeatId() {
		return fromSeatId;
	}

	public int getToSeartId() {
		return toSeartId;
	}
}
