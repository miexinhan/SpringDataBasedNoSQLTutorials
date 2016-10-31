package com.spike.springdata.neo4j.nativeAPI;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.Neo4jAppUtils;

/**
 * Demonstration of Neo4j transaction with explicit read/write lock
 * @author zhoujiagen<br/>
 *         Aug 24, 2015 10:10:06 PM
 */
public class Neo4jTransactionDemonstration {

  public static void main(String[] args) {
    Neo4jAppUtils.clean(Neo4jAppDevConfig.Embedded_DB_DIR);

    // create graph database service
    GraphDatabaseService gds =
        new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

    // create execute engine
    ExecutionEngine engine = new ExecutionEngine(gds);

    Neo4jCypherAPIDemonstration.populateGraphData(gds, engine);

    readlock(gds);

    writelock(gds);
  }

  private static void readlock(GraphDatabaseService gds) {
    try (Transaction tx = gds.beginTx();) {
      Node john = gds.index().forNodes("users").get("name", "John Johnson").getSingle();
      Long age = (Long) john.getProperty("age");
      System.out.println(age);

      // explicit read lock
      tx.acquireReadLock(john);

      Thread.sleep(1000L);

      // read again
      Long secondAge = (Long) john.getProperty("age");
      if (age != secondAge) {
        throw new RuntimeException("wrong!");
      }

      tx.success();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void writelock(GraphDatabaseService gds) {
    try (Transaction tx = gds.beginTx();) {

      Node john = gds.index().forNodes("users").get("name", "John Johnson").getSingle();
      Node kate = gds.index().forNodes("users").get("name", "Kate Smith").getSingle();

      // explicit write lock
      tx.acquireWriteLock(john);
      tx.acquireWriteLock(kate);

      Thread.sleep(1000L);

      john.setProperty("age", 100L);
      kate.setProperty("age", 90L);

      tx.success();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
