package eCommerse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressResponse {

	private Long id;

	private String fullName;

	private String mobileNumber;

	private String addressLine1;

	private String addressLine2;

	private String city;

	private String state;

	private String pincode;

	private String landmark;

	private String addressType;
}