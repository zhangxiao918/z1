
package com.skymobi.cac.maopao.xip.bto.task.challenge;

import android.os.Parcel;
import android.os.Parcelable;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

/*
 * 玩家挑战任务信息
 */
public class PlayerChallengeTaskInfo implements ByteBean, Parcelable {

    @ByteField(index = 0, bytes = 4, description = "挑战任务ID")
    private long challengeTaskId;

    @ByteField(index = 1, bytes = 2, description = "游戏ID")
    private int gameId;

    @ByteField(index = 2, bytes = 4, description = "房间底注")
    private long roomBase;

    @ByteField(index = 3, bytes = 4, description = "任务内容长度")
    private int taskDesclen;

    @ByteField(index = 4, length = 3, description = "任务内容")
    private String taskDesc;

    @ByteField(index = 5, bytes = 4, description = "任务提示长度")
    private int taskTipslen;

    @ByteField(index = 6, length = 5, description = "任务提示")
    private String taskTips;

    @ByteField(index = 7, bytes = 1, description = "奖励类型,只对单个挑战任务")
    private int awardType;

    @ByteField(index = 8, bytes = 4, description = "奖励数量")
    private long awardAmount;

    @ByteField(index = 9, description = "玩家任务状态，-1：未触发，0：可接取，1：已接取，2：已完成 ，3：任务已关闭")
    private byte palyerTaskStatus;

    @ByteField(index = 10, bytes = 4, description = "完成条件")
    private long completeCondition;

    @ByteField(index = 11, bytes = 1, description = "挑战任务大奖描述长度")
    private int prizeDescLen;

    @ByteField(index = 12, length = 11, description = "挑战任务大奖描述")
    private String prizeDesc;

    public int getPrizeDescLen() {
        return prizeDescLen;
    }

    public String getPrizeDesc() {
        return prizeDesc;
    }

    public void setPrizeDesc(String prizeDesc) {
        this.prizeDesc = prizeDesc;
        this.prizeDescLen = (null == this.prizeDesc ? 0 : this.prizeDesc.length() * 2);
    }

    public long getChallengeTaskId() {
        return challengeTaskId;
    }

    public void setChallengeTaskId(long challengeTaskId) {
        this.challengeTaskId = challengeTaskId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public long getRoomBase() {
        return roomBase;
    }

    public void setRoomBase(long roomBase) {
        this.roomBase = roomBase;
    }

    public int getTaskDesclen() {
        return taskDesclen;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
        this.taskDesclen = (null == this.taskDesc ? 0 : this.taskDesc.length() * 2);
    }

    public int getTaskTipslen() {
        return taskTipslen;
    }

    public String getTaskTips() {
        return taskTips;
    }

    public void setTaskTips(String taskTips) {
        this.taskTips = taskTips;
        this.taskTipslen = (null == this.taskTips ? 0 : this.taskTips.length() * 2);
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public long getAwardAmount() {
        return awardAmount;
    }

    public void setAwardAmount(long awardAmount) {
        this.awardAmount = awardAmount;
    }

    public int getPalyerTaskStatus() {
        return palyerTaskStatus;
    }

    public void setPalyerTaskStatus(byte palyerTaskStatus) {
        this.palyerTaskStatus = palyerTaskStatus;
    }

    public long getCompleteCondition() {
        return completeCondition;
    }

    public void setCompleteCondition(long completeCondition) {
        this.completeCondition = completeCondition;
    }

    /*
     * (non-Javadoc)
     * @see android.os.Parcelable#describeContents()
     */
    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeLong(challengeTaskId);
        arg0.writeInt(gameId);
        arg0.writeLong(roomBase);
        arg0.writeInt(taskDesclen);
        arg0.writeString(taskDesc);
        arg0.writeInt(taskTipslen);
        arg0.writeString(taskTips);
        arg0.writeInt(awardType);
        arg0.writeLong(awardAmount);
        arg0.writeInt(palyerTaskStatus);
        arg0.writeLong(completeCondition);
        arg0.writeInt(prizeDescLen);
        arg0.writeString(prizeDesc);
    }

    public void readFromParcel(Parcel in) {
        challengeTaskId = in.readByte();
        gameId = in.readInt();
        roomBase = in.readInt();
        taskDesclen = in.readByte();
        taskDesc = in.readString();
        taskTipslen = in.readInt();
        taskTips = in.readString();
        awardType = in.readInt();
        awardAmount = in.readLong();
        palyerTaskStatus = in.readByte();
        completeCondition = in.readLong();
        prizeDescLen = in.readInt();
        prizeDesc = in.readString();
    }
}
