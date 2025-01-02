package com.example.login.config;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.login.model.User;
@Service
public class WebAppService {
    RestTemplate restTemplate = new RestTemplate();
    
      public void addUser(User user) {
        HttpEntity<User> entity = new HttpEntity<>(user);
        ResponseEntity<Void> response = restTemplate.exchange("http://localhost:8090/users/createaccount",
                HttpMethod.POST, entity, Void.class);

    }
}
