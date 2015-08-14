package com.spike.springdata.neo4j.repository;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.spike.springdata.neo4j.domain.Country;

public interface CountryRepository extends GraphRepository<Country> {

}
