/**
 * 
 */

package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 获取可用支付列表响应
 * 
 * @author bluestome.zhang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_CARD_PAY_INFO_LIST_RESP)
public class CardPayInfoListResp extends XipResp {
    @ByteField(index = 3, bytes = 1, description = "卡密类型长度")
    private int cardPayTypeListSize;

    @ByteField(index = 4, length = 3, description = "支付数组")
    private CardPayTypeInfo[] cardPayTypeList;

    /**
     * @return the cardPayTypeListSize
     */
    public int getCardPayTypeListSize() {
        return cardPayTypeListSize;
    }

    /**
     * @param cardPayTypeListSize the cardPayTypeListSize to set
     */
    public void setCardPayTypeListSize(int cardPayTypeListSize) {
        this.cardPayTypeListSize = cardPayTypeListSize;
    }

    /**
     * @return the cardPayTypeList
     */
    public CardPayTypeInfo[] getCardPayTypeList() {
        return cardPayTypeList;
    }

    /**
     * @param cardPayTypeList the cardPayTypeList to set
     */
    public void setCardPayTypeList(CardPayTypeInfo[] cardPayTypeList) {
        this.cardPayTypeList = cardPayTypeList;
    }

}
