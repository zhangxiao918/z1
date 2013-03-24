package com.skymobi.cac.maopao.passport.android.util;
import java.util.Enumeration;

/**
 * 
 */

/**
 * @author Marvin.Ma
 * @param <E>
 *
 */
public class EnumerationImplOfArray<E> implements Enumeration<E> {

	private	E[] data;
	private	int	offset;
	
	public EnumerationImplOfArray(E[] array) {
		this.data = array;
		this.offset = 0;
	}
	
	public boolean hasMoreElements() {
		return this.offset < data.length;
	}

	public E nextElement() {
		if ( hasMoreElements() ) {
			return this.data[this.offset++];
		}
		else {
			return	null;
		}
	}

}
