package com.spike.springdata.neo4j.nativeAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.Neo4jAppUtils;
import com.spike.springdata.neo4j.anno.Neo4jInActionBook;

/**
 * demonstration of neo4j Cypher language APIs<br/>
 * use data in {@link Neo4jAPICompreheansiveDemonstration} with modification?<br/>
 * NO! Populating your own data!!!
 * 
 * @author zhoujiagen<br/>
 *         Aug 18, 2015 1:00:43 PM
 */
@Neo4jInActionBook(chapter = { "6" })
public class Neo4jCypherAPIDemonstration {

	private static final String NEWLINE = System.getProperty("line.separator");

	public static void main(String[] args) {
		Neo4jAppUtils.clean(Neo4jAppDevConfig.Embedded_DB_DIR);

		// create graph database service
		GraphDatabaseService gds = new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

		// create execute engine
		ExecutionEngine engine = new ExecutionEngine(gds);

		populateGraphData(gds, engine);

		// populate Cypher query
		StringBuilder sb = new StringBuilder();

		// pattern matching 1
		sb.append("START user=node(2) ").append(NEWLINE);
		sb.append("MATCH (user) -[:HAS_SEEN]-> (movie) ").append(NEWLINE);
		sb.append("RETURN movie;");
		executeCypherQuery(engine, sb.toString());

		// pattern matching 2
		sb.setLength(0);
		sb.append("START user1=node(1)").append(NEWLINE);
		sb.append("MATCH (user1) -[:IS_FRIEND_OF]- (user2)").append(NEWLINE);
		sb.append("RETURN user2");
		executeCypherQuery(engine, sb.toString());

		// pattern matching 3
		sb.setLength(0);
		sb.append("START user=node(1)").append(NEWLINE);
		sb.append("MATCH (user) -[r:IS_FRIEND_OF]- ()").append(NEWLINE);
		sb.append("RETURN r");
		executeCypherQuery(engine, sb.toString());

		// pattern matching 4
		sb.setLength(0);
		sb.append("START john=node(0)").append(NEWLINE);
		sb.append("MATCH john -[:IS_FRIEND_OF]-> () - [:HAS_SEEN]-> (movie)").append(NEWLINE);
		sb.append("RETURN movie");
		executeCypherQuery(engine, sb.toString());

		// pattern matching 5
		sb.setLength(0);
		sb.append("START john=node(0)").append(NEWLINE);
		sb.append("MATCH").append(NEWLINE);
		sb.append("	john -[:IS_FRIEND_OF]-> () - [:HAS_SEEN]-> (movie),").append(NEWLINE);
		sb.append("	john - [:HAS_SEEN]-> (movie)").append(NEWLINE);
		sb.append("RETURN movie");
		executeCypherQuery(engine, sb.toString());

		// pattern matching 6
		sb.setLength(0);
		sb.append("START john=node:users(name=\"John Johnson\")").append(NEWLINE);
		sb.append("MATCH john -[:IS_FRIEND_OF]-> (user) - [:HAS_SEEN]-> (movie)").append(NEWLINE);
		sb.append("WHERE NOT john-[:HAS_SEEN]-> (movie)").append(NEWLINE);
		sb.append("RETURN movie.name");
		executeCypherQuery(engine, sb.toString());
	}

	private static void populateGraphData(GraphDatabaseService gds, ExecutionEngine engine) {

		StringBuilder sb = new StringBuilder();

		// read schema and data script
		try (BufferedReader reader = new BufferedReader(new FileReader(new File("cypher/user-and-movie-schema.cypher")));) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (sb.toString() != null) {
			executeCypherQuery(engine, sb.toString());
		}

		// manually create the index
		try (Transaction tx = gds.beginTx()) {
			// 6 nodes: 3 users and 3 movies
			Long[] nodeIds = new Long[] { 0L, 1L, 2L, 3L, 4L, 5L };
			List<Node> nodes = Neo4jAppUtils.getNodesByIds(gds, nodeIds);

			String nodeIndexName = "users";
			for (int i = 0; i < 5; i++) {
				Node node = nodes.get(i);

				if (i < 3) {// user
					Neo4jAppUtils.createNodeIndex(gds, nodeIndexName, node, PropEnum.NAME.name().toLowerCase(),
							node.getProperty(PropEnum.NAME.name().toLowerCase()));
					Neo4jAppUtils.createNodeIndex(gds, nodeIndexName, node, PropEnum.EMAIL.name().toLowerCase(),
							node.getProperty(PropEnum.EMAIL.name().toLowerCase()));
					Neo4jAppUtils.createNodeIndex(gds, nodeIndexName, node, PropEnum.AGE.name().toLowerCase(),
							node.getProperty(PropEnum.AGE.name().toLowerCase()));

				} else {// movie
						// do nothing
				}
			}

			tx.success();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static void executeCypherQuery(ExecutionEngine engine, String query) {
		// execute query
		System.out.println("executing Cypher Query: \n" + query);
		ExecutionResult result = engine.execute(query);

		// parse query result
		System.out.println("Result:");
		List<String> columns = result.columns();
		for (String column : columns) {
			Iterator<Object> columnValues = result.columnAs(column);
			while (columnValues.hasNext()) {
				System.out.println("Value:" + columnValues.next());
			}
		}
		System.out.println();
	}

	private static enum PropEnum {
		NAME, EMAIL, AGE
	}

}
