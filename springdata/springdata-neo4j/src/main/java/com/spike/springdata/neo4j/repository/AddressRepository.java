package com.spike.springdata.neo4j.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.spike.springdata.neo4j.domain.Address;

public interface AddressRepository extends GraphRepository<Address> {

}
