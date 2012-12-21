/**
 * 
 */
package com.skymobi.android.bean.xip.util;

import java.util.UUID;
import org.apache.commons.lang.ArrayUtils;

import com.skymobi.android.bean.bytebean.core.BeanFieldCodec;
import com.skymobi.android.bean.util.meta.Int2TypeMetainfo;
import com.skymobi.android.bean.xip.annotation.SaipSignalAnnotation;
import com.skymobi.android.bean.xip.core.SaipSignal;
import com.skymobi.android.transport.protocol.xip.XipHeader;

/**
 * @author Marvin.Ma
 *
 */
public class XipSignalUtils {

	static private XipHeader createHeader(
			byte 	basicVer, 
			UUID 	id, 
			int 	messageCode, 
			int 	messageLen,
			int		headerSize ) {

		XipHeader 	header = new XipHeader();

        header.setTransaction(id);
        
        header.setLength(headerSize + messageLen);
        header.setMessageLength(messageLen);
		header.setMessageCode( messageCode );
		header.setBasicVer(basicVer);
		
		return	header;
	}
	
	public static byte[] encodeSaipSignal(
			SaipSignal 		signal, 
			BeanFieldCodec 	beanCodec) throws Exception {
    	//	once
        byte[] bytesBody =
        	beanCodec.encode(
        			beanCodec.getEncContextFactory().createEncContext(
        					signal, signal.getClass(), null));
        
		SaipSignalAnnotation attr = 
			signal.getClass().getAnnotation(SaipSignalAnnotation.class);
		if ( null == attr ) {
			throw new RuntimeException("invalid saip signal, bcs of no messageCode.");
		}
		
        XipHeader 	header = createHeader(
        		(byte)1, 
        		signal.getIdentification(), 
        		attr.messageCode(), 
        		null == bytesBody ? 0 : bytesBody.length, 
        		XipHeader.SAIP_HEADER_LENGTH);
        
    	header.setTypeForClass(signal.getClass());
    	header.setSrcModule(signal.getSrcModule());
    	header.setDstModule(signal.getDstModule());

        byte[] bytes = ArrayUtils.addAll(
        	beanCodec.encode(
    				beanCodec.getEncContextFactory().createEncContext(
    						header, XipHeader.class, null) ),
    		bytesBody );
        
        return	bytes;
	}

	public static XipHeader decodeSaipSignalHeader(
			byte[] 			headerBytes, 
			BeanFieldCodec 	beanCodec) throws Exception {
        if ( headerBytes.length < XipHeader.SAIP_HEADER_LENGTH ) {
            return null;
        }
		
    	return	(XipHeader)beanCodec.decode(
    			beanCodec.getDecContextFactory().createDecContext(
    					headerBytes, XipHeader.class, null, null) ).getValue();
	}
	
	public static SaipSignal decodeSaipSignalBody(
			XipHeader		header,
			byte[] 			bodyBytes, 
			BeanFieldCodec 	beanCodec,
			Int2TypeMetainfo	typeMetainfo) throws Exception {
        Class<?> type = typeMetainfo.find(header.getMessageCode());
        if ( null == type ) {
        	throw new RuntimeException("unknow message code:" + header.getMessageCode());
        }
        SaipSignal signal = (SaipSignal)beanCodec.decode(
    			beanCodec.getDecContextFactory().createDecContext(
    					bodyBytes, type, null, null) ).getValue();
		if ( null != signal ) {
			signal.setIdentification(header.getTransactionAsUUID());
			signal.setSrcModule(header.getSrcModule());
			signal.setDstModule(header.getDstModule());
		}
		
		return	signal;
	}
}
