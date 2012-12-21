/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.codec;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.core.BeanFieldCodec;
import com.skymobi.android.bean.bytebean.core.DecContext;
import com.skymobi.android.bean.bytebean.core.DecResult;
import com.skymobi.android.bean.esb.annotation.EsbSignal;
import com.skymobi.android.bean.esb.core.EsbAccess2ModuleSignal;
import com.skymobi.android.bean.esb.core.EsbHeaderable;
import com.skymobi.android.bean.esb.core.EsbModule2AccessSignal;
import com.skymobi.android.bean.esb.core.EsbModule2ModuleSignal;
import com.skymobi.android.bean.util.meta.Int2TypeMetainfo;
import com.skymobi.android.transport.protocol.esb.ESBFixedHeader;
import com.skymobi.android.transport.protocol.esb.hdr.ESBAccess2ModuleHeader;
import com.skymobi.android.transport.protocol.esb.hdr.ESBModule2AccessHeader;
import com.skymobi.android.transport.protocol.esb.hdr.ESBModule2ModuleHeader;
import com.skymobi.android.transport.protocol.esb.hdr.ETFTerminalAccessInfo;
import com.skymobi.android.transport.protocol.esb.hdr.ETFTerminalCookie;
import com.skymobi.android.transport.protocol.esb.hdr.ETFTerminalMessageHeader;
import com.skymobi.android.transport.protocol.esb.hdr.ETFTerminalUserAgent;
import com.skymobi.android.transport.protocol.esb.hdr.ETFTerminalUserAgentNew;
import com.skymobi.android.transport.protocol.esb.hdr.a2m.SanpTerminalMsg;

/**
 * @author Marvin.Ma
 *
 */
public class EsbSignalCodecUtils {
	
    private static final Logger logger = 
    	LoggerFactory.getLogger(EsbSignalCodecUtils.class);
    
	/*	from ESB鏈嶅姟绔唴閮ㄦā鍧椾氦浜掑崗璁�txt
	or http://172.16.3.102/redmine/boards/9/topics/10
	"
	#define ESB_PROTOCOL_CODE_MODULE2MODULE 0x1       ESB涓婄殑妯″潡涔嬮棿鐨勯�淇℃秷鎭�
	#define ESB_PROTOCOL_CODE_ESB2ACCESS    0x2       ESB涓婃ā鍧楀悜缁堢鍙戞秷鎭紝 鐢辨帴鍏ヨ繘琛岃浆鍙�
	#define ESB_PROTOCOL_CODE_ACCESS2ESB    0x3                  鎺ュ叆杞彂鏉ヨ嚜缁堢鐨勬秷鎭埌ESB涓婄殑妯″潡銆�
	"
	*/
	
	/* protocolCode 瀹氫箟 */
	public final static short ESB_PROTOCOL_CODE_MODULE2MODULE = 0x1;       /* ESB涓婄殑妯″潡涔嬮棿鐨勯�淇℃秷鎭�*/
	public final static short ESB_PROTOCOL_CODE_ESB2ACCESS = 0x2;       	/* ESB涓婃ā鍧楀悜缁堢鍙戞秷鎭紝 鐢辨帴鍏ヨ繘琛岃浆鍙�*/
	public final static short ESB_PROTOCOL_CODE_ACCESS2ESB = 0x3;       	/* 鎺ュ叆杞彂鏉ヨ嚜缁堢鐨勬秷鎭埌ESB涓婄殑妯″潡銆�*/

	public static ESBFixedHeader decodeSignalHeader(
			byte[] 			headerBytes, 
			BeanFieldCodec 	esbBeanCodec) throws Exception {
        if ( headerBytes.length < ESBFixedHeader.FIXED_HEADER_SIZE ) {
            return null;
        }
		
    	return	(ESBFixedHeader)esbBeanCodec.decode(
    			esbBeanCodec.getDecContextFactory().createDecContext(
    					headerBytes, ESBFixedHeader.class, null, null) ).getValue();
	}
	
