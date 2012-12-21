/**
 * 
 */
package com.skymobi.android.bean.esb.codec;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.codec.BeanCodecUtil;
import com.skymobi.android.bean.bytebean.core.BeanFieldCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldCodec;
import com.skymobi.android.bean.bytebean.core.ByteFieldDesc;
import com.skymobi.android.bean.bytebean.core.DecContext;
import com.skymobi.android.bean.bytebean.core.DecContextFactory;
import com.skymobi.android.bean.bytebean.core.DecResult;
import com.skymobi.android.bean.bytebean.core.EncContext;
import com.skymobi.android.bean.bytebean.core.EncContextFactory;
import com.skymobi.android.bean.bytebean.core.FieldCodecCategory;
import com.skymobi.android.bean.esb.core.EsbFieldDesc;
import com.skymobi.android.bean.esb.core.EsbFieldToDesc;
import com.skymobi.android.bean.esb.core.EsbFieldToDescForTLV;
import com.skymobi.android.bean.esb.core.EsbHeaderable;
import com.skymobi.android.bean.esb.core.EsbMutableHeaderable;
import com.skymobi.android.util.FieldUtils;

/**
 * @author isdom
 *
 */
public class EsbBeanCodecSupportTLV implements BeanFieldCodec {

	public	static final String TLV_LENGTH_KEY = "_TLV_LENGTH_SIZE";
	
	private	static final int TAG_LEN_BYTESIZE = 2;
	
    private static final Logger logger = 
    	LoggerFactory.getLogger(EsbBeanCodecSupportTLV.class);
    
	private	DecContextFactory	decContextFactory;
	private	EncContextFactory	encContextFactory;
	
    private BeanCodecUtil   util;
    private BeanCodecUtil   utilForTLV;
    
    public EsbBeanCodecSupportTLV() {
        util = new BeanCodecUtil(new EsbFieldToDesc());
        utilForTLV = new BeanCodecUtil(new EsbFieldToDescForTLV());
    }
    
	public static EsbFieldDesc tag2desc(List<ByteFieldDesc> desces, int tag) {
		for (ByteFieldDesc desc : desces ) {
			if ( ((EsbFieldDesc)desc).getTag() == tag ) {
				return	(EsbFieldDesc)desc;
			}
		}
		
		return	null;
	}
	
	public static Class<?> desc2class(EsbFieldDesc desc) {
		Class<?> fieldType = desc.getFieldType();
		
		return	fieldType.equals(ArrayList.class) 
			? 	FieldUtils.getComponentClass(desc.getField())
			:	fieldType;
	}
	
//	public static int desc2tag(Class<?> clazz) {
//		
//	}
	
