package com.spike.springdata.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.spike.springdata.jpa.domain.Customer;

@Repository
public interface CustomerRepository //
    extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

}
