package com.spike.springdata.jpa.domain.experiment;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.spike.springdata.jpa.domain.AbstractJpaEntity;

@Entity
@SuppressWarnings("serial")
public class Project extends AbstractJpaEntity {
  private String name;

  /**
   * the non-owner side of many-to-many association (mappedBy)
   */
  @ManyToMany(mappedBy = "projects")
  private List<Employee> employees;

  public Project() {
  }

  public Project(String name, List<Employee> employees) {
    this.name = name;
    this.employees = employees;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(List<Employee> employees) {
    this.employees = employees;
  }

}
