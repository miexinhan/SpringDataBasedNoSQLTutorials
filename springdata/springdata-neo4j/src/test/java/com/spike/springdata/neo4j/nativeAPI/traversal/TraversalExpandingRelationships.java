package com.spike.springdata.neo4j.nativeAPI.traversal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
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
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.OrderedByTypeExpander;
import org.neo4j.kernel.StandardExpander;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.Neo4jAppUtils;
import com.spike.springdata.neo4j.anno.Neo4jInActionBook;

/**
 * A demonstration of expanding relationships when<br/>
 * visit nodes in the traversal
 * @author zhoujiagen<br/>
 *         Aug 29, 2015 5:39:09 PM
 */
@Neo4jInActionBook(chapter = { "8.2" })
public class TraversalExpandingRelationships {
  private static final Logger logger = Logger.getLogger(TraversalExpandingRelationships.class);

  private static List<Node> userNodes = new ArrayList<Node>();
  private static List<Node> movieNodes = new ArrayList<Node>();

  public static void main(String[] args) {
    Neo4jAppUtils.clean(Neo4jAppDevConfig.Embedded_DB_DIR);

    // create graph database service
    GraphDatabaseService gds =
        new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

    populateGraphData(gds);

    /**
     * traversal goal: find movies that John's friend or work mates likes
     */

    // standardExpander(gds, userNodes.get(0));

    // orderingRelationshipForExpand(gds, userNodes.get(0));

    customedExpander(gds, userNodes.get(0));
  }

  static void customedExpander(GraphDatabaseService gds, Node startNode) {
    // set up DepthAwareExpander
    Map<Integer, List<RelationshipType>> mappings = new HashMap<Integer, List<RelationshipType>>();
    mappings.put(0,
      Arrays.asList(new RelationshipType[] { RelTypeEnum.IS_FRIEND_OF, RelTypeEnum.WORK_WITH }));
    mappings.put(1, Arrays.asList(new RelationshipType[] { RelTypeEnum.LIKES }));

    DepthAwareExpander pathExpander = new DepthAwareExpander(mappings);

    try (Transaction tx = gds.beginTx();) {
      TraversalDescription traversalDescription = gds.traversalDescription()
      // path expander
          .expand(pathExpander).evaluator(Evaluators.atDepth(2));

      ResourceIterable<Node> nodes = traversalDescription.traverse(startNode).nodes();
      for (Node node : nodes) {
        System.out.println(node.getProperty(PropEnum.TITLE.name()));
      }
      tx.success();
    } catch (Exception e) {
      logger.error("Something strange happened traversal with DepthAwareExpander,  refer", e);
    }
  }

  static class DepthAwareExpander implements PathExpander<Object> {
    private Map<Integer, List<RelationshipType>> relationshipsToDepthMapping;

    public DepthAwareExpander(Map<Integer, List<RelationshipType>> relationshipsToDepthMapping) {
      this.relationshipsToDepthMapping = relationshipsToDepthMapping;
    }

    @Override
    public Iterable<Relationship> expand(Path path, BranchState<Object> state) {
      int depth = path.length();
      List<RelationshipType> relationshipTypes = relationshipsToDepthMapping.get(depth);

      return path.endNode().getRelationships(relationshipTypes.toArray(new RelationshipType[0]));
    }

    @Override
    public PathExpander<Object> reverse() {
      throw new UnsupportedOperationException();
    }

  }

  /**
   * the customed evaluator
   * @author zhoujiagen<br/>
   *         Aug 29, 2015 6:55:43 PM
   */
  static class MyEvaluator implements Evaluator {
    @Override
    public Evaluation evaluate(Path path) {
      if (path.endNode().hasProperty(PropEnum.TITLE.name())) {
        return Evaluation.INCLUDE_AND_CONTINUE;
      }
      return Evaluation.EXCLUDE_AND_CONTINUE;
    }
  }

  static void orderingRelationshipForExpand(GraphDatabaseService gds, Node startNode) {

    try (Transaction tx = gds.beginTx();) {

      // expand the relationships in a specific order
      PathExpander<?> pathExpander =
          new OrderedByTypeExpander().add(RelTypeEnum.IS_FRIEND_OF).add(RelTypeEnum.WORK_WITH)
              .add(RelTypeEnum.LIKES);

      TraversalDescription traversalDescription = gds.traversalDescription()
      // path expander
          .expand(pathExpander).evaluator(Evaluators.atDepth(2)).evaluator(new MyEvaluator());

      ResourceIterable<Node> nodes = traversalDescription.traverse(startNode).nodes();
      for (Node node : nodes) {
        System.out.println(node.getProperty(PropEnum.TITLE.name()));
      }

      tx.success();
    } catch (Exception e) {
      logger.error("Something strange happened traversal with OrderedByTypeExpander,  refer", e);
    }

  }

