
package com.skymobi.cac.maopao.xip.bto.pet;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

/**
 * 通信协议处理类 用户女宠操作请求
 * 
 * @author shangguo.wu
 * @date 2013-02-21
 */
@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_DDZ_PET_OPER_REQ)
public class PlayerPetOperReq extends XipBody {
    @ByteField(index = 0, bytes = 8, description = "女宠缠绵者ID")
    private long playerInfoId = 0;
    @ByteField(index = 1, description = "女宠缠绵者昵称长度")
    private long playerInfoNicknameLen;
    @ByteField(index = 2, length = 1, description = "女宠缠绵者昵称")
    private String playerInfoNickname;

    @ByteField(index = 3, bytes = 8, description = "女宠拥有者ID")
    private long skyId = 0;
    @ByteField(index = 4, description = "女宠拥有者昵称长度")
    private long skyNicknameLen;
    @ByteField(index = 5, length = 4, description = "女宠拥有者昵称")
    private String skyNickname;

    @ByteField(index = 6, bytes = 8, description = "女宠ID")
    private long playPetId = 0;
    @ByteField(index = 7, description = "女宠昵称长度")
    private long playPetNicknameLen;
    @ByteField(index = 8, length = 7, description = "女宠昵称")
    private String playPetNickname;
    @ByteField(index = 9, bytes = 8, description = "斗地主房间类型")
    private long roomStyleId;
    @ByteField(index = 10, bytes = 8, description = "物品ID")
    private long propId;

    @ByteField(index = 11, bytes = 8, description = "女宠操作类型：1.缠绵  2-打牌结束体力经验增加   3-体力药水    ")
    private long operType = 0;

    public long getPlayerInfoId() {
        return playerInfoId;
    }

    public void setPlayerInfoId(long playerInfoId) {
        this.playerInfoId = playerInfoId;
    }

    public long getPlayerInfoNicknameLen() {
        return playerInfoNicknameLen;
    }

    public void setPlayerInfoNicknameLen(long playerInfoNicknameLen) {
        this.playerInfoNicknameLen = playerInfoNicknameLen;
    }

    public String getPlayerInfoNickname() {
        return playerInfoNickname;
    }

    public void setPlayerInfoNickname(String playerInfoNickname) {
        this.playerInfoNicknameLen = (this.playerInfoNickname == null ? 0 : this.playerInfoNickname
                .length() * 2);
        this.playerInfoNickname = playerInfoNickname;
    }

    public long getSkyId() {
        return skyId;
    }

    public void setSkyId(long skyId) {
        this.skyId = skyId;
    }

    public long getSkyNicknameLen() {
        return skyNicknameLen;
    }

    public void setSkyNicknameLen(long skyNicknameLen) {
        this.skyNicknameLen = skyNicknameLen;
    }

    public String getSkyNickname() {
        return skyNickname;
    }

    public void setSkyNickname(String skyNickname) {
        this.skyNicknameLen = (this.skyNickname == null ? 0 : this.skyNickname.length() * 2);
        this.skyNickname = skyNickname;
    }

    public long getPlayPetId() {
        return playPetId;
    }

    public void setPlayPetId(long playPetId) {
        this.playPetId = playPetId;
    }

    public long getPlayPetNicknameLen() {
        return playPetNicknameLen;
    }

    public void setPlayPetNicknameLen(long playPetNicknameLen) {
        this.playPetNicknameLen = playPetNicknameLen;
    }

    public String getPlayPetNickname() {
        return playPetNickname;
    }

    public void setPlayPetNickname(String playPetNickname) {
        this.playPetNicknameLen = (this.playPetNickname == null ? 0
                : this.playPetNickname.length() * 2);
        this.playPetNickname = playPetNickname;
    }

    public long getOperType() {
        return operType;
    }

    public void setOperType(long operType) {
        this.operType = operType;
    }

    public long getRoomStyleId() {
        return roomStyleId;
    }

    public void setRoomStyleId(long roomStyleId) {
        this.roomStyleId = roomStyleId;
    }

    public long getPropId() {
        return propId;
    }

    public void setPropId(long propId) {
        this.propId = propId;
    }

}
