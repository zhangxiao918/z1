/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.hdr;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.esb.annotation.EsbField;

//鏈嶅姟绔�Module-->鏈嶅姟绔�Access 灏佸寘鍗忚
//0               1               2               3                
//0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7  
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  >-------------+
//|           srcmodule(2)        |            dstmodule(2)       |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//|      protocol code(2)         |            seqNum(4)       	  |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//|                               |            flags(4)           |                 \
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

/**
 * @author Marvin.Ma
 *
 */
public class ESBModule2AccessHeader {
	
    @EsbField(index = 1)
	private	ETFTerminalAccessInfo		terminalAccessInfo;
    
    @EsbField(index = 2)
	private	ETFTerminalMessageHeader	terminalMessageHeader;
    
    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

	/**
	 * @return the terminalAccessInfo
	 */
	public ETFTerminalAccessInfo getETFTerminalAccessInfo() {
		return terminalAccessInfo;
	}

	/**
	 * @return the terminalMessageHeader
	 */
	public ETFTerminalMessageHeader getETFTerminalMessageHeader() {
		return terminalMessageHeader;
	}

	/**
	 * @param terminalAccessInfo the terminalAccessInfo to set
	 */
	public void setETFTerminalAccessInfo(ETFTerminalAccessInfo terminalAccessInfo) {
		this.terminalAccessInfo = terminalAccessInfo;
	}

	/**
	 * @param terminalMessageHeader the terminalMessageHeader to set
	 */
	public void setETFTerminalMessageHeader(ETFTerminalMessageHeader terminalMessageHeader) {
		this.terminalMessageHeader = terminalMessageHeader;
	}
    
    
}
