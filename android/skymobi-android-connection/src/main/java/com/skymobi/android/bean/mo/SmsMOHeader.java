/**
 * 
 */
package com.skymobi.android.bean.mo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.bytebean.ByteBean;
import com.skymobi.android.bean.bytebean.annotation.ByteField;
import com.skymobi.android.util.ByteUtils;


/**
 * @author hp
 *
 */
public class SmsMOHeader implements ByteBean {
	
	@ByteField(index = 0, description="")
	private byte protocol = (byte)0x1b;
	
	@ByteField(index = 1, description="")
	private byte facLen;
	
	@ByteField(index = 2, charset="UTF-8", length="facLen", description="")
	private String manufactory;
	
	@ByteField(index = 3, description="鏈哄瀷瀛愪覆闀垮害锛岃寖鍥�锝�")
	private byte modelLen;
	
	@ByteField(index = 4, charset="UTF-8", length="modelLen", description="")
	private String handsetType;
	
	@SuppressWarnings("unused")
	@ByteField(index = 5, fixedLength = 8, description="")
	private	byte[]	imei;
	
	@SuppressWarnings("unused")
	@ByteField(index = 6, fixedLength = 8, description="")
	private	byte[]	imsi;
	
	@ByteField(index = 7, description="")
	private	int		appid;
	
	@ByteField(index = 8, description="")
	private	int		vmver;
	
	@ByteField(index = 9, description="")
	private	int		hsver;
	
	@ByteField(index = 10, description="")
	private	byte	msgnum;
	
	@ByteField(index = 11, description="")
	private	byte	msgidx;
	
	@ByteField(index = 12, description="")
	private	short	chargeModule = 0x0006;

	/**
	 * @return the protocol
	 */
	public byte getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(byte protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the facLen
	 */
	public byte getFacLen() {
		return facLen;
	}

	/**
	 * @return the manufactory
	 */
	public String getManufactory() {
		return manufactory;
	}

	/**
	 * @param manufactory the manufactory to set
	 */
	public void setManufactory(String manufactory) {
		this.manufactory = manufactory;
		this.facLen = (byte)this.manufactory.length();
	}

	/**
	 * @return the modelLen
	 */
	public byte getModelLen() {
		return modelLen;
	}

	/**
	 * @return the handsetType
	 */
	public String getHandsetType() {
		return handsetType;
	}

	/**
	 * @param handsetType the handsetType to set
	 */
	public void setHandsetType(String handsetType) {
		this.handsetType = handsetType;
		this.modelLen = (byte)this.handsetType.length();
	}

	/**
	 * @return the imei
	 */
//	public String getImei() {
//		return imei;
//	}

	/**
	 * @param imei the imei to set
	 */
	public void setImei(String imei) {
		if ( (imei.length() % 2) != 0 ) {
			imei += "0";
		}
		
		this.imei = ByteUtils.string2BCD(imei);
	}

	/**
	 * @return the imsi
	 */
//	public String getImsi() {
//		return imsi;
//	}

	/**
	 * @param imsi the imsi to set
	 */
	public void setImsi(String imsi) {
		if ( (imsi.length() % 2) != 0 ) {
			imsi += "0";
		}
		this.imsi = ByteUtils.string2BCD(imsi);
	}

	/**
	 * @return the appid
	 */
	public int getAppid() {
		return appid;
	}

	/**
	 * @param appid the appid to set
	 */
	public void setAppid(int appid) {
		this.appid = appid;
	}

	/**
	 * @return the vmver
	 */
	public int getVmver() {
		return vmver;
	}

	/**
	 * @param vmver the vmver to set
	 */
	public void setVmver(int vmver) {
		this.vmver = vmver;
	}

	/**
	 * @return the hsver
	 */
	public int getHsver() {
		return hsver;
	}

	/**
	 * @param hsver the hsver to set
	 */
	public void setHsver(int hsver) {
		this.hsver = hsver;
	}

	/**
	 * @return the msgnum
	 */
	public byte getMsgnum() {
		return msgnum;
	}

	/**
	 * @param msgnum the msgnum to set
	 */
	public void setMsgnum(byte msgnum) {
		this.msgnum = msgnum;
	}

	/**
	 * @return the msgidx
	 */
	public byte getMsgidx() {
		return msgidx;
	}

	/**
	 * @param msgidx the msgidx to set
	 */
	public void setMsgidx(byte msgidx) {
		this.msgidx = msgidx;
	}

	/**
	 * @return the chargeModule
	 */
	public short getChargeModule() {
		return chargeModule;
	}

	/**
	 * @param chargeModule the chargeModule to set
	 */
	public void setChargeModule(short chargeModule) {
		this.chargeModule = chargeModule;
	}
	
    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
