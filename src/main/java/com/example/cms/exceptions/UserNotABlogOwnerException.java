package com.example.cms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@SuppressWarnings("serial")
@AllArgsConstructor
@Getter
public class UserNotABlogOwnerException extends RuntimeException {

	private String message;
}
