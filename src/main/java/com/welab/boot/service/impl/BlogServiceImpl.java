package com.welab.boot.service.impl;

import com.welab.boot.entity.Blog;
import com.welab.boot.repository.BlogRepository;
import com.welab.boot.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog save(Blog blog) {
        blogRepository.save(blog);
        return blog;
    }

    @Override
    public Blog findOne(String id) {
        return blogRepository.findOne(id);
    }

    @Override
    public Iterable<Blog> findAll() {
        return blogRepository.findAll();
    }

    @Override
    public Page<Blog> findByTitle(String title, PageRequest pageRequest) {
        return blogRepository.findByTitle(title, pageRequest);
    }
}
