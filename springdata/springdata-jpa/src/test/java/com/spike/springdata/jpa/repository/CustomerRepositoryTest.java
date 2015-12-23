package com.spike.springdata.jpa.repository;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.spike.springdata.jpa.JpaTestBase;

public class CustomerRepositoryTest extends JpaTestBase {

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	public void resource() {
		Assert.assertNotNull(customerRepository);
	}

	@Test
	public void saveAndfind() {

	}
}
