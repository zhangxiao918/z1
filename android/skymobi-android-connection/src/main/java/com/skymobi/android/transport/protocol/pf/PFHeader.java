package com.skymobi.android.transport.protocol.pf;

import com.skymobi.android.bean.pf.annotation.PFField;

//PF header
//0                             
//0 1 2 3 4 5 6 7 
//+-+-+-+-+-+-+-+-+
//|    seqId(4)   |
//|               |
//|               |
//|               |
//+-+-+-+-+-+-+-+-+
//|   msgCode(4)  |
//|               |
//|               |
//|               |
//+-+-+-+-+-+-+-+-+
//|   result(1)   |
//+-+-+-+-+-+-+-+-+
//| msgbodylen(4) |
//|               |
//|               |
//|               |
//+-+-+-+-+-+-+-+-+
//|   msgbody(n)  |
//+-+-+-+-+-+-+-+-+

//瀛楁鍚嶇О    瀛楁澶у皬    鍙栧�鑼冨洿    鍔熻兘璇存槑
//搴忓垪鍙�
//(seqId)   4(byte) [0,2^32-1]  浠�寮�鏈�ぇ涓�294967295鐨勮嚜澧炲簭鍒楀彿
//娑堟伅缂栫爜
//(msgCode) 4(byte) [0,2^32]    鍏蜂綋姣忕娑堟伅鐨勭被鍨嬬紪鍙凤紙濂囨暟涓鸿姹傦紝鍋舵暟涓哄搷搴旓級
//娑堟伅缁撴灉
//(result)  1(byte) [0,255]     杩斿洖娑堟伅缁撴灉缂栧彿锛堥敊璇爜锛�
//娑堟伅闀垮害
//(msgbodylen)  4(byte)   [0,2^32-1]   婧愬姛鑳芥ā鍧楃紪鍙�
//鍗忚鏁版嵁鍩�
//(msgbody)   n   n/a   鍞竴瀵瑰簲鏌愪釜娑堟伅缂栫爜

/**
 * PF(鐢ㄦ埛妗ｆ鏈嶅姟)鍗忚澶�
 * 
 * @author Shawn.Du
 */
public class PFHeader {
	public static final int PF_HEADER_LENGTH = 9;

	@PFField(index = 0)
	private int seqId;

	@PFField(index = 1)
	private int msgCode;

	@PFField(index = 2)
	private byte result;

	@PFField(index = 3)
	private int msgbodylen;

	public int getSeqId() {
		return seqId;
	}

	public int getMsgCode() {
		return msgCode;
	}

	public byte getResult() {
		return result;
	}

	public int getMsgbodylen() {
		return msgbodylen;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public void setMsgCode(int msgCode) {
		if (msgCode <= 0) {
			throw new RuntimeException("invalid message code.");
		}
		this.msgCode = msgCode;
	}

	public void setResult(byte result) {
		this.result = result;
	}

	public void setMsgbodylen(int msgbodylen) {
		this.msgbodylen = msgbodylen;
	}

}
