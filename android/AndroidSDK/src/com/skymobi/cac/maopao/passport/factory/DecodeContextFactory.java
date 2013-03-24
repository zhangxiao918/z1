package com.skymobi.cac.maopao.passport.factory;

import java.util.HashMap;
import java.util.Map;

import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.DefaultDecodeContextFactory;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.DefaultDecoderRepository;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.TLVDecoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.decoders.BeanTLVDecoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.decoders.ByteArrayTLVDecoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.decoders.ByteTLVDecoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.decoders.IntTLVDecoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.decoders.ShortTLVDecoder;
import com.skymobi.cac.maopao.passport.android.bean.tlv.decode.decoders.StringTLVDecoder;
import com.skymobi.cac.maopao.passport.android.util.DefaultNumberCodecs;

public class DecodeContextFactory {
	
	private static  DefaultDecodeContextFactory instance = null;
	
	private DecodeContextFactory(){}
	
	public synchronized static DefaultDecodeContextFactory getInstance(){
		if(instance == null){
			instance = new DefaultDecodeContextFactory();
			DefaultDecoderRepository repo = new DefaultDecoderRepository();
			repo.setDecoders(getDecoders());
			instance.setDecoderRepository(repo);
			instance.setNumberCodec(DefaultNumberCodecs.getBigEndianNumberCodec());
		}
		return instance;
	}
	
	private static Map<Class<?>, TLVDecoder> getDecoders(){
		Map<Class<?>, TLVDecoder> decoders = new HashMap<Class<?>, TLVDecoder>();
		decoders.put(String.class, new StringTLVDecoder());
		decoders.put(Integer.class, new IntTLVDecoder());
		decoders.put(int.class, new IntTLVDecoder());
		decoders.put(Byte.class, new ByteTLVDecoder());
		decoders.put(byte.class, new ByteTLVDecoder());
		decoders.put(byte[].class, new ByteArrayTLVDecoder());
		decoders.put(Short.class, new ShortTLVDecoder());
		decoders.put(short.class, new ShortTLVDecoder());
		decoders.put(Object.class, new BeanTLVDecoder());
		return decoders;
	}

}
