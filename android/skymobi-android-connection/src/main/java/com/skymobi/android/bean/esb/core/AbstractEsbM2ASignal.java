/**
 * 
 */
package com.skymobi.android.bean.esb.core;

/**
 * @author hp
 *
 */
public class AbstractEsbM2ASignal extends AbstractEsbHeaderable 
	implements	EsbModule2AccessSignal {
	
	private	TerminalAccessInfo	terminalAccessInfo;
	
	private	TerminalMessageHeader	terminalMessageHeader;
	
	public TerminalAccessInfo getTerminalAccessInfo() {
		return terminalAccessInfo;
	}

	public TerminalMessageHeader getTerminalMessageHeader() {
		return terminalMessageHeader;
	}

	public void setTerminalAccessInfo(TerminalAccessInfo info) {
		terminalAccessInfo = info;
		
	}

	public void setTerminalMessageHeader(TerminalMessageHeader hdr) {
		terminalMessageHeader = hdr;		
	}

	public boolean checkIntegrity() {
		return	(null != terminalAccessInfo) && (null != terminalMessageHeader);
	}
}
