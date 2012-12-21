package com.skymobi.android.transport.protocol.xip;


import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.xip.annotation.ApplistField;
import com.skymobi.android.bean.xip.annotation.SaipField;
import com.skymobi.android.bean.xip.annotation.SsipField;
import com.skymobi.android.bean.xip.core.XipBean;
import com.skymobi.android.bean.xip.core.XipNotify;
import com.skymobi.android.bean.xip.core.XipRequest;
import com.skymobi.android.bean.xip.core.XipResponse;

//SAIP header
//0               1               2               3                
//0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7  
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                   sky protocol identifier(8)                  |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|  basic ver(1) |                    length(3)                  |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|           srcmodule(2)        |            dstmodule(2)       |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                                                               |
//|                       transaction(16)                         |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|   type(1)     |                     reserved(3)               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                           code(4)                             |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                           data length(4)                      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|application data field(...)                                    |
//|                                                               |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

//瀛楁鍚嶇О  瀛楁澶у皬    鍙栧�鑼冨洿    鍔熻兘璇存槑
//鍗忚鏍囪瘑
//(protocol id) 8(byte) 0x00ff00ff  {0x8A,0xED,0x9C,0xF3,0x7E,0x32,0xB9,reserved}
//鍩虹鍗忚鐗堟湰
//(basic ver)   1(byte) [1,255] 鏍囪瘑鍩虹鍗忚鐨勭増鏈彿锛屼笉浼氶绻佹洿鏂帮紝浠�涓哄紑濮嬬増鏈�
//鍗忚鍖呴暱搴�
//(length)  3   [32,16777215]   鏁翠釜鍗忚鍖呴暱搴︼紝鏈�皬鍊间负鍗忚澶村ぇ灏�44)锛岄珮浣嶅瓧鑺傚簭
//婧愭ā鍧楃紪鍙�
//(srcmodule)   2   [0,65535]   婧愬姛鑳芥ā鍧楃紪鍙�
//鐩爣妯″潡缂栧彿
//(dstmodule)   2   [0,65535]   鐩爣鍔熻兘妯″潡缂栧彿
//浜嬪姟鏍囪瘑
//(transaction) 16  GUID    鍙傜収GUID鐨勭敓鎴愮畻娉曪紝鐢辫姹傛垨鑰呴�鐭ョ殑鍙戦�鑰呬繚璇佸叾鍦ㄤ换浣曟椂闂淬�鍦扮偣銆佸钩鍙板敮涓�紝璇锋眰鍜屽搷搴旀秷鎭殑浜嬬墿鏍囪瘑蹇呴』涓�嚧銆傝鏍囪瘑鐢ㄤ簬淇濊瘉璇锋眰鍜屽搷搴旂殑鍞竴瀵瑰簲浠ュ強娑堟伅鐨勪笉閲嶅鎬�纭畾瀹㈡埛绔钩鍙扮殑鐢熸垚瑙勫垯)
//鍘熻绫诲瀷
//(type)    1   [0,255] 鐩墠鏀寔璇锋眰(0)锛屽搷搴�1)锛岄�鐭ワ紙2锛夛紝閫掗�(3) 
//淇濈暀瀛楁
//锛坮eserved锛�   3   N/A 鍩虹鍗忚澶寸殑淇濈暀瀛楁
//娑堟伅缂栫爜
//锛坈ode锛�   4   [0,2^32]    鍏蜂綋姣忕娑堟伅鐨勭紪鍙凤紝寤鸿灏嗗搴旂殑璇锋眰鍜屽搷搴旀秷鎭繛缁紪鍙凤紝楂樹綅瀛楄妭搴忥紝鎸夌収涓嶅悓鍔熻兘杩涜鍒嗙粍缂栧彿銆�
//娑堟伅闀垮害  4   [0,2^32-1]  娑堟伅鏁版嵁鍩熺殑闀垮害
//鍗忚鏁版嵁鍩�
//(data field)  n   n/a 鍞竴瀵瑰簲鏌愪釜娑堟伅缂栫爜

//SSIP
//0               1               2               3                
//0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7  
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|  basic ver(1) |                    length(3)                  |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                                                               |
//|                       transaction(16)                         |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|   type(1)     |                     reserved(3)               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                           code(4)                             |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|                           data length(4)                      |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|application data field(...)                                    |
//|                                                               |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

