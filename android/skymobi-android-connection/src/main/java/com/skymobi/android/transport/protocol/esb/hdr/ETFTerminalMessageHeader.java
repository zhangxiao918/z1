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
import com.skymobi.android.bean.esb.core.TerminalMessageHeader;

// in m2a header
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           srcmodule(2)        |        terminalModule(2)      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           length(2)           |        flags(2)               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           msgCode(2)          |    nResult(1) | application...
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

/**
 * @author Marvin.Ma
 *
 */
public class ETFTerminalMessageHeader {

    private static final Logger logger 
		= LoggerFactory.getLogger(ETFTerminalMessageHeader.class);
    
	public final static short TERMINAL_MESSAGE_HEADER_SIZE = 10;
	
	@EsbField(index = 1, bytes = 2)
	private int srcModule;

	@EsbField(index = 2, bytes = 2)
	private int dstModule;

	@EsbField(index = 3)
	private short length;

	@EsbField(index = 4)
	private short flags;

	@EsbField(index = 5, bytes = 2)
	private int msgCode;
    
    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

	/**
	 * @return the srcModule
	 */
	public int getSrcModule() {
		return srcModule;
	}

	/**
	 * @return the dstModule
	 */
	public int getDstModule() {
		return dstModule;
	}

	/**
	 * @return the length
	 */
	public short getLength() {
		return length;
	}

	/**
	 * @return the flags
	 */
	public short getFlags() {
		return flags;
	}

	/**
	 * @return the msgCode
	 */
	public int getMsgCode() {
		return msgCode;
	}

	public	int	getMessageBodySize() {
		return	length - TERMINAL_MESSAGE_HEADER_SIZE;
	}

	/**
	 * @param srcModule the srcModule to set
	 */
	public void setSrcModule(int srcModule) {
		this.srcModule = srcModule;
	}

	/**
	 * @param dstModule the dstModule to set
	 */
	public void setDstModule(int dstModule) {
		this.dstModule = dstModule;
	}

	/**
	 * @param flags the flags to set
	 */
	public void setFlags(short flags) {
		this.flags = flags;
	}

	/**
	 * @param length the length to set
	 */
	public ETFTerminalMessageHeader setLengthWithMessageBodySize(short messageBodySize) {
		this.length = (short) (messageBodySize + TERMINAL_MESSAGE_HEADER_SIZE);
		return	this;
	}

	/**
	 * @param msgCode the msgCode to set
	 */
	public ETFTerminalMessageHeader setMsgCode(int msgCode) {
		this.msgCode = msgCode;
		return	this;
	}
	
	public TerminalMessageHeader toTerminalMessageHeader() {
		TerminalMessageHeader ret = new TerminalMessageHeader();
		
		try {
			BeanUtils.copyProperties(ret, this);
		} catch (Exception e) {
			logger.error("toTerminalMessageHeader:", e);
		}
		
		return	ret;
	}
	
	public static ETFTerminalMessageHeader fromTerminalMessageHeader(TerminalMessageHeader header) {
		ETFTerminalMessageHeader ret = new ETFTerminalMessageHeader();
		
		try {
			BeanUtils.copyProperties(ret, header);
		} catch (Exception e) {
			logger.error("fromTerminalMessageHeader:", e);
		}
		
		return	ret;
	}
}
