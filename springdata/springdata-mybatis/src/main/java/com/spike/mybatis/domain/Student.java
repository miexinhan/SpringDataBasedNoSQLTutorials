package com.spike.mybatis.domain;

import java.util.Date;

public class Student {
	private Integer studId;
	private String name;
	private String email;
	private Date dob;

	private PhoneNumber phone;

	public Student() {}

	public Student(Integer studId, String name, String email, Date dob, PhoneNumber phone) {
		this.studId = studId;
		this.name = name;
		this.email = email;
		this.dob = dob;
		this.phone = phone;
	}

	public PhoneNumber getPhone() {
		return phone;
	}

	public void setPhone(PhoneNumber phone) {
		this.phone = phone;
	}

	public Integer getStudId() {
		return studId;
	}

	public void setStudId(Integer studId) {
		this.studId = studId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

}
