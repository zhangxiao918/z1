/**
 * 
 */
package com.skymobi.android.bean.esb.core;

import com.skymobi.android.transport.protocol.esb.hdr.TerminalUserAgentNew;

/**
 * @author hp
 *
 */
public interface EsbAccess2ModuleSignal extends EsbMutableHeaderable {
	
	public	TerminalAccessInfo	getTerminalAccessInfo();
	
	public	void	setTerminalAccessInfo(TerminalAccessInfo info);

	public	TerminalCookie	getTerminalCookie();
	
	public	void	setTerminalCookie(TerminalCookie cookie);

	public	TerminalUserAgent	getTerminalUserAgent();
	
	public	void	setTerminalUserAgent(TerminalUserAgent ua);
	
	public	TerminalMessageHeader	getTerminalMessageHeader();
	
	public	void	setTerminalMessageHeader(TerminalMessageHeader hdr);
	
	public	TerminalUserAgentNew	getTerminalUserAgentNew();
	
	public	void	setTerminalUserAgentNew(TerminalUserAgentNew uan);
}
