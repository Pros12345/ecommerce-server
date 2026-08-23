package eCommerse.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eCommerse.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);

	boolean existsByMobileNumber(String mobileNumber);

	Optional<User> findByEmail(String email);

	Optional<User> findByMobileNumber(String mobileNumber);
}