/**
 * 
 */
package com.skymobi.android.transport.util;

import java.util.UUID;

/**
 * @author hp
 *
 */
public interface MutableIdentifyable extends Identifyable {
	public void	setIdentification(UUID id);
}
