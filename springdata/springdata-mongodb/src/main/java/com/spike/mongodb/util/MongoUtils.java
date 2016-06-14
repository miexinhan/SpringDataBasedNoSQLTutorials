package com.spike.mongodb.util;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * 
 * <pre>
 * MongoDB Java Driver工具类
 * </pre>
 * 
 * @author zhoujiagen
 *
 */
public final class MongoUtils {

	private static final Logger LOG = LoggerFactory.getLogger(MongoUtils.class);

	/** 示例用Schema常量 */
	public static interface Constants {
		/** 数据库名称 */
		String DB_NANE = "test";
		/** 集合名称 */
		String COLLECTION_NAME = "restaurants";
	}

	/** 按(localhost, defaultport)获取客户端 */
	public static MongoClient client() {
		LOG.info("获取默认服务器的客户端...");
		MongoClient mongoClient = new MongoClient();
		return mongoClient;
	}

	/** 按(host, port)获取客户端 */
	public static MongoClient client(String host, int port) {
		LOG.info("获取服务器[host=" + host + ", port=" + port + "]的客户端...");

		MongoClient mongoClient = new MongoClient(host, port);
		return mongoClient;
	}

	/** 获取数据库 */
	public static MongoDatabase database(MongoClient mongoClient, String databaseName) {
		LOG.info("获取Master[" + mongoClient.getAddress() + "]数据库[name=" + databaseName + "]...");

		return mongoClient.getDatabase(databaseName);
	}

	/** 获取集合 */
	public static MongoCollection<Document> collection(MongoDatabase database, String collectionName) {
		LOG.info("获取数据库[name=" + database.getName() + "]中集合[name=" + collectionName + "]...");

		MongoCollection<Document> mongoCollection = database.getCollection(collectionName);

		if (mongoCollection == null) {
			LOG.error("集合[name=" + collectionName + "]不存在!");
		}

		return mongoCollection;
	}

	/** 输出文档内容的代码块 */
	public static final Block<Document> renderDocumentBlock() {
		return new Block<Document>() {
			@Override
			public void apply(Document t) {
				LOG.info("结果文档：" + t);
			}
		};
	}

	/** 输出查询结果中文档数量 */
	public static final void renderDocumentCount(final FindIterable<Document> queryResult) {
		final Map<String, Long> result = new HashMap<String, Long>();
		result.put("result", 0L);

		// 函数式风格调用
		queryResult.forEach(new Block<Document>() {
			@Override
			public void apply(Document t) {
				result.put("result", result.get("result") + 1);
			}
		});

		System.out.println("Query result Count = " + result);
	}

}
