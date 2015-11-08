package com.spike.springdata.jpa.experiment;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.spike.springdata.jpa.JpaTestBase;
import com.spike.springdata.jpa.domain.experiment.Department;
import com.spike.springdata.jpa.domain.experiment.Employee;
import com.spike.springdata.jpa.repository.DepartmentRepository;
import com.spike.springdata.jpa.repository.EmployeeRepository;

public class JpaExperimentTest extends JpaTestBase {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Test
	public void resources() {
		assertNotNull(applicationContext);
		assertNotNull(departmentRepository);
		assertNotNull(employeeRepository);
	}

	@Test
	public void OneToMany() {
		// 1 prepare
		// non-owner
		Department itDepartment = new Department("IT");
		itDepartment = departmentRepository.save(itDepartment);
		assertNotNull(itDepartment.getId());

		// owner
		Employee alice = new Employee("alice", new BigDecimal(10000));
		alice.setDepartment(itDepartment);
		alice = employeeRepository.save(alice);
		assertNotNull(alice.getId());
		Employee bob = new Employee("bob", new BigDecimal(20000));
		bob.setDepartment(itDepartment);
		bob = employeeRepository.save(bob);
		assertNotNull(bob.getId());

		// 2 retrieval
		Department department = departmentRepository.findOne(itDepartment.getId());
		// eager fetch
		System.err.println(department.getEmployees());

		Employee employee = employeeRepository.findOne(alice.getId());
		System.err.println(employee.getDepartment());
	}
}
