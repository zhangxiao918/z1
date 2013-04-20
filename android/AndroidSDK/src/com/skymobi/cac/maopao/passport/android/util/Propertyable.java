/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.util;

import java.util.Map;

/**
 * @author hp
 *
 */
public interface Propertyable {
    public Object   getProperty(String key);
    public Map<String, Object>  getProperties();
}
