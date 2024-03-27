package com.example.cms.utility;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.cms.exceptions.UserEmailAlreadyExistsException;
import com.example.cms.exceptions.UserNotFoundException;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{
	
	private ErrorStructure<String> error;	
	
	private ErrorStructure<Map<String,String>> struct;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//		List<ObjectError> allErrors = ex.getAllErrors();
		Map<String,String> messages=new HashMap<>();
		ex.getAllErrors().forEach(error->messages.put(((FieldError)error).getField(),error.getDefaultMessage()));
//		{
//			FieldError fieldError=(FieldError)err;
//			errors.put(((FieldError)err).getField(),err.getDefaultMessage());
//		});
		return ResponseEntity.badRequest().body(struct.setRootCause(messages)
												.setMessage("Invalid Data Entered")
												.setStatuscode(HttpStatus.BAD_REQUEST.value()));
	}
	
	private ResponseEntity<ErrorStructure<String>> errorResponse(HttpStatus status,String message,String rootCause){
		return new ResponseEntity<ErrorStructure<String>>(error.setStatuscode(status.value()).setRootCause(rootCause).setMessage(message),status);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserEmailAlreadyExistsException(UserEmailAlreadyExistsException ex){
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"User Email Alread exists! please enter a Valid Email ID");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserNotFoundException(UserNotFoundException ex){
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"User not found, please check the database");
	}
}
//@RestControllerAdvice
//public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler{
//	private ErrorStructure<Map<String,String>> struct;
//	public ApplicationExceptionHandler(ErrorStructure<Map<String,String>> struct) {
//		this.struct = struct;
//	}
//
//	@Override
//	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
////		List<ObjectError> allErrors = ex.getAllErrors();
//		Map<String,String> messages=new HashMap<>();
//		ex.getAllErrors().forEach(error->messages.put(((FieldError)error).getField(),error.getDefaultMessage()));
////		{
////			FieldError fieldError=(FieldError)err;
////			errors.put(((FieldError)err).getField(),err.getDefaultMessage());
////		});
//		return ResponseEntity.badRequest().body(struct.setRootCause(messages)
//												.setMessage("Invalid Data Entered")
//												.setStatuscode(HttpStatus.BAD_REQUEST.value()));
//	}
//}