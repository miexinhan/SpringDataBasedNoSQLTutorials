package com.spike.springdata.neo4j.nativeAPI;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.tooling.GlobalGraphOperations;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
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

	public static void main(String[] args) {
		prepare(Neo4jAppDevConfig.Embedded_DB_DIR);

		// create graph database service
		GraphDatabaseService gds = new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

		populateGraphData(gds);

		navigateUsingLables(gds);
	}

	/**
	 * clean
	 * 
	 * @param dbDirPath
	 *            The path to delete
	 */
	static void prepare(String dbDirPath) {
		try {
			FileUtils.deleteRecursively(new File(dbDirPath));
		} catch (IOException e) {
			logger.error("Fail to delete local database directory, refer", e);
			throw new RuntimeException();
		}
	}

	/**
	 * populate graph database data
	 * 
	 * @param gds
	 *            The {@link GraphDatabaseService}
	 */
	static void populateGraphData(final GraphDatabaseService gds) {

		// begin transaction
		try (Transaction tx = gds.beginTx();) {
			// create nodes
			Node user1 = gds.createNode();
			logger.info("create user:" + user1.getId());
			Node user2 = gds.createNode();
			logger.info("create user:" + user2.getId());
			Node user3 = gds.createNode();
			logger.info("create user:" + user3.getId());

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
	}

	/**
	 * navigate {@link Node}s using {@link Label}
	 * 
	 * @param gds
	 */
	static void navigateUsingLables(final GraphDatabaseService gds) {
		try (Transaction tx = gds.beginTx();) {
			// QUERY using labels
			ResourceIterable<Node> users = GlobalGraphOperations.at(gds).getAllNodesWithLabel(NodeTypeEnum.USERS);
			for (Node user : users) {
				System.out.println(user.getProperty(PropEnum.NAME.name()));
			}

			tx.success();
		} catch (Exception e) {
			logger.error("Something strange happened when query using lables, refer", e);
		}
	}

	/**
	 * properties enumeration for ease of access
	 */
	enum PropEnum {
		NAME, YEAR_OF_BIRTH, LOCKED, CARS_OWNED, TYPE, STARS
	}

	/**
	 * node type enumeration for ease of access: {@link Label}
	 */
	enum NodeTypeEnum implements Label {
		USERS, MOVIES
	}

	/**
	 * Relationship type definition as enumeration
	 */
	enum RelTypeEnum implements RelationshipType {
		IS_FRIEND_OF, HAS_SEEN
	}
}
