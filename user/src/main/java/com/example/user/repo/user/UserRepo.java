package com.example.user.repo.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.user.model.user.User;

@Repository

public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findByName(String name);

    Optional<User> findByEmail(String email);
}
