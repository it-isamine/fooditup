package com.example.user.repo.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.user.model.user.Confirmation;

@Repository
public interface ConfirmationRepo extends JpaRepository<Confirmation, Integer> {

    Optional<Confirmation> findByToken(String token);

}
