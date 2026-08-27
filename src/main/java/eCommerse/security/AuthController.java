package eCommerse.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eCommerse.dto.LoginRequest;
import eCommerse.dto.LoginResponse;
import eCommerse.entity.User;
import eCommerse.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginData) {

		logger.info("AuthController : login :: Started");

		String identifier = loginData.getIdentifier();

		String password = loginData.getPassword();

		/*
		 * Validate identifier
		 */
		if (identifier == null || identifier.isBlank()) {

			return ResponseEntity.badRequest().body("Email or mobile number is required");
		}

		/*
		 * Validate password
		 */
		if (password == null || password.isBlank()) {

			return ResponseEntity.badRequest().body("Password is required");
		}

		identifier = identifier.trim();

		User user;

		/*
		 * Login using email
		 */
		if (identifier.contains("@")) {

			logger.info("Login attempt using email: {}", identifier);

			user = userRepository.findByEmail(identifier).orElse(null);

		}

		/*
		 * Login using mobile number
		 */
		else {

			String mobileNumber = identifier;

			if (mobileNumber.startsWith("+91")) {

				mobileNumber = mobileNumber.substring(3);
			}

			logger.info("Login attempt using mobile: {}", mobileNumber);

			user = userRepository.findByMobileNumber(mobileNumber).orElse(null);
		}

		/*
		 * User not found
		 */
		if (user == null) {

			logger.warn("User not found: {}", identifier);

			return ResponseEntity.status(401).body("Invalid email/mobile number or password");
		}

		/*
		 * Validate password
		 */
		if (!passwordEncoder.matches(password, user.getPassword())) {

			logger.warn("Invalid password for: {}", identifier);

			return ResponseEntity.status(401).body("Invalid email/mobile number or password");
		}

		/*
		 * Generate JWT using user's email.
		 *
		 * This is important because your AddressService uses Authentication.getName()
		 * to find the user.
		 */
		String token = jwtUtil.generateToken(user.getEmail());

		logger.info("Login successful for user: {}", user.getEmail());

		return ResponseEntity.ok(new LoginResponse(token, user.getEmail(), user.getFirstName()));
	}
}