package com.spike.springdata.neo4j.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import com.spike.springdata.neo4j.anno.SpringDataBook;

/**
 * 关系：打分
 * 
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 9:16:50 PM
 */
@SpringDataBook
@RelationshipEntity(type = "RATED")
public class Rating extends AbstractEntity {
	@StartNode
	private Customer customer;

	@EndNode
	private Product product;

	private int stars;
	private String comment;

	public Rating() {
	}

	public Rating(Customer customer, Product product, int stars, String comment) {
		this.customer = customer;
		this.product = product;
		this.stars = stars;
		this.comment = comment;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}
