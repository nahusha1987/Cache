/**
 * 
 */
package com.home.nahusha.nncache;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
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
	public void defaultCacheCreation() {
		NNCache<Object, Object> testCache = new TTLCache<>();
		assertSame(Integer.valueOf(10), Integer.valueOf(testCache.size()));
	}
	
	@Test
	public void sizedCacheCreation() {
		NNCache<Object, Object> testCache = new TTLCache<>(75);
		assertSame(Integer.valueOf(75), Integer.valueOf(testCache.size()));
	}
}
