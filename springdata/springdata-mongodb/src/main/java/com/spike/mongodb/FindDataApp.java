package com.spike.mongodb;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.spike.mongodb.util.MongoUtils;

/**
 * 
 * <pre>
 * 查询数据
 * 
 * 注意
 * (1) MongoDB中查询均在单个集合作用域内
 * (2) 在org.bson.Document上指定过滤器或条件，作为find()的参数
 * (3) 查询结果FindIterable，即产生文件的迭代对象
 * 
 * 示例
 * (1) 获取集合中所有文档
 * (2) 指定相等条件
 * (3) 用操作符指定条件
 * (4) 复合条件
 * (5) 查询结果排序
 * 
 * 
 * 其他方法：
 * 完整查询文档：http://docs.mongodb.com/manual/tutorial/query-documents
 * 投影：http://docs.mongodb.com/manual/tutorial/project-fields-from-query-results
 * 投影操作符参考：http://docs.mongodb.com/manual/reference/operator/query
 * 游标方法：http://docs.mongodb.com/manual/reference/method/js-cursor
 * 
 * </pre>
 * 
 * @see com.mongodb.client.MongoCollection#find()
 * @see com.mongodb.client.MongoCollection#find(org.bson.conversions.Bson)
 * 
 * @see com.mongodb.client.model.Filters
 * @see com.mongodb.client.model.Sorts
 * 
 * @author zhoujiagen
 *
 */
public class FindDataApp {

	public static void main(String[] args) {
		MongoClient mongoClient = MongoUtils.client();
		MongoDatabase mongoDatabase = MongoUtils.database(mongoClient, MongoUtils.Constants.DB_NANE);
		MongoCollection<Document> mongoCollection = MongoUtils.collection(mongoDatabase,
				MongoUtils.Constants.COLLECTION_NAME);

		// _1_findAllDocumentInCollection(mongoCollection);

		// _2_findWithEqConditions(mongoCollection);

		// _3_findWithConditionsUsingOperators(mongoCollection);

		// _4_findWithCombineConditions(mongoCollection);

		_5_sort_query_result(mongoCollection);
	}

	/** (1) 获取集合中所有文档 */
	static void _1_findAllDocumentInCollection(MongoCollection<Document> mongoCollection) {
		FindIterable<Document> iterableQueryResult = mongoCollection.find();
		final Map<String, Long> result = new HashMap<String, Long>();
		result.put("result", 0L);

		// 函数式风格调用
		iterableQueryResult.forEach(new Block<Document>() {
			@Override
			public void apply(Document t) {
				result.put("result", result.get("result") + 1);
			}
		});

		System.out.println("Query result Count = " + result);
	}

	/** (2) 指定相等条件 */
	static void _2_findWithEqConditions(MongoCollection<Document> mongoCollection) {
		FindIterable<Document> iterableQueryResult = null;
		// 1 顶层字段上
		Document condition = new Document("borough", "Manhattan");
		// iterableQueryResult = mongoCollection.find(condition);

		// 2 内嵌文档字段上，使用点缀记号
		condition = new Document("address.zipcode", "10075");
		// iterableQueryResult = mongoCollection.find(condition);

		// 2.1 使用Filters帮助类
		// iterableQueryResult = mongoCollection.find(Filters.eq("address.zipcode", "10075"));

		// 3 列表字段上
		condition = new Document("grades.grade", "B");
		iterableQueryResult = mongoCollection.find(condition);

		// 输出结果
		iterableQueryResult.forEach(MongoUtils.renderDocumentBlock());
	}

	/** (3) 用操作符指定条件 */
	static void _3_findWithConditionsUsingOperators(MongoCollection<Document> mongoCollection) {
		// 比较操作符: $lt, $gt

		FindIterable<Document> iterableQueryResult = null;
		// Document condition = new Document("grades.score", new Document("$gt", 30));
		// iterableQueryResult = mongoCollection.find(condition);

		// 或者使用Filters
		iterableQueryResult = mongoCollection.find(Filters.gt("grades.score", 30));

		// 输出结果
		iterableQueryResult.forEach(MongoUtils.renderDocumentBlock());
	}

	/** (4) 复合条件 */
	static void _4_findWithCombineConditions(MongoCollection<Document> mongoCollection) {
		// 关系操作符: $and, $or
		FindIterable<Document> iterableQueryResult = null;

		// 1 AND
		// Document condition = new Document()//
		// .append("cuisine", "Italian")//
		// .append("address.zipcode", "10075");
		// iterableQueryResult = mongoCollection.find(condition);

		// 或者使用Filters
		iterableQueryResult = mongoCollection.find(//
				Filters.and(//
						Filters.eq("cuisine", "Italian"), //
						Filters.eq("address.zipcode", "10075"))//
				);

		// 2 OR
		// Document condition = new Document(//
		// "$or",//
		// Arrays.asList(new Document("cuisine", "Italian"), new Document("address.zipcode", "10075"))//
		// );
		// iterableQueryResult = mongoCollection.find(condition);

		// 或者使用Filters
		iterableQueryResult = mongoCollection.find(//
				Filters.or(new Document("cuisine", "Italian"), new Document("address.zipcode", "10075"))//
				);

		// 输出结果
		iterableQueryResult.forEach(MongoUtils.renderDocumentBlock());
	}

	/** (5) 查询结果排序 */
	static void _5_sort_query_result(MongoCollection<Document> mongoCollection) {
		FindIterable<Document> iterableQueryResult = null;

		// 1 升序，-1降序
		Document sortCondition = new Document()//
				.append("borough", 1)//
				.append("address.zipcode", 1);
		iterableQueryResult = mongoCollection.find().sort(sortCondition);

		// 或者使用Sorts工具类

		iterableQueryResult = mongoCollection.find().sort(Sorts.ascending("borough", "address.zipcode"));

		// 输出结果
		iterableQueryResult.forEach(MongoUtils.renderDocumentBlock());
	}
}
