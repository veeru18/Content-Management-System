package com.example.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@Getter @AllArgsConstructor
public class BlogPostNotFoundException extends RuntimeException{

	private String message;
}
