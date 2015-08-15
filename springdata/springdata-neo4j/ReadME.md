# 1 Descriptive Annotation
This package `anno`contains annotation to direct classes' source books or online links.

`@OnlineResource`: online tutorials or other materials

`@SpringDataBook`: Pollack M., Gierke O., Risberg T. et al. **Spring Data**. 2012.

`@Neo4jInActionBook`: Vukotic A., Watt N. et al. **Neo4j in Action**. 2015.

`@GraphDatabasesBook`: Robinson I., Webber J., Eifrem E. **Graph Databases**. 2013.

# 2 Configuration
## 2.1 `Neo4jAppDevConfig`
Spring annotation based configuration class, using Neo4j embedded database

## 2.2 `Neo4jAppDevConfig`
Spring annotation based configuration class, using Neo4j production database

**tricks when play with production database server**:
	
	./neo4j stop
	rm -rf ../data/graph.db/*
	./neo4j start
	
# 3 Use Cases
## 3.1 `PersonIntegrationTest`
Offical tutorial of Spring-data-neo4j

## 3.2 Spring Data book
### `SpringDataBookDevTest`
Integration test using embedded database for spring-data-neo4j.

### `SpringDataBookProdTest`
Integration test using production database for spring-data-neo4j and spring-data-neo4j-rest.

## 3.3 Spring in Action book
### `Neo4jAPICompreheansiveDemonstration`
A comprehensive demonstration of Neo4j native Java APIs.

## 3.4 Graph Database book
### `CoreNeo4jAPIDemonstration`
Simple demonstration of core Neo4j API with comment in Chinese.
