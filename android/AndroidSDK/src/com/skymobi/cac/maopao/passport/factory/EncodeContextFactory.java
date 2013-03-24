package com.skymobi.cac.maopao.passport.factory;

import java.util.HashMap;
import java.util.Map;

import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.DefaultEncodeContextFactory;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.DefaultEncoderRepository;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.TLVEncoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.encoders.BeanTLVEncoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.encoders.ByteArrayTLVEncoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.encoders.ByteTLVEncoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.encoders.IntTLVEncoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.encoders.ShortTLVEncoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.encode.encoders.StringTLVEncoder;
import com.skymobi.cac.maopao.passport.android.util.DefaultNumberCodecs;

/**
 * 编码
 * 
 * */
public class EncodeContextFactory {
	
	private static  DefaultEncodeContextFactory instance = null;
	
	private EncodeContextFactory(){}
	
	public synchronized static DefaultEncodeContextFactory getInstance(){
		if(instance == null){
			instance = new DefaultEncodeContextFactory();
			DefaultEncoderRepository repo = new DefaultEncoderRepository();
			repo.setEncoders(getEncoders());
			instance.setEncoderRepository(repo);
			instance.setNumberCodec(DefaultNumberCodecs.getBigEndianNumberCodec());
		}
		return instance;
	}
	
	private static Map<Class<?>, TLVEncoder> getEncoders(){
		Map<Class<?>, TLVEncoder> encoders = new HashMap<Class<?>, TLVEncoder>();
		encoders.put(String.class, new StringTLVEncoder());
		encoders.put(Integer.class, new IntTLVEncoder());
		encoders.put(int.class, new IntTLVEncoder());
		encoders.put(Byte.class, new ByteTLVEncoder());
		encoders.put(byte.class, new ByteTLVEncoder());
		encoders.put(byte[].class, new ByteArrayTLVEncoder());
		encoders.put(Short.class, new ShortTLVEncoder());
		encoders.put(short.class, new ShortTLVEncoder());
		encoders.put(Object.class, new BeanTLVEncoder());
		return encoders;
	}

}
