/**
 * 
 */
package com.skymobi.android.bean.mo;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.bytebean.ByteBean;
import com.skymobi.android.bean.bytebean.annotation.ByteField;

/**
 * @author hp
 *
 */
public class SmsMOEx implements ByteBean {
	@ByteField(index = 0, description="")
	private byte subType;
	
	@ByteField(index = 1, description="")
	private byte chargeVer;
	
	@ByteField(index = 2, description="")
	private int moFlag;
	
	@ByteField(index = 3, description="")
	private int appVer;

	/**
	 * @return the subType
	 */
	public byte getSubType() {
		return subType;
	}

	/**
	 * @param subType the subType to set
	 */
	public void setSubType(byte subType) {
		this.subType = subType;
	}

	/**
	 * @return the chargeVer
	 */
	public byte getChargeVer() {
		return chargeVer;
	}

	/**
	 * @param chargeVer the chargeVer to set
	 */
	public void setChargeVer(byte chargeVer) {
		this.chargeVer = chargeVer;
	}

	/**
	 * @return the moFlag
	 */
	public int getMoFlag() {
		return moFlag;
	}

	/**
	 * @param moFlag the moFlag to set
	 */
	public void setMoFlag(int moFlag) {
		this.moFlag = moFlag;
	}

	/**
	 * @return the appVer
	 */
	public int getAppVer() {
		return appVer;
	}

	/**
	 * @param appVer the appVer to set
	 */
	public void setAppVer(int appVer) {
		this.appVer = appVer;
	}
	
    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
