/**
 * 
 */
package com.skymobi.android.bean.esb.core;

/**
 * @author Marvin.Ma
 *
 */
public interface EsbHeaderable {
	
	public 	int getSrcESBAddr();
	
	public	int	getDstESBAddr();
	
	public	int	getSeqNum();
	
	public 	short getResult();
	
	public 	int	getFlags();

	public	boolean checkIntegrity();
}
