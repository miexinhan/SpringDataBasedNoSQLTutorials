package com.spike.springdata.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringCypherRestGraphDatabase;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

@Configuration
@EnableNeo4jRepositories
public class Neo4jAppConfig extends Neo4jConfiguration {

	/**
	 * 嵌入式数据库的本地文件目录
	 */
	public static final String Embedded_DB_DIR = "springdataneo4j.db";

	/**
	 * Neo4j服务器URL
	 */
	public static final String SERVER_URL = "http://localhost:7474/db/data";

	/**
	 * Must use {@link #setBasePackage(String...)}!!!
	 */
	public Neo4jAppConfig() {
		setBasePackage("com.spike.springdata.neo4j");
	}

	@Bean
	@Profile("DEV")
	public GraphDatabaseService graphDatabaseServiceForTest() {
		return new GraphDatabaseFactory().newEmbeddedDatabase(Embedded_DB_DIR);
	}

	// NOT WORKING!!!
	@Bean
	@Profile("ERROR")
	public GraphDatabaseService graphDatabaseServiceWithError() {
		SpringCypherRestGraphDatabase result = new SpringCypherRestGraphDatabase(SERVER_URL, "neo4j", "root");
		return result;
	}

	@Bean
	@Profile("PRODUCT")
	public GraphDatabaseService graphDatabaseService() {
		@SuppressWarnings("deprecation")
		SpringRestGraphDatabase result = new SpringRestGraphDatabase(SERVER_URL, "neo4j", "root");
		return result;
	}
}
