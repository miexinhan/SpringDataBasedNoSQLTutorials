package com.spike.springdata.jpa.domain;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * 订单项
 * 
 * @author zhoujiagen
 *
 */
@Entity
@SuppressWarnings("serial")
public class LineItem extends AbstractJpaEntity {
	@ManyToOne
	private Product product;// 产品

	private int quantity;// 数量

	private BigDecimal price;// 售价

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
