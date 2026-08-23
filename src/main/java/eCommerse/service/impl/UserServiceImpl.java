package eCommerse.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import eCommerse.entity.User;
import eCommerse.repository.UserRepository;
import eCommerse.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User registerUser(User user) {

		logger.info("UserServiceImpl : registerUser :: Started");

		// Check email
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new RuntimeException("Email already exists!");
		}

		// Check mobile
		if (userRepository.existsByMobileNumber(user.getMobileNumber())) {
			throw new RuntimeException("Mobile number already exists!");
		}

		// Encode password
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		logger.info("UserServiceImpl : registerUser :: Ended");

		return userRepository.save(user);
	}
}