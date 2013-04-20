
package com.skymobi.cac.maopao.xip.bto.task.challenge;

public enum PlayerChallengeTaskStatusEnum {

    UNACTIVE(-1, "任务未激活"),
    ACCEPTABLE(0, "可接取"),
    ACCEPTED(1, "已接取"),
    FINISHED(2, "已完成"),
    OVERTIME(3, "超时未完成"),
    CLOSED(4, "已关闭");// 表示有人完成该挑战任务，任务关闭

    private int status;
    private String statusDesc;

    private PlayerChallengeTaskStatusEnum(int status, String statusDesc) {
        this.status = status;
        this.statusDesc = statusDesc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

}
