package com.example.cms.service.impl;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cms.enums.PostType;
import com.example.cms.exceptions.BlogPostNotFoundException;
import com.example.cms.exceptions.IllegalAccessRequestException;
import com.example.cms.exceptions.ScheduledDateTimeInvalidException;
import com.example.cms.model.Publish;
import com.example.cms.model.Schedule;
import com.example.cms.repository.BlogPostRepository;
import com.example.cms.repository.PublishRepository;
import com.example.cms.repository.ScheduleRepository;
import com.example.cms.requestdto.PublishRequest;
import com.example.cms.requestdto.ScheduleRequest;
import com.example.cms.responsedto.PublishResponse;
import com.example.cms.service.PublishService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service @AllArgsConstructor
public class PublishServiceImpl implements PublishService {

	private PublishRepository publishRepo;
	private ScheduleRepository scheduleRepository;
	private BlogPostRepository blogPostRepo;

	private ResponseStructure<PublishResponse> respStructure;

	public static PublishResponse mapToPublishResponse(Publish publish) {
		if(publish==null) return PublishResponse.builder().build();
		return PublishResponse.builder().publishId(publish.getPublishId())
				.seoTitle(publish.getSeoTitle())
				.seoDescription(publish.getSeoDescription())
				.seoTags(publish.getSeoTags())
				.schedule(publish.getSchedule())
				.createdAt(publish.getCreatedAt()).build();
	}

	public Publish mapToPublish(PublishRequest pubReq,Publish publish) {
		publish.setSeoTitle(pubReq.getSeoTitle());
		publish.setSeoDescription(pubReq.getSeoDescription());
		publish.setSeoTags(pubReq.getSeoTags());
		publish.setSchedule(mapToSchedule(pubReq.getSchedule(), new Schedule()));
		return publish;
	}

	private Schedule mapToSchedule(ScheduleRequest schedule, Schedule scheduler) {
		scheduler.setDateTime(schedule.getDateTime());
		return scheduler;
	}

	@Override
	public ResponseEntity<ResponseStructure<PublishResponse>> publishPost(int postId, PublishRequest pubReq) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return blogPostRepo.findById(postId).map(post->{
			if(!post.getCreatedBy().equals(email)&&!post.getBlog().getUser().getEmail().equals(email))
				throw new IllegalAccessRequestException("user entered is neither owner nor creator of this post");
			Publish publish = null;
			if(post.getPublish()!=null)
				publish=mapToPublish(pubReq, post.getPublish());
			else
				publish = mapToPublish(pubReq, new Publish());
			if(pubReq.getSchedule()!=null) {
				if(!pubReq.getSchedule().getDateTime().isAfter(LocalDateTime.now()))
				{
					
					throw new ScheduledDateTimeInvalidException("Cannot schedule");
				}
				if(publish.getSchedule()==null) {
				Schedule schedule = mapToSchedule(pubReq.getSchedule(), new Schedule());
				scheduleRepository.save(schedule);
				publish.setSchedule(schedule);
				post.setPostType(PostType.SCHEDULED);}
				else {
					Schedule schedule = mapToSchedule(pubReq.getSchedule(), publish.getSchedule());
					scheduleRepository.save(schedule);
					post.setPostType(PostType.SCHEDULED);
				}
			}
			else
			{
				
				post.setPostType(PostType.PUBLISHED);
			}
			
			publish.setBlogPost(post);
			publishRepo.save(publish);
			
			return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("blog post has been published or scheduled as per request")
					.setData(mapToPublishResponse(publish)));
		}).orElseThrow(()->new BlogPostNotFoundException("can't find the post mentioned by Id"));
	}

}
