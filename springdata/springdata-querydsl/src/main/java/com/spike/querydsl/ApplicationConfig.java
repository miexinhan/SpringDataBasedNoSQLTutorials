package com.spike.querydsl;

import java.util.Properties;

import javax.inject.Provider;
import javax.persistence.EntityManager;
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

import com.mysema.query.jpa.impl.JPAQueryFactory;

@Configuration
@ComponentScan
@EnableJpaRepositories(basePackages = { "com.spike.querydsl.repository" })
@EnableTransactionManagement
@PropertySource(value = "classpath:datasource.properties")
public class ApplicationConfig {

	private static final Logger LOG = Logger.getLogger(ApplicationConfig.class);

	@Autowired private Environment env;

	@Bean
	public DataSource dataSource() {
		LOG.info("实例化DataSource...");

		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();

		driverManagerDataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		driverManagerDataSource.setUrl(env.getProperty("spring.datasource.url"));
		driverManagerDataSource.setUsername(env.getProperty("spring.datasource.username"));
		driverManagerDataSource.setPassword(env.getProperty("spring.datasource.password"));

		return driverManagerDataSource;
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		LOG.info("实例化EntityManagerFactory...");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);
		vendorAdapter.setDatabase(Database.MYSQL);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.spike.querydsl.domain");

		factory.setDataSource(this.dataSource());// set the data source

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
		jpaProperties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
		jpaProperties.put("hibernate.ejb.naming_strategy", env.getRequiredProperty("hibernate.ejb.naming_strategy"));
		jpaProperties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));
		jpaProperties.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));
		factory.setJpaProperties(jpaProperties);

		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public JpaTransactionManager transactionManager() {
		LOG.info("实例化JpaTransactionManager...");

		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(this.entityManagerFactory());

		return txManager;
	}

	/** Querydsl的查询工厂 */
	@Bean
	public JPAQueryFactory jpaQueryFactory() {
		Provider<EntityManager> provider = new Provider<EntityManager>() {
			@Override
			public EntityManager get() {
				return entityManagerFactory().createEntityManager();
			}
		};
		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(provider);
		return jpaQueryFactory;
	}

}
