/**
 * 
 */
package com.skymobi.android.transport.protocol.esb.hdr;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.skymobi.android.bean.esb.annotation.EsbField;

//鏈嶅姟绔�Module-->鏈嶅姟绔�Module 灏佸寘鍗忚
//0               1               2               3                
//0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7 0 1 2 3 4 5 6 7  
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  >-------------+
//|           srcmodule(2)        |            dstmodule(2)       |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//|      protocol code(2)         |            seqNum(4)       	  |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//                            |            flags(4)               |                 \
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                  >--> FixedHeader
//|                               |            length(4)          |                 /
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//|                               |            result(2)          |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+                |
//|                          reserved(4)                          |                |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+  >-------------+
//|           msgCode(2)          |    application data           | 
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
//|application data field(...)                                    |
//|                                                               |
//|                                                               |
//|                                                               |
//+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+

//uint16 srcESBAddr;       /* 婧愭ā鍧楃殑鎬荤嚎鍦板潃锛岃繖鍦ㄤ竴鏉℃�绾夸笂鏄敮涓�殑銆�*/
//uint16 dstESBAddr;       /* 鐩爣妯″潡鐨勬�绾垮湴鍧�*/
//uint16 protocolCode;     /* 2瀛楄妭锛涘崗璁唬鐮�*/
//uint32 seqNum;           /* seqnumber number, 鐢卞彂閫佽�濉啓銆�涓�埇鐢ㄦ潵鏍￠獙Resp娑堟伅 */
//uint32 flags;            /* 鐩爣瀹氫箟浜嗙兢鍙�*/
//uint32 length;           /* 鏁翠釜娑堟伅鐨勯暱搴︼紝鍖呮嫭杩欎釜ESB澶存湰韬� */
//uint16 result;           /* 鍦‥SB Server 杞彂娑堟伅澶辫触鐨勬儏鍐碉紙濡傚鐩爣鍦板潃澶辫触锛変笅锛�灏唕esult璁剧疆鎴愰敊璇殑浠ｇ爜锛�灏嗘秷鎭弽寮瑰洖鍙戦�鑰�*/


/**
 * @author hp
 *
 */
public class ESBModule2ModuleHeader {
	
    @EsbField(index = 1, bytes = 2)
	private int		msgCode;

	/**
	 * @return the msgCode
	 */
	public int getMsgCode() {
		return msgCode;
	}

    public String toString() {
        
        return  ToStringBuilder.reflectionToString(this, 
                            ToStringStyle.SHORT_PREFIX_STYLE);
    }

	/**
	 * @param msgCode the msgCode to set
	 */
	public void setMsgCode(int msgCode) {
		this.msgCode = msgCode;
	}
}
