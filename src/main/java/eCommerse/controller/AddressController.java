package eCommerse.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(AddressController.class);

	private final AddressService addressService;

	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	@GetMapping
	public ResponseEntity<List<AddressResponse>> getMyAddresses() {

		logger.info("AddressController : getMyAddresses :: Started");

		List<AddressResponse> addresses = addressService.getMyAddresses();

		logger.info("AddressController : getMyAddresses :: Ended");

		return ResponseEntity.ok(addresses);
	}

	@GetMapping("/{addressId}")
	public ResponseEntity<AddressResponse> getAddressById(@PathVariable Long addressId) {

		logger.info("AddressController : getAddressById :: Started");

		AddressResponse response = addressService.getAddressById(addressId);

		logger.info("AddressController : getAddressById :: Ended");

		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<AddressResponse> addAddress(@Valid @RequestBody AddressRequest request) {

		logger.info("AddressController : addAddress :: Started");

		AddressResponse response = addressService.addAddress(request);

		logger.info("AddressController : addAddress :: Ended");

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{addressId}")
	public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long addressId,
			@Valid @RequestBody AddressRequest request) {

		logger.info("AddressController : updateAddress :: Started");

		AddressResponse response = addressService.updateAddress(addressId, request);

		logger.info("AddressController : updateAddress :: Ended");

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{addressId}")
	public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {

		logger.info("AddressController : deleteAddress :: Started");

		addressService.deleteAddress(addressId);

		logger.info("AddressController : deleteAddress :: Ended");

		return ResponseEntity.noContent().build();
	}
}