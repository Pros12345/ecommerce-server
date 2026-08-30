package eCommerse.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import eCommerse.dto.AddressRequest;
import eCommerse.dto.AddressResponse;
import eCommerse.entity.Address;
import eCommerse.entity.User;
import eCommerse.repository.AddressRepository;
import eCommerse.repository.UserRepository;
import eCommerse.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {

		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
	}

	private User getLoggedInUser() {

		logger.info("AddressServiceImpl : getLoggedInUser :: Started");

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		logger.info("AddressServiceImpl : getLoggedInUser :: Ended");

		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	@Override
	public List<AddressResponse> getMyAddresses() {

		logger.info("AddressServiceImpl : getMyAddresses :: Started");

		User user = getLoggedInUser();

		logger.info("AddressServiceImpl : getMyAddresses :: Ended");

		return addressRepository.findByUser(user).stream().map(this::convertToResponse).collect(Collectors.toList());
	}

	@Override
	public AddressResponse getAddressById(Long addressId) {

		logger.info("AddressServiceImpl : getAddressById :: Started");

		User user = getLoggedInUser();
		Address address = addressRepository.findByIdAndUser(addressId, user)
				.orElseThrow(() -> new RuntimeException("Address not found"));

		logger.info("AddressServiceImpl : getAddressById :: Ended");

		return convertToResponse(address);
	}

	@Override
	public AddressResponse addAddress(AddressRequest request) {

		logger.info("AddressServiceImpl : addAddress :: Started");

		User user = getLoggedInUser();
		Address address = new Address();
		address.setUser(user);
		address.setFullName(request.getFullName());
		address.setMobileNumber(request.getMobileNumber());
		address.setAddressLine1(request.getAddressLine1());
		address.setAddressLine2(request.getAddressLine2());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setPincode(request.getPincode());
		address.setLandmark(request.getLandmark());
		address.setAddressType(request.getAddressType());
		Address saved = addressRepository.save(address);

		logger.info("AddressServiceImpl : addAddress :: Ended");

		return convertToResponse(saved);
	}

	@Override
	public AddressResponse updateAddress(Long addressId, AddressRequest request) {

		logger.info("AddressServiceImpl : updateAddress :: Started");

		User user = getLoggedInUser();
		Address address = addressRepository.findByIdAndUser(addressId, user)
				.orElseThrow(() -> new RuntimeException("Address not found"));
		address.setFullName(request.getFullName());
		address.setMobileNumber(request.getMobileNumber());
		address.setAddressLine1(request.getAddressLine1());
		address.setAddressLine2(request.getAddressLine2());
		address.setCity(request.getCity());
		address.setState(request.getState());
		address.setPincode(request.getPincode());
		address.setLandmark(request.getLandmark());
		address.setAddressType(request.getAddressType());
		Address updated = addressRepository.save(address);

		logger.info("AddressServiceImpl : updateAddress :: Ended");

		return convertToResponse(updated);
	}

	@Override
	public void deleteAddress(Long addressId) {

		logger.info("AddressServiceImpl : deleteAddress :: Started");

		User user = getLoggedInUser();
		Address address = addressRepository.findByIdAndUser(addressId, user)
				.orElseThrow(() -> new RuntimeException("Address not found"));
		addressRepository.delete(address);

		logger.info("AddressServiceImpl : deleteAddress :: Ended");

	}

	private AddressResponse convertToResponse(Address address) {

		logger.info("AddressServiceImpl : convertToResponse :: Started");

		AddressResponse response = new AddressResponse();
		response.setId(address.getId());
		response.setFullName(address.getFullName());
		response.setMobileNumber(address.getMobileNumber());
		response.setAddressLine1(address.getAddressLine1());
		response.setAddressLine2(address.getAddressLine2());
		response.setCity(address.getCity());
		response.setState(address.getState());
		response.setPincode(address.getPincode());
		response.setLandmark(address.getLandmark());
		response.setAddressType(address.getAddressType());

		logger.info("AddressServiceImpl : convertToResponse :: Ended");

		return response;
	}
}