package com.spike.mongodb;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.spike.mongodb.util.MongoUtils;

/**
 * <pre>
 * 插入数据
 * 
 * 注意：
 * (1) 如果尝试向不存在的集合中插入文档，则MongoDB会创建该集合
 * (2) 如果在insertOne()中文档不包含_id字段，则驱动器自动添加该字段，并赋值ObjectId
 * 
 * </pre>
 * @see org.bson. Document
 * @see com.mongodb.client.MongoCollection#insertOne(Object)
 * @see com.mongodb.client.MongoCollection#insertMany(List)
 * @author zhoujiagen
 */
public class InsertDataApp {

  /**
   * <pre>
   * 样例文档
   * {
   *   "address": {
   *      "building": "1007",
   *      "coord": [ -73.856077, 40.848447 ],
   *      "street": "Morris Park Ave",
   *      "zipcode": "10462"
   *   },
   *   "borough": "Bronx",
   *   "cuisine": "Bakery",
   *   "grades": [
   *      { "date": { "$date": 1393804800000 }, "grade": "A", "score": 2 },
   *      { "date": { "$date": 1378857600000 }, "grade": "A", "score": 6 },
   *      { "date": { "$date": 1358985600000 }, "grade": "A", "score": 10 },
   *      { "date": { "$date": 1322006400000 }, "grade": "A", "score": 9 },
   *      { "date": { "$date": 1299715200000 }, "grade": "B", "score": 14 }
   *   ],
   *   "name": "Morris Park Bake Shop",
   *   "restaurant_id": "30075445"
   * }
   * </pre>
   * @param args
   * @throws ParseException
   */
  public static void main(String[] args) throws ParseException {

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);

    MongoClient mongoClient = MongoUtils.client();
    MongoDatabase db = MongoUtils.database(mongoClient, MongoUtils.Constants.DB_NANE);

    // 内嵌文档
    Document addressDocument = new Document()//
        .append("street", "2 Avenue")//
        .append("zipcode", "10075")//
        .append("building", "1480")//
        .append("coord", Arrays.asList(-73.9557413, 40.7720266));

    // 内嵌文档列表
    List<Document> gradesDocumentList = Arrays.asList(//
      new Document().append("date", format.parse("2014-10-01T00:00:00Z"))//
          .append("grade", "A")//
          .append("score", 11), //
      new Document().append("date", format.parse("2014-01-16T00:00:00Z"))//
          .append("grade", "B")//
          .append("score", 17));

    // 最终文档
    Document document = new Document("address", addressDocument)//
        .append("borough", "Manhattan")//
        .append("cuisine", "Italian")//
        .append("grades", gradesDocumentList)//
        .append("name", "Vella")//
        .append("restaurant_id", "41704620");

    // 向集合中插入文档
    MongoCollection<Document> mongoCollection =
        MongoUtils.collection(db, MongoUtils.Constants.COLLECTION_NAME);
    mongoCollection.insertOne(document);
  }
}
