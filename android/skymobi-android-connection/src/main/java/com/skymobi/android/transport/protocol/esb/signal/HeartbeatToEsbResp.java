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
@EsbSignal( messageCode = 0xE006 )
public class HeartbeatToEsbResp extends AbstractEsbM2MSignal {
}
