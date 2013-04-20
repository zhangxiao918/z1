package com.skymobi.cac.maopao.xip.bto.game;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class PlayerRoundResultInfo implements ByteBean {
	@ByteField(index = 0, bytes = 4, description = "skyID")
	private long skyId;

	@ByteField(index = 1, bytes = 1, description = "座位ID")
	private int seatId;

	@ByteField(index = 2, bytes = 1, description = "经验等级")
	private int level;

	@ByteField(index = 3)
	private int exp;

	@ByteField(index = 4)
	private int efforExp;

	@ByteField(index = 5)
	private int gap;

	@ByteField(index = 6, bytes = 4, description = "玩家金豆变化量")
	private int deltaMoney;

	@ByteField(index = 7, bytes = 4, description = "虚拟积分（训练场）")
	private int point;

	@ByteField(index = 8, bytes = 1, description = "玩家状态")
	private int status;

	@ByteField(index = 9, description = "赢的局数")
	private int winRound;

	@ByteField(index = 10, description = "输的局数")
	private int loseRound;

	@ByteField(index = 11, description = "逃跑的局数")
	private int escapeRound;

	@ByteField(index = 12, description = "断线次数")
	private int disconnRound;

	@ByteField(index = 13, bytes=4, description = "玩家金豆")
	private long money;
	
	@ByteField(index = 14, bytes=4, description = "玩家成就值变化量")
	private long deltaAchievement;
	
    @ByteField(index = 15, bytes = 4, description = "茶水费")
    private long teaFee;
    
    @ByteField(index = 16, bytes = 1, description = "昵称长度")
    private int nicknameLen;
    
    @ByteField(index = 17, length = 16, description = "昵称")
    private String nickName;
	
	public long getSkyId() {
		return skyId;
	}

	public void setSkyId(long skyId) {
		this.skyId = skyId;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public int getSeatId() {
		return seatId;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getEfforExp() {
		return efforExp;
	}

	public void setEfforExp(int efforExp) {
		this.efforExp = efforExp;
	}

	public int getGap() {
		return gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}
	
	/**
	 * @return the deltaMoney
	 */
	public int getDeltaMoney() {
		return deltaMoney;
	}

	/**
	 * @param deltaMoney the deltaMoney to set
	 */
	public void setDeltaMoney(int deltaMoney) {
		this.deltaMoney = deltaMoney;
	}

	public long getDeltaAchievement() {
		return deltaAchievement;
	}

	public void setDeltaAchievement(long deltaAchievement) {
		this.deltaAchievement = deltaAchievement;
	}
	
    public long getTeaFee() {
        return teaFee;
    }

    public void setTeaFee(long teaFee) {
        this.teaFee = teaFee;
    }
    
    public int getNicknameLen() {
        return nicknameLen;
    }

    public void setNicknameLen(int nicknameLen) {
        this.nicknameLen = nicknameLen;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        this.nicknameLen = (this.nickName == null) ? 0 : (this.nickName.length() * 2);
    }

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
