package com.spike.springdata.neo4j;

import static com.spike.springdata.neo4j.Neo4jAppConfig.Embedded_DB_DIR;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;

public class CoreNeo4jAPIDemonstration {

	private static final Logger logger = Logger.getLogger(CoreNeo4jAPIDemonstration.class);

	public static void main(String[] args) {
		try {
			FileUtils.deleteRecursively(new File(Embedded_DB_DIR));
		} catch (IOException e) {
			logger.error("remove file failed, refer: ", e);
		}

		// 创建嵌入式数据库
		GraphDatabaseService gdb = new GraphDatabaseFactory().newEmbeddedDatabase(Embedded_DB_DIR);

		try (Transaction tx = gdb.beginTx();) {// 开启事务
			// 创建节点
			Node daveNode = gdb.createNode();
			String emailKey = "email";
			// 设置节点属性
			daveNode.setProperty(emailKey, "dave@dmband.com");

			// 创建索引
			gdb.index().forNodes("Customer").add(daveNode, emailKey, daveNode.getProperty(emailKey));

			// 创建另一个节点
			Node ipadNode = gdb.createNode();
			ipadNode.setProperty("name", "Apple iPad");

			// 创建关系
			Relationship relationship = daveNode.createRelationshipTo(ipadNode, Types.RATED);
			// 设置关系属性
			relationship.setProperty("stars", 5);

			// 提交事务
			tx.success();

			// 查询
			Node queriedNode = gdb.index().forNodes("Customer").get(emailKey, "dave@dmband.com").getSingle();
			for (Relationship rel : queriedNode.getRelationships(Direction.OUTGOING, Types.RATED)) {
				logger.info(rel.getEndNode().toString() + ": " + rel.getProperty("stars") + " stars");
			}

		} catch (Exception e) {
			logger.error("play with neo4j failed, refer: ", e);
		}

	}

	/**
	 * 自定义关系{@link RelationshipType}
	 * 
	 * @author zhoujiagen<br/>
	 *         Aug 12, 2015 8:42:26 PM
	 */
	public enum Types implements RelationshipType {
		RATED("rated");

		private String name;

		Types(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

}
