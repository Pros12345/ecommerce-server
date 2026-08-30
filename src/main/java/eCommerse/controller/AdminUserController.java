package eCommerse.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

	private final AdminUserService adminUserService;

	public AdminUserController(AdminUserService adminUserService) {

		this.adminUserService = adminUserService;
	}

	@GetMapping("/admin/users")
	public ResponseEntity<List<AdminUserResponse>> getAllUsers(Authentication authentication) {

		logger.info("AdminUserController : getAllUsers :: Started");

		String email = authentication.getName();

		logger.info("AdminUserController : getAllUsers :: Ended");

		return ResponseEntity.ok(adminUserService.getAllUsers(email));
	}

	@DeleteMapping("/admin/users/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId, Authentication authentication) {

		logger.info("AdminUserController : deleteUser :: Started");

		String email = authentication.getName();
		adminUserService.deleteUser(userId, email);

		logger.info("AdminUserController : deleteUser :: Ended");

		return ResponseEntity.noContent().build();
	}
}