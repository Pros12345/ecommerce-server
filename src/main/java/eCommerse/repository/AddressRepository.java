package eCommerse.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import eCommerse.entity.Address;
import eCommerse.entity.User;

public interface AddressRepository extends JpaRepository<Address, Long> {

	List<Address> findByUser(User user);

	Optional<Address> findByIdAndUser(Long id, User user);
}