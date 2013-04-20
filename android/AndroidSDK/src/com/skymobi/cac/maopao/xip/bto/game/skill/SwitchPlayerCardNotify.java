package com.skymobi.cac.maopao.xip.bto.game.skill;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 请求和其他玩家换牌的回复（神偷技能）
 * 
 * @author Terry.Fang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_SWITCH_PLAYER_CARD_NOTIFY)
public class SwitchPlayerCardNotify extends XipBody {

	@ByteField(index = 0, bytes = 1, description = "错误代码")
    private int errorCode;

    @ByteField(index = 1, bytes = 1, description = "提示消息长度")
    private int errorMsgLen;

    @ByteField(index = 2, length = 1, description = "提示消息内容")
    private String errorMessage;

    @ByteField(index = 3, bytes = 1, description = "换出去的牌")
    private int fromCard;

    @ByteField(index = 4, bytes = 1, description = "换进来的牌")
    private int toCard;

    @ByteField(index = 5, bytes = 1, description = "发起换牌的位置号")
    private int fromSeatId;

    @ByteField(index = 6, bytes = 1, description = "被换牌的位置号")
    private int toSeartId;

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

    public int getFromCard() {
        return fromCard;
    }

    public void setFromCard(int fromCard) {
        this.fromCard = fromCard;
    }

    public int getToCard() {
        return toCard;
    }

    public void setToCard(int toCard) {
        this.toCard = toCard;
    }

    public int getFromSeatId() {
        return fromSeatId;
    }

    public void setFromSeatId(int fromSeatId) {
        this.fromSeatId = fromSeatId;
    }

    public int getToSeartId() {
        return toSeartId;
    }

    public void setToSeartId(int toSeartId) {
        this.toSeartId = toSeartId;
    }
}
