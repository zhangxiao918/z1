/**
 * 
 */
package com.skymobi.android.bean.pf.core;

import com.skymobi.android.util.DefaultPropertiesSupport;

/**
 * 
 * @author Shawn.Du
 *
 */
public class AbstractPFSignal extends DefaultPropertiesSupport implements IPFSignal {

	private	int		seqId;
	private	byte	result;

	public int getIntIdentification() {
		return getSeqId();
	}

	public int getSeqId() {
		return seqId;
	}

	public byte getResult() {
		return result;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}

	public void setResult(byte result) {
		this.result = result;
	}
}