	public static EsbHeaderable decodeSignalBody(
			ESBFixedHeader	fixedHdr,
			byte[] 			bodyBytes, 
			BeanFieldCodec 	esbBeanCodec,
			Int2TypeMetainfo	esbTypeMetainfo) throws Exception {
		
    	switch ( fixedHdr.getProtocolCode() ) {
    	case	ESB_PROTOCOL_CODE_MODULE2MODULE:
    		return	decodeModule2Module(fixedHdr, bodyBytes, esbBeanCodec, esbTypeMetainfo );
    		
    	case	ESB_PROTOCOL_CODE_ESB2ACCESS:
    		return	decodeModule2Access(fixedHdr, bodyBytes, esbBeanCodec, esbTypeMetainfo );
    		
    	case	ESB_PROTOCOL_CODE_ACCESS2ESB:
    		return	decodeAccess2Module(fixedHdr, bodyBytes, esbBeanCodec, esbTypeMetainfo );
    		
    	default:
//    		return	null;
    	}
    	
    	return	null;
	}
	
    private static EsbHeaderable decodeModule2Module(
    		ESBFixedHeader 	fixedHdr, 
    		byte[] 			bytes,
    		BeanFieldCodec 	esbBeanCodec,
			Int2TypeMetainfo	esbTypeMetainfo) {
    	
    	DecResult rslt = esbBeanCodec.decode(
    			esbBeanCodec.getDecContextFactory().createDecContext(
    					bytes, ESBModule2ModuleHeader.class, null, null) );

    	ESBModule2ModuleHeader m2mHdr = (ESBModule2ModuleHeader) rslt.getValue();
    		
        if ( logger.isTraceEnabled()) {
            logger.trace("m2m header {}", m2mHdr);
        }
        
        //	message body bytes
        bytes = rslt.getRemainBytes();
        
        EsbModule2ModuleSignal signal = null;
        Class<?> type = esbTypeMetainfo.find(m2mHdr.getMsgCode());
        if ( null == type ) {
        	throw new RuntimeException("unknow message code for m2m:" + m2mHdr.getMsgCode());
        }
        
        DecContext decCtx = esbBeanCodec.getDecContextFactory().createDecContext(
				bytes, type, null, null);
        
        //	鏀寔鍦∕inaEsbDecoder涓敖鍙兘鏃╃殑杩涜hdr鐩稿叧璧嬪�
		decCtx.setProperty(EsbHeaderable.class.getSimpleName(), fixedHdr);
				
        signal = (EsbModule2ModuleSignal)esbBeanCodec.decode(decCtx).getValue();
        
        //	鍦∕inaEsbDecoder涓凡杩涜鐩稿叧璧嬪�
//        signal.setDstESBAddr(hdr.getDstESBAddr());
//        signal.setSeqNum(hdr.getSeqNum());
//        signal.setResult(hdr.getResult());
//        signal.setFlags(hdr.getFlags());
//        signal.setSrcESBAddr(hdr.getSrcESBAddr());
        
        return signal;
	}

    private static EsbHeaderable decodeAccess2Module(
    		ESBFixedHeader 	fixedHdr, 
    		byte[] 			bytes,
    		BeanFieldCodec 	esbBeanCodec,
			Int2TypeMetainfo	esbTypeMetainfo) {
    	DecResult rslt = esbBeanCodec.decode(
    			esbBeanCodec.getDecContextFactory().createDecContext(
    					bytes, ESBAccess2ModuleHeader.class, null, null) );

    	ESBAccess2ModuleHeader a2mHdr = (ESBAccess2ModuleHeader) rslt.getValue();
    		
        if ( logger.isTraceEnabled() ) {
            logger.trace("a2m header {}", a2mHdr);
        }

        //	message body bytes
        bytes = a2mHdr.getTerminalMsgBody();
        ETFTerminalMessageHeader	msgHdr = a2mHdr.getETFTerminalMessageHeader();
        
        EsbAccess2ModuleSignal signal = null;
        Class<?> type = esbTypeMetainfo.find(msgHdr.getMsgCode());
        if ( null == type ) {
        	throw new RuntimeException("unknow message code for a2m:" + msgHdr.getMsgCode());
        }
        
        DecContext decCtx = esbBeanCodec.getDecContextFactory().createDecContext(
				bytes, type, null, null);
        
        //	鏀寔鍦∕inaEsbDecoder涓敖鍙兘鏃╃殑杩涜hdr鐩稿叧璧嬪�
		decCtx.setProperty(EsbHeaderable.class.getSimpleName(), fixedHdr);
				
        signal = (EsbAccess2ModuleSignal)esbBeanCodec.decode(decCtx).getValue();
        
        if ( null != signal ) {
        	ETFTerminalAccessInfo etai = a2mHdr.getETFTerminalAccessInfo();
        	
        	if ( null != etai ) {
        		signal.setTerminalAccessInfo( etai.toTerminalAccessInfo());
        	}
        	
        	ETFTerminalUserAgent etua = a2mHdr.getETFTerminalUserAgent();
        	
        	if ( null != etua ) {
	        	signal.setTerminalUserAgent( etua.toTerminalUserAgent());
        	}
        	
        	//Modify by bluces.wang 2011-08-25
        	ETFTerminalUserAgentNew etuan = a2mHdr.getETFTerminalUserAgentNew();
        	
        	if ( null != etuan ) {
	        	signal.setTerminalUserAgentNew(etuan.toTerminalUserAgentNew());
        	}
        	
        	ETFTerminalCookie etcookie = a2mHdr.getETFTerminalCookie();
        	
        	if ( null != etcookie ) {
	        	signal.setTerminalCookie( etcookie.toTerminalCookie());
        	}
        	
        	signal.setTerminalMessageHeader(
        			msgHdr.toTerminalMessageHeader());
        }
        
        return signal;
    }
    
