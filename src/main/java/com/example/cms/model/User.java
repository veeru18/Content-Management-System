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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	private String username;
	@Column(unique = true)
	private String email;
	private String password;
	//Association will always be BI-DIRECTIONAL,Composition & Aggregation will always be UNI-DIRECTIONAL
	@OneToMany(mappedBy = "user") @JsonIgnore
	private List<Blog> blogs=new ArrayList<>();
	@CreatedDate @Column(updatable = false)
	private LocalDateTime createdAt;
	@LastModifiedDate
	private LocalDateTime lastModifiedAt;
	private boolean deleted;
}
