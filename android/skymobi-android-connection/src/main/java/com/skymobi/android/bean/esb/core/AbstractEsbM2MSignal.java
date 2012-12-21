/**
 * 
 */
package com.skymobi.android.bean.esb.core;

/**
 * @author hp
 *
 */
public class AbstractEsbM2MSignal extends AbstractEsbHeaderable 
	implements	EsbModule2ModuleSignal {

	public boolean checkIntegrity() {
		return true;
	}
}
