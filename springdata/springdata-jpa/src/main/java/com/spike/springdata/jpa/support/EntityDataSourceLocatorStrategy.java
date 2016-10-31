package com.spike.springdata.jpa.support;

import java.util.List;
import java.util.Random;

/**
 * entity storage routing strategy
 * @author zhoujiagen
 */
public interface EntityDataSourceLocatorStrategy {

  /**
   * routing method
   * @param entityIdentifier identifier of entity, may be primary keys or unique keys
   * @return
   */
  String getDataSourceIndentifier(String entityIdentifier);

  public static EntityDataSourceLocatorStrategy INSTANCE = DataSourceLocatorStrategy.DEFAULT;

  /**
   * put some default implementations of routing strategy here
   * @author zhoujiagen
   */
  public enum DataSourceLocatorStrategy implements EntityDataSourceLocatorStrategy {
    DEFAULT(StrategyKey.HASHCODE);

    private StrategyKey strategyKey;

    DataSourceLocatorStrategy(StrategyKey strategyKey) {
      this.strategyKey = strategyKey;
    }

    public enum StrategyKey {
      HASHCODE
    }

    @Override
    public String getDataSourceIndentifier(String entityIdentifier) {
      List<String> availableTargets = MultipleTargetRoutingDataSource.availableTargets();
      int size = availableTargets.size();
      if (size == 0) {
        throw new RuntimeException("No avaiable data source identified!!!");
      }

      if (StrategyKey.HASHCODE.equals(this.strategyKey)) {

        int hashCode = entityIdentifier.hashCode();

        return availableTargets.get(hashCode % size);

      } else {

        return availableTargets.get(new Random().nextInt() % size);
      }

    }

  }
}
