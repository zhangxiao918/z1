/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.signal;

import com.skymobi.android.bean.esb.annotation.EsbField;
import com.skymobi.android.bean.esb.annotation.EsbSignal;
import com.skymobi.android.bean.esb.core.AbstractEsbM2MSignal;

/**
 * @author hp
 *
 */
@EsbSignal( messageCode = 0xE001 )
public class RegisterToEsbReq extends AbstractEsbM2MSignal {

	//	DstESBAddr  填写0xE000
	
	@EsbField(index = 0, bytes = 2)
	private int moduleId;

	@EsbField(index = 1, bytes = 2)
	private int instanceId;

	/**
	 * @return the moduleId
	 */
	public int getModuleId() {
		return moduleId;
	}

	/**
	 * @param moduleId the moduleId to set
	 */
	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * @return the instanceId
	 */
	public int getInstanceId() {
		return instanceId;
	}

	/**
	 * @param instanceId the instanceId to set
	 */
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

}
