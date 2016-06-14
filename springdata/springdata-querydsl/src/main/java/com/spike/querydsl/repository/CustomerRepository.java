package com.spike.querydsl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.spike.querydsl.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
