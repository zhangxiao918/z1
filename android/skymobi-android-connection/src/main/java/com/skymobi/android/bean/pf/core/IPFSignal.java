/**
 * 
 */
package com.skymobi.android.bean.pf.core;

import com.skymobi.android.util.IdentifyableOfInt;

/**
 * 
 * @author Shawn.Du
 *
 */
public interface IPFSignal extends IdentifyableOfInt {

	int getSeqId();

	byte getResult();

	void setSeqId(int seqId);

	void setResult(byte result);
}
