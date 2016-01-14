package com.spike.springdata.jpa.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.spike.springdata.jpa.domain.Address;
import com.spike.springdata.jpa.support.jpa.CustomedRepository;

//@Repository
//public interface AddressRespository //
//		extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {
//
//	List<Address> findByStreetLike(String street);
//}

@Repository
public interface AddressRespository extends CustomedRepository<Address, Long> {

	List<Address> findByStreetLike(String street);
}
