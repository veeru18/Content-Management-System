package com.example.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.requestdto.BlogRequest;
import com.example.cms.responsedto.BlogResponse;
import com.example.cms.service.BlogService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class BlogController {

	private BlogService blogService;
	
	@PostMapping("/users/{userId}/blogs")
	public ResponseEntity<ResponseStructure<BlogResponse>> saveBlog(@PathVariable int userId,@RequestBody BlogRequest blogreq) {
		return blogService.saveBlog(userId, blogreq);
	}
	
	@GetMapping("titles/{title}/blogs")
	public ResponseEntity<Boolean> checkBlogTitleAvailability(@PathVariable String title) {
		return blogService.checkBlogTitleAvailability(title);
	}
	
	@GetMapping("/blogs/{blogId}")
	public ResponseEntity<ResponseStructure<BlogResponse>> findByBlogId(@PathVariable int blogId){
		return blogService.findByBlogId(blogId);
	}
	
	@PutMapping("/blogs/{blogId}")
	public ResponseEntity<ResponseStructure<BlogResponse>> updateByBlogId(@PathVariable int blogId,@RequestBody BlogRequest blogreq){
		return blogService.updateByBlogId(blogId,blogreq);
	}
}
