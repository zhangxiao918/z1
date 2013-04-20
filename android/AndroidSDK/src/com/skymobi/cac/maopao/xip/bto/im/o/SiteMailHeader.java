
package com.skymobi.cac.maopao.xip.bto.im.o;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class SiteMailHeader implements ByteBean {

    @ByteField(index = 0, bytes = 4, description = "站内信编号")
    private long mailId;

    @ByteField(index = 1)
    private byte titleLength;

    @ByteField(index = 2, length = 1, description = "站内信标题")
    private String title;

    @ByteField(index = 3, bytes = 4)
    private long senderId;

    @ByteField(index = 4)
    private byte nickNameLength;

    @ByteField(index = 5, length = 4, description = "发件人昵称")
    private String nickName;

    @ByteField(index = 6, fixedLength = 38, description = "站内信发送的日期，格式(yy-MM-dd HH:mm:ss)")
    private String createTime;

    public long getMailId() {
        return mailId;
    }

    public void setMailId(long mailId) {
        this.mailId = mailId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public byte getTitleLength() {
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
        this.titleLength = (byte) ((title == null) ? 0 : title.length() * 2);
    }

    public byte getNickNameLength() {
        return nickNameLength;
    }

    public void setNickNameLength(byte nickNameLength) {
        this.nickNameLength = nickNameLength;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.nickNameLength = (byte) ((nickName == null) ? 0 : nickName.length() * 2);
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

}
