package com.spike.springdata.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

import com.spike.springdata.redis.serializer.LongSerializer;

@Configuration
public class ApplicationConfig {

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();
		factory.setHostName("localhost");
		factory.setPort(6379);
		factory.setUsePool(true);
		factory.setPassword("");
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(10);
		factory.setPoolConfig(poolConfig);
		factory.setTimeout(1000);

		factory.afterPropertiesSet();// !!!

		return factory;
	}

	// string-string
	@Bean
	public StringRedisTemplate stringRedisTemplate() {
		return new StringRedisTemplate(this.redisConnectionFactory());
	}

	// string-long
	@Bean
	public RedisTemplate<String, Long> longRedisTemplate() {
		RedisTemplate<String, Long> result = new RedisTemplate<String, Long>();

		result.setConnectionFactory(this.redisConnectionFactory());
		result.setKeySerializer(new StringRedisSerializer());
		result.setValueSerializer(LongSerializer.INSTANCE);

		return result;
	}

}
