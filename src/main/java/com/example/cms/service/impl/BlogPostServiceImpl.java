package com.example.cms.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cms.enums.PostType;
import com.example.cms.exceptions.BlogNotFoundByIdException;
import com.example.cms.exceptions.IllegalAccessRequestException;
import com.example.cms.exceptions.PanelNotFoundByIdException;
import com.example.cms.model.Blog;
import com.example.cms.model.BlogPost;
import com.example.cms.repository.BlogPostRepository;
import com.example.cms.repository.BlogRepository;
import com.example.cms.repository.ContributionPanelRepository;
import com.example.cms.requestdto.BlogPostRequest;
import com.example.cms.responsedto.BlogPostResponse;
import com.example.cms.service.BlogPostService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BlogPostServiceImpl implements BlogPostService{

	private BlogPostRepository postsRepo;
	private BlogRepository blogRepo;
	private ContributionPanelRepository panelRepo;

	private ResponseStructure<BlogPostResponse> respStructure;

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> saveBlogPost(int blogId,BlogPostRequest breq) {
		return blogRepo.findById(blogId).map(blog->{
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			BlogPost post=new BlogPost();
			post.setBlog(blog);
			post.setPostType(PostType.DRAFT);
			if(!blog.getUser().getEmail().equals(email)) 
				throw new IllegalAccessRequestException("cannot access the blogpost, since neither he is owner nor he is contributor");
			return panelRepo.findById(blog.getContributionPanel().getPanelId()).map(panel->{
//				List<User> contributors = panel.getContributors(); 
//				int flag=0;
//				for(User user:contributors) {
//					if(user.getEmail()==email) flag=1;
//				}
//				if(flag==0) 
//					throw new IllegalAccessRequestException("cannot access the blogpost, since neither he is owner nor he is contributor");
				if(!panel.getContributors().stream().anyMatch(user->user.getEmail()==email)) 
					throw new IllegalAccessRequestException("cannot access the blogpost, since neither he is owner nor he is contributor");
				
			BlogPost uniqueBlogPost = postsRepo.save(mapToBlogPost(breq, post));
			return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("BlogPost has been successfully drafted..")
					.setData(mapToBlogPostResponse(uniqueBlogPost)));
		}).orElseThrow(()->new BlogNotFoundByIdException("cannot add blogpost to get drafted"));
		}).orElseThrow(()->new PanelNotFoundByIdException("panel Not found"));
	}

	private BlogPost mapToBlogPost(BlogPostRequest breq,BlogPost post) {
		post.setTitle(breq.getTitle());
		post.setSubTitle(breq.getSubTitle());
		post.setSummary(breq.getSummary());
		return post;
	}

	private BlogPostResponse mapToBlogPostResponse(BlogPost post) {
		return BlogPostResponse.builder().blogPostId(post.getBlogPostId())
				.title(post.getTitle()).subTitle(post.getSubTitle())
				.summary(post.getSummary()).postType(post.getPostType())
				.createdAt(post.getCreatedAt()).createdBy(post.getCreatedBy())
				.lastModifiedAt(post.getLastModifiedAt())
				.lastModifiedBy(post.getLastModifiedBy())
				.blog(post.getBlog())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> updateBlogPost(int blogPostId,BlogPostRequest breq) {
		return postsRepo.findById(blogPostId).map(post->{
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			Blog blog=post.getBlog();
			if(!blog.getUser().getEmail().equals(email)) 
				throw new IllegalAccessRequestException("cannot access the blogpost, since neither he is owner nor he is contributor");
			return panelRepo.findById(blog.getContributionPanel().getPanelId()).map(panel->{

				if(!panel.getContributors().stream().anyMatch(user->user.getEmail()==email)) 
					throw new IllegalAccessRequestException("cannot access the blogpost, since neither he is owner nor he is contributor");
				BlogPost uniqueBlogPost = postsRepo.save(mapToBlogPost(breq, post));
				return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
						.setMessage("BlogPost has been successfully updated in drafts..")
						.setData(mapToBlogPostResponse(uniqueBlogPost)));
			}).orElseThrow(()->new BlogNotFoundByIdException("cannot add blogpost to get drafted"));
		}).orElseThrow(()->new PanelNotFoundByIdException("panel Not found"));

	}
}
