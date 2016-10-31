package com.spike.springdata.neo4j.service;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class APretendService {
  @Autowired
  private GraphDatabaseService gds;

  @Transactional
  public Node createNodeViaAnnotatedMethod(String name, int age) {
    Node node = gds.createNode();
    node.setProperty("name", name);
    node.setProperty("age", age);
    return node;
  }

  public void setGraphDatabaseService(GraphDatabaseService gds) {
    this.gds = gds;
  }
}