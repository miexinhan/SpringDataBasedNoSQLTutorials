package com.spike.springdata.neo4j.nativeAPI.traversal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.Neo4jAppUtils;
import com.spike.springdata.neo4j.anno.Neo4jInActionBook;

/**
 * Demonstration of traversal algorithms: <br/>
 * depth-first and breadth-first
 * @author zhoujiagen<br/>
 *         Aug 28, 2015 12:08:38 PM
 */
@Neo4jInActionBook(chapter = { "8.1" })
public class TraversalOrdering {
  private static final Logger logger = Logger.getLogger(TraversalOrdering.class);

  private static Node root;

  public static void main(String[] args) {
    Neo4jAppUtils.clean(Neo4jAppDevConfig.Embedded_DB_DIR);

    // create graph database service
    GraphDatabaseService gds =
        new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

    populateGraphData(gds);

    traversal(gds, root, TraversalAlgorithmEnum.DEPTH_FIRST);

    traversal(gds, root, TraversalAlgorithmEnum.BREADTH_FIRST);
  }

  private static final void traversal(GraphDatabaseService gds, Node rootNode,
      TraversalAlgorithmEnum traversalAlgorithmEnum) {
    try (Transaction tx = gds.beginTx();) {

      TraversalDescription traversalDescription = null;
      if (TraversalAlgorithmEnum.DEPTH_FIRST.equals(traversalAlgorithmEnum)) {
        traversalDescription =
            gds.traversalDescription().relationships(RelTypeEnum.CHILD, Direction.OUTGOING)
                .depthFirst();
        System.out.println(TraversalAlgorithmEnum.DEPTH_FIRST.name());
      } else if (TraversalAlgorithmEnum.BREADTH_FIRST.equals(traversalAlgorithmEnum)) {
        traversalDescription =
            gds.traversalDescription().relationships(RelTypeEnum.CHILD, Direction.OUTGOING)
                .breadthFirst();
        System.out.println(TraversalAlgorithmEnum.BREADTH_FIRST.name());
      } else {
        throw new UnsupportedOperationException("Not implemented yet");
      }

      Traverser traverser = traversalDescription.traverse(rootNode);
      Iterable<Node> nodes = traverser.nodes();
      for (Node node : nodes) {
        System.out.print(node.getProperty(PropEnum.ID.name()) + "\t");
      }
      System.out.println();
      tx.success();
    } catch (Exception e) {
      logger
          .error("Something strange happened when traversal data in depth-first style,  refer", e);
    }
  }

  private static void populateGraphData(GraphDatabaseService gds) {
    try (Transaction tx = gds.beginTx();) {

      // nodes: 1,...,9
      List<Node> nodes = new ArrayList<Node>();
      for (int i = 0; i < 9; i++) {
        Node node = gds.createNode();
        node.setProperty(PropEnum.ID.name(), i + 1);

        nodes.add(node);
      }
      root = nodes.get(0);

      // relationships
      // 1->2,1->3, 1->4
      // 2->5, 2->6
      // 3->7, 3->8
      // 4->9
      root.createRelationshipTo(nodes.get(1), RelTypeEnum.CHILD);
      root.createRelationshipTo(nodes.get(2), RelTypeEnum.CHILD);
      root.createRelationshipTo(nodes.get(3), RelTypeEnum.CHILD);
      nodes.get(1).createRelationshipTo(nodes.get(4), RelTypeEnum.CHILD);
      nodes.get(1).createRelationshipTo(nodes.get(5), RelTypeEnum.CHILD);
      nodes.get(2).createRelationshipTo(nodes.get(6), RelTypeEnum.CHILD);
      nodes.get(2).createRelationshipTo(nodes.get(7), RelTypeEnum.CHILD);
      nodes.get(3).createRelationshipTo(nodes.get(8), RelTypeEnum.CHILD);

      tx.success();
    } catch (Exception e) {
      logger.error("Something strange happened when populate data,  refer", e);
    }
  }

  private static enum TraversalAlgorithmEnum {
    DEPTH_FIRST, BREADTH_FIRST
  }

  /**
   * properties enumeration for ease of access
   */
  private static enum PropEnum {
    ID, NAME
  }

  /**
   * Relationship type definition as enumeration
   */
  private static enum RelTypeEnum implements RelationshipType {
    CHILD
  }
}
