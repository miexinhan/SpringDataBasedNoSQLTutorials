
# project home
[Spring Data Neo4J](http://projects.spring.io/spring-data-neo4j/)

# tutorial
[Accessing Data with Neo4j](http://spring.io/guides/gs/accessing-data-neo4j/)

# issues
## 1 no Validator
		<!--spring module validation: NO!!!-->
		<dependency>
			<groupId>org.springmodules</groupId>
			<artifactId>spring-modules-validation</artifactId>
			<version>0.8</version>
		</dependency>
	
		<!-- javax validation-api: NO!!!-->
		<dependency>
			<groupId>javax.validation</groupId>
			<artifactId>validation-api</artifactId>
			<version>1.1.0.Final</version>
		</dependency>

		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
			<version>5.1.3.Final</version>
		</dependency>

## 2 nknown persistent entity

Problem:

		java.lang.IllegalStateException: Failed to load ApplicationContext
		....
		Caused by: org.springframework.data.mapping.model.MappingException: Unknown persistent entity com.spike.springdata.neo4j.domain.Person
		
Solution:

	@Configuration
	@EnableNeo4jRepositories
	public class Neo4jAppConfig extends Neo4jConfiguration {
		public Neo4jAppConfig() {
			setBasePackage("com.spike.springdata.neo4j");
		}
		