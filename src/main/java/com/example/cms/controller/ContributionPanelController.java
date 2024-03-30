package com.example.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cms.model.ContributionPanel;
import com.example.cms.service.ContributionPanelSevice;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ContributionPanelController {

	private ContributionPanelSevice panelService;
	
	@PutMapping("/users/{userId}/contribution-panels/{panelId}")
	public ResponseEntity<ResponseStructure<ContributionPanel>> addUserToPanel(@PathVariable int userId,@PathVariable int panelId) {
		return panelService.addUserToPanel(userId, panelId);
	}
	
	@DeleteMapping("/users/{userId}/contribution-panels/{panelId}")
	public ResponseEntity<ResponseStructure<ContributionPanel>> removeUserFromPanel(@PathVariable int userId,@PathVariable int panelId) {
		return panelService.removeUserFromPanel(userId, panelId);
	}
}
