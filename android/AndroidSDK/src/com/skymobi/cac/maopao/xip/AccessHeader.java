/*******************************************************************************
 * CopyRight (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename:  XipInnerHeader.java
 * Creator:   Qi Wang
 * Version:   1.0
 * Date: 	 2010-4-1 涓嫔崃12:31:43
 * Description: 
 *******************************************************************************/
package com.skymobi.cac.maopao.xip;

import android.os.Parcel;
import android.os.Parcelable;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;

//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           srcmodule(2)        |        terminalModule(2)      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           length(2)           |        flags(2)               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           msgCode(2)          |    nResult(1) | application...
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|application data field(...)                                    |
//|                                                               |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
public class AccessHeader implements Parcelable{

	@ByteField(index = 0, bytes = 2)
	private int srcModule;

	@ByteField(index = 1, bytes = 2)
	private int dstModule;

	@ByteField(index = 2)
	private short length;

	@ByteField(index = 3)
	private short flags = 0x100;

	@ByteField(index = 4, bytes = 2)
	private int msgCode;

	
	
	public int getSrcModule() {
		return srcModule;
	}

	public void setSrcModule(int srcModule) {
		this.srcModule = srcModule;
	}

	public int getDstModule() {
		return dstModule;
	}

	public void setDstModule(int dstModule) {
		this.dstModule = dstModule;
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public short getFlags() {
		return flags;
	}

	public void setFlags(short flags) {
		this.flags = flags;
	}

	public int getMsgCode() {
		return msgCode;
	}
	
	public String getMsgCodeStr(){
		return msgCode + "/0x" + Integer.toHexString(msgCode);
	}

	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}

	public void exchangeSrcDest() {
		int tmp = this.dstModule;
		this.dstModule = this.srcModule;
		this.srcModule = tmp;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<AccessHeader> CREATOR = new Parcelable.Creator<AccessHeader>() {
		public AccessHeader createFromParcel(Parcel in) {
			return new AccessHeader(in);
		}

		public AccessHeader[] newArray(int size) {
			return new AccessHeader[size];
		}
	};
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeInt(this.srcModule);
		arg0.writeInt(this.dstModule);
		arg0.writeInt(this.length);
		arg0.writeInt(this.flags);
		arg0.writeInt(this.msgCode);
		
	}
	
	private AccessHeader(Parcel in) {
		readFromParcel(in);
	}
	
	public AccessHeader(){
		
	}

	public void readFromParcel(Parcel in) {
		this.srcModule = in.readInt();
		this.dstModule = in.readInt();
		this.length = (short) in.readInt();
		this.flags = (short) in.readInt();
		this.msgCode = in.readInt();
	}
	
}
