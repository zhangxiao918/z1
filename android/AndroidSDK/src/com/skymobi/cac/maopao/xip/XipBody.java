package com.skymobi.cac.maopao.xip;

import com.skymobi.cac.maopao.xip.annotation.XipSignal;

public class XipBody {

	private AccessHeader header;
	
	private int msgCode = -1;
	
	public AccessHeader getHeader() {
		if(null == header)
			header = new AccessHeader();
		return header;
	}


	public void setHeader(AccessHeader header) {
		this.header = header;
	}
	
	public int getMsgCode() {
		if (msgCode == -1) {
			XipSignal attr = this.getClass().getAnnotation(XipSignal.class);
			if (null == attr) {
				throw new RuntimeException(
						"invalid xip body, no messageCode found.");
			}
			msgCode = attr.messageCode();
		}
		return msgCode;
	}


	public XipMessage toMessage() {
        getHeader().setMsgCode(getMsgCode());
        XipMessage msg = new XipMessage();

        msg.setHeader(getHeader());
        msg.setBody(this);

        return msg;
    }
	
	public int getStringLen(String src){
		return src == null ? 0 : src.length() * 2; 
	}
	
	public void addHeader(int srcId, int dstId, short flag){
		AccessHeader header = new AccessHeader();
		header.setSrcModule(srcId);
		header.setDstModule(dstId);
		header.setFlags(flag);
		this.header = header;
	}
	
	//是否需要跳出解码的过程，默认为False
	public boolean breakDecode(){
		return false;
	}
}
