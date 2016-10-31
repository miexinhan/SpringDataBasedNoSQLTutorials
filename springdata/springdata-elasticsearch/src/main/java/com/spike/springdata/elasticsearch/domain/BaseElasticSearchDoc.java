package com.spike.springdata.elasticsearch.domain;

import org.springframework.data.annotation.Id;

/**
 * 基础实体，提供ID。
 * @author zhoujiagen
 */
public class BaseElasticSearchDoc {

  @Id
  protected Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

}
