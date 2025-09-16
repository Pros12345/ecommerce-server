package eCommerse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import eCommerse.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
}
