package com.example.cms.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.cms.exceptions.PanelNotFoundException;
import com.example.cms.exceptions.IllegalAccessRequestException;
import com.example.cms.exceptions.UserNotFoundException;
import com.example.cms.model.ContributionPanel;
import com.example.cms.repository.BlogRepository;
import com.example.cms.repository.ContributionPanelRepository;
import com.example.cms.repository.UserRepository;
import com.example.cms.service.ContributionPanelSevice;
import com.example.cms.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContributionPanelServiceImpl implements ContributionPanelSevice{

	private UserRepository userRepo;
	private BlogRepository blogRepo;
	private ContributionPanelRepository panelRepo;
	
	private ResponseStructure<ContributionPanel> struct;
	
	
	@Override
	public ResponseEntity<ResponseStructure<ContributionPanel>> addUserToPanel(int userId,int panelId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(panelId);
		return userRepo.findByEmail(email).map(owner->{
			return panelRepo.findById(panelId).map(panel->{
				if(!blogRepo.existsByUserAndContributionPanel(owner, panel))
					throw new IllegalAccessRequestException("Failed to add contributor");
				return userRepo.findById(userId).map(contributor->{
					if(contributor.getUserId()==owner.getUserId()) 
						throw new IllegalArgumentException("Failed to add contributor since owner and contributor are same");
					ContributionPanel uniquePanel=null;
					if(!panel.getContributors().contains(contributor)) {
						panel.getContributors().add(contributor);
						uniquePanel = panelRepo.save(panel);
					}
					else uniquePanel=panel;
					return ResponseEntity.ok(struct.setStatusCode(HttpStatus.OK.value())
												.setMessage("Contributor added successfully")
												.setData(uniquePanel));
				}).orElseThrow(()->new UserNotFoundException("User Not found in the database, please register first"));
			}).orElseThrow(()->new PanelNotFoundException("Panel Not found in db, create a blog first"));
		}).get();// since the "/login" already validates the owner no need to check and throw
		//thus get() is used to finish optional chaining to obtain the responseEntity.ok();
	}

	@Override
	public ResponseEntity<ResponseStructure<ContributionPanel>> removeUserFromPanel(int userId,int panelId) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println(panelId);
		return userRepo.findByEmail(email).map(owner->{
			return panelRepo.findById(panelId).map(panel->{
				if(!blogRepo.existsByUserAndContributionPanel(owner, panel))
					throw new IllegalAccessRequestException("Failed to add contributor");
				return userRepo.findById(userId).map(contributor->{
					if(contributor.getUserId()==owner.getUserId()) 
						throw new IllegalArgumentException("Failed to add contributor since owner and contributor are same");
					ContributionPanel uniquePanel=null;
					if(panel.getContributors().contains(contributor)) {
						panel.getContributors().remove(contributor);
						uniquePanel = panelRepo.save(panel);
					}
					else uniquePanel=panel;
					return ResponseEntity.ok(struct.setStatusCode(HttpStatus.OK.value())
												.setMessage("Contributor deleted successfully")
												.setData(uniquePanel));
				}).orElseThrow(()->new UserNotFoundException("User Not found in the database, please register first"));
			}).orElseThrow(()->new PanelNotFoundException("Panel Not found in db, create a blog first"));
		}).get();
	}
	
}
