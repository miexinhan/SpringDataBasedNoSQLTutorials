package com.spike.springdata.neo4j.nativeAPI;

import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.Neo4jAppUtils;
import com.spike.springdata.neo4j.anno.Neo4jInActionBook;

/**
 * DemonStration of Neo4j manual indexing using APIs
 * @author zhoujiagen<br/>
 *         Aug 15, 2015 11:15:32 PM
 */
@Neo4jInActionBook(chapter = { "5" })
public class Neo4jManualIndexingDemonstration {
  private static final Logger logger = Logger.getLogger(Neo4jManualIndexingDemonstration.class);

  /**
   * it's not a good practice!
   */
  private static final String NEWLINE = System.getProperty("line.separator");

  private static final String names[] = { "Joanne Smith", "Kate Smith", "John Johnson" };
  private static final String emails[] = { "jsmith@example.org", "ksmith@exmaple.org",
      "jjohnson@exmaple.org" };
  private static final Integer ages[] = { 34, 35, 35 };

  private static Long[] person_ids = new Long[3];

  private static final String NODE_INDEX_NAME = "users";

  public static void main(String[] args) {
    Neo4jAppUtils.clean(Neo4jAppDevConfig.Embedded_DB_DIR);

    // create graph database service
    GraphDatabaseService gds =
        new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

    populateGraphData(gds);

    List<Node> persons = Neo4jAppUtils.getNodesByIds(gds, person_ids);
    Neo4jAppUtils.createNodeIndex(gds, NODE_INDEX_NAME, persons.get(0), PropEnum.EMAIL.name(),
      emails[0]);
    searchWithNodeIndex(gds, NODE_INDEX_NAME, PropEnum.EMAIL.name(), emails[0]);

    Neo4jAppUtils.createNodeIndex(gds, NODE_INDEX_NAME, persons.get(0), PropEnum.AGE.name(),
      ages[0]);
    Neo4jAppUtils.createNodeIndex(gds, NODE_INDEX_NAME, persons.get(1), PropEnum.AGE.name(),
      ages[1]);
    Neo4jAppUtils.createNodeIndex(gds, NODE_INDEX_NAME, persons.get(2), PropEnum.AGE.name(),
      ages[2]);
    searchWithIndexReturnMultipleResults(gds, PropEnum.AGE.name(), ages[1]);

    // dealing with changed indexed entry
    String newEmail = "jsmith_new@example.org";
    Neo4jAppUtils.changeIndexValue(gds, NodeIndexEnum.USERS.name(), PropEnum.EMAIL.name(),
      emails[0], newEmail);
    searchWithNodeIndex(gds, NODE_INDEX_NAME, PropEnum.EMAIL.name(), emails[0]);
    searchWithNodeIndex(gds, NODE_INDEX_NAME, PropEnum.EMAIL.name(), newEmail);
  }

  static void populateGraphData(GraphDatabaseService gds) {
    try (Transaction tx = gds.beginTx();) {
      Node person1 = gds.createNode();
      person_ids[0] = person1.getId();
      person1.setProperty(PropEnum.NAME.name(), names[0]);
      person1.setProperty(PropEnum.EMAIL.name(), emails[0]);
      person1.setProperty(PropEnum.AGE.name(), ages[0]);

      Node person2 = gds.createNode();
      person_ids[1] = person2.getId();
      person2.setProperty(PropEnum.NAME.name(), names[1]);
      person2.setProperty(PropEnum.EMAIL.name(), emails[1]);
      person2.setProperty(PropEnum.AGE.name(), ages[1]);

      Node person3 = gds.createNode();
      person_ids[2] = person3.getId();
      person3.setProperty(PropEnum.NAME.name(), names[2]);
      person3.setProperty(PropEnum.EMAIL.name(), emails[2]);
      person3.setProperty(PropEnum.AGE.name(), ages[2]);

      tx.success();
    } catch (Exception e) {
      logger.error("Strange things happeded when populate graph data, refer", e);
    }
  }

  static void searchWithNodeIndex(GraphDatabaseService gds, String indexName, String indexKey,
      String indexValue) {
    logger.info(NEWLINE + "searchWithIndex(" + indexKey + "," + indexValue + ")");

    try (Transaction tx = gds.beginTx();) {
      Index<Node> index = gds.index().forNodes(indexName);

      // search with index
      IndexHits<Node> indexHits = index.get(indexKey, indexValue);
      Node resultNode = indexHits.getSingle();// single match

      if (resultNode != null) {
        logger.info(NEWLINE + renderNode(resultNode));
      }

      tx.success();
    } catch (Exception e) {
      logger.error("Strange things happeded when search with index, refer", e);
    }
  }

  static void searchWithIndexReturnMultipleResults(GraphDatabaseService gds, String indexKey,
      Integer indexValue) {

    try (Transaction tx = gds.beginTx();) {
      Index<Node> index = gds.index().forNodes(indexKey);

      // search with index
      IndexHits<Node> indexHits = index.get(indexKey, indexValue);
      for (Node node : indexHits) {
        logger.info(NEWLINE + renderNode(node));
      }

      tx.success();
    } catch (Exception e) {
      logger.error(
        "Strange things happeded when search with index, and multiple results expected, refer", e);
    }

  }

  /**
   * How lovely is Scala's `sealed`
   */
  static final PropEnum[] ALL_PropEnum = { PropEnum.NAME, PropEnum.EMAIL, PropEnum.AGE };

  static String renderNode(Node node) {
    StringBuilder sb = new StringBuilder();
    for (PropEnum propEnum : ALL_PropEnum) {
      sb.append(propEnum.name() + ": " + node.getProperty(propEnum.name()) + ",");
    }
    String result = sb.toString();
    return result.substring(0, result.length() - 1);
  }

  private static enum PropEnum {
    NAME, EMAIL, AGE
  }

  /**
   * the indexes used for nodes
   */
  private static enum NodeIndexEnum {
    USERS
  }

}
