package com.spike.springdata.jpa.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 订单
 * @author zhoujiagen
 */
@Entity
// conflict with SQL keywords`order`
@Table(name = "ORDERS")
@SuppressWarnings("serial")
public class Order extends AbstractJpaEntity {
  @Temporal(value = TemporalType.TIMESTAMP)
  private Date date;// 日期

  @ManyToOne
  private Customer customer;

  @ManyToOne
  private Address shippingAddress;

  @ManyToOne
  private Address billingAddress;

  @OneToMany(cascade = { CascadeType.ALL }, orphanRemoval = true)
  @JoinColumn(name = "ORDER_ID")
  private List<LineItem> lineItems = new ArrayList<LineItem>();

  private String status;

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

}
