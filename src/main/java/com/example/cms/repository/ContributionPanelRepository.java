package com.example.cms.repository;

import org.springframework.stereotype.Repository;
import com.example.cms.model.ContributionPanel;
import com.example.cms.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ContributionPanelRepository extends JpaRepository<ContributionPanel,Integer>{

	boolean existsByPanelIdAndContributors(int panelId,User contributor);
}
