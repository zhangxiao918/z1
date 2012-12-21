package com.skymobi.android.bean.esb.core;

public interface EsbMutableHeaderable extends EsbHeaderable {

	public void setSrcESBAddr(int srcESBAddr);

	public void	setDstESBAddr(int dstESBAddr);
	
	public void	setSeqNum(int seqNum);
	
	public void	setResult(short result);
	
	public void	setFlags(int flags);
}
