package com.spike.springdata.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.spike.springdata.elasticsearch.domain.BookDoc;

@Repository
public interface BookDocRepository extends ElasticsearchRepository<BookDoc, Long> {

}
