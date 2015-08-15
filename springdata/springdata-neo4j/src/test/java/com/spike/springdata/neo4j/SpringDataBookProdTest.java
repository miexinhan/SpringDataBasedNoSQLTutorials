package com.spike.springdata.neo4j;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.spike.springdata.neo4j.anno.OnlineResource;
import com.spike.springdata.neo4j.anno.SpringDataBook;
import com.spike.springdata.neo4j.domain.Address;
import com.spike.springdata.neo4j.domain.Country;
import com.spike.springdata.neo4j.domain.Customer;
import com.spike.springdata.neo4j.domain.LineItem;
import com.spike.springdata.neo4j.domain.Order;
import com.spike.springdata.neo4j.domain.Product;
import com.spike.springdata.neo4j.domain.Tag;
import com.spike.springdata.neo4j.repository.AddressRepository;
import com.spike.springdata.neo4j.repository.CountryRepository;
import com.spike.springdata.neo4j.repository.CustomerRepository;
import com.spike.springdata.neo4j.repository.LineItemRepository;
import com.spike.springdata.neo4j.repository.OrderRepository;
import com.spike.springdata.neo4j.repository.ProductRepository;

/**
 * Spring Data Book中案例测试，使用{@link GraphRepository}
 * 
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 9:28:00 PM
 */
@SpringDataBook(chapter = { "7" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Neo4jAppConfig.class })
// 不要回滚
@TransactionConfiguration(defaultRollback = false)
public class SpringDataBookProdTest {
	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	CountryRepository countryRepository;
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	OrderRepository orderRepository;
	@Autowired
	LineItemRepository lineItemRepository;

	@Test
	public void resources() {
		assertNotNull(customerRepository);
	}

	@Test
	@Transactional
	public void populateGraph() {
		Customer dave = customerRepository.save(new Customer("Dave", "Matthew", "dave@dmband.com"));
		customerRepository.save(new Customer("Carter", "Beauford", "carter@dmband"));
		customerRepository.save(new Customer("Boyd", "Tinsley", "boyd@dmband.com"));

		Country usa = countryRepository.save(new Country("US", "United States"));
		Address address = addressRepository.save(new Address("27 Broadway", "New York", usa));
		Set<Address> addresses = new HashSet<Address>();
		addresses.add(address);
		dave.setAddresses(addresses);
		dave = customerRepository.save(dave);

		Set<Tag> tags = new HashSet<Tag>();
		Tag tag = new Tag();
		tag.setName("Apple");
		tags.add(tag);
		Product iPad = productRepository
				.save(new Product("iPad", "Apple tablet device", BigDecimal.valueOf(499), tags));
		Product mbp = productRepository.save(new Product("MacBook Pro", "Apple notebook", BigDecimal.valueOf(1299),
				tags));

		// 需要注意顺序 - 下面在嵌入式测试环境中正常，在REST服务器环境下抛异常
		@OnlineResource(referenceUrls = {
				"https://jira.spring.io/browse/DATAGRAPH-324",
				"http://stackoverflow.com/questions/29631052/neo4j-spring-data-error-adding-element-with-relatedtovia-relationship-to-inde" })
		Order order = new Order();
		order.setCustomer(dave);
		order = orderRepository.save(order);

		Set<LineItem> lineItems = new HashSet<LineItem>();
		LineItem lineItem = new LineItem();
		lineItem.setProduct(iPad);
		lineItem.setAmount(2);
		lineItem.setOrder(order);
		lineItem = lineItemRepository.save(lineItem);
		lineItems.add(lineItem);

		lineItem = new LineItem();
		lineItem.setProduct(mbp);
		lineItem.setAmount(1);
		lineItem.setOrder(order);
		lineItem = lineItemRepository.save(lineItem);
		lineItems.add(lineItem);

		order.setLineItems(lineItems);
		orderRepository.save(order);
	}

}
