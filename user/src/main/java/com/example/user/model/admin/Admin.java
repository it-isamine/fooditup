package com.example.user.model.admin;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "user")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "userid")
	UUID id;

	String name;

	String email;

	String password;

	String phone;

	String address;

	String role;

	LocalDateTime createdat;

	public Admin() {
	}

	public Admin(String name, String email, String password, String role) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	@PrePersist
	public void prePersist() {
		if (id == null) {
			id = UUID.randomUUID(); // Automatically generate the UUID if it's not set
		}
		this.createdat = LocalDateTime.now();
	}
}
