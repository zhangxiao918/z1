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
import com.skymobi.android.bean.esb.core.TerminalAccessInfo;

// in a2m header
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                          skyId(4)                             |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                          accessId(4)                          |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                          ai(4)                                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

/**
 * @author Marvin.Ma
 *
 */
public class ETFTerminalAccessInfo {
	
    private static final Logger logger 
		= LoggerFactory.getLogger(ETFTerminalAccessInfo.class);
    
    @EsbField(index = 1, bytes=4)
	private long skyId;

    @EsbField(index = 2, bytes=4)
	private long accESBAddr;

    @EsbField(index = 3, bytes=4)
	private long accSessioIndex;
    
    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }



	/**
	 * @return the skyId
	 */
	public long getSkyId() {
		return skyId;
	}



	/**
	 * @param skyId the skyId to set
	 */
	public void setSkyId(long skyId) {
		this.skyId = skyId;
	}



	/**
	 * @return the accESBAddr
	 */
	public long getAccESBAddr() {
		return accESBAddr;
	}



	/**
	 * @param accESBAddr the accESBAddr to set
	 */
	public void setAccESBAddr(long accESBAddr) {
		this.accESBAddr = accESBAddr;
	}



	/**
	 * @return the accSessioIndex
	 */
	public long getAccSessioIndex() {
		return accSessioIndex;
	}



	/**
	 * @param accSessioIndex the accSessioIndex to set
	 */
	public void setAccSessioIndex(long accSessioIndex) {
		this.accSessioIndex = accSessioIndex;
	}

	public TerminalAccessInfo toTerminalAccessInfo() {
		TerminalAccessInfo ret = new TerminalAccessInfo();
		
		try {
			BeanUtils.copyProperties(ret, this);
		} catch (Exception e) {
			logger.error("toTerminalAccessInfo:", e);
		}
		
		return	ret;
	}
	
	public static ETFTerminalAccessInfo fromTerminalAccessInfo(TerminalAccessInfo orig) {
		ETFTerminalAccessInfo ret = new ETFTerminalAccessInfo();
		
		try {
			BeanUtils.copyProperties(ret, orig);
		} catch (Exception e) {
			logger.error("fromTerminalAccessInfo:", e);
		}
		
		return	ret;
	}
}
