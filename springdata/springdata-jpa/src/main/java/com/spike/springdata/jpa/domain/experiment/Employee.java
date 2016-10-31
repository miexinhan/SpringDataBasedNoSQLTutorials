package com.spike.springdata.jpa.domain.experiment;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.spike.springdata.jpa.domain.AbstractJpaEntity;

@Entity
@SuppressWarnings("serial")
public class Employee extends AbstractJpaEntity {
  private String name;
  private BigDecimal salary;

  /**
   * bidirection one-to-many association: many side is the owner, and one side is the non-owner,
   * should use mappedBy
   */
  @ManyToOne
  private Department department;

  @OneToOne
  @JoinColumn(name = "PSPACE_ID")
  private ParkingSpace parkingSpace;

  /**
   * bidirection many-to-many association
   */
  @ManyToMany
  @JoinTable(name = "EMP_PROJ",// name of join table
      joinColumns = { @JoinColumn(name = "EMP_ID") }, // owner side
      inverseJoinColumns = { @JoinColumn(name = "PROJ_ID") }// non-owner side
  )
  private List<Project> projects;

  /**
   * unidirection one-to-many association
   */
  @OneToMany
  @JoinTable(name = "EMP_PHONE",// name of join table
      joinColumns = { @JoinColumn(name = "EMP_ID") }, // this side
      inverseJoinColumns = { @JoinColumn(name = "PHONE_ID") }// that side
  )
  private List<Phone> phones;

  /**
   * use embedded object, with customized
   */
  @Embedded
  @AttributeOverrides(value = {//
      @AttributeOverride(name = "state", column = @Column(name = "PROVINCE")),//
          @AttributeOverride(name = "zip", column = @Column(name = "POSTAL_CODE")) //
      })
  private Address address;

  public Employee() {
  }

  public Employee(String name, BigDecimal salary) {
    this.name = name;
    this.salary = salary;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  public Department getDepartment() {
    return department;
  }

  public void setDepartment(Department department) {
    this.department = department;
  }

  public ParkingSpace getParkingSpace() {
    return parkingSpace;
  }

  public void setParkingSpace(ParkingSpace parkingSpace) {
    this.parkingSpace = parkingSpace;
  }

}