    public DecResult decode(DecContext ctx) {
        byte[]  bytes   = ctx.getDecBytes();
        Class<?>  clazz = ctx.getDecClass();
        ByteFieldDesc selfDesc = ctx.getFieldDesc();
        if ( null != selfDesc ) {
            Class<?> customClass = selfDesc.getCustomType( ctx.getDecOwner() );
            if ( null != customClass) {
                clazz = customClass;
            }
        }
        Object target = null;

        try {
            target = clazz.newInstance();
            
            if ( target instanceof EsbMutableHeaderable ) {
            	EsbHeaderable hdr = 
            		(EsbHeaderable)ctx.getProperty(EsbHeaderable.class.getSimpleName());
            	if ( null != hdr ) {
            		EsbMutableHeaderable signal = (EsbMutableHeaderable)target;
            		
                    signal.setSrcESBAddr(hdr.getSrcESBAddr());
                    signal.setDstESBAddr(hdr.getDstESBAddr());
                    signal.setSeqNum(hdr.getSeqNum());
                    signal.setResult(hdr.getResult());
                    signal.setFlags(hdr.getFlags());
            	}
            }
            
            List<ByteFieldDesc> offDesces = util.getFieldDesces(clazz);
            
            ByteFieldCodec  anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
            
            for ( ByteFieldDesc desc : offDesces ) {
                if ( 0 == bytes.length ) {
                    //  瑙ｇ爜鎵�敤鐨勫瓧鑺傛祦宸插埌缁撴潫浣嶇疆
//                    if ( logger.isDebugEnabled() ) {
//                        logger.debug( "bytes2Compound: encounter end of decode bytes stream.");
//                    }
                    break;
                }
                
                Field   field = desc.getField();
                
                Class<?> fieldClass = field.getType();
                
//                if ( logger.isDebugEnabled() ) {
//                    logger.debug( "bytes2Compound: filling Field[" + field.toString() + "]");
//                }
                DecResult ret = anyCodec.decode(
                		decContextFactory.createDecContext(
                				bytes, fieldClass, target, desc) );
                
                Object fieldValue = ret.getValue();
                bytes = ret.getRemainBytes();
                
                field.setAccessible(true);
                field.set(target, fieldValue);
//                if (logger.isDebugEnabled()) {
//                    logger.debug( "bytes2Compound: filled Field[" + field.toString() 
//                            + "] with value:" + fieldValue );
//                }
            }
            
            //	for tlv support
            List<ByteFieldDesc> tlvDesces = utilForTLV.getFieldDesces(clazz);
            
            if ( (null != tlvDesces) && !tlvDesces.isEmpty() ) {
            	
            	while ( bytes.length >= 2 * TAG_LEN_BYTESIZE ) {
            		
	            	int tag = ctx.getNumberCodec().bytes2Int(bytes, TAG_LEN_BYTESIZE);
	            	
	            	bytes = ArrayUtils.subarray(bytes, TAG_LEN_BYTESIZE, bytes.length);
	            	
	            	int len = ctx.getNumberCodec().bytes2Int(bytes, TAG_LEN_BYTESIZE);
	
	            	byte[] bytesValue = ArrayUtils.subarray(bytes, TAG_LEN_BYTESIZE, TAG_LEN_BYTESIZE + len);
	            	
	            	bytes = ArrayUtils.subarray(bytes, TAG_LEN_BYTESIZE + len, bytes.length);
	
	            	EsbFieldDesc desc = tag2desc(tlvDesces, tag);
	            	
	            	if ( null != desc ) {
	            		DecContext valueCtx = ctx.getDecContextFactory().createDecContext(
	        					bytesValue, desc2class(desc), ctx.getDecOwner(), desc);
	            		
	            		valueCtx.setProperty(TLV_LENGTH_KEY, new Integer(len));
	            		
		            	DecResult ret = anyCodec.decode(valueCtx);
	
		            	Field	field = desc.getField();
		            	
		                field.setAccessible(true);
		    			Class<?> fieldType = desc.getFieldType();
		    			if ( fieldType.equals(ArrayList.class)) {
		    				try {
		    					ArrayList<Object> list = (ArrayList<Object>)field.get(target);
		    					if ( null == list ) {
		    						list = new ArrayList<Object>();
		    						field.set(target, list);
		    					}
		    	            	list.add(ret.getValue());
		    				} catch (IllegalArgumentException e) {
		    					logger.error("EsbBeanCodecSupportTLV:", e);
		    				} catch (IllegalAccessException e) {
		    					logger.error("EsbBeanCodecSupportTLV:", e);
		    				}
		    			}
		    			else {
		    				try {
		    					field.set(target, ret.getValue());
		    				} catch (IllegalArgumentException e) {
		    					logger.error("EsbBeanCodecSupportTLV:", e);
		    				} catch (IllegalAccessException e) {
		    					logger.error("EsbBeanCodecSupportTLV:", e);
		    				}
		    			}
	            	}
	            	else {
	            		//	skip unknow tag
	            		logger.warn("EsbBeanCodecSupportTLV: unknown tag: {}, just skip len: {}", tag, len);
	            	}
	
	            }
            }
            
        } catch (InstantiationException e) {
            logger.error( "EsbBeanCodecSupportTLV:", e);
        } catch (IllegalAccessException e) {
            logger.error( "EsbBeanCodecSupportTLV:", e);
        } catch (Exception e) {
            logger.error( "EsbBeanCodecSupportTLV:", e);
        }
        
        return  new DecResult( target, bytes);
    }

    private byte[] encodeTLV(
    		EncContext 		ctx,
    		ByteFieldCodec 	anyCodec, 
    		Object 			fieldValue, 
    		Class<?> 		fieldType, 
    		ByteFieldDesc 	desc ) {
    	byte[] ret = null;
    	byte[] bytesValue = anyCodec.encode(
        		encContextFactory.createEncContext(
        				fieldValue, fieldType, desc) );
    	
    	if ( null != bytesValue && bytesValue.length > 0 ) {
            //	tag
            byte[] bytesTag = ctx.getNumberCodec().int2Bytes(
            		((EsbFieldDesc)desc).getTag(), TAG_LEN_BYTESIZE);

            //	len
            byte[] bytesLen = ctx.getNumberCodec().int2Bytes(
            		bytesValue.length, TAG_LEN_BYTESIZE);
            
            ret = ArrayUtils.addAll(ret, bytesTag);
            ret = ArrayUtils.addAll(ret, bytesLen);
            ret = ArrayUtils.addAll(ret, bytesValue);
    	}
    	
    	return	ret;
    }
    
