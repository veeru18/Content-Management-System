package com.example.cms.service;

import org.springframework.http.ResponseEntity;

import com.example.cms.requestdto.BlogPostRequest;
import com.example.cms.responsedto.BlogPostResponse;
import com.example.cms.utility.ResponseStructure;

public interface BlogPostService {

	ResponseEntity<ResponseStructure<BlogPostResponse>> saveBlogPost(int blogId, BlogPostRequest breq);
	ResponseEntity<ResponseStructure<BlogPostResponse>> updateBlogPost(int blogPostId, BlogPostRequest breq);
}
