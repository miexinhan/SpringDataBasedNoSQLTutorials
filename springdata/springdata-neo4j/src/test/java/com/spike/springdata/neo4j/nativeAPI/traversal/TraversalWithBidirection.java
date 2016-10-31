package com.spike.springdata.neo4j.nativeAPI.traversal;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.BidirectionalTraversalDescription;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.SideSelectorPolicies;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.Neo4jAppUtils;
import com.spike.springdata.neo4j.anno.Neo4jInActionBook;

/**
 * @author zhoujiagen<br/>
 *         Aug 29, 2015 8:19:14 PM
 */
@Neo4jInActionBook(chapter = { "8.4" })
public class TraversalWithBidirection {
  private static final Logger logger = Logger.getLogger(TraversalWithBidirection.class);

  private static List<Node> userNodes = new ArrayList<Node>();

  public static void main(String[] args) {

    Neo4jAppUtils.clean(Neo4jAppDevConfig.Embedded_DB_DIR);

    // create graph database service
    GraphDatabaseService gds =
        new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

    populateGraphData(gds);

    /**
     * Question: does Jane(0) and Ben(4) can know each other through transitive KNOWS relationship<br/>
     * add an isolated node Me(6)
     */

    // 2 pathes
    traversal(gds, userNodes.get(0), userNodes.get(4));

    // 0 path
    // traversal(gds, userNodes.get(0), userNodes.get(6));
  }

  private static void traversal(GraphDatabaseService gds, Node startNode1, Node startNode2) {
    try (Transaction tx = gds.beginTx();) {

      TraversalDescription startSideDescription =
          gds.traversalDescription().relationships(RelTypeEnum.KNOWNS)
              .uniqueness(Uniqueness.NODE_PATH);

      TraversalDescription endSideDescription =
          gds.traversalDescription().relationships(RelTypeEnum.KNOWNS)
              .uniqueness(Uniqueness.NODE_PATH);

      BidirectionalTraversalDescription biTraversalDescription =
          gds.bidirectionalTraversalDescription().startSide(startSideDescription)
              .endSide(endSideDescription)
              // .startSide(
              // Traversal.description().relationships(RelTypeEnum.KNOWNS).uniqueness(Uniqueness.NODE_PATH))
              // .endSide(Traversal.description().relationships(RelTypeEnum.KNOWNS).uniqueness(Uniqueness.NODE_PATH))
              .collisionEvaluator(new Evaluator() {
                @Override
                public Evaluation evaluate(Path path) {
                  System.out.println("Current Path: " + path);
                  return Evaluation.INCLUDE_AND_CONTINUE;
                }
              }).sideSelector(SideSelectorPolicies.ALTERNATING, 100);

      Traverser traverser = biTraversalDescription.traverse(startNode1, startNode2);
      ResourceIterator<Path> iterator = traverser.iterator();
      while (iterator.hasNext()) {
        System.out.println(iterator.next());
      }

      tx.success();
    } catch (Exception e) {
      logger.error("Something strange happened when traversal bidirection,  refer", e);
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

      Node me = gds.createNode();// new added node
      me.setProperty(PropEnum.NAME.name(), "Me");

      userNodes.add(jane);
      userNodes.add(john);
      userNodes.add(kate);
      userNodes.add(emma);
      userNodes.add(ben);
      userNodes.add(jack);
      userNodes.add(me);

      // 2 relationships
      // how can we create bidirection relationship???
      jane.createRelationshipTo(john, RelTypeEnum.KNOWNS);
      jane.createRelationshipTo(kate, RelTypeEnum.KNOWNS);
      john.createRelationshipTo(jack, RelTypeEnum.KNOWNS);
      john.createRelationshipTo(kate, RelTypeEnum.KNOWNS);
      john.createRelationshipTo(ben, RelTypeEnum.KNOWNS);
      kate.createRelationshipTo(emma, RelTypeEnum.KNOWNS);

      // dual relationships
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
