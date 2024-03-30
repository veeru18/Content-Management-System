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

import com.example.cms.exceptions.TopicNotSpecifiedException;
import com.example.cms.exceptions.IllegalAccessRequestException;
import com.example.cms.exceptions.BlogNotFoundByIdException;
import com.example.cms.exceptions.PanelNotFoundByIdException;
import com.example.cms.exceptions.TitleNotAvailableException;
import com.example.cms.exceptions.UserEmailAlreadyExistsException;
import com.example.cms.exceptions.UserNotFoundByIdException;

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
		return new ResponseEntity<ErrorStructure<String>>(error.setStatuscode(status.value())
															.setRootCause(rootCause)
															.setMessage(message),status);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserEmailAlreadyExistsException(UserEmailAlreadyExistsException ueaex){
		return errorResponse(HttpStatus.BAD_REQUEST,ueaex.getMessage(),"User Email Alread exists! please enter a Valid Email ID");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserNotFoundByIdException(UserNotFoundByIdException usex){
		return errorResponse(HttpStatus.BAD_REQUEST,usex.getMessage(),"User not found, please check the database");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleTopicNotSpecifiedException(TopicNotSpecifiedException topnex){
		return errorResponse(HttpStatus.BAD_REQUEST,topnex.getMessage(),"Topics or about not specified, please specify");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleTitleNotAvailableException(TitleNotAvailableException tnaex){
		return errorResponse(HttpStatus.BAD_REQUEST,tnaex.getMessage(),"Blog Title already exists, enter a valid title");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleBlogNotFoundByIdException(BlogNotFoundByIdException ibex){
		return errorResponse(HttpStatus.NOT_FOUND,ibex.getMessage(),"Blog doesn't exist of specified ID, enter a valid id");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlePanelNotFoundByIdException(PanelNotFoundByIdException pex){
		return errorResponse(HttpStatus.BAD_REQUEST,pex.getMessage(),"Panel not found, please check the database");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleIllegalAccessRequestException(IllegalAccessRequestException unauthorizedAccessEx){
		return errorResponse(HttpStatus.UNAUTHORIZED,unauthorizedAccessEx.getMessage(),"Panel not found, please check the database");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleIllegalArgumentException(IllegalArgumentException illegalArgEx){
		return errorResponse(HttpStatus.UNAUTHORIZED,illegalArgEx.getMessage(),"Owner and Contributor cannnot be same! please add different user");
	}
}