//瀛楁鍚嶇О    瀛楁澶у皬    鍙栧�鑼冨洿    鍔熻兘璇存槑
//鍩虹鍗忚鐗堟湰
//(basic ver) 1(byte) [1,255] 鏍囪瘑鍩虹鍗忚鐨勭増鏈彿锛屼笉浼氶绻佹洿鏂帮紝浠�涓哄紑濮嬬増鏈�
//鍗忚鍖呴暱搴�
//(length)    3   [32,16777215]   鏁翠釜鍗忚鍖呴暱搴︼紝鏈�皬鍊间负鍗忚澶村ぇ灏�32)锛岄珮浣嶅瓧鑺傚簭
//浜嬪姟鏍囪瘑
//(transaction)   16  GUID    鍙傜収GUID鐨勭敓鎴愮畻娉曪紝鐢辫姹傛垨鑰呴�鐭ョ殑鍙戦�鑰呬繚璇佸叾鍦ㄤ换浣曟椂闂淬�鍦扮偣銆佸钩鍙板敮涓�紝璇锋眰鍜屽搷搴旀秷鎭殑浜嬬墿鏍囪瘑蹇呴』涓�嚧銆傝鏍囪瘑鐢ㄤ簬淇濊瘉璇锋眰鍜屽搷搴旂殑鍞竴瀵瑰簲浠ュ強娑堟伅鐨勪笉閲嶅鎬�
//鍘熻绫诲瀷
//(type)  1   [0,255] 鐩墠鏀寔璇锋眰(1)锛屽搷搴�2)锛岄�鐭ワ紙3锛夛紝閫掗�(4) 
//淇濈暀瀛楁
//锛坮eserved锛� 3   N/A 鍩虹鍗忚澶寸殑淇濈暀瀛楁
//娑堟伅缂栫爜
//锛坈ode锛� 4   [0,2^32]    鍏蜂綋姣忕娑堟伅鐨勭紪鍙凤紝寤鸿灏嗗搴旂殑璇锋眰鍜屽搷搴旀秷鎭繛缁紪鍙凤紝楂樹綅瀛楄妭搴忥紝鎸夌収涓嶅悓鍔熻兘杩涜鍒嗙粍缂栧彿銆�
//娑堟伅闀垮害    4   [0,2^32-1]  娑堟伅鏁版嵁鍩熺殑闀垮害
//鍗忚鏁版嵁鍩�
//(data field)    n   n/a 鍞竴瀵瑰簲鏌愪釜娑堟伅缂栫爜

/**
 * @author isdom
 *
 */
public class XipHeader implements XipBean {
    public static final int SAIP_HEADER_LENGTH = 44;
    public static final int SSIP_HEADER_LENGTH = 32;
    public static final int XIP_REQUEST = 1;
    public static final int XIP_RESPONSE = 2;
    public static final int XIP_NOTIFY = 3;
    
	// (protocol id)  8(byte) 0x00ff00ff  {0x8A,0xED,0x9C,0xF3,0x7E,0x32,0xB9,reserved}
	private static final long SKY_PROTOCAL_ID = 
          (0x8AL << 56)
        | (0xEDL << 48)
        | (0x9CL << 40)
        | (0xF3L << 32)
        | (0x7EL << 24)
        | (0x32L << 16)
	    | (0xB9L << 8);
	
    @SaipField(index = 0)
	private	long 	skyProtocolId = SKY_PROTOCAL_ID;
    
    @SaipField(index = 1)
    @SsipField(index = 1)
    @ApplistField(index = 1)
	private byte 	basicVer = 1;
    
    @SaipField(index = 2, bytes=3)
    @SsipField(index = 2, bytes=3)
    @ApplistField(index = 2, bytes=3)
	private int		length = 0;
    
    @SaipField(index = 3 )
	private short 	srcModule;
    
    @SaipField(index = 4 )
	private short	dstModule;
    
    @SaipField(index = 5)
    @SsipField(index = 5)
    @ApplistField(index = 5)
	private long	firstTransaction;
    
    @SaipField(index = 6)
    @SsipField(index = 6)
    @ApplistField(index = 6)
    private long    secondTransaction;
    