    private static EsbHeaderable decodeModule2Access(
    		ESBFixedHeader 	fixedHdr, 
    		byte[] 			bytes,
    		BeanFieldCodec 	esbBeanCodec,
			Int2TypeMetainfo	esbTypeMetainfo) {
    	DecResult rslt = esbBeanCodec.decode(
    			esbBeanCodec.getDecContextFactory().createDecContext(
    					bytes, ESBModule2AccessHeader.class, null, null) );

    	ESBModule2AccessHeader m2aHdr = (ESBModule2AccessHeader) rslt.getValue();
    		
        if ( logger.isTraceEnabled() ) {
            logger.trace("m2a header {}", m2aHdr);
        }

        //	message body bytes
        bytes = rslt.getRemainBytes();
        
        ETFTerminalMessageHeader	msgHdr = m2aHdr.getETFTerminalMessageHeader();
        
        EsbModule2AccessSignal signal = null;
        Class<?> type = esbTypeMetainfo.find(msgHdr.getMsgCode());
        if ( null == type ) {
        	throw new RuntimeException("unknow message code for a2m:" + msgHdr.getMsgCode());
        }
        
        DecContext decCtx = esbBeanCodec.getDecContextFactory().createDecContext(
				bytes, type, null, null);
        
        //	鏀寔鍦∕inaEsbDecoder涓敖鍙兘鏃╃殑杩涜hdr鐩稿叧璧嬪�
		decCtx.setProperty(EsbHeaderable.class.getSimpleName(), fixedHdr);
				
        signal = (EsbModule2AccessSignal)esbBeanCodec.decode(decCtx).getValue();
        
        if ( null != signal ) {
        	signal.setTerminalAccessInfo(
        			m2aHdr.getETFTerminalAccessInfo().toTerminalAccessInfo());
        	signal.setTerminalMessageHeader(msgHdr.toTerminalMessageHeader());
        }
        
        return signal;
    }
	
	
	public static byte[] encodeSignal(
			EsbHeaderable 	signal, 
			BeanFieldCodec 	esbBeanCodec,
		    int				myESBAddr) throws Exception {
		
		if ( null == signal || !signal.checkIntegrity()) {
			logger.error("invalid signal {}", signal);
			
			throw new RuntimeException("invalid signal, signal is null or checkIntegrity failed");
		}
		
    	byte[] bytes = null;
    	if ( signal instanceof EsbModule2ModuleSignal ) {
    		bytes = encodeModule2Module((EsbModule2ModuleSignal)signal, esbBeanCodec, myESBAddr );
    	}
    	else if ( signal instanceof EsbAccess2ModuleSignal ) {
    		bytes = encodeAccess2Module((EsbAccess2ModuleSignal)signal, esbBeanCodec, myESBAddr );
    	}
    	else if ( signal instanceof EsbModule2AccessSignal ) {
    		bytes = encodeModule2Access((EsbModule2AccessSignal)signal, esbBeanCodec, myESBAddr );
    	}
    	else {
	        throw   new RuntimeException("encode: bean " + signal 
	        		+ " is not EsbAccess2ModuleSignal/EsbModule2AccessSignal/EsbModule2ModuleSignal.");
    	}
		
    	return	bytes;
	}
	
