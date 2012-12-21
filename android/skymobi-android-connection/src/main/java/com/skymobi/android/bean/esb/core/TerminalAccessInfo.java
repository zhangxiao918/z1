/**
 * 
 */
package com.skymobi.android.bean.esb.core;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

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
public class TerminalAccessInfo implements Cloneable {
	
	private long skyId;

	private long accESBAddr;

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



	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (accESBAddr ^ (accESBAddr >>> 32));
		result = prime * result
				+ (int) (accSessioIndex ^ (accSessioIndex >>> 32));
		result = prime * result + (int) (skyId ^ (skyId >>> 32));
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
		TerminalAccessInfo other = (TerminalAccessInfo) obj;
		if (accESBAddr != other.accESBAddr)
			return false;
		if (accSessioIndex != other.accSessioIndex)
			return false;
		if (skyId != other.skyId)
			return false;
		return true;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public TerminalAccessInfo clone() throws CloneNotSupportedException {
		return (TerminalAccessInfo)super.clone();
	}
}
