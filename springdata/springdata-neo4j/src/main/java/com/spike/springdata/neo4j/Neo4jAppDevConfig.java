package com.spike.springdata.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

import com.spike.springdata.neo4j.anno.Neo4jInActionBook;
import com.spike.springdata.neo4j.service.Neo4jTransactionManagerConfig;

@Configuration
@EnableNeo4jRepositories(excludeFilters = { @Filter(value = { Neo4jTransactionManagerConfig.class }) })
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

	@Neo4jInActionBook(chapter = { "5" })
	@Bean
	public GraphDatabaseService graphDatabaseService() {
		// GraphDatabaseService result = new
		// GraphDatabaseFactory().newEmbeddedDatabase(Embedded_DB_DIR);

		// see manual-v2.2.3 37.12. Automatic Indexing
		GraphDatabaseService result = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(Embedded_DB_DIR)
				.setConfig(GraphDatabaseSettings.node_auto_indexing, "true")
				.setConfig(GraphDatabaseSettings.relationship_auto_indexing, "true")
				.setConfig(GraphDatabaseSettings.node_keys_indexable, "name, dateOfBirth")
				.setConfig(GraphDatabaseSettings.relationship_keys_indexable, "type, name").newGraphDatabase();

		return result;

	}
}
