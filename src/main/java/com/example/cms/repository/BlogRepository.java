package com.example.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cms.model.Blog;
import com.example.cms.model.ContributionPanel;
import com.example.cms.model.User;

public interface BlogRepository extends JpaRepository<Blog, Integer> {

	public boolean existsByTitle(String title);
	
	public boolean existsByUserAndContributionPanel(User user,ContributionPanel contributionPanel);
}
