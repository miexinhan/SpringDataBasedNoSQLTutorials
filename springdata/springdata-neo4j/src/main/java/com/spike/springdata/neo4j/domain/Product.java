package com.spike.springdata.neo4j.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;
import org.springframework.data.neo4j.fieldaccess.PrefixedDynamicProperties;
import org.springframework.data.neo4j.support.index.IndexType;

import com.spike.springdata.neo4j.anno.SpringDataBook;

/**
 * 实体：产品
 * 
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 9:07:06 PM
 */
@SpringDataBook(chapter = { "7" })
@NodeEntity
public class Product extends AbstractEntity {
	@Indexed(unique = true)
	private String name;

	@Indexed(indexType = IndexType.FULLTEXT, indexName = "search")
	private String description;
	private BigDecimal price;

	@RelatedTo(type = "TAG")
	private Set<Tag> tags = new HashSet<Tag>();

	private DynamicProperties attributes = new PrefixedDynamicProperties("attributes");

	public Product() {
	}

	public Product(String name, String description, BigDecimal price, Set<Tag> tags) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.tags = tags;
	}

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

	public Set<Tag> getTags() {
		return tags;
	}

	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}

	public DynamicProperties getAttributes() {
		return attributes;
	}

	public void setAttributes(DynamicProperties attributes) {
		this.attributes = attributes;
	}

}
