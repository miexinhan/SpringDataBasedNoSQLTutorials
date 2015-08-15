package com.spike.springdata.neo4j.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import com.spike.springdata.neo4j.anno.SpringDataBook;

/**
 * 实体：顾客
 * 
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 8:56:05 PM
 */
@SpringDataBook(chapter = { "7" })
@NodeEntity
public class Customer extends AbstractEntity {
	private String firstName;
	private String lastName;

	@Indexed(unique = true)
	private String emailAddress;

	/**
	 * 定义关系: ADDRESS
	 */
	@RelatedTo(type = "ADDRESS")
	private Set<Address> addresses = new HashSet<Address>();

	public Customer() {
	}

	/**
	 * 顾客
	 * 
	 * @param firstName
	 *            名
	 * @param lastName
	 *            姓
	 * @param emailAddress
	 *            邮箱(唯一)
	 * @param addresses
	 *            地址集合
	 */
	public Customer(String firstName, String lastName, String emailAddress) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailAddress = emailAddress;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setAddresses(Set<Address> addresses) {
		this.addresses = addresses;
	}

}
