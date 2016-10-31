package com.spike.springdata.jpa.service;

import java.util.List;

import com.spike.springdata.jpa.domain.Address;
import com.spike.springdata.jpa.exception.BussinessException;
import com.spike.springdata.jpa.exception.RuntimeBussinessException;

public interface AddressService {

  List<Address> getByStreet(String street);

  Long methodShouldTxRollback0() throws RuntimeBussinessException;

  Long methodShouldTxRollback1() throws Exception;

  Long methodShouldTxRollback2() throws BussinessException;

}
