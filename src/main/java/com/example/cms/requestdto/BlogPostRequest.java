package com.example.cms.requestdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostRequest {

	@NotNull @NotEmpty
	private String title;
	//can be null
	private String subTitle;
	@Min(value = 500,message = "summary should atleast contain 500 characters")
	private String summary;
	//can be null
	private String seoTitle;
	//can be null
	private String seoDescription;
	//can be null
	private String[] seoTopics;
}
