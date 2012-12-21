/**
 * 
 */
package com.skymobi.android.bean.esb.core;

import com.skymobi.android.bean.bytebean.core.DefaultFieldDesc;

/**
 * @author Marvin.Ma
 *
 */
public class EsbFieldDescImpl extends DefaultFieldDesc implements EsbFieldDesc {

	private	int	tag = -1;
	
	/* (non-Javadoc)
	 * @see com.skymobi.bean.esb.core.EsbFieldDesc#getTag()
	 */
	public int getTag() {
		return tag;
	}

	public EsbFieldDescImpl setTag(int tag) {
		this.tag = tag;
		return	this;
	}
}
