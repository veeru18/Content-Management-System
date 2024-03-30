package com.example.cms.service;

import org.springframework.http.ResponseEntity;

import com.example.cms.model.ContributionPanel;
import com.example.cms.utility.ResponseStructure;

public interface ContributionPanelSevice {

	ResponseEntity<ResponseStructure<ContributionPanel>> addUserToPanel(int userId,int panelId);
	ResponseEntity<ResponseStructure<ContributionPanel>> removeUserFromPanel(int userId,int panelId);
}
