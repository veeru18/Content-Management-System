package com.example.cms.exceptions;

public class UserIdNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public UserIdNotFoundException(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	
}