    @SaipField(index = 7)
    @SsipField(index = 7)
    @ApplistField(index = 7)
	private	byte	type;
    
    @SsipField(index = 8)
	private	short	sourceId;
    
    @SuppressWarnings("unused")
    @SaipField(index = 8, bytes=3)
    @ApplistField(index = 8, bytes=3)
    @SsipField(index = 9, bytes=1)
	private	int		reserved = 0;
    
    @SaipField(index = 9)
    @ApplistField(index = 9)
    @SsipField(index = 10)
	private int 	messageCode;

    @SaipField(index = 10)
    @ApplistField(index = 10)
    @SsipField(index = 11)
	private int 	messageLength;

	public boolean    isSkyProtocolIdValid() {
	    return ( SKY_PROTOCAL_ID == this.skyProtocolId );
	}
	
	/**
	 * @return the skyProtocolId
	 */
	public long getSkyProtocolId() {
		return skyProtocolId;
	}
	/**
	 * @param protocolId the skyProtocolId to set
	 */
	public void setSkyProtocolId(long protocolId) {
		skyProtocolId = protocolId;
	}
	/**
	 * @return the basicVer
	 */
	public byte getBasicVer() {
		return basicVer;
	}
	/**
	 * @param ver the basicVer to set
	 */
	public void setBasicVer(byte ver) {
		basicVer = ver;
	}
	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}
	/**
	 * @return the srcModule
	 */
	public short getSrcModule() {
		return srcModule;
	}
	/**
	 * @param module the srcModule to set
	 */
	public void setSrcModule(short module) {
		srcModule = module;
	}
	/**
	 * @return the dstModule
	 */
	public short getDstModule() {
		return dstModule;
	}
	/**
	 * @param module the dstModule to set
	 */
	public void setDstModule(short module) {
		dstModule = module;
	}
	
	/**
	 * @return the type
	 */
	public byte getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(byte type) {
		this.type = type;
	}
	/**
	 * @return the code
	 */
	public int getMessageCode() {
		return messageCode;
	}
	/**
	 * @param code the code to set
	 */
	public void setMessageCode(int code) {
	    if ( code <= 0 ) {
	        throw new RuntimeException("invalid message code.");
	    }
		this.messageCode = code;
	}
	
	/**
	 * @return the messageLength
	 */
	public int getMessageLength() {
		return messageLength;
	}
	/**g
	 * @param length the messageLength to set
	 */
	public void setMessageLength(int length) {
		messageLength = length;
	}
	
    /**
     * @return the firstTransaction
     */
    public long getFirstTransaction() {
        return firstTransaction;
    }

    /**
     * @param firstTransaction the firstTransaction to set
     */
    public void setFirstTransaction(long firstTransaction) {
        this.firstTransaction = firstTransaction;
    }

    /**
     * @return the secondTransaction
     */
    public long getSecondTransaction() {
        return secondTransaction;
    }

    /**
     * @param secondTransaction the secondTransaction to set
     */
    public void setSecondTransaction(long secondTransaction) {
        this.secondTransaction = secondTransaction;
    }
	
    public void setTransaction(UUID uuid) {
        this.firstTransaction = uuid.getMostSignificantBits();
        this.secondTransaction = uuid.getLeastSignificantBits();
    }
    
    public UUID getTransactionAsUUID() {
        return  new UUID(this.firstTransaction, this.secondTransaction);
    }
    
    public void exchangeSrcDest() {
        short   tmp = this.dstModule;
        this.dstModule = this.srcModule;
        this.srcModule = tmp;
    }
    
    public void setTypeForClass(Class<?> cls) {
    	if ( XipRequest.class.isAssignableFrom(cls) ) {
    		this.type = XIP_REQUEST;
    	}
    	else if ( XipResponse.class.isAssignableFrom(cls) ) {
    		this.type = XIP_RESPONSE;
    	}
    	else if ( XipNotify.class.isAssignableFrom(cls) ) {
    		this.type = XIP_NOTIFY;
    	}
    }
    
    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

	/**
	 * @return the sourceId
	 */
	public short getSourceId() {
		return sourceId;
	}

	/**
	 * @param sourceId the sourceId to set
	 */
	public void setSourceId(short sourceId) {
		this.sourceId = sourceId;
	}
}
