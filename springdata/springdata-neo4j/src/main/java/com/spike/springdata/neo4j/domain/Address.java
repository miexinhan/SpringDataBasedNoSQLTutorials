package com.spike.springdata.neo4j.domain;

import org.springframework.data.neo4j.annotation.NodeEntity;

import com.spike.springdata.neo4j.anno.SpringDataBook;

/**
 * 实体：地址
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 8:58:58 PM
 */
@SpringDataBook(chapter = { "7" })
@NodeEntity
public class Address extends AbstractEntity {
  private String street;
  private String city;
  private Country country;

  public Address() {
  }

  /**
   * 地址
   * @param street 街道
   * @param city 城市
   * @param country 国家
   */
  public Address(String street, String city, Country country) {
    this.street = street;
    this.city = city;
    this.country = country;
  }

  public String getStreet() {
    return street;
  }

  public String getCity() {
    return city;
  }

  public Country getCountry() {
    return country;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

}
