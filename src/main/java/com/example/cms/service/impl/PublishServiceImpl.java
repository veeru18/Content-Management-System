package com.example.cms.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cms.enums.PostType;
import com.example.cms.exceptions.BlogPostNotFoundException;
import com.example.cms.exceptions.IllegalAccessRequestException;
import com.example.cms.model.Publish;
import com.example.cms.repository.BlogPostRepository;
import com.example.cms.repository.PublishRepository;
import com.example.cms.requestdto.PublishRequest;
import com.example.cms.responsedto.PublishResponse;
import com.example.cms.service.PublishService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service @AllArgsConstructor
public class PublishServiceImpl implements PublishService {

	private PublishRepository publishRepo;
	private BlogPostRepository blogPostRepo;
	
	private ResponseStructure<PublishResponse> respStructure;
	
	public static PublishResponse mapToPublishResponse(Publish publish) {
		if(publish==null) return PublishResponse.builder().build();
		return PublishResponse.builder().publishId(publish.getPublishId())
							.seoTitle(publish.getSeoTitle())
							.seoDescription(publish.getSeoDescription())
							.seoTags(publish.getSeoTags())
							.createdAt(publish.getCreatedAt()).build();
	}
	
	public Publish mapToPublish(PublishRequest preq,Publish publish) {
		publish.setSeoTitle(preq.getSeoTitle());
		publish.setSeoDescription(preq.getSeoDescription());
		publish.setSeoTags(preq.getSeoTags());
		return publish;
	}

	@Override
	public ResponseEntity<ResponseStructure<PublishResponse>> publishPost(int postId, PublishRequest preq) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return blogPostRepo.findById(postId).map(post->{
			if(!post.getCreatedBy().equals(email)&&!post.getBlog().getUser().getEmail().equals(email))
				throw new IllegalAccessRequestException("user entered is neither owner nor creator of this post");
			if(post.getPublish()==null) {
				Publish publish = mapToPublish(preq, new Publish());
				publish.setBlogPost(post);
				Publish uniquePublish = publishRepo.save(publish);
				post.setPostType(PostType.PUBLISHED);	
				post.setPublish(publish);
				blogPostRepo.save(post);
				return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
											.setMessage("blog post draft has been published")
											.setData(mapToPublishResponse(uniquePublish)));
			}
			else {
				post.setPostType(PostType.PUBLISHED);	
				blogPostRepo.save(post);
				return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
						.setMessage("blog post draft has been published")
						.setData(mapToPublishResponse(post.getPublish())));
			}
		}).orElseThrow(()->new BlogPostNotFoundException("can't find the post mentioned by Id"));
	}
	
}
