/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.util;

import java.util.UUID;

/**
 * @author hp
 *
 */
public class AbstractIdentifyable implements MutableIdentifyable {

	private UUID uuid = UUID.randomUUID();
	
	/* (non-Javadoc)
	 * @see com.skymobi.util.MutableIdentifyable#setIdentification(java.util.UUID)
	 */
	public void setIdentification(UUID id) {
		this.uuid = id;
	}

	/* (non-Javadoc)
	 * @see com.skymobi.util.Identifyable#getIdentification()
	 */
	public UUID getIdentification() {
		return uuid;
	}

}
