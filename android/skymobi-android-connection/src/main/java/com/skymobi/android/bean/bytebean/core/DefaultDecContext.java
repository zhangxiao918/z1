/**
 * 
 */
package com.skymobi.android.bean.bytebean.core;

import java.util.Map;

import com.skymobi.android.util.DefaultPropertiesSupport;
import com.skymobi.android.util.NumberCodec;

/**
 * @author isdom
 *
 */
public class DefaultDecContext extends AbstractContext implements DecContext {

    private byte[]          decBytes;
    private Object          decOwner;
    private	DefaultPropertiesSupport	propertiesSupport = 
    	new DefaultPropertiesSupport();
    private	DecContextFactory	decContextFactory;
    
    public DefaultDecContext setCodecProvider(FieldCodecProvider codecProvider) {
        this.codecProvider = codecProvider;
        return  this;
    }

    /**
     * @param decClass the decClass to set
     */
    public DefaultDecContext setDecClass(Class<?> decClass) {
        this.targetType = decClass;
        return  this;
    }

    /**
     * @param decBytes the decBytes to set
     */
    public DefaultDecContext setDecBytes(byte[] decBytes) {
        this.decBytes = decBytes;
        return  this;
    }

    /**
     * @param decImpl the decImpl to set
     */
    public DefaultDecContext setFieldDesc(ByteFieldDesc desc) {
        this.fieldDesc = desc;
        return  this;
    }

    /**
     * @param decOwner the decOwner to set
     */
    public DefaultDecContext setDecOwner(Object decOwner) {
        this.decOwner = decOwner;
        return  this;
    }

    /**
     * @param numberCodec the numberCodec to set
     */
    public DefaultDecContext setNumberCodec(NumberCodec numberCodec) {
        this.numberCodec = numberCodec;
        return  this;
    }

	/**
	 * @param decContextFactory the decContextFactory to set
	 */
	public DefaultDecContext setDecContextFactory(DecContextFactory decContextFactory) {
		this.decContextFactory = decContextFactory;
        return  this;
	}
	
    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.DecContext#getDecBytes()
     */
    public byte[] getDecBytes() {
        return decBytes;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.DecContext#getDecClass()
     */
    public Class<?> getDecClass() {
        return this.targetType;
    }

    /* (non-Javadoc)
     * @see com.sky.applist20.bytebean.codec.DecContext#getDecOwner()
     */
    public Object getDecOwner() {
        return decOwner;
    }

	public Map<String, Object> getProperties() {
		return propertiesSupport.getProperties();
	}

	public Object getProperty(String key) {
		return propertiesSupport.getProperty(key);
	}

	public void setProperties(Map<String, Object> properties) {
		propertiesSupport.setProperties(properties);
	}

	public void setProperty(String key, Object value) {
		propertiesSupport.setProperty(key, value);
	}

	public DecContextFactory getDecContextFactory() {
		return decContextFactory;
	}
}
