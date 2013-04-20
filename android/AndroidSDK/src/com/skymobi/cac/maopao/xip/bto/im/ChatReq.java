
package com.skymobi.cac.maopao.xip.bto.im;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_CHAT_REQ)
public class ChatReq extends XipBody {

    @ByteField(index = 0, bytes = 1, description = "表情")
    private int expressionId;

    @ByteField(index = 1, bytes = 2)
    private int msgLen;

    @ByteField(index = 2, length = 1)
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        this.msgLen = (msg == null) ? 0 : (msg.length() * 2);
    }

    public int getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }

    public int getExpressionId() {
        return expressionId;
    }

    public void setExpressionId(int expressionId) {
        this.expressionId = expressionId;
    }

}
