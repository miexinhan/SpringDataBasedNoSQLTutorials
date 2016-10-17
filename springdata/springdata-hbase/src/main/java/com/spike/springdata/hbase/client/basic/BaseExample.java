package com.spike.springdata.hbase.client.basic;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.springdata.hbase.client.util.HBaseClientHelper;

/**
 * 示例支持基类
 * 
 * @author zhoujiagen
 */
public abstract class BaseExample {
	private static final Logger LOG = LoggerFactory.getLogger(BaseExample.class);

	// 应用常量
	public interface AppConstants {
		String ROWKEY_1 = "com.cnn.www";
		String ROWKEY_2 = "com.example.www";
	}

	protected Configuration conf;
	protected Connection connection;
	protected Admin admin;
	protected Table table;

	public BaseExample(String tableName, String... columnFamilyNames) {

		try {
			Configuration conf = HBaseConfiguration.create();
			// 从类路径加载资源
			conf.addResource("conf/hbase-site.xml");

			connection = HBaseClientHelper.connection(conf);
			admin = HBaseClientHelper.admin(connection);
			// 不存在则创建表
			if (HBaseClientHelper.checkTableExists(admin, tableName)) {
				LOG.info("表[" + tableName + "]存在");
				table = connection.getTable(TableName.valueOf(tableName));
			} else {
				if (columnFamilyNames == null || columnFamilyNames.length == 0) {
					throw new IOException("列族为空");
				}

				LOG.info("表[" + tableName + "]不存在, 开始");
				HBaseClientHelper.createTable(admin, tableName, columnFamilyNames[0]);
				table = connection.getTable(TableName.valueOf(tableName));
			}

			// 添加列族
			for (int i = 1, len = columnFamilyNames.length; i < len; i++) {
				if (!HBaseClientHelper.checkColumnFamilyExists(admin, tableName, columnFamilyNames[i])) {
					HBaseClientHelper.addColumnFamily(admin, tableName, columnFamilyNames[i]);
				}
			}

			LOG.info("初始化客户端完成");

		} catch (IOException e) {
			LOG.error("初始化客户端失败", e);
		}
	}

	public Configuration getConf() {
		return conf;
	}

	public Admin getAdmin() {
		return admin;
	}

	public Connection getConnection() {
		return connection;
	}

	public void doWork() throws IOException {

		try {
			doSomething();
		} finally {
			LOG.info("执行清理工作");
			if (table != null)
				table.close();
			if (admin != null)
				admin.close();
			if (connection != null)
				connection.close();
		}

	}

	protected abstract void doSomething() throws IOException;

}
