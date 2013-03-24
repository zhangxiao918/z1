/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.util;

/**
 * @author hp
 *
 */
public interface Transformer<FROM, TO> {
    public TO transform(FROM from);
}
