package com.example.cms.exceptions;

public class UserNotFoundByIdException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public UserNotFoundByIdException(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	
}
