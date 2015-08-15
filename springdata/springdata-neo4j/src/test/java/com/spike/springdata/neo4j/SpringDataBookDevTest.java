package com.spike.springdata.neo4j;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.spike.springdata.neo4j.anno.SpringDataBook;
import com.spike.springdata.neo4j.domain.Address;
import com.spike.springdata.neo4j.domain.Country;
import com.spike.springdata.neo4j.domain.Customer;
import com.spike.springdata.neo4j.domain.LineItem;
import com.spike.springdata.neo4j.domain.Order;
import com.spike.springdata.neo4j.domain.Product;
import com.spike.springdata.neo4j.domain.Tag;

/**
 * Spring Data Book中案例测试，使用{@link Neo4jTemplate}
 * 
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 9:28:00 PM
 */
@SpringDataBook(chapter = { "7" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Neo4jAppDevConfig.class })
// 不要回滚
@TransactionConfiguration(defaultRollback = false)
public class SpringDataBookDevTest {

	@Autowired
	private Neo4jTemplate neo4jTemplate;

	@BeforeClass
	public static void setUpBeforeClass() throws IOException {
		// 清空嵌入式数据库的本地目录
		FileUtils.deleteRecursively(new File(Neo4jAppDevConfig.Embedded_DB_DIR));
	}

	@Test
	public void resources() {
		assertNotNull(neo4jTemplate);
	}

	@Test
	@Transactional
	public void populateGraph() {
		Customer dave = neo4jTemplate.save(new Customer("Dave", "Matthew", "dave@dmband.com"));
		neo4jTemplate.save(new Customer("Carter", "Beauford", "carter@dmband"));
		neo4jTemplate.save(new Customer("Boyd", "Tinsley", "boyd@dmband.com"));

		Country usa = neo4jTemplate.save(new Country("US", "United States"));
		Address address = neo4jTemplate.save(new Address("27 Broadway", "New York", usa));
		Set<Address> addresses = new HashSet<Address>();
		addresses.add(address);
		dave.setAddresses(addresses);
		dave = neo4jTemplate.save(dave);

		Set<Tag> tags = new HashSet<Tag>();
		Tag tag = new Tag();
		tag.setName("Apple");
		tags.add(tag);
		Product iPad = neo4jTemplate.save(new Product("iPad", "Apple tablet device", BigDecimal.valueOf(499), tags));
		Product mbp = neo4jTemplate.save(new Product("MacBook Pro", "Apple notebook", BigDecimal.valueOf(1299), tags));

		// 需要注意顺序
		Order order = new Order();
		order.setCustomer(dave);
		order = neo4jTemplate.save(order);

		Set<LineItem> lineItems = new HashSet<LineItem>();
		LineItem lineItem = new LineItem();
		lineItem.setProduct(iPad);
		lineItem.setAmount(2);
		lineItem.setOrder(order);
		lineItem = neo4jTemplate.save(lineItem);
		lineItems.add(lineItem);

		lineItem = new LineItem();
		lineItem.setProduct(mbp);
		lineItem.setAmount(1);
		lineItem.setOrder(order);
		lineItem = neo4jTemplate.save(lineItem);
		lineItems.add(lineItem);

		order.setLineItems(lineItems);
		neo4jTemplate.save(order);
	}

	@Test
	public void errors() {
		Customer dave = neo4jTemplate.save(new Customer("Dave", "Matthew", "dave@dmband.com"));

		Order order = new Order();
		order.setCustomer(dave);
		order = neo4jTemplate.save(order);

		Set<Tag> tags = new HashSet<Tag>();
		Tag tag = new Tag();
		tag.setName("Apple");
		tags.add(tag);
		Product iPad = neo4jTemplate.save(new Product("iPad", "Apple tablet device", BigDecimal.valueOf(499), tags));

		LineItem lineItem = new LineItem();
		lineItem.setProduct(iPad);
		lineItem.setAmount(2);
		lineItem.setOrder(order);
		lineItem = neo4jTemplate.save(lineItem);
	}

}