	private static byte[] encodeModule2Module( 
			EsbModule2ModuleSignal 	signal, 
			BeanFieldCodec 			esbBeanCodec,
			int						myESBAddr) throws Exception {
    	//	once
        byte[] bytesBody =
        	esbBeanCodec.encode(
    			esbBeanCodec.getEncContextFactory().createEncContext(
    					signal, signal.getClass(), null));
        
        int	bodyLength = (null != bytesBody) ? bytesBody.length : 0;
        
		EsbSignal attr = signal.getClass().getAnnotation(EsbSignal.class);
		if ( null == attr ) {
			throw new RuntimeException("invalid esb signal, bcs of no messageCode.");
		}
		
		ESBFixedHeader	fixedHdr = new ESBFixedHeader();

		fixedHdr.setDstESBAddr(signal.getDstESBAddr());
		fixedHdr.setFlags(signal.getFlags());
		fixedHdr.setProtocolCode(ESB_PROTOCOL_CODE_MODULE2MODULE);
		fixedHdr.setResult(signal.getResult());
		fixedHdr.setSeqNum(signal.getSeqNum());
		fixedHdr.setSrcESBAddr( ( 0 == signal.getSrcESBAddr() ) ? myESBAddr : signal.getSrcESBAddr() );
		
		//	create esb module2module header
		ESBModule2ModuleHeader	m2mHdr = new ESBModule2ModuleHeader();
		
		m2mHdr.setMsgCode(attr.messageCode());
		
		fixedHdr.setLength(
				//	娑堟伅澶�
				esbBeanCodec.getStaticByteSize(ESBFixedHeader.class) 
				+ esbBeanCodec.getStaticByteSize(ESBModule2ModuleHeader.class) 
				+ bodyLength );
		
        byte[] bytesHdr = ArrayUtils.addAll(
        	esbBeanCodec.encode(
    				esbBeanCodec.getEncContextFactory().createEncContext(
    						fixedHdr, ESBFixedHeader.class, null) ),
        	esbBeanCodec.encode(
    				esbBeanCodec.getEncContextFactory().createEncContext(
    						m2mHdr, ESBModule2ModuleHeader.class, null) ) );
        
        byte[] bytes = ArrayUtils.addAll(bytesHdr, bytesBody );
        
        return	bytes;
	}
	
	private static byte[] encodeModule2Access( 
			EsbModule2AccessSignal 	signal,
			BeanFieldCodec 			esbBeanCodec,
			int						myESBAddr) throws Exception {
    	//	once
        byte[] bytesBody =
        	esbBeanCodec.encode(
    			esbBeanCodec.getEncContextFactory().createEncContext(
    					signal, signal.getClass(), null));
        
        int	bodySize = (null != bytesBody) ? bytesBody.length : 0;
        
		EsbSignal attr = signal.getClass().getAnnotation(EsbSignal.class);
		if ( null == attr ) {
			throw new RuntimeException("invalid esb signal, bcs of no messageCode.");
		}
		
		ESBFixedHeader	fixedHdr = new ESBFixedHeader();

		fixedHdr.setDstESBAddr(signal.getDstESBAddr());
		fixedHdr.setFlags(signal.getFlags());
		fixedHdr.setProtocolCode(ESB_PROTOCOL_CODE_ESB2ACCESS);
		fixedHdr.setResult(signal.getResult());
		fixedHdr.setSeqNum(signal.getSeqNum());
		fixedHdr.setSrcESBAddr( ( 0 == signal.getSrcESBAddr() ) ? myESBAddr : signal.getSrcESBAddr() );
		
		//	create esb module2access header
		ESBModule2AccessHeader	m2aHdr = new ESBModule2AccessHeader();
		
		ETFTerminalMessageHeader msgHdr = 
			ETFTerminalMessageHeader.fromTerminalMessageHeader( 
					signal.getTerminalMessageHeader() );

		//	fill msg's code and msg's length
		msgHdr.setMsgCode(attr.messageCode());
		msgHdr.setLengthWithMessageBodySize((short)bodySize);
		
		m2aHdr.setETFTerminalMessageHeader(msgHdr);
		
		m2aHdr.setETFTerminalAccessInfo(
				ETFTerminalAccessInfo.fromTerminalAccessInfo(
						signal.getTerminalAccessInfo()));

		byte[] bytesM2AHdr = 
        	esbBeanCodec.encode(
    				esbBeanCodec.getEncContextFactory().createEncContext(
    						m2aHdr, ESBModule2AccessHeader.class, null) );
			
		fixedHdr.setLength(
				esbBeanCodec.getStaticByteSize(ESBFixedHeader.class) 
				+ bytesM2AHdr.length
				+ bodySize );
		
        byte[] bytesHdr = ArrayUtils.addAll(
        	esbBeanCodec.encode(
    				esbBeanCodec.getEncContextFactory().createEncContext(
    						fixedHdr, ESBFixedHeader.class, null) ),
    		bytesM2AHdr );
        
        byte[] bytes = ArrayUtils.addAll(bytesHdr, bytesBody );
        
        return	bytes;
	}
	
