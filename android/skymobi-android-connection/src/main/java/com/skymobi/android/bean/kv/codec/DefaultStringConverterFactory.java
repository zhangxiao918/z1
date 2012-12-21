/**
 * 
 */
package com.skymobi.android.bean.kv.codec;

import java.util.HashMap;
import java.util.Map;

import com.skymobi.android.util.Transformer;


/**
 * @author hp
 *
 */
public class DefaultStringConverterFactory implements Transformer<Class<?>, StringConverter> {
    
    private Map<Class<?>, StringConverter>  converters = 
        new HashMap<Class<?>, StringConverter>();
    
    
    /**
     * @param converters the converters to set
     */
    public void setConverters(Map<Class<?>, StringConverter> converters) {
        this.converters.clear();
        
        for (Map.Entry<Class<?>, StringConverter> entry : converters.entrySet()) {
            if ( null != entry.getValue() ) {
                this.converters.put(entry.getKey(), entry.getValue());
            }
        }
    }

    public DefaultStringConverterFactory setConverter(Class<?> cls, StringConverter converter) {
        this.converters.put(cls, converter);
        return  this;
    }
    
    public StringConverter transform(Class<?> from) {
        return converters.get(from);
    }

}
