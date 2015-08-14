package com.spike.springdata.neo4j.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spike.springdata.neo4j.domain.Person;

@Repository
public interface PersonRepository extends CrudRepository<Person, String> {

	Person findByName(String name);

	Set<Person> findByTeammatesName(String name);
}
