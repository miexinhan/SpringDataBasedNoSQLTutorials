package com.spike.springdata.neo4j.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于在源码中标注来源于Neo4j In Action
 * @author zhoujiagen<br/>
 *         Aug 15, 2015 4:47:46 PM
 */
@Retention(RetentionPolicy.SOURCE)
@Target(value = { ElementType.TYPE, ElementType.METHOD })
public @interface Neo4jInActionBook {
  String[] chapter() default { "" };
}
