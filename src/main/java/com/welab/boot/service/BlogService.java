package com.welab.boot.service;

import com.welab.boot.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface BlogService {

    Blog save(Blog blog);
    Blog findOne(String id);
    Iterable<Blog> findAll();
    Page<Blog> findByTitle(String title, PageRequest pageRequest);

}
