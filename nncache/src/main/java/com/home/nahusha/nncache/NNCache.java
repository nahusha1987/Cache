package com.home.nahusha.nncache;

public interface NNCache<K,V> {
	void put(K key, V value);
	
	V get(K key);
	
	int size();
	
	boolean isEmpty();
	
	void clear();
	
}
