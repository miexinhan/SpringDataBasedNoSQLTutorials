package com.spike.springdata.neo4j.service;

import javax.transaction.TransactionManager;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.support.Neo4jEmbeddedTransactionManager;

@Configuration
@ComponentScan
public class Neo4jTransactionManagerConfig {
	public static final String Embedded_DB_DIR = "springdataneo4j.db";

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		GraphDatabaseService result = new GraphDatabaseFactory().newEmbeddedDatabase(Embedded_DB_DIR);
		return result;
	}

	@Bean
	@Qualifier("tx")
	public TransactionManager transactionManager() {
		TransactionManager result = new Neo4jEmbeddedTransactionManager(graphDatabaseService());
		return result;
	}

}
