/**
 * 
 */
package com.skymobi.android.bean.mo;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.skymobi.android.bean.bytebean.codec.BeanCodec;
import com.skymobi.android.util.Decrypt;

/**
 * @author hp
 *
 */
public class SmsMOEncryptionFactory {
	
    private static final Logger logger = LoggerFactory.getLogger(SmsMOEncryptionFactory.class);
    
    private	BeanCodec 	beanCodec;
    
	/**
	 * @return the beanCodec
	 */
	public BeanCodec getBeanCodec() {
		return beanCodec;
	}

	/**
	 * @param beanCodec the beanCodec to set
	 */
	public void setBeanCodec(BeanCodec beanCodec) {
		this.beanCodec = beanCodec;
	}

	public String	createMOEncryption(MOEncryptionContext ctx) {
		SmsMOMessage	mo = new SmsMOMessage();

		MOEncryptionSource	source = ctx.getSource();
		
		mo.setAppid(source.getAppid());
		mo.setAppVer(source.getAppver());
		mo.setChargeVer((byte)source.getCharver());
		
		mo.setManufactory(source.getHsman());
		mo.setHandsetType(source.getHstype());
		mo.setImsi(source.getImsi());
		mo.setImei(source.getImei());
		mo.setHsver(source.getPortv());
		
		mo.setMoFlag(ctx.getMoFlag());
		
		mo.setMsgnum((byte) ctx.getMessageNum());
		mo.setMsgidx((byte) ctx.getMessageIdx());
		mo.setSubType((byte)source.getSubType());
		
		mo.setVmver(source.getVmv());
//		mo.setChargeModule((short)1);
		
		byte[] bytes = beanCodec.encode(
				beanCodec.getEncContextFactory().createEncContext(
						mo, SmsMOMessage.class, null));
		
		String encryption = null;
		try {
			String c = new String(bytes, "ISO-8859-1");
		    encryption = new Decrypt().encode(c, c.length());
		} catch (UnsupportedEncodingException e) {
			logger.error("encryption text", e);
		}
		
		return	encryption;
	}
}
