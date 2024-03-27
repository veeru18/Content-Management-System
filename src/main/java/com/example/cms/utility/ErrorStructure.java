package com.example.cms.utility;

import org.springframework.stereotype.Component;

@Component
public class ErrorStructure<T> {
	private int statuscode;
	private String message;
	private T rootCause;
	public int getStatuscode() {
		return statuscode;
	}
	public ErrorStructure<T> setStatuscode(int statuscode) {
		this.statuscode = statuscode;
		return this;
	}
	public String getMessage() {
		return message;
	}
	public ErrorStructure<T> setMessage(String message) {
		this.message = message;
		return this;
	}
	public T getRootCause() {
		return rootCause;
	}
	public ErrorStructure<T> setRootCause(T rootCause) {
		this.rootCause = rootCause;
		return this;
	}
}
