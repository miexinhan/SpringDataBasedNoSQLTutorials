package com.spike.springdata.jpa.domain.experiment;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.spike.springdata.jpa.domain.AbstractJpaEntity;

@Entity
@SuppressWarnings("serial")
public class Department extends AbstractJpaEntity {
	private String name;

	/**
	 * bidirection association:
	 * 
	 * multiple side is the owner,
	 * 
	 * and one side is the non-owner, should use mappedBy
	 */
	@OneToMany(targetEntity = Employee.class, mappedBy = "department", fetch = FetchType.EAGER)
	private List<Employee> employees;

	public Department() {
	}

	public Department(String name) {
		this.name = name;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
