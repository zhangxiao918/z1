
package com.skymobi.cac.maopao.xip.bto.im;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_SITEMAIL_SEND_REQ)
public class SendSiteMailReq extends XipBody {

    @ByteField(index = 0, bytes = 4)
    private long receiverId;

    @ByteField(index = 1)
    private byte receiverNickLength;

    @ByteField(index = 2, length = 1)
    private String receiverNick;

    @ByteField(index = 3, bytes = 4)
    private long sendMailId;

    @ByteField(index = 4)
    private byte titleLength;

    @ByteField(index = 5, length = 4)
    private String title;

    @ByteField(index = 6, bytes = 2)
    private int contentLength;

    @ByteField(index = 7, length = 6)
    private String content;

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public byte getReceiverNickLength() {
        return receiverNickLength;
    }

    public void setReceiverNickLength(byte receiverNickLength) {
        this.receiverNickLength = receiverNickLength;
    }

    public String getReceiverNick() {
        return receiverNick;
    }

    public void setReceiverNick(String receiverNick) {
        this.receiverNick = receiverNick;
        this.receiverNickLength = (byte) (receiverNick == null ? 0 : receiverNick.length() * 2);
    }

    public long getSendMailId() {
        return sendMailId;
    }

    public void setSendMailId(long sendMailId) {
        this.sendMailId = sendMailId;
    }

    public int getTitleLength() {
        return titleLength;
    }

    public void setTitleLength(byte titleLength) {
        this.titleLength = titleLength;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleLength = (byte) (title == null ? 0 : title.length() * 2);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.contentLength = (content == null ? 0 : content.length() * 2);
    }

}
