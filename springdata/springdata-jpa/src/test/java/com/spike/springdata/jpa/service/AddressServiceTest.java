package com.spike.springdata.jpa.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.spike.springdata.jpa.JpaTestBase;
import com.spike.springdata.jpa.domain.Address;
import com.spike.springdata.jpa.exception.BussinessException;
import com.spike.springdata.jpa.exception.RuntimeBussinessException;

public class AddressServiceTest extends JpaTestBase {

	@Autowired
	private AddressService addressService;

	@Test
	public void resource() {
		assertNotNull(addressService);
	}

	@Test
	public void getByStreet() {
		List<Address> addresses = addressService.getByStreet("三虎桥南路");

		assertNotNull(addresses);
		assertTrue(addresses.size() > 0);
	}

	@Test(expected = RuntimeBussinessException.class)
	public void txRollback0() throws RuntimeBussinessException {
		Long id = addressService.methodShouldTxRollback0();
		assertNull(id);
	}

	@Test(expected = Exception.class)
	public void txRollback1() throws Exception {
		Long id = addressService.methodShouldTxRollback1();
		assertNull(id);
	}

	@Test(expected = BussinessException.class)
	public void txRollback2() throws BussinessException {
		Long id = addressService.methodShouldTxRollback2();
		assertNull(id);
	}
}
