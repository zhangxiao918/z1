/**
 * 
 */
package com.skymobi.android.bean.mo;

/**
 * @author hp
 *
 */
public class MOEncryptionContext {
	
	private	MOEncryptionSource source;
	
	private	int	messageNum;
	
	private	int	messageIdx;
	
	private	int	moFlag = 0;

	/**
	 * @return the source
	 */
	public MOEncryptionSource getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(MOEncryptionSource source) {
		this.source = source;
	}

	/**
	 * @return the messageNum
	 */
	public int getMessageNum() {
		return messageNum;
	}

	/**
	 * @param messageNum the messageNum to set
	 */
	public void setMessageNum(int messageNum) {
		this.messageNum = messageNum;
	}

	/**
	 * @return the messageIdx
	 */
	public int getMessageIdx() {
		return messageIdx;
	}

	/**
	 * @param messageIdx the messageIdx to set
	 */
	public void setMessageIdx(int messageIdx) {
		this.messageIdx = messageIdx;
	}

	/**
	 * @return the moFlag
	 */
	public int getMoFlag() {
		return moFlag;
	}

	/**
	 * @param moFlag the moFlag to set
	 */
	public void setMoFlag(int moFlag) {
		this.moFlag = moFlag;
	}
	

}
