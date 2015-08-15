package com.spike.springdata.neo4j.domain;

import org.springframework.data.neo4j.annotation.GraphId;

import com.spike.springdata.neo4j.anno.SpringDataBook;

/**
 * 抽象实体基类<br/>
 * 约定：<br/>
 * (1) 实体首字母大写<br/>
 * (2) 关系全大写
 * 
 * @author zhoujiagen<br/>
 *         Aug 12, 2015 8:52:39 PM
 */
@SpringDataBook(chapter = { "7" })
public abstract class AbstractEntity {

	@GraphId
	private Long id;

	public Long getId() {
		return id;
	}

}
