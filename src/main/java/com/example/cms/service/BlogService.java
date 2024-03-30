package com.example.cms.service;

import org.springframework.http.ResponseEntity;

import com.example.cms.requestdto.BlogRequest;
import com.example.cms.responsedto.BlogResponse;
import com.example.cms.utility.ResponseStructure;

public interface BlogService {

	ResponseEntity<ResponseStructure<BlogResponse>> createBlog(int userId,BlogRequest blog);
	ResponseEntity<Boolean> checkBlogTitleAvailability(String title);
	ResponseEntity<ResponseStructure<BlogResponse>> findByBlogId(int blogId);
	ResponseEntity<ResponseStructure<BlogResponse>> updateByBlogId(int blogId,BlogRequest blog);
}
