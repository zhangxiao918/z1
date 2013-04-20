package com.skymobi.cac.maopao.xip.bto.basis;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import android.os.Parcel;
import android.os.Parcelable;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;


public class SkillInfo implements ByteBean{

    @ByteField(index = 0, bytes = 1, description = "技能ID")
    private int skillId;

    @ByteField(index = 1, bytes = 1, description = "总数量")
    private int totalNum;

    @ByteField(index = 2, bytes = 1, description = "可用数量")
    private int availableNum;

	@Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getAvailableNum() {
        return availableNum;
    }

    public void setAvailableNum(int availableNum) {
        this.availableNum = availableNum;
    }
//    
//    public static final Parcelable.Creator<SkillInfo> CREATOR = new Parcelable.Creator<SkillInfo>() {
//		public SkillInfo createFromParcel(Parcel in) {
//			return new SkillInfo(in);
//		}
//
//		public SkillInfo[] newArray(int size) {
//			return new SkillInfo[size];
//		}
//	};
//	
//	public SkillInfo(Parcel in) {
//		readFromParcel(in);
//	}
//	 
//    private void readFromParcel(Parcel in){
//    	this.skillId = in.readInt();
//    	this.totalNum = in.readInt();
//    	this.availableNum = in.readInt();
//    }
//	
//
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeInt(skillId);
//		dest.writeInt(totalNum);
//		dest.writeInt(availableNum);
//	}
}
