/**
 * 
 */
package com.skymobi.android.bean.esb.core;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

// in m2a header
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           srcmodule(2)        |        terminalModule(2)      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           length(2)(autogen)  |        flags(2)               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           msgCode(2)(autogen) |    nResult(1) | application...
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

/**
 * @author Marvin.Ma
 *
 */
public class TerminalMessageHeader implements Cloneable {

	private int srcModule;

	private int dstModule;

	private short flags;

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
	 * @return the flags
	 */
	public short getFlags() {
		return flags;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dstModule;
		result = prime * result + flags;
		result = prime * result + srcModule;
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
		TerminalMessageHeader other = (TerminalMessageHeader) obj;
		if (dstModule != other.dstModule)
			return false;
		if (flags != other.flags)
			return false;
		if (srcModule != other.srcModule)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TerminalMessageHeader clone() throws CloneNotSupportedException {
		return (TerminalMessageHeader)super.clone();
	}
	
	public TerminalMessageHeader exchangeSrcDstModule() {
		int tmp = this.srcModule;
		this.srcModule = this.dstModule;
		this.dstModule = tmp;
		
		return	this;
	}
}
