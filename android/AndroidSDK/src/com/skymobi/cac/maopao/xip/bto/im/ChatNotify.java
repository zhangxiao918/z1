
package com.skymobi.cac.maopao.xip.bto.im;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_CHAT_NOTIFY)
public class ChatNotify extends XipBody {

    @ByteField(index = 0, bytes = 4, description = "发送者SkyId")
    private long skyId;

    @ByteField(index = 1, bytes = 1, description = "昵称长度")
    private int nickNameLen;

    @ByteField(index = 2, length = 1, description = "昵称")
    private String nickName;

    @ByteField(index = 3, bytes = 1, description = "表情")
    private int expressionId;

    @ByteField(index = 4, bytes = 2, description = "聊天消息长度")
    private int msgLen;

    @ByteField(index = 5, length = 4, description = "消息")
    private String msg;

    public long getSkyId() {
        return skyId;
    }

    public void setSkyId(long skyId) {
        this.skyId = skyId;
    }

    public int getNickNameLen() {
        return nickNameLen;
    }

    public void setNicknameLen(int nickNameLen) {
        this.nickNameLen = nickNameLen;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        // TODO 临时解决
        this.nickName = nickName;
        this.nickNameLen = (this.nickName == null) ? (byte) 0 : (byte) (this.nickName.length() * 2);
    }

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
