package eCommerse.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

	private Long id;
	private String firstName;
	private String email;
	private String countryCode;
	private String mobileNumber;
}