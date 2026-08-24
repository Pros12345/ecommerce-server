package eCommerse.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eCommerse.dto.ChangePasswordRequest;
import eCommerse.dto.UpdateProfileRequest;
import eCommerse.dto.UserProfileResponse;
import eCommerse.entity.User;
import eCommerse.repository.UserRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserProfileController(UserRepository userRepository, PasswordEncoder passwordEncoder) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	// ==========================================
	// GET PROFILE
	// ==========================================

	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(Authentication authentication) {

		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElse(null);

		if (user == null) {
			return ResponseEntity.status(404).body("User not found");
		}

		UserProfileResponse response = new UserProfileResponse(user.getId(), user.getFirstName(), user.getEmail(),
				user.getCountryCode(), user.getMobileNumber());

		return ResponseEntity.ok(response);
	}

	// ==========================================
	// UPDATE PROFILE
	// ==========================================

	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(Authentication authentication,
			@Valid @RequestBody UpdateProfileRequest request) {

		String currentEmail = authentication.getName();

		User user = userRepository.findByEmail(currentEmail).orElse(null);

		if (user == null) {
			return ResponseEntity.status(404).body("User not found");
		}

		// --------------------------------------
		// Check email uniqueness
		// --------------------------------------

		if (!currentEmail.equalsIgnoreCase(request.getEmail())) {

			if (userRepository.findByEmail(request.getEmail()).isPresent()) {

				return ResponseEntity.badRequest().body("Email already exists");
			}
		}

		// --------------------------------------
		// Update profile
		// --------------------------------------

		user.setFirstName(request.getFirstName());
		user.setEmail(request.getEmail());
		user.setCountryCode(request.getCountryCode());
		user.setMobileNumber(request.getMobileNumber());

		userRepository.save(user);

		UserProfileResponse response = new UserProfileResponse(user.getId(), user.getFirstName(), user.getEmail(),
				user.getCountryCode(), user.getMobileNumber());

		return ResponseEntity.ok(response);
	}

	// ==========================================
	// CHANGE PASSWORD
	// ==========================================

	@PutMapping("/change-password")
	public ResponseEntity<?> changePassword(Authentication authentication,
			@Valid @RequestBody ChangePasswordRequest request) {

		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElse(null);

		if (user == null) {
			return ResponseEntity.status(404).body(Map.of("message", "User not found"));
		}

		// Verify current password
		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {

			return ResponseEntity.status(401).body(Map.of("message", "Current password is incorrect"));
		}

		// Confirm new password
		if (!request.getNewPassword().equals(request.getConfirmPassword())) {

			return ResponseEntity.badRequest()
					.body(Map.of("message", "New password and confirm password do not match"));
		}

		// Prevent same password
		if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {

			return ResponseEntity.badRequest()
					.body(Map.of("message", "New password must be different from current password"));
		}

		// Encode and save
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));

		userRepository.save(user);

		return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
	}
}