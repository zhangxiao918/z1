/**
 * 
 */
package com.skymobi.android.bean.bytebean.codec;

import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.FieldCodecCategory;

/**
 * @author isdom
 *
 */
public abstract class AbstractPrimitiveCodec implements ByteFieldCodec {

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.core.ByteFieldCodec#getCategory()
     */
    public FieldCodecCategory getCategory() {
        return null;
    }
}
