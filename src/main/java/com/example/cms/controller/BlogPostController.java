package com.example.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.requestdto.BlogPostRequest;
import com.example.cms.responsedto.BlogPostResponse;
import com.example.cms.service.BlogPostService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class BlogPostController {

	private BlogPostService blogPostService;
	
	@PostMapping("/blogs/{blogId}/blog-posts")
	ResponseEntity<ResponseStructure<BlogPostResponse>> saveBlogPost(@PathVariable int blogId, @RequestBody BlogPostRequest blogPostReq){
		return blogPostService.saveBlogPost(blogId,blogPostReq);
	}
	
	@PutMapping("/blog-posts/{blogPostId}")
	public ResponseEntity<ResponseStructure<BlogPostResponse>> updateBlogPost(@PathVariable int blogPostId,@RequestBody BlogPostRequest blogPostReq) {
		return blogPostService.updateBlogPost(blogPostId, blogPostReq);
	}
}
