package com.spike.springdata.neo4j.nativeAPI;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.Neo4jAppUtils;
import com.spike.springdata.neo4j.anno.Neo4jInActionBook;

/**
 * Demonstration of Neo4j Schema Indexing<br/>
 * a feature was introduced in 2.0+
 * @author zhoujiagen<br/>
 *         Aug 17, 2015 11:07:27 PM
 */
@Neo4jInActionBook(chapter = { "5" })
public class Neo4jSchemaIndexingDemonstration {
  private static final Logger logger = Logger.getLogger(Neo4jSchemaIndexingDemonstration.class);

  /**
   * it's not a good practice!
   */
  private static final String NEWLINE = System.getProperty("line.separator");

  static final Label movieLabel = DynamicLabel.label(LabelEnum.MOVIE.name());
  static final Label userLabel = DynamicLabel.label(LabelEnum.USER.name());
  static Node movie, user;
  static final String name = "Michael Collins";

  public static void main(String[] args) {
    Neo4jAppUtils.clean(Neo4jAppDevConfig.Embedded_DB_DIR);

    Label movieLabel = DynamicLabel.label(LabelEnum.MOVIE.name());
    Label userLabel = DynamicLabel.label(LabelEnum.USER.name());

    // create graph database service
    GraphDatabaseService gds =
        new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

    createSchemaWithIndex(gds);

    populateGraphDataWithLabel(gds);

    searchWithIndex(gds, movieLabel, PropEnum.NAME.name(), name);
    searchWithIndex(gds, userLabel, PropEnum.NAME.name(), name);

  }

  static void searchWithIndex(GraphDatabaseService gds, Label label, String indexKey,
      String indexValue) {
    try (Transaction tx = gds.beginTx();) {

      ResourceIterable<Node> result = gds.findNodesByLabelAndProperty(label, indexKey, indexValue);
      logger.info(NEWLINE + "result's size=" + IteratorUtil.count(result));

      logger.info(NEWLINE + "movie[" + movie.getId() + "], user[" + user.getId()
          + "], and result's id=" + result.iterator().next().getId());

      tx.success();
    } catch (Exception e) {
      logger.error("Strange things happend when search with indexes, refer", e);
    }
  }

  static void populateGraphDataWithLabel(GraphDatabaseService gds) {
    try (Transaction tx = gds.beginTx();) {

      /**
       * note {@link GraphDatabaseService#createNode(Label... labels)}<br/>
       * so we can create a node with multiple labels, this example is omitted
       */
      movie = gds.createNode(movieLabel);
      movie.setProperty(PropEnum.NAME.name(), name);

      user = gds.createNode(userLabel);
      user.setProperty(PropEnum.NAME.name(), name);

      tx.success();
    } catch (Exception e) {
      logger.error("Strange things happend when populateing data with labels, refer", e);
    }
  }

  static void createSchemaWithIndex(GraphDatabaseService gds) {

    // create schema
    try (Transaction tx = gds.beginTx();) {
      // define indexes
      gds.schema().indexFor(movieLabel).on(PropEnum.NAME.name()).create();
      gds.schema().indexFor(userLabel).on(PropEnum.NAME.name()).create();

      tx.success();
    } catch (Exception e) {
      logger.error("Strange things happend when creating schema, refer", e);
    }
  }

  private static enum LabelEnum {
    MOVIE, USER
  }

  private static enum PropEnum {
    NAME
  }
}
