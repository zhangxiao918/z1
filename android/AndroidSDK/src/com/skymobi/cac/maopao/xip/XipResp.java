package com.skymobi.cac.maopao.xip;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class XipResp extends XipBody {
	@ByteField(index = 0, bytes = 1, description = "错误代码")
    private int errorCode;

    @ByteField(index = 1, bytes = 1, description = "提示消息长度")
    private int errorMsgLen;

    @ByteField(index = 2, length = 1, description = "提示消息内容")
    private String errorMessage;
    
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
}
