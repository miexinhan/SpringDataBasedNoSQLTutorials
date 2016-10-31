package com.spike.springdata.neo4j.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;

import com.spike.springdata.neo4j.anno.SpringDataBook;

/**
 * 实体：订单
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 9:19:19 PM
 */
@SpringDataBook(chapter = { "7" })
@NodeEntity
public class Order extends AbstractEntity {
  @RelatedTo(type = "ORDERED", direction = Direction.INCOMING)
  private Customer customer;

  @RelatedTo
  private Address billingAddress;

  @RelatedTo
  private Address shippingAddress;

  /**
   * {@link @Fetch}用于贪婪和完整的加载<br/>
   * {@link @RelatedToVia}仅用于关联关系({@link Relationship})
   */
  @Fetch
  @RelatedToVia
  private Set<LineItem> lineItems = new HashSet<LineItem>();

  public Order() {
  }

  public Order(Customer customer, Address billingAddress, Address shippingAddress,
      Set<LineItem> lineItems) {
    this.customer = customer;
    this.billingAddress = billingAddress;
    this.shippingAddress = shippingAddress;
    this.lineItems = lineItems;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public Set<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(Set<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

}
