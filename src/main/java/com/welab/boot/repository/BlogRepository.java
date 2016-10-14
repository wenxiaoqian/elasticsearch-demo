package com.welab.boot.repository;

import com.welab.boot.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface BlogRepository extends ElasticsearchRepository<Blog, String>{

    Page<Blog> findByTitle(String name, Pageable pageable);

}