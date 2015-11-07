package com.spike.springdata.jpa.repository;

import static org.junit.Assert.assertNotNull;

import javax.transaction.Transactional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.spike.springdata.jpa.JpaTestBase;
import com.spike.springdata.jpa.domain.Address;

@Transactional
public class AddressRespositoryTest extends JpaTestBase {

	@Autowired
	private AddressRespository addressRespository;

	@Test(expected = Exception.class)
	public void resources() throws Exception {
		assertNotNull(addressRespository);

		Address address1 = new Address("三虎桥南路", "北京", "中国");
		address1 = addressRespository.save(address1);
		assertNotNull(address1.getId());

		if ("三虎桥南路".equals(address1.getStreet())) {
			throw new Exception("expected");
		}

		Address address2 = new Address("新海村", "南通", "中国");
		address2 = addressRespository.save(address2);
		assertNotNull(address2.getId());
	}

}
