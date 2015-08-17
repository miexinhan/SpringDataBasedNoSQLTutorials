package com.spike.springdata.neo4j.nativeAPI;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.Uniqueness;
import org.neo4j.tooling.GlobalGraphOperations;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.Neo4jAppUtils;
import com.spike.springdata.neo4j.anno.Neo4jInActionBook;

/**
 * Comprehensive demonstration of Neo4j Native API
 * 
 * @author zhoujiagen<br/>
 *         Aug 15, 2015 4:48:11 PM
 */
@Neo4jInActionBook(chapter = { "4" })
public class Neo4jAPICompreheansiveDemonstration {
	private static final Logger logger = Logger.getLogger(Neo4jAPICompreheansiveDemonstration.class);
	
	/**
	 * it's not a good practice!
	 */
	private static final String NEWLINE = System.getProperty("line.separator");

	/**
	 * static filed to behold the id of the created node
	 */
	static Long USER1_ID = null;

	public static void main(String[] args) {
		prepare(Neo4jAppDevConfig.Embedded_DB_DIR);

		// create graph database service
		GraphDatabaseService gds = new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

		populateGraphData(gds);

		navigateUsingLables(gds);

		traverseUsingCoreJavaAPI(gds);

		traverseUsingBuiltinTraverseAPI(gds);

		traverseUsingBuiltinTraverseAPIWithCustomedEvaluator(gds);
	}

	/**
	 * clean
	 * 
	 * @param dbDirPath
	 *            The path to delete
	 */
	static void prepare(String dbDirPath) {
		logger.info(NEWLINE + "prepare start");

		Neo4jAppUtils.clean(dbDirPath);

		logger.info(NEWLINE + "prepare end");
	}

	/**
	 * populate graph database data
	 * 
	 * @param gds
	 *            The {@link GraphDatabaseService}
	 */
	static void populateGraphData(final GraphDatabaseService gds) {
		logger.info(NEWLINE + "populateGraphData start");

		// begin transaction
		try (Transaction tx = gds.beginTx();) {
			// create nodes
			Node user1 = gds.createNode();
			logger.info(NEWLINE + "create user:" + user1.getId());
			USER1_ID = user1.getId();
			Node user2 = gds.createNode();
			logger.info(NEWLINE + "create user:" + user2.getId());
			Node user3 = gds.createNode();
			logger.info(NEWLINE + "create user:" + user3.getId());

			Node movie1 = gds.createNode();
			Node movie2 = gds.createNode();
			Node movie3 = gds.createNode();

			// create relationships
			user1.createRelationshipTo(user2, RelTypeEnum.IS_FRIEND_OF);
			user1.createRelationshipTo(user3, RelTypeEnum.IS_FRIEND_OF);

			// add properties to nodes
			user1.setProperty(PropEnum.NAME.name(), "John Johnson");
			user1.setProperty(PropEnum.YEAR_OF_BIRTH.name(), 1982);
			user1.setProperty(PropEnum.TYPE.name(), NodeTypeEnum.USERS.name());

			user2.setProperty(PropEnum.NAME.name(), "Kate Smith");
			user2.setProperty(PropEnum.LOCKED.name(), true);
			user2.setProperty(PropEnum.TYPE.name(), NodeTypeEnum.USERS.name());

			user3.setProperty(PropEnum.NAME.name(), "Jack Jeffries");
			user3.setProperty(PropEnum.CARS_OWNED.name(), new String[] { "BMW", "Audi" });
			user3.setProperty(PropEnum.TYPE.name(), NodeTypeEnum.USERS.name());

			movie1.setProperty(PropEnum.NAME.name(), "Fargo");
			movie1.setProperty(PropEnum.TYPE.name(), NodeTypeEnum.MOVIES.name());
			movie2.setProperty(PropEnum.NAME.name(), "Alien");
			movie2.setProperty(PropEnum.TYPE.name(), NodeTypeEnum.MOVIES.name());
			movie3.setProperty(PropEnum.NAME.name(), "Heat");
			movie3.setProperty(PropEnum.TYPE.name(), NodeTypeEnum.MOVIES.name());

			// add properties to relationships
			Relationship r_u1_m1_hasSeen = user1.createRelationshipTo(movie1, RelTypeEnum.HAS_SEEN);
			r_u1_m1_hasSeen.setProperty(PropEnum.STARS.name(), 5);
			Relationship r_u2_m3_hasSeen = user2.createRelationshipTo(movie3, RelTypeEnum.HAS_SEEN);
			r_u2_m3_hasSeen.setProperty(PropEnum.STARS.name(), 3);
			Relationship r_u3_m1_hasSeen = user3.createRelationshipTo(movie1, RelTypeEnum.HAS_SEEN);
			r_u3_m1_hasSeen.setProperty(PropEnum.STARS.name(), 4);
			Relationship r_u3_m2_hasSeen = user3.createRelationshipTo(movie2, RelTypeEnum.HAS_SEEN);
			r_u3_m2_hasSeen.setProperty(PropEnum.STARS.name(), 5);

			// add labels, label is different with property `type`
			user1.addLabel(NodeTypeEnum.USERS);
			user2.addLabel(NodeTypeEnum.USERS);
			user3.addLabel(NodeTypeEnum.USERS);
			movie1.addLabel(NodeTypeEnum.MOVIES);
			movie2.addLabel(NodeTypeEnum.MOVIES);
			movie3.addLabel(NodeTypeEnum.MOVIES);

			// commit the transaction
			tx.success();
		} catch (Exception e) {
			logger.error("Something strange happened when populate data,  refer", e);
		}

		logger.info(NEWLINE + "populateGraphData end");
	}

