
package com.skymobi.cac.maopao.xip.bto.task.daily;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class PlayerDailyTaskInfo implements ByteBean {

    @ByteField(index = 0, bytes = 4, description = "日常任务ID")
    private long dailyTaskId;

    @ByteField(index = 1, bytes = 2, description = "游戏ID")
    private int gameId;

    @ByteField(index = 2, bytes = 4, description = "房间底注")
    private long roomBase;

    @ByteField(index = 3, bytes = 1, description = "日常任务内容长度")
    private int taskContentLen;

    @ByteField(index = 4, length = 3, description = "日常任务内容")
    private String taskContent;

    @ByteField(index = 5, bytes = 4, description = "当前进度")
    private int currentProgress;

    @ByteField(index = 6, bytes = 4, description = "需要完成进度")
    private int requestProgress;

    @ByteField(index = 7, bytes = 4, description = "任务完成奖励金豆")
    private long rewardGold;

    @ByteField(index = 8, bytes = 4, description = "对应元宝价值")
    private int yuanbaoWorth;

    public long getDailyTaskId() {
        return dailyTaskId;
    }

    public void setDailyTaskId(long dailyTaskId) {
        this.dailyTaskId = dailyTaskId;
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

    public int getTaskContentLen() {
        return taskContentLen;
    }

    public String getTaskContent() {
        return taskContent;
    }

    public void setTaskContent(String taskContent) {
        this.taskContent = taskContent;
        this.taskContentLen = (null == this.taskContent ? 0 : this.taskContent.length() * 2);
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public int getRequestProgress() {
        return requestProgress;
    }

    public void setRequestProgress(int requestProgress) {
        this.requestProgress = requestProgress;
    }

    public long getRewardGold() {
        return rewardGold;
    }

    public void setRewardGold(long rewardGold) {
        this.rewardGold = rewardGold;
    }

    public int getYuanbaoWorth() {
        return yuanbaoWorth;
    }

    public void setYuanbaoWorth(int yuanbaoWorth) {
        this.yuanbaoWorth = yuanbaoWorth;
    }

}
