package com.spike.springdata.redis.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class LongSerializer implements RedisSerializer<Long> {
	public static final LongSerializer INSTANCE = new LongSerializer();

	@Override
	public byte[] serialize(Long t) throws SerializationException {
		if (t != null) {
			return t.toString().getBytes();
		} else {
			return new byte[0];
		}
	}

	@Override
	public Long deserialize(byte[] bytes) throws SerializationException {
		if (bytes != null && bytes.length > 0) {
			return Long.parseLong(new String(bytes));
		} else {
			return null;
		}

	}
}