package com.skymobi.cac.maopao.xip.bto.basis;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;


public class PlayerInfo implements ByteBean {

    @ByteField(index = 0, bytes = 4, description = "skyID")
    private long skyId;

    @ByteField(index = 1, description = "昵称长度")
    private byte nicknameLen;

    @ByteField(index = 2, length = 1, description = "昵称")
    private String nickName;

    @ByteField(index = 3, bytes = 1, description = "性别")
    private int gender;

    @ByteField(index = 4, bytes = 2, description = "头像ID")
    private int portraitId;

    @ByteField(index = 5, bytes = 4, description = "金豆数量")
    private long money;

    @ByteField(index = 6, bytes = 4, description = "成就值")
    private long achievement;

    @ByteField(index = 7, bytes = 4, description = "虚拟积分（训练场）")
    private long point;

    @ByteField(index = 8, bytes = 1, description = "经验等级")
    private int level;

    @ByteField(index = 9, bytes = 4, description = "玩家的经验值")
    private int exp;

    @ByteField(index = 10, bytes = 4, description = "这一级已有的经验值")
    private int effortExp;

    @ByteField(index = 11, bytes = 4, description = "升级到下一级需要的经验值")
    private int gap;

    @ByteField(index = 12, bytes = 1, description = "座位ID")
    private int seatId;

    @ByteField(index = 13, bytes = 1, description = "玩家状态")
    private int status;

    @ByteField(index = 14, description = "赢的局数")
    private int winRound;

    @ByteField(index = 15, description = "输的局数")
    private int loseRound;

    @ByteField(index = 16, description = "平的局数")
    private int equalRound;

    @ByteField(index = 17, description = "逃跑的局数")
    private int escapeRound;

    @ByteField(index = 18, description = "断线次数")
    private int disconnRound;

    @ByteField(index = 19, bytes = 1, description = "宠物数量")
    private int petNum;

    @ByteField(index = 20, length = 19, description = "宠物")
    private PetInfo[] pets;

    public long getSkyId() {
        return skyId;
    }

    public void setSkyId(long skyId) {
        this.skyId = skyId;
    }

    public byte getNicknameLen() {
        return nicknameLen;
    }

    public void setNicknameLen(byte nicknameLen) {
        this.nicknameLen = nicknameLen;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.nicknameLen = (this.nickName == null) ? (byte) 0 : (byte) (this.nickName.length() * 2);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getPortraitId() {
        return portraitId;
    }

    public void setPortraitId(int portraitId) {
        this.portraitId = portraitId;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWinRound() {
        return winRound;
    }

    public void setWinRound(int winRound) {
        this.winRound = winRound;
    }

    public int getLoseRound() {
        return loseRound;
    }

    public void setLoseRound(int loseRound) {
        this.loseRound = loseRound;
    }

    public int getEqualRound() {
        return equalRound;
    }

    public void setEqualRound(int equalRound) {
        this.equalRound = equalRound;
    }

    public int getEscapeRound() {
        return escapeRound;
    }

    public void setEscapeRound(int escapeRound) {
        this.escapeRound = escapeRound;
    }

    public int getDisconnRound() {
        return disconnRound;
    }

    public void setDisconnRound(int disconnRound) {
        this.disconnRound = disconnRound;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getEffortExp() {
        return effortExp;
    }

    public void setEffortExp(int efforExp) {
        this.effortExp = efforExp;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public long getAchievement() {
        return achievement;
    }

    public void setAchievement(long achievement) {
        this.achievement = achievement;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public PetInfo[] getPets() {
        return pets;
    }

    public void setPets(PetInfo[] pets) {
        this.pets = pets;
        this.petNum = (null == pets) ? 0 : pets.length;
    }
}
