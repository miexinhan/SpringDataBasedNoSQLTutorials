package com.spike.springdata.hbase.client.basic;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spike.springdata.hbase.domain.WebTable;

public class GetExample extends BaseExample {
	private static final Logger LOG = LoggerFactory.getLogger(GetExample.class);

	public GetExample(String tableName, String... columnFamilyNames) {
		super(tableName, columnFamilyNames);
	}

	public static void main(String[] args) throws IOException {
		GetExample example = new GetExample(WebTable.TABLE_NAME, //
				WebTable.CF_ANCHOR, WebTable.CF_CONTENTS, WebTable.CF_PEOPLE);

		example.doWork();
	}

	@Override
	protected void doSomething() throws IOException {
		Get get = new Get(Bytes.toBytes(AppConstants.ROWKEY_1));
		get.addColumn(Bytes.toBytes(WebTable.CF_ANCHOR), Bytes.toBytes(WebTable.C_ANCHOR_CSSNSI_COM));
		get.setMaxVersions(3);

		Result result = table.get(get);
		LOG.info(result.toString());
	}

}
