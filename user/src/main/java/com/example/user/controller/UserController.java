package com.example.user.controller;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServletRequest;

import com.example.user.model.HttpResponse;
import com.example.user.model.admin.Admin;
import com.example.user.model.user.Confirmation;
import com.example.user.model.user.User;
import com.example.user.repo.admin.AdminRepo;
import com.example.user.repo.user.ConfirmationRepo;
import com.example.user.repo.user.UserRepo;
import com.example.user.service.UserService;
import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	AdminRepo Arepo;

	@Autowired
	UserRepo repo;

	@Autowired
	ConfirmationRepo cRepo;

	private List<User> users = new CopyOnWriteArrayList<>();

	@Autowired
	PasswordEncoder passwordEncoder;

	RestTemplate restTemplate = new RestTemplate();

	@Autowired
	UserService service;

	public static final String NEW_USER_ACCOUNT_VERIFICATION = "New User Account Verification";

	public static final String UTF_8_ENCODING = "UTF-8";

	public static final String EMAIL_TEMPLATE = "emailtemplate";

	public static final String TEXT_HTML_ENCONDING = "text/html";

	private static final Logger logger = (Logger) LoggerFactory.getLogger(UserController.class);

	@PostMapping("/register")
	public User registerUser(@RequestBody User user) {
		repo.save(user);
		return user;
	}

	@PostMapping("/registerAdmin")
	public Admin registerAdmin(@RequestBody Admin user) {
		Arepo.save(user);
		return user;
	}

	@GetMapping("/user/me")
	public Object getUserme(@RequestAttribute("userid") String name) {
		Optional<User> user = repo.findByName(name);
		if (!user.isPresent()) {
			return Arepo.findByName(name).orElseThrow();
		}
		return user.orElseThrow();
	}

	@GetMapping("/get")
	public Iterable<User> getUsers() {
		return repo.findAll();
	}

	@GetMapping("/getadmin")
	public Iterable<Admin> getAdmis() {
		return Arepo.findAll();
	}

	@GetMapping("/users/me")
	public ResponseEntity<HttpResponse> getMySelf(HttpServletRequest request) {
		User user = repo.findByName((String) request.getSession().getAttribute("name")).orElseThrow();
		return ResponseEntity.created(URI.create("")).body(
				HttpResponse.builder()
						.timeStamp(LocalDateTime.now().toString())
						.data(Map.of("user", user))
						.message("User created")
						.status(HttpStatus.CREATED)
						.statusCode(HttpStatus.CREATED.value())
						.build());
	}

	@PutMapping("/users")
	public void updateUser(@RequestAttribute("userid") String userid, @RequestBody User user) {
		User user2 = repo.findByName(userid).orElseThrow();
		if (user2.getRole().equals("ADMIN") || user2.getRole().equals("RESTAURANT_ADMIN")) {
			User usero = repo.findById(user.getId()).orElseThrow();
			usero.setName(user.getName());
			usero.setEmail(user.getEmail());
			String encryptedPassword = passwordEncoder.encode(user.getPassword());
			usero.setPassword(encryptedPassword);
			System.out.println(user.getPassword());
			usero.setRole(user.getRole());
			repo.save(usero);
		}
	}
	@PutMapping("/self")
public void updateSelf(@RequestAttribute("userid") String userid, @RequestBody User user) {
    User usero = repo.findByName(userid).orElseThrow();

    // Update fields only if they are not null or empty
    if (isValid(user.getName())) {
        usero.setName(user.getName());
    }
    if (isValid(user.getEmail())) {
        usero.setEmail(user.getEmail());
    }
    if (isValid(user.getPhone())) {
        usero.setPhone(user.getPhone());
    }
    if (isValid(user.getPassword())) {
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        usero.setPassword(encryptedPassword);
    }

    repo.save(usero);
}

private boolean isValid(String value) {
    return value != null && !value.trim().isEmpty();
}



	@GetMapping("/users/{name}")
	public Object getUserByName(@PathVariable("name") String name) {
		Optional<User> user = repo.findByName(name);
		if (!user.isPresent()) {
			return Arepo.findByName(name).orElseThrow();
		}
		return user.orElseThrow();
	}

	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable UUID id, @RequestAttribute("userid") String name) {
		User user = repo.findByName(name).orElseThrow();
		if (user.getRole().equals("ADMIN") || user.getRole().equals("RESTAURANT_ADMIN")) {
			System.out.println(repo.findById(id));
			repo.deleteById(id);
		}

	}

	@GetMapping("/users/{id}/adresses")
	public String getAdress(@PathVariable UUID id) {
		return repo.findById(id).orElseThrow().getAddress();
	}

	@PostMapping("/forgot-password")
	public void forgotPassword(@RequestParam String email, HttpServletRequest request) {
		User user = new User();
		user.setName("aminzz");
		user.setEmail(email);
		user.setPassword("password");
		user.setRole("CUSTOMER");
		repo.save(user);
		Confirmation confirmation = new Confirmation(user.getId());
		cRepo.save(confirmation);
		service.sendEmailVerification(user.getName(), email, confirmation.getToken());
	}

	@PostMapping("/createaccount")
	public User createaccount(@RequestBody User user) {
		System.out.println(user);
		Confirmation confirmation = new Confirmation(user.getId());
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);
		users.add(user);
		cRepo.save(confirmation);
		System.out.println(users);
		service.sendEmailVerification(user.getName(), user.getEmail(), confirmation.getToken());
		return user;
	}

	@GetMapping("/users/verify")
	public ResponseEntity<String> verify(@RequestParam String token) {
		logger.info("Verifying token: {}", token);
		Confirmation confirmation = cRepo.findByToken(token)
				.orElseThrow(() -> new IllegalArgumentException("Invalid or expired token."));

		Optional<User> userOptional = users.stream().filter(e -> e.getId().equals(confirmation.getUserid()))
				.findFirst();

		if (userOptional.isEmpty()) {
			logger.warn("User not found for token: {}", token);
			return ResponseEntity.badRequest().body("{\"message\": \"User not found for confirmation.\"}");
		}

		User user = userOptional.get();
		repo.save(user);
		logger.info("User {} verified successfully.", user.getId());
		cRepo.deleteById(confirmation.getId());

		return ResponseEntity.ok("{\"message\": \"Email verified successfully.\"}");
	}

}
