package com.example.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.login.config.WebAppService;
import com.example.login.model.RegisterForm;
import com.example.login.model.User;

@Controller
public class LoginController {
    @Autowired
    WebAppService webAppService;

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

   @GetMapping("/register")
public String showRegisterForm(Model model) {
    model.addAttribute("registerForm", new RegisterForm());
    return "register"; // Matches register.html
}
@PostMapping("/register")
public String goHome(@ModelAttribute RegisterForm registerForm) throws InterruptedException {
    User user = new User(registerForm.getUsername(),registerForm.getEmail(),registerForm.getPassword());
     webAppService.addUser(user);
    

    return "registration";
    
}
    @GetMapping("/forgot-password")
    public String forgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email) {
        // Add your password reset logic here
        return "redirect:/login?reset=requested";
    }
}
