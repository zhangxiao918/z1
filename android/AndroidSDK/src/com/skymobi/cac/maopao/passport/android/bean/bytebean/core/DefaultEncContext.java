/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.bytebean.core;

import com.skymobi.cac.maopao.passport.android.util.NumberCodec;

/**
 * @author isdom
 *
 */
public class DefaultEncContext extends AbstractContext implements EncContext {
    private Object          encObject;
    private	EncContextFactory	encContextFactory;

    public DefaultEncContext setCodecProvider(FieldCodecProvider codecProvider) {
        this.codecProvider = codecProvider;
        return  this;
    }
    
    /**
     * @param encClass the encClass to set
     */
    public DefaultEncContext setEncClass(Class<?> encClass) {
        this.targetType = encClass;
        return  this;
    }

    /**
     * @param encImpl the encImpl to set
     */
    public DefaultEncContext setFieldDesc(ByteFieldDesc desc) {
        this.fieldDesc = desc;
        return  this;
    }

    /**
     * @param numberCodec the numberCodec to set
     */
    public DefaultEncContext setNumberCodec(NumberCodec numberCodec) {
        this.numberCodec = numberCodec;
        return  this;
    }

    /**
     * @param encObject the encObject to set
     */
    public DefaultEncContext setEncObject(Object encObject) {
        this.encObject = encObject;
        return  this;
    }

	/**
	 * @param encContextFactory the encContextFactory to set
	 */
	public DefaultEncContext setEncContextFactory(EncContextFactory encContextFactory) {
		this.encContextFactory = encContextFactory;
		return	this;
	}
	
    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.EncContext#getEncClass()
     */
    public Class<?> getEncClass() {
        return this.targetType;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.EncContext#getEncObject()
     */
    public Object getEncObject() {
        return encObject;
    }

	public EncContextFactory getEncContextFactory() {
		return encContextFactory;
	}
}
