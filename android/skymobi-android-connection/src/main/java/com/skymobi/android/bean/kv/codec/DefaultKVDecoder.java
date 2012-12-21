/**
 * 
 */
package com.skymobi.android.bean.kv.codec;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.kv.KVDecoder;
import com.skymobi.android.bean.kv.KVUtils;
import com.skymobi.android.bean.kv.annotation.KeyValueAttribute;
import com.skymobi.android.util.Transformer;

/**
 * @author hp
 *
 */
public abstract class DefaultKVDecoder implements KVDecoder {

    private static final Logger logger = LoggerFactory.getLogger(DefaultKVDecoder.class);
    
    private Transformer<Class<?>, StringConverter> converterFactory;
    
    protected abstract Object createBean();
    
    /**
     * @param converterFactory the converterFactory to set
     */
    public void setConverterFactory(
            Transformer<Class<?>, StringConverter> converterFactory) {
        this.converterFactory = converterFactory;
    }
    
    /* (non-Javadoc)
     * @see com.skymobi.util.Transformer#transform(java.lang.Object)
     */
    public Object transform(Map<String, List<String>> from) {
    	//	TODO
    	//	鏄惁澶勭悊濡備笅鎯呭喌锛屽嵆key鍊煎墠鍚庢湁绌烘牸锛屾彁楂樺吋瀹规�
    	//	浣嗘�鑳戒細鍙椾竴瀹氬奖鍝�
//        <void method="put"> 
//        <string> reqVersion</string> 
//        <object class="java.util.ArrayList"> 
//         <void method="add"> 
//          <string>104</string> 
//         </void> 
//        </object> 
//       </void> 
    	
        Object bean = createBean();
        
        Field[] fields = KVUtils.getKVFieldsOf(bean.getClass());
        
        for ( Field field : fields ) {
            KeyValueAttribute param  = field.getAnnotation(KeyValueAttribute.class);
            
            if ( null == param ) {
                //  not KVAttribute
                continue;
            }
            
            List<String> values = from.get(param.key());
            
            if ( null != values && !values.isEmpty() ) {
                
            	if ( logger.isDebugEnabled() ) {
            		logger.debug("found field[" + field + "]");
            	}
            	
                field.setAccessible(true);
                try {
                	Class<?> fieldType = field.getType();
                	Class<?> kvType = fieldType;
                	if ( fieldType.isArray() ) {
                		kvType = fieldType.getComponentType();
                	}
                	
                    StringConverter converter = converterFactory.transform( kvType );
                    if ( null != converter ) {
                    	Object v = null;
                    	if ( !fieldType.isArray() ) {
	                    	v = converter.transform( values.get(0) );
                    	}
                    	else {
                    		v = Array.newInstance(kvType, values.size());
                    		int idx = 0;
                    		for ( String e : values ) {
                    			Array.set(v, idx++, converter.transform(e) );
                    		}
                    	}
                    	if ( logger.isDebugEnabled() ) {
                    		logger.debug(",and set value[" + v + "]");
                    	}
                        field.set(bean, v );
                    }
                } catch (Exception e) {
                	logger.error("transform", e);
                }
            }
        }
        
        return bean;
    }

}
