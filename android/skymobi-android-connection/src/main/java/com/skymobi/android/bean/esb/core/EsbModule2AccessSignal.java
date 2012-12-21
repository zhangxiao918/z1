/**
 * 
 */
package com.skymobi.android.bean.esb.core;

/**
 * @author hp
 *
 */
public interface EsbModule2AccessSignal extends EsbMutableHeaderable {

	public	TerminalAccessInfo	getTerminalAccessInfo();
	
	public	void	setTerminalAccessInfo(TerminalAccessInfo info);

	public	TerminalMessageHeader	getTerminalMessageHeader();
	
	public	void	setTerminalMessageHeader(TerminalMessageHeader hdr);
}
