/**
 * 
 */
package com.skymobi.android.bean.bytebean.core;

import com.skymobi.android.util.MutablePropertyable;

/**
 * @author isdom
 *
 */
public interface DecContext extends FieldCodecContext, MutablePropertyable {
    
    public Object           getDecOwner();
    public byte[]           getDecBytes();
    public Class<?>         getDecClass();
    
	public DecContextFactory getDecContextFactory();
}
