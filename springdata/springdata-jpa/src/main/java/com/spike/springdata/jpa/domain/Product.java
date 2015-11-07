package com.spike.springdata.jpa.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

/**
 * 产品
 * 
 * @author zhoujiagen
 *
 */
@Entity
@SuppressWarnings("serial")
public class Product extends AbstractJpaEntity {
	private String name;// 名称
	private String description;// 描述

	@ElementCollection
	private Map<String, String> attributes = new HashMap<String, String>();// 属性

	private BigDecimal price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}
