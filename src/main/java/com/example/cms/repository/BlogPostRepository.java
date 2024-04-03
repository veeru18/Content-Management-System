package com.example.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cms.model.BlogPost;
import com.example.cms.model.Publish;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {

	boolean existsByPublish(Publish publish);
}
