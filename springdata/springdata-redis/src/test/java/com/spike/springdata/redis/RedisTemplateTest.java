package com.spike.springdata.redis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

public class RedisTemplateTest extends RedisTestBase {
	@Autowired
	private RedisTemplate<String, Long> longRedisTemplate;

	@Test
	public void resources() {
		assertNotNull(longRedisTemplate);
	}

	@Test
	public void readAndWrite() {
		String key = "test:" + 1234;
		Long value = 0L;

		// write
		ValueOperations<String, Long> valueOperations = longRedisTemplate.opsForValue();
		Boolean result = valueOperations.setIfAbsent(key, value);
		assertTrue(result);
		// put again
		result = valueOperations.setIfAbsent(key, value);
		assertFalse(result);

		// read
		Long longResult = valueOperations.get(key);
		assertTrue(longResult.equals(value));
	}

}
