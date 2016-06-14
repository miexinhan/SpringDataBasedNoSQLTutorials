package com.spike.mongodb;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.spike.mongodb.util.MongoUtils;

/**
 * 
 * <pre>
 * 索引
 * 
 * 注意
 * MongoDB自动在_id字段上创建索引。
 * 
 * 
 * 
 * 示例
 * (1) 单字段上创建索引
 * (2) 创建复合索引
 * 
 * 
 * 
 * </pre>
 * 
 * @see com.mongodb.client.MongoCollection#createIndex(org.bson.conversions.Bson)
 * @see com.mongodb.client.MongoCollection#createIndex(org.bson.conversions.Bson, com.mongodb.client.model.IndexOptions)
 * @see com.mongodb.client.MongoCollection#createIndexes(java.util.List)
 * 
 * @author zhoujiagen
 *
 */
public class IndexApp {

	public static void main(String[] args) {
		MongoClient mongoClient = MongoUtils.client();
		MongoDatabase mongoDatabase = MongoUtils.database(mongoClient, MongoUtils.Constants.DB_NANE);
		MongoCollection<Document> mongoCollection = MongoUtils.collection(mongoDatabase,
				MongoUtils.Constants.COLLECTION_NAME);

		_1_indexOnSingleField(mongoCollection);

		// _2_compoundIndex(mongoCollection);

		_3_dropIndex(mongoCollection);
	}

	/** (1) 单字段上创建索引 */
	static void _1_indexOnSingleField(MongoCollection<Document> mongoCollection) {
		// 1为升序类型索引，-1为降序类型索引
		Document indexDocument = new Document("cuisine", 1);

		// 创建索引，返回索引名称
		String indexName = mongoCollection.createIndex(indexDocument);

		// cuisine_1
		System.out.println(indexName);
	}

	/** (2) 创建复合索引 */
	static void _2_compoundIndex(MongoCollection<Document> mongoCollection) {
		Document indexDocument = new Document("cuisine", 1).append("address.zipcode", -1);

		String indexName = mongoCollection.createIndex(indexDocument);

		// cuisine_1_address.zipcode_-1
		System.out.println(indexName);
	}

	/** 删除索引 */
	static void _3_dropIndex(MongoCollection<Document> mongoCollection) {
		Document indexDocument = new Document("cuisine", 1);
		mongoCollection.dropIndex(indexDocument);
	}

}
