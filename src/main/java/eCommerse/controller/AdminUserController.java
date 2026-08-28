package eCommerse.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eCommerse.dto.AdminUserResponse;
import eCommerse.service.AdminUserService;

@RestController
@RequestMapping("/api")
public class AdminUserController {

	private final AdminUserService adminUserService;

	public AdminUserController(AdminUserService adminUserService) {

		this.adminUserService = adminUserService;
	}

	// =====================================================
	// GET ALL USERS
	// =====================================================

	@GetMapping("/admin/users")
	public ResponseEntity<List<AdminUserResponse>> getAllUsers(Authentication authentication) {

		String email = authentication.getName();

		return ResponseEntity.ok(adminUserService.getAllUsers(email));
	}

	// =====================================================
	// DELETE USER
	// =====================================================

	@DeleteMapping("/admin/users/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId, Authentication authentication) {

		String email = authentication.getName();

		adminUserService.deleteUser(userId, email);

		return ResponseEntity.noContent().build();
	}
}