package com.spike.springdata.jpa.domain.experiment;

import javax.persistence.Entity;

import com.spike.springdata.jpa.domain.AbstractJpaEntity;

@Entity
@SuppressWarnings("serial")
public class Phone extends AbstractJpaEntity {

	private String type;
	private String number;

	public Phone() {
	}

	public Phone(String type, String number) {
		this.type = type;
		this.number = number;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

}
