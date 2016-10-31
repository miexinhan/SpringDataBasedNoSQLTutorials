package com.spike.springdata.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.spike.springdata.jpa.domain.experiment.Employee;

public interface EmployeeRepository //
    extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

}
