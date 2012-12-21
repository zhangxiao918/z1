/**
 * 
 */
package com.skymobi.android.util;

import java.io.Serializable;

/**
 * @author hp
 *
 */
public final class Pair<FIRST, SECOND> implements Serializable {

	/**
	 * Pair's serialVersionUID
	 */
	private static final long serialVersionUID = -3560542815329489993L;

	public final FIRST first;

	public final SECOND second;

	private Pair(FIRST first, SECOND second) {

		this.first = first;

		this.second = second;

	}
	
	public FIRST getFirst() {
		return first;
	}

	public SECOND getSecond() {
		return second;
	}

	public static <FIRST, SECOND> Pair<FIRST, SECOND> of(FIRST first,
			SECOND second) {

		return new Pair<FIRST, SECOND>(first, second);

	}

	@Override
	public String toString() {

		return String.format("Pair[%s,%s]", first, second);

	}

}
