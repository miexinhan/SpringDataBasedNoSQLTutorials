package com.spike.springdata.neo4j.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.spike.springdata.neo4j.anno.SpringDataBook;

/**
 * 关系：产品项
 * 
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 9:24:34 PM
 */
@SpringDataBook
@RelationshipEntity(type = "ITEMS")
public class LineItem extends AbstractEntity {
	@StartNode
	private Order order;

	@EndNode
	private Product product;
	private int amount;

	public LineItem() {
	}

	public LineItem(Order order, Product product, int amount) {
		this.order = order;
		this.product = product;
		this.amount = amount;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
