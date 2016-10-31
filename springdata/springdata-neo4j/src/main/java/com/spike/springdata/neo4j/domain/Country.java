package com.spike.springdata.neo4j.domain;

import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

import com.spike.springdata.neo4j.anno.SpringDataBook;

/**
 * 实体：国家
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 8:54:18 PM
 */
@SpringDataBook(chapter = { "7" })
@NodeEntity
public class Country extends AbstractEntity {

  @Indexed(unique = true)
  private String code;

  private String name;

  public Country() {
  }

  /**
   * 国家
   * @param code 代码
   * @param name 名称
   */
  public Country(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public String getName() {
    return name;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public void setName(String name) {
    this.name = name;
  }

}
