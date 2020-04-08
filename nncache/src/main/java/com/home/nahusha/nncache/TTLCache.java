package com.home.nahusha.nncache;

import java.util.concurrent.TimeUnit;

public class TTLCache<K,V> implements NNCache<K, V> {

	private int size;

	private static final int DEFAULT_SIZE = 10;

	public TTLCache() {
		this(DEFAULT_SIZE);
	}

	public TTLCache(int size) {
		this.size = size;
	}
	
	public void put(K key, V value) {
		// TODO Auto-generated method stub
		
	}

	public void put(K key, V value, TimeUnit timeToLive) {
		// TODO Auto-generated method stub
		
	}

	public V get(K key) {
		// TODO Auto-generated method stub
		return null;
	}

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public void clear() {
		// TODO Auto-generated method stub
		
	}

	public boolean containsKey(K key) {
		// TODO Auto-generated method stub
		return false;
	}

}
