package com.spike.springdata.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

import com.spike.springdata.redis.serializer.LongSerializer;

@Configuration
public class ApplicationConfig {

	@Bean(name = "redisConnectionFactory")
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory factory = new JedisConnectionFactory();

		// with sentinel configuration
		// JedisConnectionFactory factory = new
		// JedisConnectionFactory(this.sentinelConfig());

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

	// @Bean
	public RedisSentinelConfiguration sentinelConfig() {
		RedisSentinelConfiguration result = //
		new RedisSentinelConfiguration().master("mymaster")//
				.sentinel("127.0.0.1", 26379)//
				.sentinel("127.0.0.1", 26380);

		return result;
	}

	// string-string
	@Bean(name = "stringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate() {
		return new StringRedisTemplate(this.redisConnectionFactory());
	}

	// string-long
	@Bean(name = "longRedisTemplate")
	public RedisTemplate<String, Long> longRedisTemplate() {
		RedisTemplate<String, Long> result = new RedisTemplate<String, Long>();

		result.setConnectionFactory(this.redisConnectionFactory());
		result.setKeySerializer(new StringRedisSerializer());
		result.setValueSerializer(LongSerializer.INSTANCE);

		return result;
	}

}
