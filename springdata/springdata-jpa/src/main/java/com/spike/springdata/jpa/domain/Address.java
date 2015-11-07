package com.spike.springdata.jpa.domain;

import javax.persistence.Entity;

/**
 * 地址
 * 
 * @author zhoujiagen
 *
 */
@Entity
@SuppressWarnings("serial")
public class Address extends AbstractJpaEntity {
	private String street;
	private String city;
	private String country;

	public Address() {
	}

	public Address(String street, String city, String country) {
		this.street = street;
		this.city = city;
		this.country = country;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
