/**
 * 
 */
package com.skymobi.android.util;

/**
 * @author hp
 *
 */
public interface Transformer<FROM, TO> {
    public TO transform(FROM from);
}
