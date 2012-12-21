/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.hdr;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.esb.annotation.EsbField;
import com.skymobi.android.transport.protocol.esb.hdr.a2m.SanpTerminalMsg;

//鏈嶅姟绔�Access-->鏈嶅姟绔�Module 灏佸寘鍗忚
//0               1               2               3                
//0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7  
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  >-------------+
//|           srcmodule(2)        |            dstmodule(2)       |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//|      protocol code(2)         |            seqNum(4)       	  |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//                          |            flags(4)               |                 \
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                  >--> FixedHeader
//|                               |            length(4)          |                 /
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//|                               |            result(2)          |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//|                          reserved(4)                          |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  >-------------+
//|                          skyId(4)                             |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                          accessId(4)                          |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                          ai(4)                                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|application data field(tlv...)                                 |
//|                                                               |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

/**
 * @author Marvin.Ma
 *
 */
public class ESBAccess2ModuleHeader {
	
    @EsbField(index = 1)
    private ETFTerminalAccessInfo	terminalAccessInfo;

//	#define SANP_NET_TLV_TYPE_UA              0x1    /* 缁堢鐨勫弬鏁�*/
//	#define SANP_NET_TLV_TYPE_ACCESS_COOKIE   0x2    /* 缁堢鎺ュ叆鐨刢ookie 淇℃伅锛�濡侷P鍦板潃绛�*/
//	#define SANP_NET_TLV_TYPE_TERMINAL_MSG    0x3    /* aka. UC,  msg from terminal encapsulated. */
//	#define SANP_NET_TLV_TYPE_SESSION_LIST    0x4    /* session鐨勫垪琛�*/
    
    @EsbField(tag = 1, index = 1000)
    private	ETFTerminalUserAgent	terminalUserAgent;
    
    @EsbField(tag = 2, index = 1001)
    private	ETFTerminalCookie		terminalCookie;
    
    @EsbField(tag = 3, index = 1002)
    private	SanpTerminalMsg			terminalMsg;
    
    @EsbField(tag = 6, index = 1003)
    private	ETFTerminalUserAgentNew	terminalUserAgentNew;
    
    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public	byte[]	getTerminalMsgBody() {
    	if ( null != terminalMsg) {
    		return	terminalMsg.getMsgBody();
    	}
    	else {
    		return	null;
    	}
    }

    public	ETFTerminalMessageHeader	getETFTerminalMessageHeader() {
    	if ( null != terminalMsg) {
    		return	terminalMsg.getETFTerminalMessageHeader();
    	}
    	else {
    		return	null;
    	}
    }
    
    public	ETFTerminalUserAgent	getETFTerminalUserAgent() {
    	return	terminalUserAgent;
    }
    
    public	ETFTerminalUserAgentNew	getETFTerminalUserAgentNew() {
    	return	terminalUserAgentNew;
    }
    
    public	ETFTerminalCookie	getETFTerminalCookie() {
    	return	terminalCookie;
    }
    
	/**
	 * @return the terminalAccessInfo
	 */
	public ETFTerminalAccessInfo getETFTerminalAccessInfo() {
		return terminalAccessInfo;
	}
	
	/**
	 * @param terminalAccessInfo the terminalAccessInfo to set
	 */
	public void setETFTerminalAccessInfo(ETFTerminalAccessInfo terminalAccessInfo) {
		this.terminalAccessInfo = terminalAccessInfo;
	}

    public	void setETFTerminalUserAgent(ETFTerminalUserAgent terminalUserAgent) {
    	this.terminalUserAgent = terminalUserAgent;
    }
    
    public	void setETFTerminalCookie(ETFTerminalCookie terminalCookie) {
    	this.terminalCookie = terminalCookie;
    }

    public	void setTerminalMsg(SanpTerminalMsg terminalMsg) {
    	this.terminalMsg = terminalMsg;
    }


    public	void setETFTerminalUserAgentNew(ETFTerminalUserAgentNew terminalUserAgentNew) {
    	this.terminalUserAgentNew = terminalUserAgentNew;
    }
    
}
