/**
 * 
 */
package com.skymobi.android.bean.mo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.bytebean.ByteBean;
import com.skymobi.android.bean.bytebean.annotation.ByteField;

/**
 * @author hp
 *
 */
public class SmsMOMessage implements ByteBean {
	
	@ByteField(index = 0, description="")
	private	SmsMOHeader	header = new SmsMOHeader();
	
	@ByteField(index = 1, description="")
	private	SmsMOEx		body = new SmsMOEx();

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOEx#getAppVer()
	 */
	public int getAppVer() {
		return body.getAppVer();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOEx#getChargeVer()
	 */
	public byte getChargeVer() {
		return body.getChargeVer();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOEx#getMoFlag()
	 */
	public int getMoFlag() {
		return body.getMoFlag();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOEx#getSubType()
	 */
	public byte getSubType() {
		return body.getSubType();
	}

	/**
	 * @param appVer
	 * @see stc.skymobi.bean.mo.SmsMOEx#setAppVer(int)
	 */
	public void setAppVer(int appVer) {
		body.setAppVer(appVer);
	}

	/**
	 * @param chargeVer
	 * @see stc.skymobi.bean.mo.SmsMOEx#setChargeVer(byte)
	 */
	public void setChargeVer(byte chargeVer) {
		body.setChargeVer(chargeVer);
	}

	/**
	 * @param moFlag
	 * @see stc.skymobi.bean.mo.SmsMOEx#setMoFlag(int)
	 */
	public void setMoFlag(int moFlag) {
		body.setMoFlag(moFlag);
	}

	/**
	 * @param subType
	 * @see stc.skymobi.bean.mo.SmsMOEx#setSubType(byte)
	 */
	public void setSubType(byte subType) {
		body.setSubType(subType);
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOHeader#getAppid()
	 */
	public int getAppid() {
		return header.getAppid();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOHeader#getChargeModule()
	 */
	public short getChargeModule() {
		return header.getChargeModule();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOHeader#getHandsetType()
	 */
	public String getHandsetType() {
		return header.getHandsetType();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOHeader#getHsver()
	 */
	public int getHsver() {
		return header.getHsver();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOHeader#getManufactory()
	 */
	public String getManufactory() {
		return header.getManufactory();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOHeader#getMsgidx()
	 */
	public byte getMsgidx() {
		return header.getMsgidx();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOHeader#getMsgnum()
	 */
	public byte getMsgnum() {
		return header.getMsgnum();
	}

	/**
	 * @return
	 * @see stc.skymobi.bean.mo.SmsMOHeader#getVmver()
	 */
	public int getVmver() {
		return header.getVmver();
	}

	/**
	 * @param appid
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setAppid(int)
	 */
	public void setAppid(int appid) {
		header.setAppid(appid);
	}

	/**
	 * @param chargeModule
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setChargeModule(short)
	 */
	public void setChargeModule(short chargeModule) {
		header.setChargeModule(chargeModule);
	}

	/**
	 * @param handsetType
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setHandsetType(java.lang.String)
	 */
	public void setHandsetType(String handsetType) {
		header.setHandsetType(handsetType);
	}

	/**
	 * @param hsver
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setHsver(int)
	 */
	public void setHsver(int hsver) {
		header.setHsver(hsver);
	}

	/**
	 * @param imei
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setImei(java.lang.String)
	 */
	public void setImei(String imei) {
		header.setImei(imei);
	}

	/**
	 * @param imsi
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setImsi(java.lang.String)
	 */
	public void setImsi(String imsi) {
		header.setImsi(imsi);
	}

	/**
	 * @param manufactory
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setManufactory(java.lang.String)
	 */
	public void setManufactory(String manufactory) {
		header.setManufactory(manufactory);
	}

	/**
	 * @param msgidx
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setMsgidx(byte)
	 */
	public void setMsgidx(byte msgidx) {
		header.setMsgidx(msgidx);
	}

	/**
	 * @param msgnum
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setMsgnum(byte)
	 */
	public void setMsgnum(byte msgnum) {
		header.setMsgnum(msgnum);
	}

	/**
	 * @param vmver
	 * @see stc.skymobi.bean.mo.SmsMOHeader#setVmver(int)
	 */
	public void setVmver(int vmver) {
		header.setVmver(vmver);
	}
	
    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }
    
    //	for test
    private static byte[] str2Bcd2(String str) {
		int j = 0;
		int len = str.length();
		byte[] out;
		int i;
		if (str.length() % 2 != 0) {
			out = new byte[str.length() / 2 + 1];
			out[(len >> 1)] = (byte) ((str.charAt(len - 1) - '0' << 4) + 15);
			--len;
			for (i = 0; i < len; i += 2) {
				out[j] = (byte) ((str.charAt(i) - '0' << 4) + str.charAt(i + 1) - '0');
				++j;
			}
		} else {
			out = new byte[str.length() / 2];
			for (i = 0; i < len; i += 2) {
				out[j] = (byte) ((str.charAt(i) - '0' << 4) + str.charAt(i + 1) - '0');
				++j;
			}
		}
		return out;
	}

	private static byte[] getEncodedSmsInfo(String hsman, String hstype,
			String imei, String imsi, int appid, int vmVer, int hsVer,
			int messageNum, int messageIndex, int chargeModule, int subType,
			int chargeVer, int moFlag, int appVer) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream byteArray = new DataOutputStream(out);
		byteArray.write(27);
		byteArray.write(hsman.length());
		byteArray.write(hsman.getBytes());
		byteArray.write(hstype.length());
		byteArray.write(hstype.getBytes());
		if (imei == null)
			imei = "               ";
		else if (imei.length() < 15) {
			while (imei.length() < 15) {
				imei = imei + " ";
			}
		}
		byteArray.write(str2Bcd2(imei));
		if (imsi == null)
			imsi = "               ";
		else if (imsi.length() < 15) {
			while (imsi.length() < 15) {
				imsi = imsi + " ";
			}
		}
		byteArray.write(str2Bcd2(imsi));
		byteArray.writeInt(appid);
		byteArray.writeInt(vmVer);
		byteArray.writeInt(hsVer);
		byteArray.write(messageNum);
		byteArray.write(messageIndex);
		byteArray.writeShort(chargeModule);
		byteArray.write(subType);
		byteArray.write(chargeVer);
		byteArray.writeInt(moFlag);
		byteArray.writeInt(appVer);
		while (out.toByteArray().length < 79) {
			byteArray.write(0);
		}
		return out.toByteArray();
	}
    
	public static void main(String argv[]) throws Exception {}
}
