/**
 * 
 */
package com.skymobi.android.bean.tlv;

import com.skymobi.android.transport.util.MutableIdentifyable;

/**
 * @author hp
 *
 */
public interface TLVSignal extends MutableIdentifyable {
	
	public	void	setSourceId(short id);
	
	public	short	getSourceId();
}
