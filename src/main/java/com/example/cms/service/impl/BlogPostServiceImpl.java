package com.example.cms.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cms.enums.PostType;
import com.example.cms.exceptions.BlogNotFoundException;
import com.example.cms.exceptions.BlogPostNotFoundException;
import com.example.cms.exceptions.IllegalAccessRequestException;
import com.example.cms.exceptions.PostNotPublishedException;
import com.example.cms.exceptions.UserNotFoundException;
import com.example.cms.model.BlogPost;
import com.example.cms.model.ContributionPanel;
import com.example.cms.model.User;
import com.example.cms.repository.BlogPostRepository;
import com.example.cms.repository.BlogRepository;
import com.example.cms.repository.ContributionPanelRepository;
import com.example.cms.repository.UserRepository;
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
	private UserRepository userRepo;

	private ResponseStructure<BlogPostResponse> respStructure;

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> saveBlogPost(int blogId,BlogPostRequest breq) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return userRepo.findByEmail(email).map(user->{
			return blogRepo.findById(blogId).map(blog->{
				BlogPost post=new BlogPost();
				post.setBlog(blog); User existUser=blog.getUser();
				post.setPostType(PostType.DRAFT);
				ContributionPanel panel = blog.getContributionPanel();
				System.out.println(panel);
				if(!existUser.getEmail().equals(email)&& !panelRepo.existsByPanelIdAndContributors(panel.getPanelId(),user)) {
					//this for logical OR of that condition above
					//				return panelRepo.findById(blog.getContributionPanel().getPanelId()).map(panel->{
					//				List<User> contributors = panel.getContributors(); 
					//				int flag=0;
					//				for(User user:contributors) {
					//					if(user.getEmail()==email) flag=1;
					//				}
					//				if(flag==0) 
					//					throw new IllegalAccessRequestException("cannot access the blogpost, since neither he is owner nor he is contributor");
					//					if(!panel.getContributors().stream().anyMatch(user->user.getEmail()==email)) 
					//						throw new IllegalAccessRequestException("cannot access the blog, since neither he is owner nor he is contributor");

					//this for logical AND of that condition above
					throw new IllegalAccessRequestException("cannot access the blog, since neither he is owner nor he is contributor");

					//				}).orElseThrow(()->new PanelNotFoundByIdException("panel Not found"));
				}
				else {
					BlogPost uniqueBlogPost = postsRepo.save(mapToBlogPost(breq, post));
					return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
							.setMessage("BlogPost has been successfully drafted..")
							.setData(mapToBlogPostResponse(uniqueBlogPost)));
				}
			}).orElseThrow(()->new BlogNotFoundException("cannot add blogpost to get drafted"));
		}).orElseThrow(()->new UserNotFoundException("cannot create post,user not found by email! please register"));
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
				.publishResponse(PublishServiceImpl.mapToPublishResponse(post.getPublish()))
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> updateBlogPost(int blogPostId,BlogPostRequest breq) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return postsRepo.findById(blogPostId).map(post->{
			if(!email.equals(post.getCreatedBy())) 
				throw new IllegalAccessRequestException("cannot access the blog, since neither he is owner nor he is contributor");
			//			return panelRepo.findById(blog.getContributionPanel().getPanelId()).map(panel->{
			//
			//				if(!panel.getContributors().stream().anyMatch(user->user.getEmail().equals(email))) 
			//					throw new IllegalAccessRequestException("cannot access the blog, since neither he is owner nor he is contributor");
			BlogPost uniqueBlogPost = postsRepo.save(mapToBlogPost(breq, post));
			return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("BlogPost has been successfully updated in drafts..")
					.setData(mapToBlogPostResponse(uniqueBlogPost)));
		}).orElseThrow(()->new BlogPostNotFoundException("cannot add blogpost to get drafted"));
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> deleteBlogPost(int blogPostId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return postsRepo.findById(blogPostId).map(post->{
			if(!post.getBlog().getUser().getEmail().equals(email)&&!email.equals(post.getCreatedBy())) 
				throw new IllegalAccessRequestException("cannot access the blog, since neither he is owner nor he is contributor");
			//			return panelRepo.findById(blog.getContributionPanel().getPanelId()).map(panel->{
			//
			//				if(!panel.getContributors().stream().anyMatch(user->user.getEmail().equals(email))) 
			//					throw new IllegalAccessRequestException("cannot access the blog, since neither he is owner nor he is contributor");
			postsRepo.delete(post);
			return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("BlogPost has been successfully updated in drafts..")
					.setData(mapToBlogPostResponse(post)));
		}).orElseThrow(()->new BlogPostNotFoundException("cannot add blogpost to get drafted"));
	}

	public ResponseEntity<ResponseStructure<BlogPostResponse>> unpublishPost(int postId){
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return postsRepo.findById(postId).map(post->{
			if(!post.getCreatedBy().equals(email)&&!post.getBlog().getUser().getEmail().equals(email))
				throw new IllegalAccessRequestException("user entered is neither owner nor creator of this post");
			if(post.getPostType()==PostType.PUBLISHED) {
				post.setPostType(PostType.DRAFT);
				BlogPost uniquePost = postsRepo.save(post);
				return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
						.setMessage("blog post published has been drafted")
						.setData(mapToBlogPostResponse(uniquePost))); 
			}else {
				return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
						.setMessage("blog post published has already been been drafted")
						.setData(mapToBlogPostResponse(post)));
			}
		}).orElseThrow(()->new BlogPostNotFoundException("can't find the post mentioned by Id"));
	}

	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> fetchBlogPost(int blogPostId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		return postsRepo.findById(blogPostId).map(post->{
			ContributionPanel panel = post.getBlog().getContributionPanel();
			return userRepo.findByEmail(email).map(user->{
				if(!post.getCreatedBy().equals(email)
						&&!post.getBlog().getUser().getEmail().equals(email)
						&&!panelRepo.existsByPanelIdAndContributors(panel.getPanelId(),user))
					throw new IllegalAccessRequestException("user entered is neither owner nor creator of this post");
				return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
						.setMessage("blog post published has already been been drafted")
						.setData(mapToBlogPostResponse(post)));
			}).orElseThrow(()->new UserNotFoundException("can't find the user"));
		}).orElseThrow(()->new BlogPostNotFoundException("can't find the post mentioned by Id"));
	}
	
	@Override
	public ResponseEntity<ResponseStructure<BlogPostResponse>> fetchBlogPostIfPublished(int blogPostId) {
		return postsRepo.findByBlogPostIdAndPostType(blogPostId,PostType.PUBLISHED).map(post->{
				return ResponseEntity.ok(respStructure.setStatusCode(HttpStatus.OK.value())
						.setMessage("blog post fetch successful")
						.setData(mapToBlogPostResponse(post)));
		}).orElseThrow(()->new BlogPostNotFoundException("can't find the post mentioned by Id is not published"));
	}

}
