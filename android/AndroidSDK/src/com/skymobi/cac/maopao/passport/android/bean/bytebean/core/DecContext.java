/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

import com.skymobi.cac.maopao.passport.android.util.MutablePropertyable;

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
