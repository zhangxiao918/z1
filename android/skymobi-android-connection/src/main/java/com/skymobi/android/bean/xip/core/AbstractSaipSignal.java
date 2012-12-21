package com.skymobi.android.bean.xip.core;

/**
 * @author hp
 *
 */
public class AbstractSaipSignal extends AbstractXipSignal implements SaipSignal {
	
	private short srcModule;
	
	private short dstModule;
	
	public short getSrcModule() {
		return srcModule;
	}
	/**
	 * @param module the srcModule to set
	 */
	public void setSrcModule(short module) {
		srcModule = module;
	}
	/**
	 * @return the dstModule
	 */
	public short getDstModule() {
		return dstModule;
	}
	/**
	 * @param module the dstModule to set
	 */
	public void setDstModule(short module) {
		dstModule = module;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + dstModule;
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
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractSaipSignal other = (AbstractSaipSignal) obj;
		if (dstModule != other.dstModule)
			return false;
		if (srcModule != other.srcModule)
			return false;
		return true;
	}
	
	
}
