package com.skymobi.android.transport.protocol.esb.hdr;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES Encrypt and Decrypt
 * <p>
 * 濡傛灉KEY濮嬬粓鏄浐瀹氱殑锛岃棣栧厛璋冪敤{@link #generateKey(byte[])}鐢熸垚瀵嗛挜锛屽姞瀵嗕娇鐢▄@link #encrypt(byte[])}锛岃В瀵嗕娇鐢▄@link #decrypt(byte[])}
 * 
 * @author albin
 */
public class DesEncrypt {
	/**
	 * 鐢变簬python榛樿浣跨敤鐨勯潪鑷姩濉厖妯″紡锛岃�java榛樿鑷姩琛ュ～锛屾墍浠ヤ负浜嗗吋瀹硅繖杈规棤琛ュ～妯″紡
	 */
	private final static String DES = "DES/ECB/NoPadding";

	private static SecretKey key;

	/**
	 * 鐢熸垚瀵嗛挜
	 * 
	 * @param k
	 * @throws Exception
	 */
	public static synchronized void generateKey(byte[] k) throws Exception {
		DESKeySpec dks = new DESKeySpec(k);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		key = keyFactory.generateSecret(dks);
	}

	/**
	 * 浣跨敤鍥哄畾鐨勫瘑閽ュ姞瀵�
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src) throws Exception {
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(src);
	}

	/**
	 * 浣跨敤鍥哄畾鐨勫瘑閽ヨВ瀵�
	 * 
	 * @param src
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src) throws Exception {
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(src);
	}

	/**
	 * 鍔犲瘑
	 * 
	 * @param src
	 *            鏁版嵁婧�
	 * @param key
	 *            瀵嗛挜锛岄暱搴﹀繀椤绘槸8鐨勫�鏁�
	 * @return 杩斿洖鍔犲瘑鍚庣殑鏁版嵁
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.ENCRYPT_MODE, securekey);
		return cipher.doFinal(src);
	}

	/**
	 * 瑙ｅ瘑
	 * 
	 * @param src
	 *            鏁版嵁婧�
	 * @param key
	 *            瀵嗛挜锛岄暱搴﹀繀椤绘槸8鐨勫�鏁�
	 * @return 杩斿洖瑙ｅ瘑鍚庣殑鍘熷鏁版嵁
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey securekey = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance(DES);
		cipher.init(Cipher.DECRYPT_MODE, securekey);
		return cipher.doFinal(src);
	}
}