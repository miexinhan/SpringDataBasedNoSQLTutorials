package com.spike.springdata.jpa.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

import com.spike.springdata.jpa.support.DataSourceConstants.PropertyKey;

/**
 * Data Source Identifier Resolver
 * @author zhoujiagen
 */
public final class DataSourceIdentifierResolver {

  private static final Logger LOG = Logger.getLogger(DataSourceIdentifierResolver.class);

  public DataSourceIdentifierResolver() {
  }

  /**
   * resolve property settings to java bean
   * @return
   */
  public List<DataSourceIdentifier> resolve(String propertyFileBasename) {
    List<DataSourceIdentifier> result = new ArrayList<DataSourceIdentifier>();

    ResourceBundle rb = ResourceBundle.getBundle(propertyFileBasename);
    Set<String> keys = rb.keySet();

    Map<String, DataSourceIdentifier> identifierDataSourceIdentifierMap =
        new HashMap<String, DataSourceIdentifier>();
    for (String key : keys) {
      if (key.startsWith(PropertyKey.MULTIPLE_TARGET_PREFIX.getKey())) {
        LOG.debug("got key: " + key);

        String[] analysisResult = this.analysis(key);

        String identifier = analysisResult[0];
        String propertyKey = analysisResult[1];

        LOG.debug("got identifier: " + identifier);
        LOG.debug("got propertyKey: " + propertyKey);

        DataSourceIdentifier dataSourceIdentifier =
            identifierDataSourceIdentifierMap.get(identifier);

        if (dataSourceIdentifier == null) {
          dataSourceIdentifier = new DataSourceIdentifier();
          dataSourceIdentifier.setIdentifier(analysisResult[0]);
        }

        if (PropertyKey.DRIVER_CLASS_NAME.getKey().equals(propertyKey)) {
          dataSourceIdentifier.setDriverClassName(rb.getString(key));
        } else if (PropertyKey.URL.getKey().equals(propertyKey)) {
          dataSourceIdentifier.setUrl(rb.getString(key));
        } else if (PropertyKey.USERNAME.getKey().equals(propertyKey)) {
          dataSourceIdentifier.setUsername(rb.getString(key));
        } else if (PropertyKey.PASSWORD.getKey().equals(propertyKey)) {
          dataSourceIdentifier.setPassword(rb.getString(key));
        }

        identifierDataSourceIdentifierMap.put(identifier, dataSourceIdentifier);
      }
    }

    result.addAll(identifierDataSourceIdentifierMap.values());

    return result;
  }

  /**
   * @param propertyLine
   * @return [identitier, property key]
   */
  private String[] analysis(String propertyKey) {
    String[] result = new String[3];

    String suffix = propertyKey.substring(PropertyKey.MULTIPLE_TARGET_PREFIX.getKey().length());

    int firstSepIndex = suffix.indexOf(PropertyKey.SEPARATOR.getKey());

    if (firstSepIndex != -1) {
      result[0] = suffix.substring(0, firstSepIndex);
      result[1] = suffix.substring(firstSepIndex + 1);
    }

    return result;
  }

}