    public byte[] encode(EncContext ctx) {
        Object bean = ctx.getEncObject();
        if ( null == bean ) {
            String  errmsg = "EarlyStopBeanCodec: bean is null";
            if ( null != ctx.getField() ) {
                errmsg += "/ cause field is [" + ctx.getField() + "]";
            }
            else {
                errmsg += "/ cause type is [" + ctx.getEncClass() + "]";
            }
            logger.error( errmsg );
            throw new RuntimeException(errmsg);
            //return  new byte[0];
        }
        
        List<ByteFieldDesc> offDesces = util.getFieldDesces( bean.getClass() );
        byte[]  ret = null;
        ByteFieldCodec  anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
        
        for ( ByteFieldDesc desc : offDesces ) {
            Field   field = desc.getField();
            Class<?> fieldClass = field.getType();
            field.setAccessible(true);
            Object fieldValue = null;
            
            try {
                fieldValue = field.get(bean);
            } catch (IllegalArgumentException e) {
                logger.error( "EsbBeanCodecSupportTLV:", e);
            } catch (IllegalAccessException e) {
                logger.error( "EsbBeanCodecSupportTLV:", e);
            }

            if ( null == fieldValue ) {
//                if ( logger.isDebugEnabled() ) {
//                    logger.debug( "compound2Bytes: field ["+ field.getName() +"] value is null" );
//                }
            	
            	//	TODO ? fieldValue is null, and continue encode ?
            }
            else {
            }
            
//            if ( logger.isDebugEnabled() ) {
//                logger.debug( "compound2Bytes: Field[" + field.toString() + "]"
//                        + ", value:" + fieldValue);
//            }
            
            ret = (byte[]) ArrayUtils.addAll(ret, 
                    anyCodec.encode(
                    		encContextFactory.createEncContext(
                    				fieldValue, fieldClass, desc) ) );
        }
        
        //	for tlv support
        List<ByteFieldDesc> tlvDesces = utilForTLV.getFieldDesces(bean.getClass());
        for ( ByteFieldDesc desc : tlvDesces ) {
            
        	Field   field = desc.getField();

            field.setAccessible(true);
            Object fieldValue = null;
            
            try {
                fieldValue = field.get(bean);
            } catch (IllegalArgumentException e) {
                logger.error( "EsbBeanCodecSupportTLV:", e);
            } catch (IllegalAccessException e) {
                logger.error( "EsbBeanCodecSupportTLV:", e);
            }
            
            if ( null == fieldValue ) {
            	continue;
            }
            
            Class<?> fieldClass = field.getType();
            if ( fieldClass.equals(ArrayList.class)) {
            	Class<?> componentType = FieldUtils.getComponentClass(field);

                ArrayList<Object> list = (ArrayList<Object>)fieldValue;
                for ( Object component : list ) {
                	byte[] bytesTLV = encodeTLV(ctx, anyCodec, component, componentType, desc);
                	if ( null != bytesTLV ) {
                        ret = ArrayUtils.addAll(ret, bytesTLV);
                	}
                	else {
                		logger.warn("can't encode field {}", field);
                	}
                }
            }
            else {
            	byte[] bytesTLV = encodeTLV(ctx, anyCodec, fieldValue, fieldClass, desc);
            	if ( null != bytesTLV ) {
                    ret = ArrayUtils.addAll(ret, bytesTLV);
            	}
            	else {
            		logger.warn("can't encode field {}", field);
            	}
            }
        }
        
        return  ret;
    }

    public FieldCodecCategory getCategory() {
        return FieldCodecCategory.BEAN;
    }

    public int getStaticByteSize(Class<?> clazz) {
    	
        List<ByteFieldDesc> desces = util.getFieldDesces(clazz);
        
        if ( null == desces || desces.isEmpty() ) {
        	return	-1;
        }
        
        int staticByteSize = 0;
        
        for ( ByteFieldDesc desc : desces ) {
            int fieldByteSize = desc.getByteSize();
            
            if ( fieldByteSize <= 0 ) {
            	fieldByteSize = getStaticByteSize( desc.getFieldType() );
            }
            
            if ( fieldByteSize <= 0 ) {
                return  -1;
            }
            staticByteSize += fieldByteSize;
        }
            
        return  staticByteSize;
    }

    public Class<?>[] getFieldType() {
        return null;
    }

	public DecContextFactory getDecContextFactory() {
		return decContextFactory;
	}

	public void setDecContextFactory(DecContextFactory decContextFactory) {
		this.decContextFactory = decContextFactory;
	}

	public EncContextFactory getEncContextFactory() {
		return encContextFactory;
	}

	public void setEncContextFactory(EncContextFactory encContextFactory) {
		this.encContextFactory = encContextFactory;
	}
}
