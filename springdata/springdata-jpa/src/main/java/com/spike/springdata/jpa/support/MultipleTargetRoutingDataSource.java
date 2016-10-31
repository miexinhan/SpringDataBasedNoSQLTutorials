package com.spike.springdata.jpa.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Data Source support multiple data source lookup
 * @author zhoujiagen
 * @see org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
 */
public final class MultipleTargetRoutingDataSource extends AbstractRoutingDataSource {
  private static final Logger LOG = Logger.getLogger(MultipleTargetRoutingDataSource.class);

  private static final ThreadLocal<String> LOOKUPKEY = new ThreadLocal<String>();

  private static final List<String> ALL_DATASOURCE_KEYS = new ArrayList<String>();

  @Override
  protected Object determineCurrentLookupKey() {
    return LOOKUPKEY.get();
  }

  /**
   * @param targetDataSourceIdentifier target data source identifier
   */
  public static void setTarget(String targetDataSourceIdentifier) {
    LOG.info("SET CURRENT DATASOURCE TO " + targetDataSourceIdentifier);

    LOOKUPKEY.set(targetDataSourceIdentifier);
  }

  /**
   * @return current used target data source identifier
   */
  public static String getCurrentTarget() {
    return LOOKUPKEY.get();
  }

  /**
   * get all data source targets in the global context
   * @return
   */
  public static List<String> availableTargets() {
    return ALL_DATASOURCE_KEYS;
  }

  /**
   * add data source target to the global context, <br>
   * only support one-by-one
   * @param target
   */
  public static void addAvaiableTarget(String target) {
    if (!ALL_DATASOURCE_KEYS.contains(target)) {
      ALL_DATASOURCE_KEYS.add(target);
    }

  }
}
