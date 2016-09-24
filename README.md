# 1 Motivation
Based on the efforts of Spring Data Project, Jave-EEer can easily introduce NoSQL technologies into daily development toolkits.

**Additional**: JPA (20151107)

# 2 Scope
This project is dedicated to play with two forms of NoSQL databases:

+ Graph: [Neo4j](https://neo4j.com/product/)

> Neo4j is a highly scalable, native graph database purpose-built to leverage not only data but also its relationships.

> Neo4j's native graph storage and processing engine deliver constant, real-time performance, helping enterprises build intelligent applications to meet today’s evolving data challenges.

+ Key-Value: [Redis](http://redis.io/)

> Redis is an open source (BSD licensed), in-memory data structure store, used as database, cache and message broker. It supports data structures such as strings, hashes, lists, sets, sorted sets with range queries, bitmaps, hyperloglogs and geospatial indexes with radius queries. Redis has built-in replication, Lua scripting, LRU eviction, transactions and different levels of on-disk persistence, and provides high availability via Redis Sentinel and automatic partitioning with Redis Cluster.

+ Document: [MongoDB](https://docs.mongodb.com/)

> MongoDB is an open-source, document database designed for ease of development and scaling.

+ Column Family: [HBase](https://hbase.apache.org/)

> Apache HBase™ is the Hadoop database, a distributed, scalable, big data store.

> Use Apache HBase™ when you need random, realtime read/write access to your Big Data. This project's goal is the hosting of very large tables -- billions of rows X millions of columns -- atop clusters of commodity hardware. Apache HBase is an open-source, distributed, versioned, non-relational database modeled after Google's Bigtable: A Distributed Storage System for Structured Data by Chang et al. Just as Bigtable leverages the distributed data storage provided by the Google File System, Apache HBase provides Bigtable-like capabilities on top of Hadoop and HDFS.

+ ORM: JPA (JSR 317, JSR 338)

as final view of specification-supported Java based RDBMS development

+ QueryDsl

+ OPM: MyBatis 3

+ Text: [ElasticSearch](https://www.elastic.co/products/elasticsearch)

> Search & Analyze Data in Real Time

> Actionable Insight at Your Fingers
>> Distributed, scalable, and highly available  
>> Real-time search and analytics capabilities  
>> Sophisticated RESTful API