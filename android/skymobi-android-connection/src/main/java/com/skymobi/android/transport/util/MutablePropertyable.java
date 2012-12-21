/**
 * 
 */
package com.skymobi.android.transport.util;

import java.util.Map;

/**
 * @author hp
 *
 */
public interface MutablePropertyable extends Propertyable {
    public void setProperty(String key, Object value);
    public void setProperties(Map<String, Object> properties );
}
