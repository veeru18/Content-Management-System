package com.example.cms.exceptions;

public class UserNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public UserNotFoundException(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	
}