	private static byte[] encodeAccess2Module( 
			EsbAccess2ModuleSignal 	signal,
			BeanFieldCodec 			esbBeanCodec,
			int						myESBAddr) throws Exception {
		//	create esb access2module header
		ESBAccess2ModuleHeader	a2mHdr = new ESBAccess2ModuleHeader();
		
		a2mHdr.setETFTerminalAccessInfo(
			ETFTerminalAccessInfo.fromTerminalAccessInfo(
				signal.getTerminalAccessInfo()) );

		//	add ua
		if ( null != signal.getTerminalUserAgent() ) {
			a2mHdr.setETFTerminalUserAgent(
				ETFTerminalUserAgent.fromTerminalUserAgent(
					signal.getTerminalUserAgent()) );
		}
		
		//	add new ua. modify by bluces.wang 2011-08-25
		if ( null != signal.getTerminalUserAgentNew() ) {
			a2mHdr.setETFTerminalUserAgentNew(
				ETFTerminalUserAgentNew.fromTerminalUserAgentNew(
					signal.getTerminalUserAgentNew()) );
		}
		//	add cookie
		if ( null != signal.getTerminalCookie() ) {
			a2mHdr.setETFTerminalCookie(
				ETFTerminalCookie.fromTerminalCookie( 
					signal.getTerminalCookie()) );
		}
		
		//	add terminal message
        byte[] bytesMsgBody =
        	esbBeanCodec.encode(
    			esbBeanCodec.getEncContextFactory().createEncContext(
    					signal, signal.getClass(), null));
        
        int	msgBodySize = (null != bytesMsgBody) ? bytesMsgBody.length : 0;
        
		EsbSignal attr = signal.getClass().getAnnotation(EsbSignal.class);
		if ( null == attr ) {
			throw new RuntimeException("invalid esb signal, bcs of no messageCode.");
		}
		
		a2mHdr.setTerminalMsg(
			new SanpTerminalMsg().setETFTerminalMessageHeader(
				ETFTerminalMessageHeader.fromTerminalMessageHeader(
						signal.getTerminalMessageHeader())
							//	fill msg's code and msg's length
							.setMsgCode(attr.messageCode())
							.setLengthWithMessageBodySize((short)msgBodySize) )
					.setMsgBody(bytesMsgBody) );

		byte[] bytesA2MHdr = 
        	esbBeanCodec.encode(
    				esbBeanCodec.getEncContextFactory().createEncContext(
    						a2mHdr, ESBAccess2ModuleHeader.class, null) );
			
		ESBFixedHeader	fixedHdr = new ESBFixedHeader();

		fixedHdr.setDstESBAddr(signal.getDstESBAddr());
		fixedHdr.setFlags(signal.getFlags());
		fixedHdr.setProtocolCode(ESB_PROTOCOL_CODE_ACCESS2ESB);
		fixedHdr.setResult(signal.getResult());
		fixedHdr.setSeqNum(signal.getSeqNum());
		fixedHdr.setSrcESBAddr( ( 0 == signal.getSrcESBAddr() ) ? myESBAddr : signal.getSrcESBAddr() );
		fixedHdr.setLength(
				esbBeanCodec.getStaticByteSize(ESBFixedHeader.class) 
				+ bytesA2MHdr.length );
		
        byte[] bytes = ArrayUtils.addAll(
        	esbBeanCodec.encode(
    				esbBeanCodec.getEncContextFactory().createEncContext(
    						fixedHdr, ESBFixedHeader.class, null) ),
    		bytesA2MHdr );
        
        return	bytes;
	}
	
}
