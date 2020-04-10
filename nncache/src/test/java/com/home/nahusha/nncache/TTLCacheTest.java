/**
 * 
 */
package com.home.nahusha.nncache;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author nahusha
 *
 */
public class TTLCacheTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void cache_with_default_size() {
		NNCache<Object, Object> testCache = new TTLCache<>();
		assertSame(Integer.valueOf(0), Integer.valueOf(testCache.size()));
	}
	
	@Test
	public void cache_with_provided_size() {
		NNCache<Object, Object> testCache = new TTLCache<>(2);
		assertSame(Integer.valueOf(0), Integer.valueOf(testCache.size()));
	}
	
	@Test
	@Ignore
	public void cache_with_default_ttl() throws InterruptedException {
		NNCache<String, String> testCache = new TTLCache<>();
		testCache.put("abc", "def");
		assertNotNull(testCache.get("abc"));
		Thread.sleep(300*1000);
		assertNull(testCache.get("abc"));
	}
	
	@Test
	public void cache_with_provided_ttl() throws InterruptedException {
		NNCache<String, String> testCache = new TTLCache<>(5, 6);
		testCache.put("someKey", "someValue");
		assertNotNull(testCache.get("someKey"));
		Thread.sleep(6000);
		assertNull(testCache.get("someKey"));
	}
	
	@Test
	public void cache_size_check() throws InterruptedException {
		NNCache<String, String> testCache = new TTLCache<>(3);
		for (int i = 0; i < 100; i++) {
			testCache.put("key" + i, "value" + i);
			Thread.sleep(1);
		}
		assertSame(Integer.valueOf(3), testCache.size());
	}
	
	@Test
	public void cache_clear_test() {
		NNCache<String, String> testCache = new TTLCache<>(3);
		testCache.put("key1", "value1");
		testCache.put("key2", "value2");
		assertSame(Integer.valueOf(2), testCache.size());
		testCache.clear();
		assertSame(Integer.valueOf(0), testCache.size());
	}
	
	@Test
	public void cache_isEmpty_test() throws InterruptedException {
		NNCache<String, String> testCache = new TTLCache<>(2,2);
		testCache.put("key1", "value1");
		testCache.put("key2", "value2");
		Thread.sleep(2000);
		testCache.get("key1");
		testCache.get("key2");
		assertTrue(testCache.isEmpty());
	}

	@Test
	public void cache_eviction_access_within_ttl() throws InterruptedException {
		NNCache<String, String> testCache = new TTLCache<>(5, 5);
		testCache.put("key1", "value1");
		Thread.sleep(4000);
		assertNotNull(testCache.get("key1"));
		Thread.sleep(4000);
		assertNotNull(testCache.get("key1"));
	}
	
	@Test
	public void cache_eviction_access_after_ttl() throws InterruptedException {
		NNCache<String, String> testCache = new TTLCache<>(2, 2);
		testCache.put("key1", "value1");
		testCache.put("key2", "value2");
		Thread.sleep(2000);
		assertNull(testCache.get("key1"));
		assertNull(testCache.get("key2"));
		
	}
}
