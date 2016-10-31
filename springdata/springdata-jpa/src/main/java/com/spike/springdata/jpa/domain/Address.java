package com.spike.springdata.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

import com.spike.springdata.jpa.support.jpa.EntityEventListener;

/**
 * 地址
 * @author zhoujiagen
 */
@Entity
@EntityListeners(EntityEventListener.class)
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

  @Override
  public String toString() {
    return "Address [street=" + street + ", city=" + city + ", country=" + country + "]";
  }

}
