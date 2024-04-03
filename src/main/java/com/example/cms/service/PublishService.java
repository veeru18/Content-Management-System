package com.example.cms.service;

import org.springframework.http.ResponseEntity;

import com.example.cms.requestdto.PublishRequest;
import com.example.cms.responsedto.PublishResponse;
import com.example.cms.utility.ResponseStructure;

public interface PublishService {

	public ResponseEntity<ResponseStructure<PublishResponse>> publishPost(int postId,PublishRequest preq);
}
