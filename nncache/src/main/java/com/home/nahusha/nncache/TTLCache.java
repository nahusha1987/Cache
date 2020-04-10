package com.home.nahusha.nncache;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class TTLCache<K,V> implements NNCache<K, V> {

	private int capacity;
	
	private long ttl;
	
	private static final int DEFAULT_CAPACITY = 10;
	
	private static final int DEFAULT_TTL = 300;

	private Map<K,CacheValueHolder<V>> map;
	
	private PriorityQueue<CacheValueHolder<V>> pq;
	
	public TTLCache() {
		this(DEFAULT_CAPACITY, DEFAULT_TTL);
	}

	public TTLCache(int capacity, int timeToLive) {
		this.capacity = capacity;
		this.ttl = timeToLive;
		map = new ConcurrentHashMap<>(capacity);
		pq = new PriorityQueue<>(capacity);
	}
	
	public TTLCache(int size) {
		this(size, DEFAULT_TTL);
	}

	public void put(K key, V value) {
		if (map.size() < capacity) {
			CacheValueHolder<V> cv = new CacheValueHolder<>(value);
			pq.add(cv);
			map.put(key, cv);
		} else if (map.size() == capacity) {
			CacheValueHolder<V> v = pq.poll();
			K k = null;
			for (Entry<K, CacheValueHolder<V>> entry : map.entrySet()) {
				if (Objects.equals(v, entry.getValue()))
					k = entry.getKey();
			}
			map.remove(k);
			CacheValueHolder<V> cv = new CacheValueHolder<>(value);
			map.put(key, cv);
			pq.offer(cv);
		}
	}

	public V get(K key) {
		if (map.containsKey(key)) {
			CacheValueHolder<V> cvalue = map.get(key);
			Instant expiry = cvalue.getLastAccessedTime();
			Instant current = Instant.now();
			if (Duration.between(expiry, current).getSeconds() >= this.ttl) {
				// expired value
				pq.remove(cvalue);
				map.remove(key);
				return null;
			} else {
				pq.remove(cvalue);
				CacheValueHolder<V> newCValue = new CacheValueHolder<>(map.get(key).getValue());
				pq.offer(newCValue);
				map.put(key, newCValue);
				return newCValue.getValue();
			}
		}
		return null;
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.size() == 0 && pq.size() == 0;
	}

	public void clear() {
		map.clear();
		pq.clear();
		capacity = 0;
	}

}

class CacheValueHolder<V> implements Comparable<V> {
	private Instant lastAccessedTime;
	
	private V value;

	CacheValueHolder(V value) {
		this.value = value;
		this.lastAccessedTime = Instant.now();
	}
	
	Instant getLastAccessedTime() {
		return lastAccessedTime;
	}

	void setLastAccessedTime(Instant lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public int compareTo(Object o) {
		CacheValueHolder<V> cv = (CacheValueHolder<V>)o;
		if (this.getLastAccessedTime().equals(cv.getLastAccessedTime()))
			return 0;
		else if (this.getLastAccessedTime().isAfter(cv.getLastAccessedTime()))
			return 1;
		return -1;
	}
}