package com.example.cms.exceptions;

public class UserEmailAlreadyExistsException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public UserEmailAlreadyExistsException(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	
}

