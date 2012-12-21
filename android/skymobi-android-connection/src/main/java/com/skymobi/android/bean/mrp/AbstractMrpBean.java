/**
 * 
 */
package com.skymobi.android.bean.mrp;

import java.lang.reflect.Field;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.bytebean.ByteBean;
import com.skymobi.android.bean.bytebean.DecodeBuffer;
import com.skymobi.android.bean.bytebean.annotation.ByteField;
import com.skymobi.android.bean.bytebean.codec.AnyCodec;
import com.skymobi.android.bean.bytebean.codec.ArrayCodec;
import com.skymobi.android.bean.bytebean.codec.BeanCodec;
import com.skymobi.android.bean.bytebean.codec.ByteArrayCodec;
import com.skymobi.android.bean.bytebean.codec.ByteCodec;
import com.skymobi.android.bean.bytebean.codec.IntCodec;
import com.skymobi.android.bean.bytebean.codec.LongCodec;
import com.skymobi.android.bean.bytebean.codec.ShortCodec;
import com.skymobi.android.bean.bytebean.codec.StringCodec;
import com.skymobi.android.bean.bytebean.core.BeanFieldCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.android.bean.bytebean.core.DecResult;
import com.skymobi.android.bean.bytebean.core.DefaultCodecProvider;
import com.skymobi.android.bean.bytebean.core.DefaultDecContextFactory;
import com.skymobi.android.bean.bytebean.core.DefaultEncContextFactory;
import com.skymobi.android.bean.bytebean.core.DefaultFieldDesc;
import com.skymobi.android.bean.bytebean.core.Field2Desc;
import com.skymobi.android.bean.bytebean.core.FieldCodecCategory;
import com.skymobi.android.util.ByteOrder;
import com.skymobi.android.util.DefaultNumberCodecs;

/**
 * @author isdom
 *
 */
public class AbstractMrpBean implements ByteBean {

    private static  final Field2Desc            field2Desc;
    private static  final DefaultCodecProvider  codecProvider;
    private static final ByteOrder  MRP_BYTEORDER = ByteOrder.LittleEndian;
    
    static {
        field2Desc = new Field2Desc() {

            public ByteFieldDesc genDesc(Field field) {
                ByteField   byteField = field.getAnnotation(ByteField.class);
                Class<?>    clazz = field.getDeclaringClass();
                if ( null != byteField ) {
                    try {
                        DefaultFieldDesc desc 
                        = new DefaultFieldDesc()
                            .setField(field)
                            .setIndex( byteField.index() )
                            .setByteSize( byteField.bytes() )
                            .setCharset( byteField.charset() )
                            .setLengthField(  
                                byteField.length().equals("") 
                                ? null 
                                : clazz.getDeclaredField( byteField.length() ) )
                            .setCustomTypeMethod(
                                byteField.customType().equals("")                                
                                ? null
                                : clazz.getDeclaredMethod( byteField.customType() ) )
                             .setByteOrder(byteField.byteOrder())
                             .setFixedLength(byteField.fixedLength());
                        return  desc;
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                return  null;
            }};
        
        codecProvider = new DefaultCodecProvider()
            .addCodec(new StringCodec())
            .addCodec(new LongCodec())
            .addCodec(new IntCodec())
            .addCodec(new ShortCodec())
            .addCodec(new ByteCodec())
            .addCodec(new ByteArrayCodec())
            .addCodec(new ArrayCodec())
            .addCodec(new AnyCodec(ByteBean.class));
        
        BeanCodec beanCodec = new BeanCodec(ByteBean.class, field2Desc);
        
        DefaultDecContextFactory decContextFactory  = new DefaultDecContextFactory();
        decContextFactory.setCodecProvider(codecProvider);
        decContextFactory.setNumberCodec(
        		DefaultNumberCodecs.byteOrder2NumberCodec(MRP_BYTEORDER));
        
        beanCodec.setDecContextFactory(decContextFactory);
        
        DefaultEncContextFactory encContextFactory  = new DefaultEncContextFactory();
        encContextFactory.setCodecProvider(codecProvider);
        encContextFactory.setNumberCodec(
        		DefaultNumberCodecs.byteOrder2NumberCodec(MRP_BYTEORDER));
        
        beanCodec.setEncContextFactory(encContextFactory);
        
        codecProvider.addCodec(beanCodec);
    }
    
    private static  BeanFieldCodec  getBeanCodec() {
        return  (BeanFieldCodec)codecProvider.getCodecOf(FieldCodecCategory.BEAN);
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, 
                ToStringStyle.MULTI_LINE_STYLE);
    }

    public int  getStaticByteSize() {
        return  getBeanCodec().getStaticByteSize(this.getClass());
    }
    
    protected void updateLength(String srcFieldAsText) {
        try {
            Field srcField = this.getClass().getDeclaredField( srcFieldAsText );
            ByteFieldDesc desc = field2Desc.genDesc( srcField );
            desc.setLength(this);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends ByteBean> T fromBytes(byte[] bytes, Class<T> clazz) {
        return  (T)getBeanCodec().decode(
        		getBeanCodec().getDecContextFactory().createDecContext(
        				bytes, clazz, null, null) )
//                    new DefaultDecContext()
//                    .setCodecProvider(codecProvider)
//                    .setDecBytes(bytes)
//                    .setDecClass(clazz)
//                    .setNumberCodec(DefaultNumberCodecs.byteOrder2NumberCodec(byteOrder)))
                .getValue();
    }
    
    public byte[] genBytes() {
        return  getBeanCodec().encode(
        			getBeanCodec().getEncContextFactory().createEncContext(
        					this, this.getClass(), null) );
//                    new DefaultEncContext()
//                    .setCodecProvider(codecProvider)
//                    .setEncClass(this.getClass())
//                    .setEncObject(this)
//                    .setNumberCodec(DefaultNumberCodecs.byteOrder2NumberCodec(byteOrder)));
    }

    @SuppressWarnings("unchecked")
    public static <T extends ByteBean> T fromDecodeBuffer(DecodeBuffer buf, Class<T> clazz) {
        DecResult rslt = getBeanCodec().decode(
        		getBeanCodec().getDecContextFactory().createDecContext(
        				buf.getBytes(), clazz, null, null) );
//                    new DefaultDecContext()
//                    .setCodecProvider(codecProvider)
//                    .setDecBytes(buf.getBytes())
//                    .setDecClass(clazz)
//                    .setNumberCodec(DefaultNumberCodecs.byteOrder2NumberCodec(byteOrder)));
        buf.setBytes(rslt.getRemainBytes());
        return  (T)rslt.getValue();
    }
}
