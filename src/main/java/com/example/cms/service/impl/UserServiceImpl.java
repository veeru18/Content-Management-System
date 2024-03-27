package com.example.cms.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.cms.exceptions.UserEmailAlreadyExistsException;
import com.example.cms.exceptions.UserNotFoundException;
import com.example.cms.model.User;
import com.example.cms.repository.UserRepository;
import com.example.cms.requestdto.Login;
import com.example.cms.requestdto.UserRequest;
import com.example.cms.responsedto.UserResponse;
import com.example.cms.service.UserService;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private ResponseStructure<UserResponse> structure;

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> userRegistration(UserRequest user) {
		if(userRepository.existsByEmail(user.getEmail()))
			throw new UserEmailAlreadyExistsException("Failed to Register the User");
		User uniqueUser=userRepository.save(mapToUser(user,new User()));
		return ResponseEntity.ok(structure.setStatusCode(HttpStatus.OK.value())
				.setMessage("User Saved Successfully!!")
				.setData(mapToUserResponse(uniqueUser)));
	}
	
	private UserResponse mapToUserResponse(User uniqueUser) {
		return UserResponse.builder().userId(uniqueUser.getUserId())
				.username(uniqueUser.getUsername())
				.deleted(uniqueUser.isDeleted())
				.email(uniqueUser.getEmail()).createdAt(uniqueUser.getCreatedAt())
				.lastModifiedAt(uniqueUser.getLastModifiedAt()).build();
	}

	public User mapToUser(UserRequest dto,User user) {
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setUsername(dto.getUserName());
		user.setEmail(dto.getEmail());
		user.setDeleted(false);
		return user;
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> login(Login loginCreds) {
		return null;
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> userDeletion(Integer userId) {
		return userRepository.findById(userId).map(prevUser->{
			UserResponse upUser = mapToUserResponse(updateRegistration(prevUser));
			return ResponseEntity.ok(structure.setStatusCode(HttpStatus.OK.value())
					.setData(upUser)
					.setMessage("User_deleted attribute updated and account will be deleted after 30 days if not used"));
		}).orElseThrow(()-> new UserNotFoundException("User with the Specified ID is not found"));
	}
	
	private User updateRegistration(User user){
		user.setDeleted(true);
		return userRepository.save(user);
	}
	
//	Optional<User> byId = userRepository.findById(userId);
//	if(byId.isPresent()) {
//		User user = byId.get();
//		user.setDeleted(true);
//		User updatedUser = userRepository.save(user);
//		UserResponse toUserResponse = mapToUserResponse(updatedUser);
//		return ResponseEntity.ok(structure.setStatusCode(HttpStatus.OK.value())
//											.setData(toUserResponse)
//											.setMessage("User Delete attribute updated"));
//	}else {
//		throw new UserNotFoundException("User with the Specified ID is not found");
//	}	
	
}
