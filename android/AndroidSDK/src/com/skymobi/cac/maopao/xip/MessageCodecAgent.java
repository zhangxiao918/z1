package com.skymobi.cac.maopao.xip;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang.ArrayUtils;

import android.util.Log;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteBean;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.ByteFieldToDesc;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.codec.AnyCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.codec.ArrayCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.codec.ByteCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.codec.EarlyStopBeanCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.codec.IntCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.codec.LongCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.codec.ShortCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.codec.StringCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.BeanFieldCodec;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DefaultCodecProvider;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DefaultDecContextFactory;
import com.skymobi.cac.maopao.passport.android.bean.bytebean.core.DefaultEncContextFactory;
import com.skymobi.cac.maopao.passport.android.util.DefaultNumberCodecs;
import com.skymobi.cac.maopao.utils.d;

public class MessageCodecAgent {
	
	private final static int HEADER_SIZE = 8;
	private static MessageCodecAgent instance = new MessageCodecAgent();
	
	private MessageCodecAgent(){
		
	}
	
	public static MessageCodecAgent getInstance(){
		return instance;
	}
	
	private BeanFieldCodec byteBeanCodec;
	
	/**
	 * 
	 * @param message 消息对象
	 * @param msgSeq 消息序号
	 * @param msgAck 
	 * @param encryptKey 消息加密密钥
	 * @return 经过加密的二进制byte数组
	 * @throws Exception
	 */
	public byte[] encodeXip(XipMessage message) throws Exception{
		
		byte[] bytes =  getByteBeanCodec().encode(
				getByteBeanCodec().getEncContextFactory().createEncContext(message, message.getClass(), null));
		
		byte[] header = new byte[HEADER_SIZE];
		header = ArrayUtils.subarray(bytes, 0, HEADER_SIZE);
		byte[] body = ArrayUtils.subarray(bytes, HEADER_SIZE, bytes.length);
		
		ByteBuffer headBuffer = ByteBuffer.wrap(header);
		headBuffer.position(4); //跳到length的位置
		headBuffer.putChar((char)(bytes.length)); //重写长度
		byte[] results = ArrayUtils.addAll(headBuffer.array(), body);
		return results;
	}

	public byte[] encryptReqBytes(byte[] bytes, int msgSeq, int msgAck, byte[] encryptKey) {
		
		byte[] header = new byte[HEADER_SIZE];
		header = ArrayUtils.subarray(bytes, 0, HEADER_SIZE);
		
		//增加msgSeq, msgAck,加密
		ByteBuffer bodyBuffer = ByteBuffer.allocate(bytes.length - HEADER_SIZE + 4);
		bodyBuffer.order(ByteOrder.BIG_ENDIAN);
		bodyBuffer.putChar((char)msgSeq);
		bodyBuffer.putChar((char)msgAck);
		bodyBuffer.put(ArrayUtils.subarray(bytes, HEADER_SIZE, bytes.length));
		
		ByteBuffer headBuffer = ByteBuffer.wrap(header);
		headBuffer.position(4); //跳到length的位置
		
		byte[] encryptBody = d.en(encryptKey, bodyBuffer.array());
		headBuffer.putChar((char)(header.length + encryptBody.length)); //重写长度
		byte[] results = ArrayUtils.addAll(headBuffer.array(), encryptBody);
		return results;
	}
	
	/**
	 * 
	 * @param bytes 除去srcId,dstId,length, flag,msgCode之后的字节数组
	 * @param header 
	 * @param messageClass xipBody
	 * @return 解码之后的xipBody
	 */
	public XipBody decodeXip(byte[] bytes, AccessHeader header, Class<?> messageClass){
		if(!XipBody.class.isAssignableFrom(messageClass)){
			Log.d(this.getClass().getCanonicalName(), "parameter wrong while decode xip, need XipBody");
			return null;
		}
		XipBody result = (XipBody)getByteBeanCodec().decode(getByteBeanCodec().getDecContextFactory().createDecContext(bytes, messageClass, null, null)).getValue();
		result.setHeader(header);
		return result;
	}
	
	
	private BeanFieldCodec getByteBeanCodec() {
		if (byteBeanCodec == null) {
			DefaultCodecProvider codecProvider = new DefaultCodecProvider();
			codecProvider.addCodec(new StringCodec()).addCodec(new LongCodec()).addCodec(new IntCodec()).addCodec(new ShortCodec()).addCodec(new ByteCodec()).addCodec(new AnyCodec(ByteBean.class));

			DefaultDecContextFactory decCtxFactory = new DefaultDecContextFactory();
			decCtxFactory.setCodecProvider(codecProvider);
			decCtxFactory.setNumberCodec(DefaultNumberCodecs.getBigEndianNumberCodec());

			DefaultEncContextFactory encCtxFactory = new DefaultEncContextFactory();
			encCtxFactory.setCodecProvider(codecProvider);
			encCtxFactory.setNumberCodec(DefaultNumberCodecs.getBigEndianNumberCodec());

			EarlyStopBeanCodec byteBeanCodec = new EarlyStopBeanCodec(ByteBean.class, new ByteFieldToDesc());
			byteBeanCodec.setDecContextFactory(decCtxFactory);
			byteBeanCodec.setEncContextFactory(encCtxFactory);

			codecProvider.addCodec(byteBeanCodec);

			ArrayCodec arrayCodec = new ArrayCodec();
			arrayCodec.setDecContextFactory(decCtxFactory);
			arrayCodec.setEncContextFactory(encCtxFactory);

			codecProvider.addCodec(arrayCodec);

			this.byteBeanCodec = byteBeanCodec;
		}
		return byteBeanCodec;
	}
	
}
