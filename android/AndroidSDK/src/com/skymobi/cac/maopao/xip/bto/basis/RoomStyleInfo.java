
package com.skymobi.cac.maopao.xip.bto.basis;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class RoomStyleInfo implements ByteBean {

    @ByteField(index = 0, bytes = 4, description = "房间类型号")
    private long roomStyleId;

    @ByteField(index = 1, bytes = 4, description = "底注")
    private long baseMoney;

    @ByteField(index = 2, bytes = 4, description = "加入房间最低金豆")
    private long minJoinMoney;

    @ByteField(index = 3, bytes = 4, description = "加入房间最高金豆，没有限制为-1")
    private long maxJoinMoney;

    @ByteField(index = 4, bytes = 1, description = "房间分区类型")
    private int type;

    @ByteField(index = 5, bytes = 1, description = "游戏模式")
    private int gameMode;

    @ByteField(index = 6, bytes = 1, description = "房间名称长度")
    private int roomNameLen;

    @ByteField(index = 7, length = 6, description = "房间名称")
    private String roomName;

    @ByteField(index = 8, bytes = 4, description = "在线人数")
    private int onlineNum;

    @ByteField(index = 9, bytes = 4, description = "初始倍数")
    private int initMagnification;

    public int getRoomNameLen() {
        return roomNameLen;
    }

    public void setRoomNameLen(int roomNameLen) {
        this.roomNameLen = roomNameLen;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
        this.roomNameLen = (roomName == null) ? 0 : (roomName.length() * 2);
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public long getBaseMoney() {
        return baseMoney;
    }

    public void setBaseMoney(long baseMoney) {
        this.baseMoney = baseMoney;
    }

    public long getMinJoinMoney() {
        return minJoinMoney;
    }

    public void setMinJoinMoney(long minJoinMoney) {
        this.minJoinMoney = minJoinMoney;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public long getRoomStyleId() {
        return roomStyleId;
    }

    public void setRoomStyleId(long roomStyleId) {
        this.roomStyleId = roomStyleId;
    }

    public long getMaxJoinMoney() {
        return maxJoinMoney;
    }

    public void setMaxJoinMoney(long maxJoinMoney) {
        this.maxJoinMoney = maxJoinMoney;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(int onlineNum) {
        this.onlineNum = onlineNum;
    }

    /**
     * @return the initMagnification
     */
    public int getInitMagnification() {
        return initMagnification;
    }

    /**
     * @param initMagnification the initMagnification to set
     */
    public void setInitMagnification(int initMagnification) {
        this.initMagnification = initMagnification;
    }

}
