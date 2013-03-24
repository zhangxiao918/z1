/**
 * 
 */
package com.skymobi.cac.maopao.passport.android.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author isdom
 *
 */
public class SimpleCache<K, V> {
    private ConcurrentMap<K, V>  map = new ConcurrentHashMap<K, V>();
    
    public V get(K key, Callable<V> ifAbsent) {
        V value = map.get(key);
        if ( null == value ) {
            try {
                value = ifAbsent.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            map.putIfAbsent( key, value );
        }
        return  value;
    }
}