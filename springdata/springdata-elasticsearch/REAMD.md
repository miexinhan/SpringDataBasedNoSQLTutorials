# 0 资源链接

1 [Could not instantiate TestExecutionListener](http://stackoverflow.com/questions/26125024/could-not-instantiate-testexecutionlistener)  
无法启动测试问题。


2 [ElasticSearch in Java (TransportClient): NoNodeAvailableException - None of the configured nodes are available](https://discuss.elastic.co/t/elasticsearch-in-java-transportclient-nonodeavailableexception-none-of-the-configured-nodesare-available/34452)  
节点不可用问题。

3 [Introduction to Spring Data Elasticsearch](http://www.baeldung.com/spring-data-elasticsearch-tutorial)
Spring Data Elasticsearch教程。

# 1 有关日期的问题

实体定义：

	@Document(indexName = "book_index", type = "book")
	public class BookDoc extends BaseElasticSearchDoc {
		...
		/** 发布日期 */
		@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, store = true, format = DateFormat.basic_date, pattern = "yyyyMMdd")
		private Date publishDate;
		...
		
ElasticsearchTemplate的配置：

	// Bean名称默认为elasticsearchTemplate
	@Bean(name = { "elasticsearchTemplate" })
	public ElasticsearchOperations elasticsearchTemplate() {

		ElasticsearchTemplate elasticsearchTemplate = new ElasticsearchTemplate(this.client(), this.entityMapper());

		return elasticsearchTemplate;
	}
	
	@Bean
	public Client client() {
		TransportClient client = TransportClient.builder().build();
		InetAddress host = InetAddresses.forString(hostname);
		TransportAddress address = new InetSocketTransportAddress(host, port);
		client.addTransportAddress(address);
		return client;
	}
	
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