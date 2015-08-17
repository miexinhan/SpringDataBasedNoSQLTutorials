package com.spike.springdata.neo4j;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.neo4j.kernel.impl.util.FileUtils;

/**
 * utilities for Neo4j Applications
 * 
 * @author zhoujiagen<br/>
 *         Aug 15, 2015 11:18:42 PM
 */
public class Neo4jAppUtils {
	private static final Logger logger = Logger.getLogger(Neo4jAppUtils.class);

	/**
	 * clean the local embedded database directory
	 * 
	 * @param localDBPath
	 *            The local file sytem directory
	 */
	public static final void clean(String localDBPath) {
		try {
			FileUtils.deleteRecursively(new File(localDBPath));
		} catch (IOException e) {
			logger.error("Fail to delete local database directory[" + localDBPath + "], refer", e);
			throw new RuntimeException();
		}

	}
}
