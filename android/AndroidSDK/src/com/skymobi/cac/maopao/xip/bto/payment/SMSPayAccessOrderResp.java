/**
 * Dec 25, 2011 3:31:31 PM
 * com.skymobi.cac.sms.bto.xip.SMSPayAccessResp.java
 * xiaolei Wan
 *
 *
 */

package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * @author xiaolei Wan
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SMS_PAY_ACCESS_RESP)
public class SMSPayAccessOrderResp extends XipResp {
    @ByteField(index = 3)
    private SMSOrderInfo smsOrderInfo;

    public SMSOrderInfo getSmsOrderInfo() {
        return smsOrderInfo;
    }

    public void setSmsOrderInfo(SMSOrderInfo smsOrderInfo) {
        this.smsOrderInfo = smsOrderInfo;
    }
}
