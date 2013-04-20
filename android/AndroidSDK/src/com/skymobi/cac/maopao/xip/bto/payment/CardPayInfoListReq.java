/**
 * 
 */

package com.skymobi.cac.maopao.xip.bto.payment;

import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 获取可用支付列表请求对象
 * 
 * @author bluestome.zhang
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_CARD_PAY_INFO_LIST_REQ)
public class CardPayInfoListReq extends XipBody {

}
