package com.example.user.repo.admin;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user.model.admin.Admin;

@Repository
public interface AdminRepo extends JpaRepository<Admin, UUID> {

     Optional<Admin> findByName(String name);
}
