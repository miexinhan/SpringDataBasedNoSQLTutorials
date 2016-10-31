package com.spike.mongodb;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import com.spike.mongodb.util.MongoUtils;

/**
 * <pre>
 * 更新数据
 * 
 * 注意update*()方法接收参数
 * (1) 匹配要更新的文档(可以是多个)的过滤文档
 * (2) 描述修改操作的更新文档
 * (3) 可选的选项参数。
 * 
 * 不能更新_id字段。
 * 
 * 
 * 示例
 * (1) 更新字段
 * (1.1) 更新顶级字段
 * (1.2) 更新嵌套字段
 * (1.3) 更新多个文档
 * (2) 替换文档
 * 
 * 原子性
 * 在当个文档上的操作是原子性的。
 * 如果单个更新操作修改了集合中多个文档，则不保证该操作的原子性。
 * 详细阐述：http://docs.mongodb.com/manual/core/write-operations-atomicity
 * 
 * 完整的更新操作符说明：http://docs.mongodb.com/manual/reference/operator/update
 * 
 * 
 * </pre>
 * @see com.mongodb.client.MongoCollection#updateOne(org.bson.conversions.Bson,
 *      org.bson.conversions.Bson)
 * @see com.mongodb.client.MongoCollection#updateOne(org.bson.conversions.Bson,
 *      org.bson.conversions.Bson, com.mongodb.client.model.UpdateOptions)
 * @see com.mongodb.client.MongoCollection#updateMany(org.bson.conversions.Bson,
 *      org.bson.conversions.Bson)
 * @see com.mongodb.client.MongoCollection#updateMany(org.bson.conversions.Bson,
 *      org.bson.conversions.Bson, com.mongodb.client.model.UpdateOptions)
 * @see com.mongodb.client.MongoCollection#replaceOne(org.bson.conversions.Bson, Object)
 * @see com.mongodb.client.MongoCollection#replaceOne(org.bson.conversions.Bson, Object,
 *      com.mongodb.client.model.UpdateOptions)
 * @see com.mongodb.client.model.UpdateOptions
 * @author zhoujiagen
 */
public class UpdateDataApp {

  public static void main(String[] args) {
    MongoClient mongoClient = MongoUtils.client();
    MongoDatabase mongoDatabase = MongoUtils.database(mongoClient, MongoUtils.Constants.DB_NANE);
    MongoCollection<Document> mongoCollection =
        MongoUtils.collection(mongoDatabase, MongoUtils.Constants.COLLECTION_NAME);

    // _1_1_updateTopLevelField(mongoCollection);

    // _1_2_updateNestedField(mongoCollection);

    // _1_3_updateMultipleDocuments(mongoCollection);

    _2_replateDocument(mongoCollection);
  }

  /** (1.1) 更新顶级字段 */
  static void _1_1_updateTopLevelField(MongoCollection<Document> mongoCollection) {
    Document filterDocument = new Document("name", "Juni");// 待更新文档匹配条件

    // 1 先查找
    FindIterable<Document> queryResult = mongoCollection.find(filterDocument);
    queryResult.forEach(MongoUtils.renderDocumentBlock());

    // 2 更新
    Document updateDocument = new Document()//
        .append("$set", new Document("cuisine", "American (New)"))// 更新字段
        .append("$currentDate", new Document("lastModified", true));// 添加字段

    UpdateResult updateResult = mongoCollection.updateOne(filterDocument, updateDocument);
    System.out.println(updateResult);

    // 3 再查找
    queryResult = mongoCollection.find(filterDocument);
    queryResult.forEach(MongoUtils.renderDocumentBlock());
  }

  /** (1.2) 更新嵌套字段 */
  static void _1_2_updateNestedField(MongoCollection<Document> mongoCollection) {
    Document filterDocument = new Document("restaurant_id", "41156888");// 待更新文档匹配条件

    // 1 先查找
    FindIterable<Document> queryResult = mongoCollection.find(filterDocument);
    queryResult.forEach(MongoUtils.renderDocumentBlock());

    // 2 更新
    Document updateDocument =
        new Document("$set", new Document("address.street", "East 31st Street."));
    UpdateResult updateResult = mongoCollection.updateOne(filterDocument, updateDocument);
    System.out.println(updateResult);

    // 3 再查找
    queryResult = mongoCollection.find(filterDocument);
    queryResult.forEach(MongoUtils.renderDocumentBlock());
  }

  /** (1.3) 更新多个文档 */
  static void _1_3_updateMultipleDocuments(MongoCollection<Document> mongoCollection) {
    Document filterDocument = new Document("address.zipcode", "10016")//
        .append("cuisine", "Category To Be Determined");// 待更新文档匹配条件

    // 1 先查找
    FindIterable<Document> queryResult = mongoCollection.find(filterDocument);
    queryResult.forEach(MongoUtils.renderDocumentBlock());

    // 2 更新
    Document updateDocument = new Document("$set", new Document("cuisine", "Other"))//
        .append("$currentDate", new Document("lastModified", true));
    UpdateResult updateResult = mongoCollection.updateMany(filterDocument, updateDocument);
    System.out.println(updateResult);

    // 3 再查找
    filterDocument = new Document("address.zipcode", "10016").append("cuisine", "Other");
    queryResult = mongoCollection.find(filterDocument);
    queryResult.forEach(MongoUtils.renderDocumentBlock());
  }

  /** (2) 替换文档 */
  static void _2_replateDocument(MongoCollection<Document> mongoCollection) {
    // 在replaceOne中传入全新的文档，替换原文档
    // 新文档的字段可以与原文档不同
    // 新文档中可以不包含_id，如果包含了则必须与现有存在值相同
    // 更新后，只存在参数中新文档的字段

    // 1 查找原文档，匹配两个
    Document filterDocument = new Document("restaurant_id", "41704620");
    FindIterable<Document> queryResult = mongoCollection.find(filterDocument);
    queryResult.forEach(MongoUtils.renderDocumentBlock());

    // 2 替换文档
    Document replacementDocument = new Document("address", //
        new Document()//
            .append("street", "2 Avenue")//
            .append("zipcode", "10075")//
            .append("building", "1480")//
            .append("coord", Arrays.asList(-73.9557413, 40.7720266)))//
        .append("name", "Vella 2");
    UpdateResult updateResult = mongoCollection.replaceOne(filterDocument, replacementDocument);
    System.out.println(updateResult);

    // 3 查找更新后的文档，只替换其中一个
    filterDocument = new Document("name", "Vella 2");
    queryResult = mongoCollection.find(filterDocument);
    queryResult.forEach(MongoUtils.renderDocumentBlock());
  }
}
