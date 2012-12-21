/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.signal;

import com.skymobi.android.bean.esb.annotation.EsbSignal;
import com.skymobi.android.bean.esb.core.AbstractEsbM2MSignal;

/**
 * @author hp
 *
 */
@EsbSignal( messageCode = 0xE005 )
public class HeartbeatToEsbReq extends AbstractEsbM2MSignal {
}
