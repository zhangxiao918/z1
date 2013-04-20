/*******************************************************************************
 * CopyRight (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename: EnterRoomModuleReq.java
 * Creator: Qi Wang
 * Version: 1.0
 * Date: 2010-3-23 ����11:26:30
 * Description:
 *******************************************************************************/
package com.skymobi.cac.maopao.xip.bto.user;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.XipResp;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_USER_REPORT_REQ)
public class UserReportReq extends XipBody {

    @ByteField(index = 0)
    private byte type;

    @ByteField(index = 1)
    private byte titleLen;

    @ByteField(index = 2, length = 1)
    private String title;

    @ByteField(index = 3, bytes = 2)
    private int contentLen;

    @ByteField(index = 4, length = 3)
    private String content;

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getTitleLen() {
        return titleLen;
    }

    public void setTitleLen(byte titleLen) {
        this.titleLen = titleLen;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleLen = (byte) ((title == null) ? 0 : title.length() * 2);
    }

    public int getContentLen() {
        return contentLen;
    }

    public void setContentLen(int contentLen) {
        this.contentLen = contentLen;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.contentLen = (content == null) ? 0 : content.length() * 2;
    }
}
