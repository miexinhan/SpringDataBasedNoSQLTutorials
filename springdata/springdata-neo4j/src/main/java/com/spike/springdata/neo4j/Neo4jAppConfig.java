package com.spike.springdata.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringCypherRestGraphDatabase;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;

@Configuration
@EnableNeo4jRepositories
public class Neo4jAppConfig extends Neo4jConfiguration {

  /**
   * URL of Neo4j server
   */
  public static final String SERVER_URL = "http://localhost:7474/db/data";

  /**
   * Must use {@link #setBasePackage(String...)}!!!
   */
  public Neo4jAppConfig() {
    setBasePackage("com.spike.springdata.neo4j");
  }

  // NOT WORKING!!!
  @Bean
  public GraphDatabaseService graphDatabaseServiceWithError() {
    SpringCypherRestGraphDatabase result =
        new SpringCypherRestGraphDatabase(SERVER_URL, "neo4j", "root");
    return result;
  }

  @Bean
  public GraphDatabaseService graphDatabaseService() {
    @SuppressWarnings("deprecation")
    SpringRestGraphDatabase result = new SpringRestGraphDatabase(SERVER_URL, "neo4j", "root");
    return result;
  }
}
