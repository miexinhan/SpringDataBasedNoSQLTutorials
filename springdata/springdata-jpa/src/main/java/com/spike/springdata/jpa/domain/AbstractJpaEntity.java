package com.spike.springdata.jpa.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
@SuppressWarnings("serial")
public abstract class AbstractJpaEntity implements Serializable {

  /**
   * 使用序列生成表生成id
   */
  @Id
  @TableGenerator(name = "ID_SEQUENCE", //
      table = "ID_SEQUENCE", //
      pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE",//
      pkColumnValue = "ID", //
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ID_SEQUENCE")
  protected Long id;

  @Temporal(TemporalType.TIMESTAMP)
  protected Date createTime = new Date();

  @Temporal(TemporalType.TIMESTAMP)
  @GeneratedValue()
  protected Date modifyTime = new Date();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(Date modifyTime) {
    this.modifyTime = modifyTime;
  }

}
