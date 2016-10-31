package com.spike.springdata.neo4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.impl.util.FileUtils;

/**
 * utilities for Neo4j Applications
 * @author zhoujiagen<br/>
 *         Aug 15, 2015 11:18:42 PM
 */
public class Neo4jAppUtils {
  private static final Logger logger = Logger.getLogger(Neo4jAppUtils.class);

  /**
   * clean the local embedded database directory
   * @param localDBPath The local file sytem directory
   */
  public static final void clean(String localDBPath) {
    try {
      FileUtils.deleteRecursively(new File(localDBPath));
    } catch (IOException e) {
      logger.error("Fail to delete local database directory[" + localDBPath + "], refer", e);
      throw new RuntimeException();
    }

  }

  public static Node getNodeById(GraphDatabaseService gds, Long nodeId) {
    if (gds == null || nodeId == null) {
      return null;
    }

    Node result = null;

    try (Transaction tx = gds.beginTx();) {
      result = gds.getNodeById(nodeId);

      tx.success();
    } catch (Exception e) {
      logger.error("Strange things happeded when get node by its id, refer", e);
    }

    return result;
  }

  public static List<Node> getNodesByIds(GraphDatabaseService gds, Long... nodeIds) {
    if (gds == null || nodeIds == null) {
      return null;
    }

    List<Node> result = new ArrayList<Node>();

    try (Transaction tx = gds.beginTx();) {
      for (Long nodeId : nodeIds) {
        result.add(gds.getNodeById(nodeId));
      }

      tx.success();
    } catch (Exception e) {
      logger.error("Strange things happeded when get nodes by their ids, refer", e);
    }

    return result;
  }

  public static void createNodeIndex(GraphDatabaseService gds, String indexName, Node node,
      String indexKey, Object indexValue) {

    try (Transaction tx = gds.beginTx();) {
      // obtain a reference to IndexManager
      IndexManager indexManager = gds.index();
      // find or create an named index
      Index<Node> index = indexManager.forNodes(indexName);
      // 3 parameters: the indexed node, the index key, the indexed value
      index.add(node, indexKey, indexValue);

      tx.success();
    } catch (Exception e) {
      logger.error("Strange things happeded when create index, refer", e);
    }

  }

  public static void changeIndexValue(GraphDatabaseService gds, String indexName, String indexKey,
      String sourceIndexValue, String targetIndexValue) {
    try (Transaction tx = gds.beginTx();) {
      Index<Node> index = gds.index().forNodes(indexName);
      IndexHits<Node> hits = index.get(indexKey, sourceIndexValue);
      Node targetNode = hits.getSingle();
      if (targetNode != null) {
        // remove source indexed entry
        index.remove(targetNode, indexKey, sourceIndexValue);
        // create the new indexed entry
        index.add(targetNode, indexKey, targetIndexValue);
      }

    } catch (Exception e) {
      logger.error("Strange things happeded when changing index values, refer", e);
    }

  }

  public static Node searchWithNodeIndex(GraphDatabaseService gds, String indexName,
      String indexKey, String indexValue) {
    logger.info("searchWithIndex(" + indexKey + "," + indexValue + ")");

    Node result = null;

    try (Transaction tx = gds.beginTx();) {
      Index<Node> index = gds.index().forNodes(indexName);

      // search with index
      IndexHits<Node> indexHits = index.get(indexKey, indexValue);
      result = indexHits.getSingle();// single match

      tx.success();
    } catch (Exception e) {
      logger.error("Strange things happeded when search with index, refer", e);
    }

    return result;
  }

  public static List<Node> searchWithNodeIndex(GraphDatabaseService gds, String indexName,
      String indexKey, Object indexValue) {
    List<Node> result = new ArrayList<Node>();

    try (Transaction tx = gds.beginTx();) {
      Index<Node> index = gds.index().forNodes(indexName);

      // search with index
      IndexHits<Node> indexHits = index.get(indexKey, indexValue);
      for (Node node : indexHits) {
        result.add(node);
      }

      tx.success();
    } catch (Exception e) {
      logger.error(
        "Strange things happeded when search with index, and multiple results expected, refer", e);
    }

    return result;
  }

}
