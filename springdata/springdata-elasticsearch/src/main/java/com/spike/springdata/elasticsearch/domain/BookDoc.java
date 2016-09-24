package com.spike.springdata.elasticsearch.domain;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * <pre>
 * 书实体
 * 
 * REF: https://github.com/SpringDataElasticsearchDevs/spring-data-elasticsearch-sample-application
 * </pre>
 * 
 * @author zhoujiagen
 * 
 * @see {@link Field}
 * @see {@link DateFormat}
 */
@Document(indexName = "book_index", type = "book")
public class BookDoc extends BaseElasticSearchDoc {
	/** 标题 */
	@Field(type=FieldType.String)
	private String title;
	/** 内容 */
	@Field(type=FieldType.String)
	private String content;
	/** 价格 */
	@Field(type=FieldType.Double)
	private Double price;
	/** 发布日期 */
	@Field(type = FieldType.Date, index = FieldIndex.not_analyzed, store = true, format = DateFormat.basic_date, pattern = "yyyyMMdd")
	private Date publishDate;

	public BookDoc() {}

	public BookDoc(Long id, String title, String content, Double price, Date publishDate) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.price = price;
		this.publishDate = publishDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	@Override
	public String toString() {
		return "BookDoc [title=" + title + ", content=" + content + ", price=" + price + ", publishDate=" + publishDate
				+ "]";
	}

}
