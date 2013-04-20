/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.codec;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.commons.lang.ArrayUtils;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DefaultFieldDesc;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.Field2Desc;
import com.skymobi.cac.maopao.passport.android.util.SimpleCache;



/**
 * @author isdom
 *
 */
public class BeanCodecUtil {
    
   // @SuppressWarnings("unused")
   // private static final Logger logger = LoggerFactory.getLogger(BeanCodecUtil.class);
    
    private Field2Desc                      field2Desc;
    private SimpleCache<Class<?>, List<ByteFieldDesc>>   descesCache = 
        new SimpleCache<Class<?>, List<ByteFieldDesc>>();

    public BeanCodecUtil(Field2Desc field2Desc) {
        this.field2Desc = field2Desc;
    }
    
    public List<ByteFieldDesc> getFieldDesces(final Class<?> clazz) {
        return  descesCache.get(clazz, new Callable<List<ByteFieldDesc>>(){

            public List<ByteFieldDesc> call(){
                List<ByteFieldDesc> ret;
                
                Field[] fields = null;
                
                Class<?> itr = clazz;
                while ( !itr.equals(Object.class)) {
                    fields = (Field[]) ArrayUtils.addAll(itr.getDeclaredFields(), fields);
                    itr = itr.getSuperclass();
                }
                Map<Integer, Field> fieldMaps = new HashMap<Integer, Field>(fields.length);
                ret = new ArrayList<ByteFieldDesc>(fields.length);
                for ( Field field : fields ) {
                    ByteFieldDesc desc = field2Desc.genDesc(field);
                    if ( null != desc ) {
                        ret.add(desc);
                        //save index field mapping
                        fieldMaps.put(desc.getIndex(), field);
                    }
                }
                
                for(ByteFieldDesc desc: ret){
                	//fill all the length field by field index mapping
                	if(desc.getLengthFieldIndex() != -1){
                		Field field = fieldMaps.get(desc.getLengthFieldIndex());
                		((DefaultFieldDesc)desc).setLengthField(field);
                	}
                }
                
                Collections.sort(ret, ByteFieldDesc.comparator);
                return  ret;
            }});
    }
}
