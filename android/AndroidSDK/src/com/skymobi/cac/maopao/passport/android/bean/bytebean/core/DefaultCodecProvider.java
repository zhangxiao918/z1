/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author isdom
 *
 */
public class DefaultCodecProvider implements FieldCodecProvider {
    
    private final Map<Class<?>, ByteFieldCodec> class2Codec
        = new HashMap<Class<?>, ByteFieldCodec>();
    
    private final Map<FieldCodecCategory, ByteFieldCodec> category2Codec
        = new HashMap<FieldCodecCategory, ByteFieldCodec>();

    public DefaultCodecProvider addCodec(ByteFieldCodec codec) {
        Class<?>[] classes = codec.getFieldType();
        
        if ( null != classes ) {
            for ( Class<?> clazz : classes ) {
                class2Codec.put(clazz, codec);
            }
        }
        else if ( null != codec.getCategory() ) {
            category2Codec.put(codec.getCategory(), codec);
        }
        
        return  this;
    }

    public void setCodecs(Collection<ByteFieldCodec> codecs) {
    	for ( ByteFieldCodec codec : codecs ) {
    		addCodec(codec);
    	}
    }
    
    public Map<String, String> getCodecsAsText() {
    	Map<String, String>	ret = new HashMap<String, String>();
        for (Map.Entry<Class<?>, ByteFieldCodec> entry : this.class2Codec.entrySet()) {
        	ret.put(entry.getKey().toString(), entry.getValue().toString());
        }
        
        for (Map.Entry<FieldCodecCategory, ByteFieldCodec> entry : this.category2Codec.entrySet()) {
        	ret.put(entry.getKey().toString(), entry.getValue().toString());
        }
        
        return	ret;
    }
    
    public ByteFieldCodec getCodecOf(FieldCodecCategory type) {
        return category2Codec.get(type);
    }

    public ByteFieldCodec getCodecOf(Class<?> clazz) {
        return class2Codec.get(clazz);
    }
}
