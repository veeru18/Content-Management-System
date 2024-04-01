package com.example.cms.responsedto;

import java.time.LocalDateTime;

import com.example.cms.enums.PostType;
import com.example.cms.model.Blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class BlogPostResponse {

	private int blogPostId;
	private String title;
	private String subTitle;
	private String summary;
	private PostType postType;
	private Blog blog;
	
	private String createdBy;
	private LocalDateTime createdAt;
	private LocalDateTime lastModifiedAt;
	private String lastModifiedBy;
}
