package com.skymobi.cac.maopao.passport.api;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentUser implements java.io.Serializable, Parcelable {
	private static final long serialVersionUID = -595618694418784526L;
	
	private int skyid;

	private String token;

	private String mobile;

	private String loginname;

	private String password;

	private int isAutoLogin;

	private int isRememberPwd;

	private int flag;

	private String memo;
	// 密文
	private byte[] cryptpwd;

	private int pwdlength;

	private String nickname;
	
	private int gender;
	
	private long  logintime;
	
	private long money = 0;
	
	private long achievement = 0;
	
	private int point = 0;	//训练场积分
	
	private boolean newRegUser = false;
	
	private static final char NULL_PWD_CHAR = '×';
	
	public static final String NULL_PWD = "××××××"; //表示空密码
	
	/**
	 * 密码为空或者全部是*号时表示没有明文密码
	 * @param pwd
	 * @return
	 */
	public static final boolean isNoPassword(String pwd){
		if(pwd == null || pwd.length() == 0){
			return true;
		}
		final int length = pwd.length();
		for(int i = 0 ;i<length;i++){
			char a = pwd.charAt(i);
			if(a != NULL_PWD_CHAR){
				return false;
			}
		}
		return true;
	}
	
	public static final String getNullPwd(int length){
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<length;i++){
			sb.append(NULL_PWD_CHAR);
		}
		return sb.toString();
	}
	
	public void setLoginTime(String time){
		try{
		logintime = Long.valueOf(time);
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
	}
	
	public long getLoginTime(){
		return logintime;
	}
	
	public CurrentUser() {
	}

	public int getIsAutoLogin() {
		return isAutoLogin;
	}

	public int getIsRememberPwd() {
		return isRememberPwd;
	}

	public String getLoginname() {
		return loginname;
	}

	public String getMobile() {
		return mobile;
	}

	public String getPassword() {
		return password;
	}

	public int getSkyid() {
		return skyid;
	}

	public String getToken() {
		return token;
	}

	public void setIsAutoLogin(int isAutoLogin) {
		this.isAutoLogin = isAutoLogin;
	}

	public void setIsRememberPwd(int isRememberPwd) {
		this.isRememberPwd = isRememberPwd;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setSkyid(int skyid) {
		this.skyid = skyid;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public byte[] getCryptpwd() {
		return cryptpwd;
	}

	public void setCryptpwd(byte[] cryptpwd) {
		this.cryptpwd = cryptpwd;
	}

	public int getPwdlength() {
		return pwdlength;
	}

	public void setPwdlength(int pwdlength) {
		this.pwdlength = pwdlength;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	@Override
	public String toString() {
		return "skyid:" + skyid + ",toKen:" + token + ",mobile:" + mobile
				+ ",flag:" + flag;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public long getMoney() {
		return money;
	}

	public void setMoney(long money) {
		this.money = money;
	}


	public long getAchievement() {
		return achievement;
	}

	public void setAchievement(long achievement) {
		this.achievement = achievement;
	}
	
	public void setMoneyAndAchievement(long money, long achievement){
		this.money = money;
		this.achievement = achievement;
	}

	public boolean isNewRegUser() {
		return newRegUser;
	}

	public void setNewRegUser(boolean newFastRegUser) {
		this.newRegUser = newFastRegUser;
	}

	public static final Parcelable.Creator<CurrentUser> CREATOR = new Parcelable.Creator<CurrentUser>() {
		public CurrentUser createFromParcel(Parcel in) {
			return new CurrentUser(in);
		}

		public CurrentUser[] newArray(int size) {
			return new CurrentUser[size];
		}
	};
	
	
	private CurrentUser(Parcel in) {
		readFromParcel(in);
	}
	
	public void readFromParcel(Parcel in) {
		skyid = in.readInt();
		token = in.readString();
		mobile = in.readString();
		loginname = in.readString();
		password = in.readString();
		isAutoLogin = in.readInt();
		isRememberPwd = in.readInt();
		flag = in.readInt();
		memo = in.readString();
		// 密文
		in.readByteArray(cryptpwd);
		pwdlength = in.readInt();
		nickname = in.readString();
		gender = in.readInt();
		logintime = in.readLong();
		money = in.readLong();
		achievement = in.readLong();
		newRegUser = in.readByte() == 1;
		point = in.readInt();
	}
	
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(skyid);
		out.writeString(token);
		out.writeString(mobile);
		out.writeString(loginname);
		out.writeString(password);
		out.writeInt(isAutoLogin);
		out.writeInt(isRememberPwd);
		out.writeInt(flag);
		out.writeString(memo);
				// 密文
		out.writeByteArray(cryptpwd);
		out.writeInt(pwdlength);
		out.writeString(nickname);
		out.writeInt(gender);
		out.writeLong(logintime);
		out.writeLong(money);
		out.writeLong(achievement);
		out.writeByte((byte)(newRegUser?1:0));
		out.writeInt(point);
	}
}
