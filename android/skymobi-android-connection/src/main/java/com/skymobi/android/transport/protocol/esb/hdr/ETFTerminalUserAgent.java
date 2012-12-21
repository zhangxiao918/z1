/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.hdr;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.esb.annotation.EsbField;
import com.skymobi.android.bean.esb.core.TerminalUserAgent;

/* from <ESB鏈嶅姟绔唴閮ㄦā鍧椾氦浜掑崗璁�txt>
 * 缃戠粶绔紶杈撶殑UA缁撴瀯瀹氫箟  
 * 娉ㄦ剰锛氱粓绔拰鎺ュ叆涔嬮棿鐨刄A瀹氫箟鍜岀綉缁滅殑鐣ユ湁宸埆銆�
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
public class ETFTerminalUserAgent {
	
    private static final Logger logger 
		= LoggerFactory.getLogger(ETFTerminalUserAgent.class);

    public final static short TERMINAL_UA_SIZE = 161;
	
	@EsbField(index = 1, fixedLength = 8, charset = "UTF-8")
	private String factoryCode;

	@EsbField(index = 2, fixedLength = 8, charset = "UTF-8")
	private String terminalCode;

	@EsbField(index = 3)
	private int vmv;

	@EsbField(index = 4)
	private int hsv;

	@EsbField(index = 5)
	private short screenSizeWidth;

	@EsbField(index = 6)
	private short screenSizeHeight;

	@EsbField(index = 7)
	private byte terminalFontWidth;

	@EsbField(index = 8)
	private byte terminalFontHeight;

	@EsbField(index = 9)
	private short terminalMemory;

	@EsbField(index = 10)
	private byte terminalTouch;

	@EsbField(index = 11, fixedLength = 16, charset = "UTF-8")
	private String imei;

	@EsbField(index = 12, fixedLength = 16, charset = "UTF-8")
	private String imsi;

	@EsbField(index = 13, fixedLength = 16, charset = "UTF-8")
	private String phone;

	@EsbField(index = 14, fixedLength = 16, charset = "UTF-8")
	private String country;

	@EsbField(index = 15, fixedLength = 32, charset="UTF-8")
	private String province;

	@EsbField(index = 16, fixedLength = 32, charset="UTF-8")
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
    
	public TerminalUserAgent toTerminalUserAgent() {
		TerminalUserAgent ret = new TerminalUserAgent();
		
		try {
			BeanUtils.copyProperties(ret, this);
		} catch (Exception e) {
			logger.error("toTerminalUserAgent:", e);
		}
		
		return	ret;
	}
	
	public static ETFTerminalUserAgent fromTerminalUserAgent(TerminalUserAgent orig) {
		ETFTerminalUserAgent ret = new ETFTerminalUserAgent();
		
		try {
			BeanUtils.copyProperties(ret, orig);
		} catch (Exception e) {
			logger.error("fromTerminalUserAgent:", e);
		}
		
		return	ret;
	}
    
}