  static void standardExpander(GraphDatabaseService gds, Node startNode) {
    try (Transaction tx = gds.beginTx();) {

      PathExpander<?> pathExpander =
          StandardExpander.DEFAULT.add(RelTypeEnum.WORK_WITH).add(RelTypeEnum.IS_FRIEND_OF)
              .add(RelTypeEnum.LIKES);

      TraversalDescription traversalDescription = gds.traversalDescription()
      // path expander
          .expand(pathExpander).evaluator(Evaluators.atDepth(2)).evaluator(new MyEvaluator());

      ResourceIterable<Node> nodes = traversalDescription.traverse(startNode).nodes();
      for (Node node : nodes) {
        System.out.println(node.getProperty(PropEnum.TITLE.name()));
      }

      tx.success();
    } catch (Exception e) {
      logger.error("Something strange happened traversal with StandardExpander,  refer", e);
    }
  }

  private static void populateGraphData(GraphDatabaseService gds) {

    try (Transaction tx = gds.beginTx();) {
      // 1 users
      Node john = gds.createNode();
      john.setProperty(PropEnum.NAME.name(), "John");
      Node kate = gds.createNode();
      kate.setProperty(PropEnum.NAME.name(), "Kate");
      Node emma = gds.createNode();
      emma.setProperty(PropEnum.NAME.name(), "Emma");
      Node alex = gds.createNode();
      alex.setProperty(PropEnum.NAME.name(), "Alex");
      Node jack = gds.createNode();
      jack.setProperty(PropEnum.NAME.name(), "Jack");
      userNodes.add(john);
      userNodes.add(kate);
      userNodes.add(emma);
      userNodes.add(alex);
      userNodes.add(jack);

      // 2 movies
      Node fargo = gds.createNode();
      fargo.setProperty(PropEnum.TITLE.name(), "Fargo");
      Node topGun = gds.createNode();
      topGun.setProperty(PropEnum.TITLE.name(), "Top Gun");
      Node alien = gds.createNode();
      alien.setProperty(PropEnum.TITLE.name(), "Alien");
      Node godfather = gds.createNode();
      godfather.setProperty(PropEnum.TITLE.name(), "Godfather");
      Node greateDictator = gds.createNode();
      greateDictator.setProperty(PropEnum.TITLE.name(), "Great Dictator");
      movieNodes.add(fargo);
      movieNodes.add(topGun);
      movieNodes.add(alien);
      movieNodes.add(godfather);
      movieNodes.add(greateDictator);

      // 3 relationships
      // 3.1 LIKES
      kate.createRelationshipTo(fargo, RelTypeEnum.LIKES);
      john.createRelationshipTo(topGun, RelTypeEnum.LIKES);
      alex.createRelationshipTo(godfather, RelTypeEnum.LIKES);
      jack.createRelationshipTo(godfather, RelTypeEnum.LIKES);
      jack.createRelationshipTo(greateDictator, RelTypeEnum.LIKES);
      emma.createRelationshipTo(alien, RelTypeEnum.LIKES);
      // 3.2 WORK_WITH
      john.createRelationshipTo(emma, RelTypeEnum.WORK_WITH);
      kate.createRelationshipTo(alex, RelTypeEnum.WORK_WITH);
      kate.createRelationshipTo(jack, RelTypeEnum.WORK_WITH);
      emma.createRelationshipTo(jack, RelTypeEnum.WORK_WITH);

      // 3.3 IS_FRIEND_OF
      john.createRelationshipTo(kate, RelTypeEnum.IS_FRIEND_OF);

      tx.success();
    } catch (Exception e) {
      logger.error("Something strange happened when populate data,  refer", e);
    }

  }

  /**
   * properties enumeration for ease of access
   */
  private static enum PropEnum {
    NAME, TITLE
  }

  /**
   * Relationship type definition as enumeration
   */
  private static enum RelTypeEnum implements RelationshipType {
    LIKES, WORK_WITH, IS_FRIEND_OF
  }
}
