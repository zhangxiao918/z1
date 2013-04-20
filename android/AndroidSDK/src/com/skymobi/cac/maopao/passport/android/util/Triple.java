/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.util;

import java.io.Serializable;

/**
 * @author hp
 *
 */
public final class Triple<FIRST, SECOND, THIRD> implements Serializable {

	/**
	 * Triple's serialVersionUID
	 */
	private static final long serialVersionUID = 8479803002911944953L;


	public final FIRST first;

	public final SECOND second;

	public final THIRD 	third;
	
	private Triple(FIRST first, SECOND second, THIRD third) {

		this.first = first;

		this.second = second;
		
		this.third = third;
	}
	
	public FIRST getFirst() {
		return first;
	}

	public SECOND getSecond() {
		return second;
	}

	public THIRD getThird() {
		return third;
	}
	
	public static <FIRST, SECOND, THIRD> Triple<FIRST, SECOND, THIRD> of(FIRST first,
			SECOND second, THIRD third) {

		return new Triple<FIRST, SECOND, THIRD>(first, second, third);

	}

	@Override
	public String toString() {

		return String.format("Triple[%s,%s,%s]", first, second, third);

	}

}
