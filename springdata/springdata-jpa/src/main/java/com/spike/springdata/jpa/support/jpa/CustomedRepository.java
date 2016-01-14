package com.spike.springdata.jpa.support.jpa;

import java.io.Serializable;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Customed repository <br>
 * 
 * Purpose: add common method to all repositories
 * 
 * @author zhoujiagen
 *
 */
@NoRepositoryBean
public interface CustomedRepository<T, ID extends Serializable> extends PagingAndSortingRepository<T, ID> {

	void sharedCustomMethod(ID id);

	/**
	 * show basic repository informations
	 */
	void showRepositoryInfo();
}