	/**
	 * navigate {@link Node}s using {@link Label}
	 * 
	 * @param gds
	 *            The {@link GraphDatabaseService}
	 */
	static void navigateUsingLables(final GraphDatabaseService gds) {
		logger.info(NEWLINE + "navigateUsingLables start");

		try (Transaction tx = gds.beginTx();) {
			// QUERY using labels
			ResourceIterable<Node> users = GlobalGraphOperations.at(gds).getAllNodesWithLabel(NodeTypeEnum.USERS);
			for (Node user : users) {
				logger.info(NEWLINE + user.getProperty(PropEnum.NAME.name()));
			}

			tx.success();
		} catch (Exception e) {
			logger.error("Something strange happened when query using lables, refer", e);
		}

		logger.info(NEWLINE + "navigateUsingLables end");
	}

	/**
	 * Traverse using Neo4j Core Java API
	 * 
	 * @param gds
	 *            The {@link GraphDatabaseService}
	 */
	static void traverseUsingCoreJavaAPI(final GraphDatabaseService gds) {
		logger.info(NEWLINE + "traverseUsingCoreJavaAPI start");

		try (Transaction tx = gds.beginTx();) {
			// 1 find the starting node
			Node user1 = gds.getNodeById(USER1_ID);
			logger.info(NEWLINE + user1.getProperty(PropEnum.NAME.name()));

			// 2 traverse direct relationship
			// all relationships
			// Iterable<Relationship> allRelathinshipOfUser1 =
			// user1.getRelationships();
			// all a specific relationship - filtering
			Iterable<Relationship> allHasSeenRelathinshipOfUser1 = user1.getRelationships(DynamicRelationshipType
					.withName(RelTypeEnum.HAS_SEEN.name()));
			Set<Node> movieNodeAssociatedWithUser1 = new HashSet<Node>();
			for (Relationship relationship : allHasSeenRelathinshipOfUser1) {
				if (RelTypeEnum.HAS_SEEN.name().equals(relationship.getType().name())) {
					movieNodeAssociatedWithUser1.add(relationship.getEndNode());
				}
			}
			for (Node movie : movieNodeAssociatedWithUser1) {
				logger.info(NEWLINE + movie.getProperty(PropEnum.NAME.name()));
			}

			// 3 traverse second-level relationship
			Set<Node> friendsOfUser1 = new HashSet<Node>();
			for (Relationship relationship : user1.getRelationships(RelTypeEnum.IS_FRIEND_OF)) {
				friendsOfUser1.add(relationship.getOtherNode(user1));
			}
			Set<Node> moviesFriendsOfUser1Like = new HashSet<Node>();
			for (Node friend : friendsOfUser1) {
				for (Relationship relationship : friend.getRelationships(Direction.OUTGOING, RelTypeEnum.HAS_SEEN)) {
					moviesFriendsOfUser1Like.add(relationship.getEndNode());
				}
			}
			for (Node movie : moviesFriendsOfUser1Like) {
				logger.info(NEWLINE + movie.getProperty(PropEnum.NAME.name()));
			}

			tx.success();
		} catch (Exception e) {
			logger.error("Something strange happened when traverse using core Java API, refer", e);

		}

		logger.info(NEWLINE + "traverseUsingCoreJavaAPI end");
	}

