package eCommerse.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eCommerse.entity.User;
import eCommerse.repository.impl.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User loginData) {
		return userRepository.findByEmail(loginData.getEmail()).map(user -> {
			System.out.println("Login attempt: " + loginData.getEmail());
			System.out.println("Raw password: " + loginData.getPassword());
			System.out.println("Stored password: " + user.getPassword());
			if (passwordEncoder.matches("Prosenjit44@",
					"$2a$10$5xONl7ri27XV2aOpt867JOar5oF/JbxvHN9GsweaXZ4XU/0hRdYHe")) {
				String token = jwtUtil.generateToken(user.getEmail());
				return ResponseEntity.ok(token);
			} else {
				return ResponseEntity.status(401).body("Invalid credentials");
			}
		}).orElse(ResponseEntity.status(404).body("User not found"));
	}
}
