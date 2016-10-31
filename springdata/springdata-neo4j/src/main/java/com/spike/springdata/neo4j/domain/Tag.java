package com.spike.springdata.neo4j.domain;

import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.spike.springdata.neo4j.anno.SpringDataBook;

/**
 * 实体：标签
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 9:13:42 PM
 */
@SpringDataBook(chapter = { "7" })
@NodeEntity
public class Tag extends AbstractEntity {

  private String name;

  /**
   * 运行时判断属性
   */
  @GraphProperty
  Object value;

  public Tag() {
  }

  public Tag(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }
}
