package com.example.user.model.user;

import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "confirmation")
public class Confirmation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int id;

  String token;

  UUID userid;

  public Confirmation() {
  }

  public Confirmation(UUID userid) {
    this.userid = userid;
    this.token = UUID.randomUUID().toString();

  }

}
