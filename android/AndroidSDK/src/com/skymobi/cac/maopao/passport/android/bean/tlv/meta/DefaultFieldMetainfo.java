/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.bean.tlv.meta;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hp
 *
 */
public class DefaultFieldMetainfo implements TLVFieldMetainfo {

	private Map<Integer, Field> tags = new HashMap<Integer, Field>();
	
	public void add(int tag, Field field) {
		tags.put(tag, field);
	}
	
	/* (non-Javadoc)
	 * @see com.skymobi.bean.tlv.Tag2Field#get(int)
	 */
	public Field get(int tag) {
		return tags.get(tag);
	}

}
