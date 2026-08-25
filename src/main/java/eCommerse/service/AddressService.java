package eCommerse.service;

import java.util.List;

import eCommerse.dto.AddressRequest;
import eCommerse.dto.AddressResponse;

public interface AddressService {

	List<AddressResponse> getMyAddresses();

	AddressResponse getAddressById(Long addressId);

	AddressResponse addAddress(AddressRequest request);

	AddressResponse updateAddress(Long addressId, AddressRequest request);

	void deleteAddress(Long addressId);
}