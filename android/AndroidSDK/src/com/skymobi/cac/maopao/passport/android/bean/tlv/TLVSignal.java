/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv;

import com.skymobi.cac.maopao.passport.android.util.MutableIdentifyable;

/**
 * @author hp
 *
 */
public interface TLVSignal extends MutableIdentifyable {
	
	public	void	setSourceId(short id);
	
	public	short	getSourceId();
}
