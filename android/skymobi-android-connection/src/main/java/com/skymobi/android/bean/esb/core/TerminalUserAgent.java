/**
 * 
 */
package com.skymobi.android.bean.esb.core;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/* from
 */
/*
typdef struct {
  unsigned char factoryCode[8];
  unsigned char terminalCode[8];

  uint32   VMV;
  uint32   HSV;
  uint16   screenSize_w, screenSize_h;
  uint8    terminalFont_w, terminalFont_h;
  uint16   terminalMemory;
  uint8   terminalTouch;
  unsigned char   IMEI[16];
  unsigned char   IMSI[16];
  unsigned char   phoneNum[16];
  unsigned char   phoneCountry[16];
  unsigned char   phoneProvince[32];
  unsigned char   phoneArea[32];
} sanp_net_ua_t;
*/

/**
 * @author Marvin.Ma
 *
 */
public class TerminalUserAgent implements Cloneable {
	
	private String factoryCode;

	private String terminalCode;

	private int vmv;

	private int hsv;

	private short screenSizeWidth;

	private short screenSizeHeight;

	private byte terminalFontWidth;

	private byte terminalFontHeight;

	private short terminalMemory;

	private byte terminalTouch;

	private String imei;

	private String imsi;

	private String phone;

	private String country;

	private String province;

	private String area;

    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

	/**
	 * @return the factoryCode
	 */
	public String getFactoryCode() {
		return factoryCode;
	}

	/**
	 * @return the terminalCode
	 */
	public String getTerminalCode() {
		return terminalCode;
	}

	/**
	 * @return the vmv
	 */
	public int getVmv() {
		return vmv;
	}

	/**
	 * @return the hsv
	 */
	public int getHsv() {
		return hsv;
	}

	/**
	 * @return the screenSizeWidth
	 */
	public short getScreenSizeWidth() {
		return screenSizeWidth;
	}

	/**
	 * @return the screenSizeHeight
	 */
	public short getScreenSizeHeight() {
		return screenSizeHeight;
	}

	/**
	 * @return the terminalFontWidth
	 */
	public byte getTerminalFontWidth() {
		return terminalFontWidth;
	}

	/**
	 * @return the terminalFontHeight
	 */
	public byte getTerminalFontHeight() {
		return terminalFontHeight;
	}

	/**
	 * @return the terminalMemory
	 */
	public short getTerminalMemory() {
		return terminalMemory;
	}

	/**
	 * @return the terminalTouch
	 */
	public byte getTerminalTouch() {
		return terminalTouch;
	}

	/**
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * @return the imsi
	 */
	public String getImsi() {
		return imsi;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param factoryCode the factoryCode to set
	 */
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	/**
	 * @param terminalCode the terminalCode to set
	 */
	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}

	/**
	 * @param vmv the vmv to set
	 */
	public void setVmv(int vmv) {
		this.vmv = vmv;
	}

	/**
	 * @param hsv the hsv to set
	 */
	public void setHsv(int hsv) {
		this.hsv = hsv;
	}

	/**
	 * @param screenSizeWidth the screenSizeWidth to set
	 */
	public void setScreenSizeWidth(short screenSizeWidth) {
		this.screenSizeWidth = screenSizeWidth;
	}

	/**
	 * @param screenSizeHeight the screenSizeHeight to set
	 */
	public void setScreenSizeHeight(short screenSizeHeight) {
		this.screenSizeHeight = screenSizeHeight;
	}

	/**
	 * @param terminalFontWidth the terminalFontWidth to set
	 */
	public void setTerminalFontWidth(byte terminalFontWidth) {
		this.terminalFontWidth = terminalFontWidth;
	}

	/**
	 * @param terminalFontHeight the terminalFontHeight to set
	 */
	public void setTerminalFontHeight(byte terminalFontHeight) {
		this.terminalFontHeight = terminalFontHeight;
	}

	/**
	 * @param terminalMemory the terminalMemory to set
	 */
	public void setTerminalMemory(short terminalMemory) {
		this.terminalMemory = terminalMemory;
	}

	/**
	 * @param terminalTouch the terminalTouch to set
	 */
	public void setTerminalTouch(byte terminalTouch) {
		this.terminalTouch = terminalTouch;
	}

	/**
	 * @param imei the imei to set
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/**
	 * @param imsi the imsi to set
	 */
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((factoryCode == null) ? 0 : factoryCode.hashCode());
		result = prime * result + hsv;
		result = prime * result + ((imei == null) ? 0 : imei.hashCode());
		result = prime * result + ((imsi == null) ? 0 : imsi.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result
				+ ((province == null) ? 0 : province.hashCode());
		result = prime * result + screenSizeHeight;
		result = prime * result + screenSizeWidth;
		result = prime * result
				+ ((terminalCode == null) ? 0 : terminalCode.hashCode());
		result = prime * result + terminalFontHeight;
		result = prime * result + terminalFontWidth;
		result = prime * result + terminalMemory;
		result = prime * result + terminalTouch;
		result = prime * result + vmv;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TerminalUserAgent other = (TerminalUserAgent) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (factoryCode == null) {
			if (other.factoryCode != null)
				return false;
		} else if (!factoryCode.equals(other.factoryCode))
			return false;
		if (hsv != other.hsv)
			return false;
		if (imei == null) {
			if (other.imei != null)
				return false;
		} else if (!imei.equals(other.imei))
			return false;
		if (imsi == null) {
			if (other.imsi != null)
				return false;
		} else if (!imsi.equals(other.imsi))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (screenSizeHeight != other.screenSizeHeight)
			return false;
		if (screenSizeWidth != other.screenSizeWidth)
			return false;
		if (terminalCode == null) {
			if (other.terminalCode != null)
				return false;
		} else if (!terminalCode.equals(other.terminalCode))
			return false;
		if (terminalFontHeight != other.terminalFontHeight)
			return false;
		if (terminalFontWidth != other.terminalFontWidth)
			return false;
		if (terminalMemory != other.terminalMemory)
			return false;
		if (terminalTouch != other.terminalTouch)
			return false;
		if (vmv != other.vmv)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TerminalUserAgent clone() throws CloneNotSupportedException {
		return (TerminalUserAgent)super.clone();
	}
    
}
