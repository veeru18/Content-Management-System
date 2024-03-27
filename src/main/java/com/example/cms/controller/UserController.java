package com.example.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.requestdto.UserRequest;
import com.example.cms.responsedto.UserResponse;
import com.example.cms.service.UserService;
import com.example.cms.utility.ErrorStructure;
import com.example.cms.utility.ResponseStructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
public class UserController {
	private UserService userService;

	@GetMapping("/test")	
	public String test() {
		return "Hello From CMS";
	}
	
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}
	
	@Operation(description = "for user registration!!",responses = {
			@ApiResponse(responseCode = "200",description = "Inserted Successfully"),
			@ApiResponse(responseCode = "400",description = "Invalid Inputs",content = {
					@Content(schema = @Schema(implementation = ErrorStructure.class))
			})
	})
	@PostMapping("/users/register")
	public ResponseEntity<ResponseStructure<UserResponse>> userRegistration(@RequestBody @Valid UserRequest user){
		return userService.userRegistration(user);
	}
	
	@DeleteMapping("/users/{userId}")
	private ResponseEntity<ResponseStructure<UserResponse>> userDeletion(@PathVariable Integer userId){
		return userService.userDeletion(userId);
	}
}
