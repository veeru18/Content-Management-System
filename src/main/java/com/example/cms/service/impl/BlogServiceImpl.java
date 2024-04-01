package com.example.cms.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.cms.exceptions.TopicNotSpecifiedException;
import com.example.cms.exceptions.BlogNotFoundByIdException;
import com.example.cms.exceptions.TitleNotAvailableException;
import com.example.cms.exceptions.UserNotFoundByIdException;
import com.example.cms.model.Blog;
import com.example.cms.model.ContributionPanel;
import com.example.cms.repository.BlogRepository;
import com.example.cms.repository.ContributionPanelRepository;
import com.example.cms.repository.UserRepository;
import com.example.cms.requestdto.BlogRequest;
import com.example.cms.responsedto.BlogResponse;
import com.example.cms.service.BlogService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlogServiceImpl implements BlogService{

	private BlogRepository blogRepo;
	
	private UserRepository userRepo;
	
	private ContributionPanelRepository panelRepo;
	
	ResponseStructure<BlogResponse> responseStructure;
	
	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> createBlog(int userId, BlogRequest blogreq) {
		return userRepo.findById(userId).map(user -> {
			if(blogRepo.existsByTitle(blogreq.getTitle())) throw new TitleNotAvailableException("Failed to create blog");
			if(blogreq.getTopics().length<1) throw new TopicNotSpecifiedException("Failed to create blog");
			Blog blog=mapToBlog(blogreq, new Blog());
			blog.setUser(user);
			ContributionPanel panel = panelRepo.save(new ContributionPanel());
			blog.setContributionPanel(panel);
			Blog uniqueBlog = blogRepo.save(blog);	
			return ResponseEntity.ok(responseStructure
										.setStatusCode(HttpStatus.OK.value())
										.setMessage("Blog saved successfully")
										.setData(mapToBlogResponse(uniqueBlog)));
		}).orElseThrow(() -> new UserNotFoundByIdException("Failed to create blog"));
	}

	public Blog mapToBlog(BlogRequest blogreq,Blog blog) {
		blog.setTitle(blogreq.getTitle());
		blog.setTopics(blogreq.getTopics());
		blog.setSummary(blogreq.getAbout());
		return blog;
	}
	
	public BlogResponse mapToBlogResponse(Blog blog) {
		return BlogResponse.builder().blogId(blog.getBlogId())
				.createdAt(blog.getCreatedAt()).lastModifiedAt(blog.getLastModifiedAt())
				.title(blog.getTitle()).summary(blog.getSummary()).user(blog.getUser())
				.topics(blog.getTopics()).build();
	}

	@Override
	public ResponseEntity<Boolean> checkBlogTitleAvailability(String title) {
		boolean existsByTitle=blogRepo.existsByTitle(title);
		return ResponseEntity.ok(existsByTitle);
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> findByBlogId(int blogId) {
		return blogRepo.findById(blogId).map(blog->{
			return ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value())
											.setMessage("blog found succesfully")
											.setData(mapToBlogResponse(blog)));
		}).orElseThrow(()->new BlogNotFoundByIdException("failed to fetch the blog"));
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogResponse>> updateByBlogId(int blogId, BlogRequest blogreq) {
		return blogRepo.findById(blogId).map(blog->{
			if(blogRepo.existsByTitle(blogreq.getTitle())) throw new TitleNotAvailableException("Failed to create blog");
			if(blogreq.getTopics().length<1) throw new TopicNotSpecifiedException("Failed to create blog");
			
			Blog uniqueUpdatedBlog = blogRepo.save(mapToBlog(blogreq, blog));
			return ResponseEntity.ok(responseStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("blog updated succesfully")
					.setData(mapToBlogResponse(uniqueUpdatedBlog)));
		}).orElseThrow(()->new BlogNotFoundByIdException("failed to fetch the blog"));
	}
}
