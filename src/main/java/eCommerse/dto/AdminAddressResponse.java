package eCommerse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminAddressResponse {

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