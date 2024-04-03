package com.example.cms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cms.enums.PostType;
import com.example.cms.model.BlogPost;
import com.example.cms.model.Publish;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {

	boolean existsByPublish(Publish publish);
	Optional<BlogPost> findByBlogPostIdAndPostType(int blogPostId,PostType postType);
}
