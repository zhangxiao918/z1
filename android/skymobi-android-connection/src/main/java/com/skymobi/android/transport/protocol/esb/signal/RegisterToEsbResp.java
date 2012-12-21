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
@EsbSignal( messageCode = 0xE002 )
public class RegisterToEsbResp extends AbstractEsbM2MSignal {
}
