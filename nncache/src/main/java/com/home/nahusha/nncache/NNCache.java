package com.home.nahusha.nncache;

import java.util.concurrent.TimeUnit;

public interface NNCache<K,V> {
	void put(K key, V value);
	
	void put(K key, V value, TimeUnit timeToLive);
	
	V get(K key);
	
	int size();
	
	boolean isEmpty();
	
	void clear();
	
	boolean containsKey(K key);
}
