package eCommerse.service.impl;

import java.util.List;
import java.util.stream.Collectors;

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

	private final AddressRepository addressRepository;
	private final UserRepository userRepository;

	public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository) {

		this.addressRepository = addressRepository;
		this.userRepository = userRepository;
	}

	// =====================================================
	// GET LOGGED-IN USER
	// =====================================================

	private User getLoggedInUser() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
	}

	// =====================================================
	// GET ALL ADDRESSES
	// =====================================================

	@Override
	public List<AddressResponse> getMyAddresses() {

		User user = getLoggedInUser();

		return addressRepository.findByUser(user).stream().map(this::convertToResponse).collect(Collectors.toList());
	}

	// =====================================================
	// GET ADDRESS BY ID
	// =====================================================

	@Override
	public AddressResponse getAddressById(Long addressId) {

		User user = getLoggedInUser();

		Address address = addressRepository.findByIdAndUser(addressId, user)
				.orElseThrow(() -> new RuntimeException("Address not found"));

		return convertToResponse(address);
	}

	// =====================================================
	// ADD ADDRESS
	// =====================================================

	@Override
	public AddressResponse addAddress(AddressRequest request) {

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

		return convertToResponse(saved);
	}

	// =====================================================
	// UPDATE ADDRESS
	// =====================================================

	@Override
	public AddressResponse updateAddress(Long addressId, AddressRequest request) {

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

		return convertToResponse(updated);
	}

	// =====================================================
	// DELETE ADDRESS
	// =====================================================

	@Override
	public void deleteAddress(Long addressId) {

		User user = getLoggedInUser();

		Address address = addressRepository.findByIdAndUser(addressId, user)
				.orElseThrow(() -> new RuntimeException("Address not found"));

		addressRepository.delete(address);
	}

	// =====================================================
	// ENTITY -> RESPONSE
	// =====================================================

	private AddressResponse convertToResponse(Address address) {

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

		return response;
	}
}