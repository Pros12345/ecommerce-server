package eCommerse.service;

import java.util.List;

import eCommerse.dto.AdminUserResponse;

public interface AdminUserService {

	List<AdminUserResponse> getAllUsers(String loggedInEmail);

	void deleteUser(Long userId, String loggedInEmail);
}