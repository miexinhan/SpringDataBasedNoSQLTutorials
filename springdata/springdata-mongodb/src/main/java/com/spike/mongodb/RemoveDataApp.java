package com.spike.mongodb;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.spike.mongodb.util.MongoUtils;

/**
 * 
 * <pre>
 * 删除数据
 * 
 * 示例：
 * (1) 删除匹配条件的文档
 * (2) 删除集合中所有文档
 * (3) 删除集合
 * 
 * </pre>
 * 
 * @see com.mongodb.client.MongoCollection#deleteOne(org.bson.conversions.Bson)
 * @see com.mongodb.client.MongoCollection#deleteMany(org.bson.conversions.Bson)
 * @see com.mongodb.client.MongoCollection#drop()
 * 
 * @author zhoujiagen
 *
 */
public class RemoveDataApp {

	public static void main(String[] args) {
		MongoClient mongoClient = MongoUtils.client();
		MongoDatabase mongoDatabase = MongoUtils.database(mongoClient, MongoUtils.Constants.DB_NANE);
		MongoCollection<Document> mongoCollection = MongoUtils.collection(mongoDatabase,
				MongoUtils.Constants.COLLECTION_NAME);

		// _1_removeDocumentWithConditions(mongoCollection);

		// _2_removeAllDocumentInCollection(mongoCollection);

		_3_dropCollection(mongoCollection);
	}

	/** (1) 删除匹配条件的文档 */
	static void _1_removeDocumentWithConditions(MongoCollection<Document> mongoCollection) {
		Document matchCondition = new Document("borough", "Manhattan");
		FindIterable<Document> queryResult = mongoCollection.find(matchCondition);
		MongoUtils.renderDocumentCount(queryResult);

		DeleteResult deleteResult = mongoCollection.deleteMany(matchCondition);
		System.out.println(deleteResult);

		queryResult = mongoCollection.find(matchCondition);
		MongoUtils.renderDocumentCount(queryResult);
	}

	/** (2) 删除集合中所有文档 */
	static void _2_removeAllDocumentInCollection(MongoCollection<Document> mongoCollection) {
		Document matchCondition = new Document();
		FindIterable<Document> queryResult = mongoCollection.find(matchCondition);
		MongoUtils.renderDocumentCount(queryResult);

		DeleteResult deleteResult = mongoCollection.deleteMany(matchCondition);
		System.out.println(deleteResult);

		queryResult = mongoCollection.find(matchCondition);
		MongoUtils.renderDocumentCount(queryResult);

	}

	/** (3) 删除集合 */
	static void _3_dropCollection(MongoCollection<Document> mongoCollection) {
		mongoCollection.drop();

		Document matchCondition = new Document();
		FindIterable<Document> queryResult = mongoCollection.find(matchCondition);
		MongoUtils.renderDocumentCount(queryResult);
	}

}
