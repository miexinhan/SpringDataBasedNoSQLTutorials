package com.spike.springdata.neo4j.nativeAPI.traversal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpander;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.Neo4jAppUtils;
import com.spike.springdata.neo4j.anno.Neo4jInActionBook;

/**
 * A demonstration of manage uniquess in the traversal
 * @author zhoujiagen<br/>
 *         Aug 29, 2015 7:01:54 PM
 */
@Neo4jInActionBook(chapter = { "8.3" })
public class TraversalWithUniqueness {
  private static final Logger logger = Logger.getLogger(TraversalWithUniqueness.class);

  private static List<Node> userNodes = new ArrayList<Node>();

  public static void main(String[] args) {
    Neo4jAppUtils.clean(Neo4jAppDevConfig.Embedded_DB_DIR);

    // create graph database service
    GraphDatabaseService gds =
        new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

    populateGraphData(gds);

    /**
     * Question: who is directly connected Jane(0) and Ben(4)
     */

    // 1: one `John`
    // traversal(gds, userNodes.get(0), userNodes.get(4),
    // Uniqueness.NODE_GLOBAL);

    // 2: two `John`s
    traversal(gds, userNodes.get(0), userNodes.get(4), Uniqueness.NODE_PATH);

    // 3: others omitted
  }

  /**
   * @param gds
   * @param node
   * @see Uniqueness#NODE_GLOBAL
   * @see Uniqueness#NODE_PATH
   */
  private static void traversal(GraphDatabaseService gds, final Node startNode, final Node endNode,
      Uniqueness uniqueness) {

    final PathExpander<Object> pathFinder = new PathExpander<Object>() {

      @Override
      public Iterable<Relationship> expand(Path path, BranchState<Object> state) {
        Node endNode = path.endNode();
        return endNode.getRelationships(RelTypeEnum.KNOWNS);
      }

      @Override
      public PathExpander<Object> reverse() {
        return this;
      }
    };

    try (Transaction tx = gds.beginTx();) {
      TraversalDescription traversalDescription =
          gds.traversalDescription().relationships(RelTypeEnum.KNOWNS).evaluator(new Evaluator() {
            @Override
            public Evaluation evaluate(Path path) {
              Node currentNode = path.endNode();

              // find the target
              if (currentNode.getId() == endNode.getId()) {
                return Evaluation.EXCLUDE_AND_PRUNE;
              }

              // use pathFinder instead
              // Path singlePath = GraphAlgoFactory.shortestPath(
              // Traversal.expanderForTypes(RelTypeEnum.KNOWNS),
              // 1).findSinglePath(currentNode,
              // endNode);
              Path singlePath =
                  GraphAlgoFactory.shortestPath(pathFinder, 1).findSinglePath(currentNode, endNode);

              if (singlePath != null) {
                // direct links exists
                return Evaluation.INCLUDE_AND_CONTINUE;
              } else {
                return Evaluation.EXCLUDE_AND_CONTINUE;
              }
            }
          })
          // set the traversal uniqueness
              .uniqueness(uniqueness);

      ResourceIterable<Node> nodes = traversalDescription.traverse(startNode).nodes();
      for (Node node : nodes) {
        System.out.println(node.getProperty(PropEnum.NAME.name()));
      }

      tx.success();
    } catch (Exception e) {
      logger.error("Something strange happened when traversal using " + uniqueness + ",  refer", e);
    }

  }

  private static void populateGraphData(GraphDatabaseService gds) {

    try (Transaction tx = gds.beginTx();) {

      // 1 users
      Node jane = gds.createNode();// 0
      jane.setProperty(PropEnum.NAME.name(), "Jane");
      Node john = gds.createNode();
      john.setProperty(PropEnum.NAME.name(), "John");
      Node kate = gds.createNode();
      kate.setProperty(PropEnum.NAME.name(), "Kate");
      Node jack = gds.createNode();
      jack.setProperty(PropEnum.NAME.name(), "Jack");
      Node ben = gds.createNode();// 4
      ben.setProperty(PropEnum.NAME.name(), "Ben");
      Node emma = gds.createNode();
      emma.setProperty(PropEnum.NAME.name(), "Emma");
      userNodes.add(jane);
      userNodes.add(john);
      userNodes.add(kate);
      userNodes.add(emma);
      userNodes.add(ben);
      userNodes.add(jack);

      // 2 relationships
      // how can we create bidirection relationship???
      jane.createRelationshipTo(john, RelTypeEnum.KNOWNS);
      jane.createRelationshipTo(kate, RelTypeEnum.KNOWNS);
      john.createRelationshipTo(jack, RelTypeEnum.KNOWNS);
      john.createRelationshipTo(kate, RelTypeEnum.KNOWNS);
      john.createRelationshipTo(ben, RelTypeEnum.KNOWNS);
      kate.createRelationshipTo(emma, RelTypeEnum.KNOWNS);

      // dual relationships - no need
      // john.createRelationshipTo(jane, RelTypeEnum.KNOWNS);
      // kate.createRelationshipTo(jane, RelTypeEnum.KNOWNS);
      // jack.createRelationshipTo(john, RelTypeEnum.KNOWNS);
      // kate.createRelationshipTo(john, RelTypeEnum.KNOWNS);
      // ben.createRelationshipTo(john, RelTypeEnum.KNOWNS);
      // emma.createRelationshipTo(kate, RelTypeEnum.KNOWNS);

      tx.success();
    } catch (Exception e) {
      logger.error("Something strange happened when populate data,  refer", e);
    }

  }

  /**
   * properties enumeration for ease of access
   */
  private static enum PropEnum {
    NAME
  }

  /**
   * Relationship type definition as enumeration
   */
  private static enum RelTypeEnum implements RelationshipType {
    KNOWNS
  }
}
