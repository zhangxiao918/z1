/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.skymobi.cac.maopao.passport.android.util.ByteOrder;



/**
 * @author isdom
 *
 */
public class DefaultFieldDesc extends ByteBeanUtil implements ByteFieldDesc {
    private Field   field;
    private int     maxByteSize = -1;
    private int     index;
    private int     byteSize = -1;
    private int     lengthFieldIndex = -1;
    private Field   lengthField = null;
    private Method  customTypeMethod = null;
    private String  charset;
    private int     bytesPerChar = 1;
    private String  description;
    private ByteOrder   byteOrder = ByteOrder.None;
    private int     fixedLength = -1;
    private	Class<? extends ByteFieldCodec>	customCodec = null;
    
    /**
	 * @param customCodec the customCodec to set
	 */
	public DefaultFieldDesc setCustomCodec(Class<? extends ByteFieldCodec> customCodec) {
		this.customCodec = customCodec;
        return  this;
	}

	/**
     * @param field the field to set
     */
    public DefaultFieldDesc setField(Field field) {
        this.field = field;
        maxByteSize = super.type2DefaultByteSize(this.field.getType());
        return  this;
    }

    /**
     * @param index the index to set
     */
    public DefaultFieldDesc setIndex(int index) {
        this.index = index;
        return  this;
    }

    /**
     * @param byteSize the byteSize to set
     */
    public DefaultFieldDesc setByteSize(int byteSize) {
        this.byteSize = byteSize;
        return  this;
    }

    /**
     * @param lengthField the lengthField to set
     */
    public DefaultFieldDesc setLengthField(Field lengthField) {
        this.lengthField = lengthField;
        return  this;
    }

    /**
     * @param customTypeMethod the customTypeMethod to set
     */
    public DefaultFieldDesc setCustomTypeMethod(Method customTypeMethod) {
        this.customTypeMethod = customTypeMethod;
        return  this;
    }

    /**
     * @param charset the charset to set
     */
    public DefaultFieldDesc setCharset(String charset) {
        this.charset = charset;
        if ( charset.startsWith("UTF-16") ) {
            bytesPerChar = 1;	//对于客户端解码来说，UTF-16已经在服务器端*2，这里不在重复乘2
        }
        else {
            bytesPerChar = 1;
        }
        return  this;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.ByteFieldDesc#getByteSize(int)
     */
    public int getByteSize() {
        return  -1 == byteSize ? maxByteSize : Math.min( byteSize, maxByteSize );
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.ByteFieldDesc#getCharset()
     */
    public String getCharset() {
        return charset;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.ByteFieldDesc#getCustomType(java.lang.Object)
     */
    public Class<?> getCustomType(Object owner) {
        if ( null == owner ) {
            return  null;
        }
        if ( null == customTypeMethod ) {
            return  null;
        }
        customTypeMethod.setAccessible(true);
        try {
            Class<?> retClass = (Class<?>)customTypeMethod.invoke( owner );
            if ( null == retClass ) {
                String errmsg = "customTypeMethod return null. ";
                if ( null != this.field ) {
                    errmsg += "field is [" + this.field + "].";
                }
                
                throw new RuntimeException(errmsg);
            }
            
            return  retClass;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return  null;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.ByteFieldDesc#getField()
     */
    public Field getField() {
        return field;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.ByteFieldDesc#getLength(java.lang.Object)
     */
    public int getLength(Object owner) {
        if ( null == owner ) {
            return  -1;
        }
        if ( null == lengthField ) {
            return  -1;
        }
        lengthField.setAccessible(true);
        try {
            Object value = lengthField.get(owner);
            if ( null == value ) {
                return  -1;
            }
            if (value instanceof Long) {
                return  ((Long)value).intValue();
            }
            else if (value instanceof Integer) {
                return  ((Integer)value).intValue();
            }
            else if (value instanceof Short) {
                return  ((Short)value).intValue();
            }
            else if (value instanceof Byte) {
                return  ((Byte)value).intValue();
            }
            else {
                return  -1;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return  -1;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return  -1;
        }
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.ByteFieldDesc#getStringLengthInBytes(java.lang.Object)
     */
    public int getStringLengthInBytes(Object owner) {
        return  getLength(owner) * this.bytesPerChar;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.ByteFieldDesc#hasLength()
     */
    public boolean hasLength() {
        return null != lengthField;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.ByteFieldDesc#setLength(java.lang.Object)
     */
    public void setLength(Object owner) {
        if ( null == owner ) {
            return;
        }
        if ( null == lengthField ) {
            return;
        }
        
        field.setAccessible(true);
        lengthField.setAccessible(true);
        try {
            Object value = field.get(owner);
            int length = -1;
            
            if ( null == value ) {
                length = 0;
            }
            else if ( value.getClass().isArray() ) {
                length = Array.getLength(value);
            }
            else if ( value instanceof String ) {
                length = ((String)value).length();
            }
            
            if ( length >= 0 ) {
                if ( lengthField.getType().equals(byte.class) ) {
                    lengthField.set(owner, (byte)length);
                }
                else if ( lengthField.getType().equals(short.class) ) {
                    lengthField.set(owner, (short)length);
                }
                else if ( lengthField.getType().equals(int.class) ) {
                    lengthField.set(owner, (int)length);
                }
                else if ( lengthField.getType().equals(long.class) ) {
                    lengthField.set(owner, (long)length);
                }
                else {
                    throw new RuntimeException("invalid length field.");
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public int getIndex() {
        return this.index;
    }

    public Class<?> getFieldType() {
        return field.getType();
    }

	public String getDescription() {
		return description;
	}

	public DefaultFieldDesc setDescription(String description){
		this.description = description;
		return this;
	}

    public ByteOrder getByteOrder() {
        return this.byteOrder;
    }
    
    public DefaultFieldDesc setByteOrder(ByteOrder byteOrder) {
        this.byteOrder = byteOrder;
        return this;
    }

    public int getFixedLength() {
        return this.fixedLength;
    }
    
    public DefaultFieldDesc setFixedLength(int fixedLength) {
        this.fixedLength = fixedLength;
        return this;
    }

	public Class<? extends ByteFieldCodec> getCustomCodec() {
		return customCodec;
	}

	public int getLengthFieldIndex() {
		return lengthFieldIndex;
	}

	public DefaultFieldDesc setLengthFieldIndex(int lengthFieldIndex) {
		this.lengthFieldIndex = lengthFieldIndex;
		return this;
	}
	
	
}
