/**
 * 
 */
package com.skymobi.android.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author isdom
 *
 */
public class StringUtils {
	static public	List<String>	line2list( String line) {
		List<String>	ret = new ArrayList<String>();
		StringTokenizer	st = new StringTokenizer(line, ",", false);
		while (st.hasMoreTokens()) {
			 ret.add( st.nextToken().trim() );
		}
		
		return	ret;
	}
	
	static public	String[]	line2array( String line) {
		return	line2list(line).toArray(new String[0]);
	}
}
