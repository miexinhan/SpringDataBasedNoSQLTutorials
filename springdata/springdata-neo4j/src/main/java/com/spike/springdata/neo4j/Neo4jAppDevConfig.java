package com.spike.springdata.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

@Configuration
@EnableNeo4jRepositories
public class Neo4jAppDevConfig extends Neo4jConfiguration {

	/**
	 * local file directory of embedded database of Neo4j
	 */
	public static final String Embedded_DB_DIR = "springdataneo4j.db";

	/**
	 * Must use {@link #setBasePackage(String...)}!!!
	 */
	public Neo4jAppDevConfig() {
		setBasePackage("com.spike.springdata.neo4j");
	}

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		return new GraphDatabaseFactory().newEmbeddedDatabase(Embedded_DB_DIR);
	}
}