	/**
	 * Traverse using Neo4j Built-in Traverse API: {@link TraversalDescription}
	 * 
	 * @param gds
	 *            The {@link GraphDatabaseService}
	 */
	static void traverseUsingBuiltinTraverseAPI(final GraphDatabaseService gds) {
		logger.info(NEWLINE + "traverseUsingBuiltinTraverseAPI start");

		try (Transaction tx = gds.beginTx();) {
			// the start node
			Node user1 = gds.getNodeById(USER1_ID);

			// construct traversal description
			TraversalDescription travesalDesc = gds.traversalDescription().relationships(RelTypeEnum.IS_FRIEND_OF)
					.relationships(RelTypeEnum.HAS_SEEN, Direction.OUTGOING).uniqueness(Uniqueness.NODE_GLOBAL)
					.evaluator(Evaluators.atDepth(2));

			// execute the traverse operation
			Traverser traverser = travesalDesc.traverse(user1);
			Iterable<Node> moviesFriendsOfUser1Like = traverser.nodes();
			for (Node movie : moviesFriendsOfUser1Like) {
				logger.info(NEWLINE + movie.getProperty(PropEnum.NAME.name()));
			}
			tx.success();
		} catch (Exception e) {
			logger.error("Something strange happened when traverse using built-in traverse API, refer", e);

		}
		logger.info(NEWLINE + "traverseUsingBuiltinTraverseAPI end");
	}

	/**
	 * Traverse using Neo4j Built-in Traverse API: {@link TraversalDescription}
	 * and customed {@link Executor}
	 * 
	 * @param gds
	 *            The {@link GraphDatabaseService}
	 */
	static void traverseUsingBuiltinTraverseAPIWithCustomedEvaluator(final GraphDatabaseService gds) {
		logger.info(NEWLINE + "traverseUsingBuiltinTraverseAPIWithCustomedEvaluator start");

		try (Transaction tx = gds.beginTx();) {
			// the start node
			Node user1 = gds.getNodeById(USER1_ID);

			// construct traversal description
			TraversalDescription travesalDesc = gds.traversalDescription().relationships(RelTypeEnum.IS_FRIEND_OF)
					.relationships(RelTypeEnum.HAS_SEEN, Direction.OUTGOING).uniqueness(Uniqueness.NODE_GLOBAL)
					.evaluator(Evaluators.atDepth(2)).evaluator(new CustomedEvaluator(user1));

			// execute the traverse operation
			Traverser traverser = travesalDesc.traverse(user1);
			Iterable<Node> moviesFriendsOfUser1Like = traverser.nodes();
			for (Node movie : moviesFriendsOfUser1Like) {
				logger.info(NEWLINE + movie.getProperty(PropEnum.NAME.name()));
			}
			tx.success();
		} catch (Exception e) {
			logger.error(
					"Something strange happened when traverse using built-in traverse API with customed evaluator, refer",
					e);

		}
		logger.info(NEWLINE + "traverseUsingBuiltinTraverseAPIWithCustomedEvaluator end");

	}

	/**
	 * The customed {@link Evaluator}<br/>
	 * {@link Evaluator} has 2 responsibilities:<br/>
	 * (1) determine whether current visited node should add to the traversal
	 * result<br/>
	 * (2) determine next steps: (a) continue futher down the path, (b) abandon
	 * current path, move to next path if possible
	 * 
	 * @author zhoujiagen<br/>
	 *         Aug 15, 2015 10:20:45 PM
	 */
	static class CustomedEvaluator implements Evaluator {
		private Node userNode;

		public CustomedEvaluator(Node userNode) {
			this.userNode = userNode;
		}

		@Override
		public Evaluation evaluate(Path path) {
			Node currentNode = path.endNode();
			if (!currentNode.hasProperty(PropEnum.TYPE.name())
					|| !NodeTypeEnum.MOVIES.name().equals(currentNode.getProperty(PropEnum.TYPE.name()))) {
				return Evaluation.EXCLUDE_AND_CONTINUE;
			}
			for (Relationship r : currentNode.getRelationships(Direction.INCOMING, RelTypeEnum.HAS_SEEN)) {
				if (userNode.equals(r.getStartNode())) {
					return Evaluation.EXCLUDE_AND_CONTINUE;
				}
			}

			return Evaluation.INCLUDE_AND_CONTINUE;
		}
	}

	/**
	 * properties enumeration for ease of access
	 */
	private static enum PropEnum {
		NAME, YEAR_OF_BIRTH, LOCKED, CARS_OWNED, TYPE, STARS
	}

	/**
	 * node type enumeration for ease of access: {@link Label}
	 */
	private static enum NodeTypeEnum implements Label {
		USERS, MOVIES
	}

	/**
	 * Relationship type definition as enumeration
	 */
	private static enum RelTypeEnum implements RelationshipType {
		IS_FRIEND_OF, HAS_SEEN
	}
}
