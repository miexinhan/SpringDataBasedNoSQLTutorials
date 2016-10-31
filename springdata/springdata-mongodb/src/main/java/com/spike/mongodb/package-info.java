/**
 * <pre>
 * MongoDB Java Driver的Spike测试包
 * 
 * 1 参考链接
 * https://docs.mongodb.com/getting-started/java/
 * 
 * 2 数据集URL
 * https://raw.githubusercontent.com/mongodb/docs-assets/primer-dataset/primer-dataset.json
 * 
 * 3 样例文档
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
 * 
 * 4 Schema信息
 * 数据库test
 * 集合restaurants
 * 
 * 5 操作
 * 插入数据 InsertDataApp
 * 查询数据 FindDataApp
 * 更新数据 UpdateDataApp
 * 删除数据 RemoveDataApp
 * 数据聚合 DataAggregationApp
 * 索引	IndexApp
 * 
 * </pre>
 */
package com.spike.mongodb;