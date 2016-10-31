package com.spike.springdata.jpa.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * 购物车
 * @author zhoujiagen
 */
@Entity
@SuppressWarnings("serial")
public class Cart extends AbstractJpaEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "CUSTOMER_ID", insertable = false, updatable = false)
  private Customer customer;

  @Column(name = "CUSTOMER_ID")
  private Long customerId;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "CART_ID")
  private List<LineItem> lineItems = new ArrayList<LineItem>();

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

}
