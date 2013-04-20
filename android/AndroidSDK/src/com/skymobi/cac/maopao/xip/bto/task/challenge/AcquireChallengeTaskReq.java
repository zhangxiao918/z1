
package com.skymobi.cac.maopao.xip.bto.task.challenge;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.xip.BasisMessageConstants;
import com.skymobi.cac.maopao.xip.XipBody;
import com.skymobi.cac.maopao.xip.annotation.XipSignal;

@XipSignal(messageCode = BasisMessageConstants.MSGCODE_CAC_ACQUIRE_CHALLENGE_TASK_REQ)
public class AcquireChallengeTaskReq extends XipBody {

    @ByteField(index = 0, bytes = 4, description = "挑战任务ID")
    private long challengeTaskId;

    @ByteField(index = 1, bytes = 2, description = "游戏ID")
    private int gameId;

    @ByteField(index = 2, bytes = 4, description = "任务内容长度")
    private int taskDescLen;

    @ByteField(index = 3, length = 2, description = "任务内容")
    private String taskDesc;

    @ByteField(index = 4, bytes = 4, description = "房间底注")
    private long roomBase;

    @ByteField(index = 5, bytes = 4, description = "完成条件")
    private long completeCondition;

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

    public long getRoomBase() {
        return roomBase;
    }

    public void setRoomBase(long roomBase) {
        this.roomBase = roomBase;
    }

    public long getCompleteCondition() {
        return completeCondition;
    }

    public void setCompleteCondition(long completeCondition) {
        this.completeCondition = completeCondition;
    }

}
