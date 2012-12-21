/**
 * 
 */
package com.skymobi.android.transport.protocol.esb;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.esb.annotation.EsbField;
import com.skymobi.android.bean.esb.core.EsbHeaderable;


//0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7  
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           srcmodule(2)        |            dstmodule(2)       |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|      protocol code(2)         |            seqNum(4)       	  |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//                            |            flags(4)               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                               |            length(4)          |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                               |            result(2)          |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                          reserved(4)                          |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

/**
 * @author Marvin.Ma
 *
 */
public class ESBFixedHeader implements EsbHeaderable {

	public static final int FIXED_HEADER_SIZE = 24;
	
    @EsbField(index = 1, bytes=2)
	private int	srcESBAddr;

    @EsbField(index = 2, bytes=2)
	private int	dstESBAddr;
    
    @EsbField(index = 3)
	private short	protocolCode;
    
    @EsbField(index = 4)
	private int		seqNum = 0;
    
    @EsbField(index = 5)
	private int		flags = 0;

    @EsbField(index = 6)
	private int		length;
    
    @EsbField(index = 7)
	private short	result = 0;
    
    @SuppressWarnings("unused")
	@EsbField(index = 8)
	private int		reserved = 0;

	/**
	 * @return the srcESBAddr
	 */
	public int getSrcESBAddr() {
		return srcESBAddr;
	}

	/**
	 * @return the dstESBAddr
	 */
	public int getDstESBAddr() {
		return dstESBAddr;
	}

	/**
	 * @return the protocolCode
	 */
	public short getProtocolCode() {
		return protocolCode;
	}

	/**
	 * @return the seqNum
	 */
	public int getSeqNum() {
		return seqNum;
	}

	/**
	 * @return the flags
	 */
	public int getFlags() {
		return flags;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @return the result
	 */
	public short getResult() {
		return result;
	}

    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

	/**
	 * @param srcESBAddr the srcESBAddr to set
	 */
	public void setSrcESBAddr(int srcESBAddr) {
		this.srcESBAddr = srcESBAddr;
	}

	/**
	 * @param dstESBAddr the dstESBAddr to set
	 */
	public void setDstESBAddr(int dstESBAddr) {
		this.dstESBAddr = dstESBAddr;
	}

	/**
	 * @param protocolCode the protocolCode to set
	 */
	public void setProtocolCode(short protocolCode) {
		this.protocolCode = protocolCode;
	}

	/**
	 * @param seqNum the seqNum to set
	 */
	public void setSeqNum(int seqNum) {
		this.seqNum = seqNum;
	}

	/**
	 * @param flags the flags to set
	 */
	public void setFlags(int flags) {
		this.flags = flags;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(short result) {
		this.result = result;
	}

	public boolean checkIntegrity() {
		return true;
	}
}
