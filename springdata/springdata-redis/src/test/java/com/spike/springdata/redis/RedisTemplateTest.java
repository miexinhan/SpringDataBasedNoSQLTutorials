package com.spike.springdata.redis;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.ValueOperations;

public class RedisTemplateTest extends RedisTestBase {
	@Autowired
	private RedisTemplate<String, Long> longRedisTemplate;

	@Resource(name = "longRedisTemplate")
	private ListOperations<String, Long> listOperations;

	@Test
	public void resources() {
		assertNotNull(longRedisTemplate);
		assertNotNull(listOperations);
	}

	/**
	 * single key - single value operations
	 */
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

	/**
	 * single key - collection value operations use {@linkplain ListOperations}
	 */
	@Test
	public void listOperations() {
		String key = "USER:Alice";

		// put
		Long startTime = new Date().getTime();
		listOperations.leftPush(key, startTime);

		try {
			Thread.sleep(10 * 1000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Long endTime = new Date().getTime();
		listOperations.leftPush(key, endTime);

		// get
		Long size = listOperations.size(key);
		List<Long> accessTimes = listOperations.range(key, 0L, new Date().getTime());
		assertTrue(accessTimes.size() == size);
		System.out.println(accessTimes);
	}

	@Test
	public void useRedisCallbak() {

		@SuppressWarnings("unchecked")
		List<Long> accessTimes = (List<Long>) longRedisTemplate.execute(new RedisCallback<Object>() {
			/**
			 * Can cast to StringRedisConnection if using a StringRedisTemplate
			 */
			@Override
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				// complete control on connection

				Long size = connection.dbSize();
				System.out.println("the total number of available keys is: " + size);

				List<byte[]> accessTimesList = connection.lRange("USER:Alice".getBytes(), 0L, new Date().getTime());
				List<Long> accessTimes = new ArrayList<Long>(accessTimesList.size());
				for (byte[] element : accessTimesList) {
					accessTimes.add(Long.valueOf(new String(element)));
				}
				return accessTimes;
			}
		});

		System.out.println(accessTimes);

	}

	/**
	 * not interested in use {@link @Transactional}
	 */
	@Test
	public void redisTransaction() {
		final String key = "PAGE:HOME";

		List<Object> transactionResult = longRedisTemplate.executePipelined(new SessionCallback<List<Object>>() {

			@SuppressWarnings("unchecked")
			@Override
			public <K, V> List<Object> execute(RedisOperations<K, V> operations) throws DataAccessException {
				operations.multi();

				// WHAT FUCK IS THIS SHIT???
				operations.opsForSet().add((K) key, (V) Long.valueOf(new Date().getTime()));
				operations.opsForSet().add((K) key, (V) Long.valueOf(new Date().getTime()));

				// all operation in the transaction
				return operations.exec();
			}
		});

		System.out.println(transactionResult);
	}

}
