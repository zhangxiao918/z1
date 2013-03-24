
package com.skymobi.cac.maopao.xip.bto.basis;

import android.os.Parcel;
import android.os.Parcelable;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

public class PlayerInfo implements ByteBean, Parcelable{
	@ByteField(index = 0, bytes = 4, description = "skyID")
	private long skyId;

	@ByteField(index = 1, description = "昵称长度")
	private byte nicknameLen;

	@ByteField(index = 2, length = 1, description = "昵称")
	private String nickName;

	@ByteField(index = 3, bytes = 1, description = "性别")
	private int gender;

	@ByteField(index = 4, bytes = 2, description = "头像ID")
	private int headBmpId;

	@ByteField(index = 5, bytes = 4, description = "金豆数量")
	private long money;

	@ByteField(index = 6, bytes = 4, description = "成就值")
	private long achievement;
	
	@ByteField(index = 7, bytes = 4, description = "虚拟积分（训练场）")
	private long point;

	@ByteField(index = 8, bytes = 1, description = "经验等级")
	private int level;

	@ByteField(index = 9)
	private int exp;

	@ByteField(index = 10)
	private int efforExp;

	@ByteField(index = 11)
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

	/**是否机器人*/
	public boolean mIsRobot = false;
	
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
		// TODO 临时解决
		this.nickName = nickName;
		this.nicknameLen = (this.nickName == null) ? (byte) 0
				: (byte) (this.nickName.length() * 2);
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getHeadBmpId() {
		return headBmpId;
	}

	public void setHeadBmpId(int headBmpId) {
		this.headBmpId = headBmpId;
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

	public long getAchievement() {
		return achievement;
	}

	public void setAchievement(long achievement) {
		this.achievement = achievement;
	}

	public static final Parcelable.Creator<PlayerInfo> CREATOR = new Parcelable.Creator<PlayerInfo>() {
		public PlayerInfo createFromParcel(Parcel in) {
			return new PlayerInfo(in);
		}

		public PlayerInfo[] newArray(int size) {
			return new PlayerInfo[size];
		}
	};
	
	private PlayerInfo(Parcel in) {
		readFromParcel(in);
	}
	
	public PlayerInfo() {
		this.skyId = 0;
		this.nicknameLen = 0;
		this.nickName = "";
		this.gender = 0;
		this.headBmpId = 0;
		this.money = 0;
		this.achievement = 0;
		this.point = 0;
		this.level = 0;
		this.exp = 0;
		this.efforExp = 0;
		this.gap = 0;
		this.seatId = 0;
		this.status = 0;
		this.winRound = 0;
		this.loseRound = 0;
		this.equalRound = 0;
		this.escapeRound = 0;
		this.disconnRound = 0;
	}

	public void setPlayerInfo(PlayerInfo player) {
		this.skyId = player.getSkyId();
		this.nicknameLen = player.getNicknameLen();
		this.nickName = player.getNickName();
		this.gender = player.getGender();
		this.headBmpId = player.getHeadBmpId();
		this.money = player.getMoney();
		this.achievement = player.getAchievement();
		this.point = player.getPoint();
		this.level = player.getLevel();
		this.exp = player.getExp();
		this.efforExp = player.getEfforExp();
		this.gap = player.getGap();
		this.seatId = player.getSeatId();
		this.status = player.getStatus();
		this.winRound = player.getWinRound();
		this.loseRound = player.getLoseRound();
		this.equalRound = player.getEqualRound();
		this.escapeRound = player.getEscapeRound();
		this.disconnRound =player.getDisconnRound();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	public void readFromParcel(Parcel in) {
		skyId = in.readLong();
		nicknameLen = in.readByte();
		nickName = in.readString();
		gender = in.readInt();
		headBmpId = in.readInt();
		money = in.readLong();
		achievement = in.readLong();
		point = in.readLong();
		level = in.readInt();
		exp = in.readInt();
		efforExp = in.readInt();
		gap = in.readInt();
		seatId = in.readInt();
		status = in.readInt();
		winRound = in.readInt();
		loseRound = in.readInt();
		equalRound = in.readInt();
		escapeRound = in.readInt();
		disconnRound = in.readInt();
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(skyId);
		dest.writeByte(nicknameLen);
		dest.writeString(nickName);
		dest.writeInt(gender);
		dest.writeInt(headBmpId);
		dest.writeLong(money);
		dest.writeLong(achievement);
		dest.writeLong(point);	
		dest.writeInt(level);
		dest.writeInt(exp);
		dest.writeInt(efforExp);
		dest.writeInt(gap);
		dest.writeInt(seatId);
		dest.writeInt(status);
		dest.writeInt(winRound);
		dest.writeInt(loseRound);
		dest.writeInt(equalRound);
		dest.writeInt(escapeRound);
		dest.writeInt(disconnRound);
	}	
}