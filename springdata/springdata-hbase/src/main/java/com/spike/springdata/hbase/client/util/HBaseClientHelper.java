package com.spike.springdata.hbase.client.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseClientHelper {

	private static final Logger LOG = LoggerFactory.getLogger(HBaseClientHelper.class);

	/**
	 * <pre>
	 * 加载默认配置.
	 * 
	 * 副作用: 生成最终的配置文件, 用于检验.
	 * source元素值表示配置来源文件
	 * </pre>
	 * 
	 * @throws IOException
	 */
	public static Configuration loadDefaultConfiguration() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		// 从类路径加载资源
		conf.addResource("conf/hbase-site.xml");

		String outputPath = System.getProperty("user.dir") + "/check/hbase-conf-final.xml";
		try (OutputStream os = new FileOutputStream(outputPath);) {
			conf.writeXml(os);
		}

		return conf;
	}

	// 获取连接
	public static Connection connection(Configuration config) throws IOException {
		if (config == null)
			return null;
		return ConnectionFactory.createConnection(config);
	}

	// 获取管理客户端
	public static Admin admin(Connection connection) throws IOException {
		if (connection == null)
			return null;
		return connection.getAdmin();
	}

	// 释放资源
	public static void releaseResource(Connection connection, Admin admin, Table table) throws IOException {
		releaseConnection(connection);
		releaseAdmin(admin);
		releaseTable(table);
	}

	// 释放链接
	public static void releaseConnection(Connection connection) throws IOException {
		if (connection == null)
			return;

		LOG.info("释放Connection.");
		connection.close();
	}

	// 释放管理客户端
	public static void releaseAdmin(Admin admin) throws IOException {
		if (admin == null)
			return;

		LOG.info("释放Admin.");
		admin.close();
	}

	// 释放表
	public static void releaseTable(Table table) throws IOException {
		if (table == null)
			return;

		LOG.info("释放Table.");
		table.close();
	}

	// 创建表
	// with namespace???
	//
	// 创建表时需要指定列族
	// Table should have at least one column family.
	// Set hbase.table.sanity.checks to false at conf or table descriptor if you
	// want to bypass sanity checks
	public static void createTable(Admin admin, String tableName, String cfName) throws IOException {
		if (admin == null) {
			LOG.warn("admin is null");
			return;
		}
		if (StringUtils.isBlank(tableName)) {
			LOG.warn("tableName is empty or null.");
			return;
		}

		HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
		HColumnDescriptor columnDescriptor = new HColumnDescriptor(cfName);
		columnDescriptor.setMaxVersions(3);// 默认最大版本
		tableDescriptor.addFamily(columnDescriptor);
		// ADD MORE COLUMN CONFIGURATION...
		createTable(admin, tableDescriptor);
	}

	// 检查表是否存在
	public static boolean checkTableExists(Admin admin, String tableName) throws IOException {
		if (admin == null) {
			LOG.warn("admin is null");
			return false;
		}
		if (StringUtils.isBlank(tableName)) {
			LOG.warn("tableName is empty or null.");
			return false;
		}

		return admin.tableExists(TableName.valueOf(tableName));
	}

	// 创建表, 类内部用
	private static void createTable(Admin admin, HTableDescriptor tableDescriptor) throws IOException {
		if (admin.tableExists(tableDescriptor.getTableName())) {
			admin.disableTable(tableDescriptor.getTableName());
			admin.deleteTable(tableDescriptor.getTableName());
		}
		admin.createTable(tableDescriptor);
	}

	// 失效表
	public static void disableTable(Admin admin, String tableName) throws IOException {
		if (admin == null) {
			LOG.warn("admin is null");
			return;
		}
		if (StringUtils.isBlank(tableName)) {
			LOG.warn("tableName is empty or null.");
			return;
		}
		admin.disableTable(TableName.valueOf(tableName));
	}

	// 激活表
	public static void enableTable(Admin admin, String tableName) throws IOException {
		if (admin == null) {
			LOG.warn("admin is null");
			return;
		}
		if (StringUtils.isBlank(tableName)) {
			LOG.warn("tableName is empty or null.");
			return;
		}
		admin.enableTable(TableName.valueOf(tableName));
	}

	// 删除表
	public static void deleteTable(Admin admin, String tableName) throws IOException {
		if (admin == null) {
			LOG.warn("admin is null");
			return;
		}
		if (StringUtils.isBlank(tableName)) {
			LOG.warn("tableName is empty or null.");
			return;
		}
		admin.deleteTable(TableName.valueOf(tableName));
	}

	// 强制删除表
	public static void forceDeleteTable(Admin admin, String tableName) throws IOException {
		if (admin == null) {
			LOG.warn("admin is null");
			return;
		}
		if (StringUtils.isBlank(tableName)) {
			LOG.warn("tableName is empty or null.");
			return;
		}
		admin.disableTable(TableName.valueOf(tableName));
		admin.deleteTable(TableName.valueOf(tableName));
	}

	// 添加列族, 暂不支持选项设置
	public static void addColumnFamily(Admin admin, String tableName, String cfName) throws IOException {
		if (admin == null) {
			LOG.warn("admin is null");
			return;
		}
		if (StringUtils.isBlank(tableName)) {
			LOG.warn("tableName is empty or null.");
			return;
		}
		if (StringUtils.isBlank(cfName)) {
			LOG.warn("cfName is empty or null");
			return;
		}

		HColumnDescriptor columnDescriptor = new HColumnDescriptor(cfName);
		columnDescriptor.setMaxVersions(3); // 列最大版本数量
		// ADD MORE COLUMN CONFIGURATION...
		// columnDescriptor.setCompactionCompressionType(Algorithm.GZ);
		// columnDescriptor.setMaxVersions(HConstants.ALL_VERSIONS);

		LOG.info("添加列族: Table[" + tableName + "], cfName=[" + cfName + "]");
		admin.addColumn(TableName.valueOf(tableName), columnDescriptor);
	}

	// 检查表中列族是否存在
	public static boolean checkColumnFamilyExists(Admin admin, String tableName, String cfName) throws IOException {
		boolean result = false;
		HTableDescriptor[] tableDescs = admin.getTableDescriptors(Arrays.asList(tableName));

		if (tableDescs != null && tableDescs.length >= 1) {
			HColumnDescriptor[] columnDescs = tableDescs[0].getColumnFamilies();
			if (columnDescs != null && columnDescs.length > 0) {
				for (HColumnDescriptor columnDesc : columnDescs) {
					if (columnDesc.getNameAsString().equals(cfName)) {
						result = true;
					}
				}
			}
		}

		LOG.info("检查表中列族是否存在: Table[" + tableName + "], cfName=[" + cfName + "], result = " + result);

		return result;
	}

	// 删除列族
	public static void deleteColumnFamily(Admin admin, String tableName, String cfName)
			throws UnsupportedEncodingException, IOException {
		if (admin == null) {
			LOG.warn("admin is null");
			return;
		}
		if (StringUtils.isBlank(tableName)) {
			LOG.warn("tableName is empty or null.");
			return;
		}
		if (StringUtils.isBlank(cfName)) {
			LOG.warn("cfName is empty or null");
			return;
		}
		admin.deleteColumn(TableName.valueOf(tableName), cfName.getBytes("UTF-8"));
	}

}
