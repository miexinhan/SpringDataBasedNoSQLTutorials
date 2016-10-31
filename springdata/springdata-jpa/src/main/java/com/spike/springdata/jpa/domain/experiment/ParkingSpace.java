package com.spike.springdata.jpa.domain.experiment;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.spike.springdata.jpa.domain.AbstractJpaEntity;

@Entity
@SuppressWarnings("serial")
public class ParkingSpace extends AbstractJpaEntity {
  private int lot;
  private String location;

  @OneToOne(mappedBy = "parkingSpace")
  private Employee employee;

  public ParkingSpace() {
  }

  public ParkingSpace(int lot, String location) {
    this.lot = lot;
    this.location = location;
  }

  public int getLot() {
    return lot;
  }

  public void setLot(int lot) {
    this.lot = lot;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

}
