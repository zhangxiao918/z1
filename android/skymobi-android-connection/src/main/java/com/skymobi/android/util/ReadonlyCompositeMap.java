/**
 * 
 */
package com.skymobi.android.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Marvin.Ma
 *
 */
public class ReadonlyCompositeMap<K, V> implements Map<K, V> {

	private	Map<K,V> first;
	private Map<K,V> second;
	
	public ReadonlyCompositeMap(Map<K,V> first, Map<K,V> second) {
		if ( null == first || null == second) {
			throw new NullPointerException();
		}
		
		this.first = first;
		this.second = second;
	}
	
	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key) {
		if ( first.containsKey(key) ) {
			return	true;
		}
		else {
			return	second.containsKey(key);
		}
	}

	public boolean containsValue(Object value) {
		if ( first.containsValue(value) ) {
			return	true;
		}
		else {
			return	second.containsValue(value);
		}
	}

	public Set<Map.Entry<K, V>> entrySet() {
		Set<Map.Entry<K, V>> ret = new HashSet<Map.Entry<K, V>>();
		
		ret.addAll(first.entrySet());
		ret.addAll(second.entrySet());
		return ret;
	}

	public V get(Object key) {
		if ( first.containsKey(key)) {
			return	first.get(key);
		}
		else {
			return	second.get(key);
		}
	}

	public boolean isEmpty() {
		return first.isEmpty() && second.isEmpty();
	}

	public Set<K> keySet() {
		Set<K> ret = new HashSet<K>();
		
		ret.addAll(first.keySet());
		ret.addAll(second.keySet());
		return ret;
	}

	public V put(K key, V value) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException();
	}

	public V remove(Object key) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return first.size() + second.size();
	}

	public Collection<V> values() {
		List<V> ret = new ArrayList<V>();
		
		ret.addAll(first.values());
		ret.addAll(second.values());
		return ret;
	}

}
