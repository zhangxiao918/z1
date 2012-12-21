/**
 * 
 */
package com.skymobi.android.bean.esb.core;

import com.skymobi.android.transport.protocol.esb.hdr.TerminalUserAgentNew;

/**
 * @author hp
 *
 */
public class AbstractEsbA2MSignal extends AbstractEsbHeaderable 
	implements	EsbAccess2ModuleSignal {
	
	private	TerminalAccessInfo	terminalAccessInfo;
	
	private	TerminalCookie		terminalCookie;
	
	private	TerminalUserAgent	terminalUserAgent;
	
	private	TerminalMessageHeader	terminalMessageHeader;
	
	private	TerminalUserAgentNew	terminalUserAgentNew;
	
	public TerminalAccessInfo getTerminalAccessInfo() {
		return terminalAccessInfo;
	}

	public TerminalCookie getTerminalCookie() {
		return terminalCookie;
	}

	public TerminalMessageHeader getTerminalMessageHeader() {
		return terminalMessageHeader;
	}

	public TerminalUserAgent getTerminalUserAgent() {
		return terminalUserAgent;
	}

	public void setTerminalAccessInfo(TerminalAccessInfo info) {
		terminalAccessInfo = info;
		
	}

	public void setTerminalCookie(TerminalCookie cookie) {
		terminalCookie = cookie;
	}

	public void setTerminalMessageHeader(TerminalMessageHeader hdr) {
		terminalMessageHeader = hdr;		
	}

	public void setTerminalUserAgent(TerminalUserAgent ua) {
		terminalUserAgent = ua;
	}

	public boolean checkIntegrity() {
		return	(null != terminalAccessInfo) && (null != terminalMessageHeader);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((terminalAccessInfo == null) ? 0 : terminalAccessInfo
						.hashCode());
		result = prime * result
				+ ((terminalCookie == null) ? 0 : terminalCookie.hashCode());
		result = prime
				* result
				+ ((terminalMessageHeader == null) ? 0 : terminalMessageHeader
						.hashCode());
		result = prime
				* result
				+ ((terminalUserAgent == null) ? 0 : terminalUserAgent
						.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEsbA2MSignal other = (AbstractEsbA2MSignal) obj;
		if (terminalAccessInfo == null) {
			if (other.terminalAccessInfo != null)
				return false;
		} else if (!terminalAccessInfo.equals(other.terminalAccessInfo))
			return false;
		if (terminalCookie == null) {
			if (other.terminalCookie != null)
				return false;
		} else if (!terminalCookie.equals(other.terminalCookie))
			return false;
		if (terminalMessageHeader == null) {
			if (other.terminalMessageHeader != null)
				return false;
		} else if (!terminalMessageHeader.equals(other.terminalMessageHeader))
			return false;
		if (terminalUserAgent == null) {
			if (other.terminalUserAgent != null)
				return false;
		} else if (!terminalUserAgent.equals(other.terminalUserAgent))
			return false;
		return true;
	}

	public TerminalUserAgentNew getTerminalUserAgentNew() {
		return terminalUserAgentNew;
	}

	public void setTerminalUserAgentNew(TerminalUserAgentNew uan) {
		this.terminalUserAgentNew = uan;
		
	}


}
