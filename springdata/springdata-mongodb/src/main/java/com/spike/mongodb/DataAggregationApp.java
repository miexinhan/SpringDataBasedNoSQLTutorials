package com.spike.mongodb;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.spike.mongodb.util.MongoUtils;

/**
 * <pre>
 * 数据聚合
 * 
 * 示例
 * (1) 按字段分组文档
 * (2) 过滤和分组文档
 * 
 * 聚合中管道化阶段(pipeline stage)的说明：https://docs.mongodb.com/manual/meta/aggregation-quick-reference/
 * 
 * </pre>
 * @see com.mongodb.client.MongoCollection#aggregate(java.util.List)
 * @see com.mongodb.client.AggregateIterable
 * @author zhoujiagen
 */
public class DataAggregationApp {
  public static void main(String[] args) {
    MongoClient mongoClient = MongoUtils.client();
    MongoDatabase mongoDatabase = MongoUtils.database(mongoClient, MongoUtils.Constants.DB_NANE);
    MongoCollection<Document> mongoCollection =
        MongoUtils.collection(mongoDatabase, MongoUtils.Constants.COLLECTION_NAME);

    // _1_groupDocumentByField(mongoCollection);

    _2_filterAndGroupDocument(mongoCollection);
  }

  /** (1) 按字段分组文档 */
  static void _1_groupDocumentByField(MongoCollection<Document> mongoCollection) {
    // 使用$group阶段按字段分组文档，在_id字段中指定需要分组的字段
    // $group阶段使用累加器执行每个分组中的计算

    Document aggregateDocument = new Document(//
        "$group", // $group阶段
        new Document("_id", "$borough")// borough分组字段
            .append("count", new Document("$sum", 1))// $sum累加器
        );

    AggregateIterable<Document> aggregateIterable =
        mongoCollection.aggregate(Arrays.asList(aggregateDocument));
    aggregateIterable.forEach(MongoUtils.renderDocumentBlock());
  }

  /** (2) 过滤和分组文档 */
  static void _2_filterAndGroupDocument(MongoCollection<Document> mongoCollection) {
    // 使用$match阶段过滤文档
    Document matchDocument = new Document(//
        "$match",//
        new Document("borough", "Queens").append("cuisine", "Brazilian")//
        );
    Document groupDocument = new Document(//
        "$group", //
        new Document("_id", "$address.zipcode").append("count", new Document("$sum", 1))//
        );

    AggregateIterable<Document> aggregateIterable = //
        mongoCollection.aggregate(Arrays.asList(matchDocument, groupDocument));
    aggregateIterable.forEach(MongoUtils.renderDocumentBlock());
  }

}
