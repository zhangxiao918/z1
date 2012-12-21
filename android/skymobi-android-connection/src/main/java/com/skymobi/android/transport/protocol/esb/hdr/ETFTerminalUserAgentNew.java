package com.skymobi.android.transport.protocol.esb.hdr;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.esb.annotation.EsbField;


/* from <ESB鏈嶅姟绔唴閮ㄦā鍧椾氦浜掑崗璁�txt>
 * 缃戠粶绔紶杈撶殑UA缁撴瀯瀹氫箟  
 * 娉ㄦ剰锛氱粓绔拰鎺ュ叆涔嬮棿鐨刄A瀹氫箟鍜岀綉缁滅殑鐣ユ湁宸埆銆�
 
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
  
  //鏂板鍔�
  uint32 entrance;
  uint32 appId;
  uint8 platform;
  uint8 screenType;
  uint8 reserved1;
  uint8 reserved2;
  uint16 reserved3;
  uint32 reserved4;
  byte[16] ipAddress
} sanp_net_ua_t;
*/

/**
 * NEW_UA = 0x6;// encrypt(UA+IP)
 * @author bluces.wang@sky-mobi.com
 */
public class ETFTerminalUserAgentNew {
	
    private static final Logger logger 
		= LoggerFactory.getLogger(ETFTerminalUserAgentNew.class);

    public final static short TERMINAL_UA_SIZE = 161 + 34;
	
    @EsbField(index = 1,fixedLength=200)
	private byte[] ua;
	
    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }
	
	public static ETFTerminalUserAgentNew fromTerminalUserAgentNew(TerminalUserAgentNew orig) {
		ETFTerminalUserAgentNew ret = new ETFTerminalUserAgentNew();
		
		try {
			ret.ua = orig.getTerminalUserAgentNew();
		} catch (Exception e) {
			logger.error("fromTerminalUserAgent:", e);
		}
		
		return	ret;
	}

	public TerminalUserAgentNew toTerminalUserAgentNew() {
		TerminalUserAgentNew ret = new TerminalUserAgentNew();
		
		try {
			ret.setTerminalUserAgentNew(this.getUa());
		} catch (Exception e) {
			logger.error("fromTerminalUserAgent:", e);
		}
		
		return	ret;
	}

	public byte[] getUa() {
		return ua;
	}


	public void setUa(byte[] ua) {
		this.ua = ua;
	}
    
}
