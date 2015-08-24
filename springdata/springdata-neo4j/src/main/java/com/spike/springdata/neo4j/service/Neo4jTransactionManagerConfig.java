package com.spike.springdata.neo4j.service;

import javax.transaction.TransactionManager;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.event.TransactionData;
import org.neo4j.graphdb.event.TransactionEventHandler;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.support.Neo4jEmbeddedTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
@ComponentScan
public class Neo4jTransactionManagerConfig {
	public static final String Embedded_DB_DIR = "springdataneo4j.db";

	@Bean
	public GraphDatabaseService graphDatabaseService() {
		GraphDatabaseService result = new GraphDatabaseFactory().newEmbeddedDatabase(Embedded_DB_DIR);

		// register transaction events
		result.registerTransactionEventHandler(new TransactionEventHandler<Object>() {

			@Override
			public Object beforeCommit(TransactionData data) throws Exception {
				System.out.println("===beforeCommit");
				return null;
			}

			@Override
			public void afterCommit(TransactionData data, Object state) {
				System.out.println("===afterCommit");
			}

			@Override
			public void afterRollback(TransactionData data, Object state) {
				System.out.println("===afterRollback");
			}
		});

		return result;
	}

	@Bean
	@Qualifier("txManager")
	public TransactionManager transactionManager() {
		TransactionManager result = new Neo4jEmbeddedTransactionManager(graphDatabaseService());
		return result;
	}

	@Bean
	@Qualifier("platformTxManager")
	public PlatformTransactionManager platformTransactionManager() {
		PlatformTransactionManager result = new JtaTransactionManager(transactionManager());
		return result;
	}

}
