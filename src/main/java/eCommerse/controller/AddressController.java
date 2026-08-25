package eCommerse.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eCommerse.dto.AddressRequest;
import eCommerse.dto.AddressResponse;
import eCommerse.service.AddressService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/addresses")
public class AddressController {

	private final AddressService addressService;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	// =====================================================
	// GET ALL SAVED ADDRESSES
	// GET /api/user/addresses
	// =====================================================

	@GetMapping
	public ResponseEntity<List<AddressResponse>> getMyAddresses() {

		List<AddressResponse> addresses = addressService.getMyAddresses();

		return ResponseEntity.ok(addresses);
	}

	// =====================================================
	// GET ADDRESS BY ID
	// GET /api/user/addresses/1
	// =====================================================

	@GetMapping("/{addressId}")
	public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long addressId) {

		AddressResponse response = addressService.getAddressById(addressId);

		return ResponseEntity.ok(response);
	}

	// =====================================================
	// ADD NEW ADDRESS
	// POST /api/user/addresses
	// =====================================================

	@PostMapping
	public ResponseEntity<AddressResponse> addAddress(@Valid @RequestBody AddressRequest request) {

		AddressResponse response = addressService.addAddress(request);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	// =====================================================
	// UPDATE ADDRESS
	// PUT /api/user/addresses/1
	// =====================================================

	@PutMapping("/{addressId}")
	public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long addressId,
			@Valid @RequestBody AddressRequest request) {

		AddressResponse response = addressService.updateAddress(addressId, request);

		return ResponseEntity.ok(response);
	}

	// =====================================================
	// DELETE ADDRESS
	// DELETE /api/user/addresses/1
	// =====================================================

	@DeleteMapping("/{addressId}")
	public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {

		addressService.deleteAddress(addressId);

		return ResponseEntity.noContent().build();
	}
}