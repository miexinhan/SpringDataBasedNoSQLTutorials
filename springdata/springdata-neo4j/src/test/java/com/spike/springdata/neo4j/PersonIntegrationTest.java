package com.spike.springdata.neo4j;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.spike.springdata.neo4j.domain.Person;
import com.spike.springdata.neo4j.repository.PersonRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Neo4jAppConfig.class })
public class PersonIntegrationTest {

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private GraphDatabase graphDatabase;

	/**
	 * 清空数据库的本地目录
	 * 
	 * @throws IOException
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		FileUtils.deleteRecursively(new File(Neo4jAppConfig.Embedded_DB_DIR));
	}

	@Test
	public void resources() {
		assertNotNull(personRepository);
		assertNotNull(graphDatabase);
	}

	@Test
	public void saveInTransaction() {
		Person greg = new Person("Greg");
		Person roy = new Person("Roy");
		Person craig = new Person("Craig");

		System.out.println("Before linking up with Neo4j...");
		for (Person person : new Person[] { greg, roy, craig }) {
			System.out.println(person);
		}

		Transaction tx = graphDatabase.beginTx();
		try {
			personRepository.save(greg);
			personRepository.save(roy);
			personRepository.save(craig);

			greg = personRepository.findByName(greg.getName());
			greg.worksWith(roy);
			greg.worksWith(craig);
			personRepository.save(greg);

			roy = personRepository.findByName(roy.getName());
			roy.worksWith(craig);
			// We already know that roy works with greg
			personRepository.save(roy);

			// We already know craig works with roy and greg
			System.out.println("Lookup each person by name...");
			for (String name : new String[] { greg.getName(), roy.getName(), craig.getName() }) {
				System.out.println(personRepository.findByName(name));
			}

			System.out.println("Looking up who works with Greg...");
			for (Person person : personRepository.findByTeammatesName("Greg")) {
				System.out.println(person.getName() + " works with Greg.");
			}

			tx.success();
		} finally {
			tx.close();
		}
	}

}
