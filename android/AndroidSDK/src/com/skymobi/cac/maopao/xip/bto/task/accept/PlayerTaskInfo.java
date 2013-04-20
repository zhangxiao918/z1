
package com.skymobi.cac.maopao.xip.bto.task.accept;

import com.skymobi.bean.bytebean.ByteBean;
import com.skymobi.bean.bytebean.annotation.ByteField;

/**
 * 玩家已接任务信息
 */
public class PlayerTaskInfo implements ByteBean {

    @ByteField(index = 0, bytes = 4, description = "任务内容长度")
    private int taskDescLen;

    @ByteField(index = 1, length = "taskDescLen", description = "任务内容")
    private String taskDesc;

    @ByteField(index = 2, bytes = 4, description = "当前进度")
    private int currentProgress;

    @ByteField(index = 3, bytes = 4, description = "需要完成进度")
    private int requestProgress;

    @ByteField(index = 4, bytes = 4, description = "任务奖励长度")
    private int taskRewardLen;

    @ByteField(index = 5, length = "taskRewardLen", description = "任务奖励")
    private String taskReward;

    public int getTaskDescLen() {
        return taskDescLen;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
        this.taskDescLen = (null == this.taskDesc ? 0 : this.taskDesc.length() * 2);
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

    public String getTaskReward() {
        return taskReward;
    }

    public void setTaskReward(String taskReward) {
        this.taskReward = taskReward;
        this.taskRewardLen = (null == this.taskReward ? 0 : this.taskReward.length() * 2);
    }

    public int getTaskRewardLen() {
        return taskRewardLen;
    }

}
