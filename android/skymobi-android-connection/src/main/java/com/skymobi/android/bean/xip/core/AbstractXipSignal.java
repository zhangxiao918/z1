/**
 * 
 */
package com.skymobi.android.bean.xip.core;

import java.util.UUID;

import com.skymobi.android.util.DefaultPropertiesSupport;

/**
 * @author Marvin.Ma
 *
 */
public class AbstractXipSignal extends DefaultPropertiesSupport 
	implements XipSignal {

	private	UUID	uuid = UUID.randomUUID();
	
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
		return	uuid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
		AbstractXipSignal other = (AbstractXipSignal) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

}
