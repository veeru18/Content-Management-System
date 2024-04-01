package com.example.cms.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blogs")
@EntityListeners(AuditingEntityListener.class)
public class Blog {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int blogId;
	@Column(unique = true)
	private String title;
	private String[] topics;
	private String summary;
	@ManyToOne
	private User user;
	@OneToOne
	private ContributionPanel contributionPanel;
	@OneToMany(mappedBy = "blog") @JsonIgnore
	private List<BlogPost> blogPosts=new ArrayList<>();
	
	@CreatedDate @Column(updatable = false)
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime lastModifiedAt;
}
