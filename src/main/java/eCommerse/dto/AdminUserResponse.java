package eCommerse.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponse {

	private Long id;

	private String firstName;

	private String email;

	private String countryCode;

	private String mobileNumber;

	private List<AddressResponse> addresses;

	private List<OrderResponse> orders;
}