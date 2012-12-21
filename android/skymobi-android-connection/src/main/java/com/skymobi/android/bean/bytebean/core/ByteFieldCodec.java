/**
 * 
 */
package com.skymobi.android.bean.bytebean.core;


/**
 * @author isdom
 *
 */
public interface ByteFieldCodec {
    public  FieldCodecCategory  getCategory();
    public  Class<?>[]          getFieldType();
    
    public  DecResult   decode(DecContext ctx);
    public  byte[]      encode(EncContext ctx);
}
