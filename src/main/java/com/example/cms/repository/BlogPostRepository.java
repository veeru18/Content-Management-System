package com.example.cms.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cms.enums.PostType;
import com.example.cms.model.BlogPost;
import com.example.cms.model.Publish;

public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {

	boolean existsByPublish(Publish publish);
	Optional<BlogPost> findByBlogPostIdAndPostType(int blogPostId,PostType postType);
	
	List<BlogPost> findAllByPublishScheduleDateTimeLessThanEqualAndPostType(LocalDateTime dateTime,PostType postType);
}
