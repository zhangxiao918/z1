/*******************************************************************************
 * CopyRight (c) 2005-2010 SKY-MOBI Ltd. All rights reserved.
 * Filename:  XipMessage.java
 * Creator:   Qi Wang
 * Version:   1.0
 * Date: 	 2010-4-1 涓嫔崃04:42:36
 * Description: 
 *******************************************************************************/
package com.skymobi.cac.maopao.xip;

import com.skymobi.cac.maopao.passport.android.bean.bytebean.annotation.ByteField;
import com.skymobi.cac.maopao.passport.android.util.DefaultPropertiesSupport;

public class XipMessage extends DefaultPropertiesSupport {

	@ByteField(index = 0)
	private AccessHeader header;

	@ByteField(index = 1)
	private XipBody body;

	
	public void setHeader(AccessHeader header) {
		this.header = header;
	}

	public void setBody(XipBody body) {
		this.body = body;
	}



	public AccessHeader getHeader() {
		return header;
	}

	public XipBody getBody() {
		return body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((header == null) ? 0 : header.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof XipMessage))
			return false;
		final XipMessage other = (XipMessage) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (header == null) {
			if (other.header != null)
				return false;
		} else if (!header.equals(other.header))
			return false;
		return true;
	}

}
