/**
 * 
 */
package com.skymobi.android.bean.bytebean.core;

/**
 * @author isdom
 *
 */
public interface EncContext extends FieldCodecContext {
    public Object           getEncObject();
    public Class<?>         getEncClass();

	public EncContextFactory getEncContextFactory();
}
