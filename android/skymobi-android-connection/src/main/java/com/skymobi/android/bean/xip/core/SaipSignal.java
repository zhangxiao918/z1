/**
 * 
 */
package com.skymobi.android.bean.xip.core;

/**
 * @author hp
 *
 */
public interface SaipSignal extends XipSignal {

	public short getSrcModule();
	
	public void setSrcModule(short module);
	
	public short getDstModule();
	
	public void setDstModule(short module);
}
