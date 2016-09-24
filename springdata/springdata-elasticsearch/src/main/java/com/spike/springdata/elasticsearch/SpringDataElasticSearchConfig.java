package com.spike.springdata.elasticsearch;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.InetAddresses;

/**
 * Spring Data ElasticSearch的配置
 * 
 * 重要：提供自定义的EntityMapper，配置jackson ObjectMapper的日期格式。
 * 
 * @author zhoujiagen
 *
 * @see {@link ElasticsearchTemplate}
 * @see {@link EntityMapper}
 */
@Configuration
@ComponentScan("com.spike.springdata.elasticsearch")
@EnableElasticsearchRepositories(basePackages = { "com.spike.springdata.elasticsearch.repository" })
// @ImportResource({ "springdata-elasticsearch.xml" })
@PropertySource(value = { "springdata-elasticsearch-config.properties" })
public class SpringDataElasticSearchConfig {

	@Value("${esearch.host:127.0.0.1}")
	private String hostname;
	@Value("${esearch.port:9300}")
	private int port;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigIn() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public static final String ELASTICSEARCH_TEMPLATE_BEAN_NAME = "elasticsearchTemplate";

	// /** Node Client */
	// @Bean(name = { ELASTICSEARCH_TEMPLATE_BEAN_NAME })
	// public ElasticsearchOperations elasticsearchTemplate() {
	// return new
	// ElasticsearchTemplate(NodeBuilder.nodeBuilder().local(true).node().client());
	// }

	/** Transport Client */
	@Bean(name = { ELASTICSEARCH_TEMPLATE_BEAN_NAME })
	public ElasticsearchOperations elasticsearchTemplate() {

		ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(this.client(), this.entityMapper());

		return elasticsearchTemplate;
	}

	/** @see {@link DefaultEntityMapper} */
	@Bean
	public EntityMapper entityMapper() {
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

		// 设置jackson的日期格式
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		objectMapper.setDateFormat(df);

		objectMapper.registerModule(new CustomGeoModule());

		EntityMapper entityMapper = new EntityMapper() {
			@Override
			public String mapToString(Object object) throws IOException {
				return objectMapper.writeValueAsString(object);
			}

			@Override
			public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
				return objectMapper.readValue(source, clazz);
			}
		};

		return entityMapper;
	}

	@Bean
	public Client client() {
		TransportClient client = TransportClient.builder().build();
		InetAddress host = InetAddresses.forString(hostname);
		TransportAddress address = new InetSocketTransportAddress(host, port);
		client.addTransportAddress(address);
		return client;
	}
}
