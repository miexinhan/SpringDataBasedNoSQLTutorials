package com.spike.springdata.neo4j.nativeAPI;

import java.util.Iterator;
import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.spike.springdata.neo4j.Neo4jAppDevConfig;
import com.spike.springdata.neo4j.anno.Neo4jInActionBook;

/**
 * demonstration of neo4j Cypher language APIs<br/>
 * use data in {@link Neo4jAPICompreheansiveDemonstration} with modification
 * 
 * @author zhoujiagen<br/>
 *         Aug 18, 2015 1:00:43 PM
 */
@Neo4jInActionBook(chapter = { "6" })
public class Neo4jCypherAPIDemonstration {

	private static final String NEWLINE = System.getProperty("line.separator");

	public static void main(String[] args) {

		// create graph database service
		GraphDatabaseService gds = new GraphDatabaseFactory().newEmbeddedDatabase(Neo4jAppDevConfig.Embedded_DB_DIR);

		// create execute engine
		ExecutionEngine engine = new ExecutionEngine(gds);

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
		sb.append("START john=node(1)").append(NEWLINE);
		sb.append("MATCH john -[:IS_FRIEND_OF]-> () - [:HAS_SEEN]-> (movie)").append(NEWLINE);
		sb.append("RETURN movie");
		executeCypherQuery(engine, sb.toString());

		// pattern matching 5
		sb.setLength(0);
		sb.append("START john=node(1)").append(NEWLINE);
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

}
