package eCommerse.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eCommerse.dto.ChangePasswordRequest;
import eCommerse.dto.DeleteAccountRequest;
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

	@GetMapping("/profile")
	public ResponseEntity<?> getProfile(Authentication authentication) {

		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElse(null);

		if (user == null) {

			return ResponseEntity.status(404).body(Map.of("message", "User not found"));
		}

		UserProfileResponse response = new UserProfileResponse(user.getId(), user.getFirstName(), user.getEmail(),
				user.getCountryCode(), user.getMobileNumber());

		return ResponseEntity.ok(response);
	}

	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(Authentication authentication,
			@Valid @RequestBody UpdateProfileRequest request) {

		String currentEmail = authentication.getName();

		User user = userRepository.findByEmail(currentEmail).orElse(null);

		if (user == null) {

			return ResponseEntity.status(404).body(Map.of("message", "User not found"));
		}

		boolean passwordMatches = passwordEncoder.matches(request.getCurrentPassword(), user.getPassword());

		if (!passwordMatches) {

			return ResponseEntity.status(401).body(Map.of("message", "Current password is incorrect"));
		}

		if (!currentEmail.equalsIgnoreCase(request.getEmail())) {

			boolean emailExists = userRepository.findByEmail(request.getEmail()).isPresent();

			if (emailExists) {

				return ResponseEntity.badRequest().body(Map.of("message", "Email already exists"));
			}
		}

		user.setFirstName(request.getFirstName());

		user.setEmail(request.getEmail());

		user.setCountryCode(request.getCountryCode());

		user.setMobileNumber(request.getMobileNumber());

		userRepository.save(user);

		UserProfileResponse response = new UserProfileResponse(user.getId(), user.getFirstName(), user.getEmail(),
				user.getCountryCode(), user.getMobileNumber());

		return ResponseEntity.ok(response);
	}

	@PutMapping("/change-password")
	public ResponseEntity<?> changePassword(Authentication authentication,
			@Valid @RequestBody ChangePasswordRequest request) {

		String email = authentication.getName();

		User user = userRepository.findByEmail(email).orElse(null);

		if (user == null) {

			return ResponseEntity.status(404).body(Map.of("message", "User not found"));
		}

		if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {

			return ResponseEntity.status(401).body(Map.of("message", "Current password is incorrect"));
		}

		if (!request.getNewPassword().equals(request.getConfirmPassword())) {

			return ResponseEntity.badRequest()
					.body(Map.of("message", "New password and confirm password do not match"));
		}

		if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {

			return ResponseEntity.badRequest()
					.body(Map.of("message", "New password must be different from current password"));
		}

		user.setPassword(passwordEncoder.encode(request.getNewPassword()));

		userRepository.save(user);

		return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
	}

	@DeleteMapping("/profile")
	public ResponseEntity<?> deleteAccount(Authentication authentication,
			@Valid @RequestBody DeleteAccountRequest request) {

		String currentEmail = authentication.getName();

		User user = userRepository.findByEmail(currentEmail).orElse(null);

		if (user == null) {

			return ResponseEntity.status(404).body(Map.of("message", "User not found"));
		}

		boolean passwordMatches = passwordEncoder.matches(request.getCurrentPassword(), user.getPassword());

		if (!passwordMatches) {

			return ResponseEntity.status(401).body(Map.of("message", "Current password is incorrect"));
		}

		userRepository.delete(user);

		return ResponseEntity.ok(Map.of("message", "Account deleted successfully"));
	}

}