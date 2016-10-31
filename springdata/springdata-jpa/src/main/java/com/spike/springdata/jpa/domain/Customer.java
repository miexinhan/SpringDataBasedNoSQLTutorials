package com.spike.springdata.jpa.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

/**
 * 顾客
 * @author zhoujiagen
 */
@Entity
@SuppressWarnings("serial")
public class Customer extends AbstractJpaEntity {
  private String name;// 姓名

  @Column(unique = true)
  private EmailAddress emailAddress;// 邮箱

  @OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "CUSTOMER_ID")
  private Set<Address> addresses = new HashSet<Address>();// 地址

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<Address> getAddresses() {
    return addresses;
  }

  public void setAddresses(Set<Address> addresses) {
    this.addresses = addresses;
  }

  public EmailAddress getEmailAddress() {
    return emailAddress;
  }

  public void setEmailAddress(EmailAddress emailAddress) {
    this.emailAddress = emailAddress;
  }

}
