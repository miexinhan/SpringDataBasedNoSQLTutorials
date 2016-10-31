package com.spike.springdata.jpa.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spike.springdata.jpa.domain.Address;
import com.spike.springdata.jpa.exception.BussinessException;
import com.spike.springdata.jpa.exception.RuntimeBussinessException;
import com.spike.springdata.jpa.repository.AddressRespository;
import com.spike.springdata.jpa.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

  @Autowired
  private AddressRespository addressRespository;

  @Override
  public List<Address> getByStreet(String street) {
    return addressRespository.findByStreetLike(street);
  }

  /**
   * roll back when throw runtime exceptions
   */
  @Override
  @Transactional
  public Long methodShouldTxRollback0() throws RuntimeBussinessException {

    Address bean = new Address("1", "1", "1");
    bean = addressRespository.save(bean);

    if ("1".equals(bean.getStreet())) {
      throw new RuntimeBussinessException("should rollback");
    }

    return bean.getId();
  }

  /**
   * roll back when throw non-runtime exceptions
   */
  @Override
  @Transactional(rollbackOn = { Exception.class })
  public Long methodShouldTxRollback1() throws Exception {

    Address bean = new Address("1", "1", "1");
    bean = addressRespository.save(bean);

    if ("1".equals(bean.getStreet())) {
      throw new Exception("should rollback");
    }

    return bean.getId();
  }

  /**
   * roll back when throw non-runtime exceptions, the subclass??? YES
   */
  @Override
  @Transactional(rollbackOn = { Exception.class })
  public Long methodShouldTxRollback2() throws BussinessException {

    Address bean = new Address("1", "1", "1");
    bean = addressRespository.save(bean);

    if ("1".equals(bean.getStreet())) {
      throw new BussinessException("should rollback");
    }

    return bean.getId();
  }
}
