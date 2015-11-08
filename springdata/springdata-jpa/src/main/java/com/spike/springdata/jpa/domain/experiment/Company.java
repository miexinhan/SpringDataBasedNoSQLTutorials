package com.spike.springdata.jpa.domain.experiment;

import javax.persistence.Embedded;
import javax.persistence.Entity;

import com.spike.springdata.jpa.domain.AbstractJpaEntity;

@Entity
@SuppressWarnings("serial")
public class Company extends AbstractJpaEntity {
	private String name;

	/**
	 * use embedded object
	 */
	@Embedded
	private Address address;

	public Company() {
	}

	public Company(String name, Address address) {
		this.name = name;
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

}
