/*******************************************************************************
 * CopyRight  (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename:  SMSPayReq.java
 * Creator:   xiaolei Wan
 * Version:   1.0
 * Date: 	  Oct 11, 2010
 * Description: 
 *******************************************************************************/

package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * @author xiaolei Wan
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SMS_PAY_ACCESS_REQ)
public class SMSPayAccessOrderReq extends XipBody {

    @ByteField(index = 0)
    private SMSAccessPayInfo smsAccessPayInfo;

    public SMSAccessPayInfo getSmsAccessPayInfo() {
        return smsAccessPayInfo;
    }

    public void setSmsAccessPayInfo(SMSAccessPayInfo smsAccessPayInfo) {
        this.smsAccessPayInfo = smsAccessPayInfo;
    }

}
