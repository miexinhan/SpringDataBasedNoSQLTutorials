package com.spike.springdata.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.spike.springdata.jpa.domain.Address;

@Repository
public interface AddressRespository //
		extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {

	List<Address> findByStreetLike(String street);
}
