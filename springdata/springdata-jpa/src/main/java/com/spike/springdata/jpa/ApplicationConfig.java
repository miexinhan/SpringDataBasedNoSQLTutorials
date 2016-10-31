package com.spike.springdata.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.CollectionUtils;

import com.spike.springdata.jpa.support.DataSourceIdentifier;
import com.spike.springdata.jpa.support.DataSourceIdentifierResolver;
import com.spike.springdata.jpa.support.MultipleTargetRoutingDataSource;
import com.spike.springdata.jpa.support.jpa.CustomedRepositoryFactoryBean;

@Configuration
@ComponentScan
@EnableJpaRepositories(basePackages = { "com.spike.springdata.jpa.repository" },
    repositoryFactoryBeanClass = CustomedRepositoryFactoryBean.class)
@EnableTransactionManagement
@PropertySource(value = "classpath:application-development.properties")
public class ApplicationConfig {

  private static final Logger LOG = Logger.getLogger(ApplicationConfig.class);

  @Autowired
  private Environment env;

  // @Bean
  // public DataSource dataSource() {
  // DriverManagerDataSource driverManagerDataSource = new
  // DriverManagerDataSource();
  //
  // driverManagerDataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
  // driverManagerDataSource.setUrl(env.getProperty("spring.datasource.url"));
  // driverManagerDataSource.setUsername(env.getProperty("spring.datasource.username"));
  // driverManagerDataSource.setPassword(env.getProperty("spring.datasource.password"));
  //
  // return driverManagerDataSource;
  // }

  @Bean
  public DataSource dataSource() {
    MultipleTargetRoutingDataSource result = new MultipleTargetRoutingDataSource();

    DataSourceIdentifierResolver resolver = new DataSourceIdentifierResolver();

    List<DataSourceIdentifier> dataSourceIdentifiers = resolver.resolve("application-development");

    Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
    if (!CollectionUtils.isEmpty(dataSourceIdentifiers)) {
      for (DataSourceIdentifier dataSourceIdentifier : dataSourceIdentifiers) {
        targetDataSources.put(dataSourceIdentifier.getIdentifier(),
          this.targetDataSource(dataSourceIdentifier));

        // record available data source target in global context
        MultipleTargetRoutingDataSource.addAvaiableTarget(dataSourceIdentifier.getIdentifier());
      }

      result.setTargetDataSources(targetDataSources);

      // set the first one as the default
      String deaultDataSourceIdentifier = dataSourceIdentifiers.get(0).getIdentifier();
      LOG.info("ROUTE TO " + deaultDataSourceIdentifier);
      result.setDefaultTargetDataSource(targetDataSources.get(deaultDataSourceIdentifier));

      // set default data source target in global context
      MultipleTargetRoutingDataSource.setTarget(deaultDataSourceIdentifier);

      result.afterPropertiesSet();
    }

    return result;
  }

  /**
   * construct a data source base on an java bean, <br>
   * which is wrapped of property files
   * @param dataSourceIdentifier
   * @return
   */
  private DataSource targetDataSource(DataSourceIdentifier dataSourceIdentifier) {
    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();

    driverManagerDataSource.setDriverClassName(dataSourceIdentifier.getDriverClassName());
    driverManagerDataSource.setUrl(dataSourceIdentifier.getUrl());
    driverManagerDataSource.setUsername(dataSourceIdentifier.getUsername());
    driverManagerDataSource.setPassword(dataSourceIdentifier.getPassword());

    return driverManagerDataSource;
  }

  @Bean
  public EntityManagerFactory entityManagerFactory() {
    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);
    vendorAdapter.setShowSql(true);
    vendorAdapter.setDatabase(Database.MYSQL);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.spike.springdata.jpa.domain");

    factory.setDataSource(this.dataSource());// set the data source
    // factory.setJtaDataSource(this.dataSource());

    Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
    jpaProperties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
    jpaProperties.put("hibernate.ejb.naming_strategy",
      env.getRequiredProperty("hibernate.ejb.naming_strategy"));
    jpaProperties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
    jpaProperties.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
    factory.setJpaProperties(jpaProperties);

    factory.afterPropertiesSet();

    return factory.getObject();
  }

  @Bean
  public JpaTransactionManager transactionManager() {
    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(this.entityManagerFactory());

    return txManager;
  }

}
