package eCommerse.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {

	@NotBlank(message = "First name is required")
	private String firstName;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email address")
	private String email;

	@NotBlank(message = "Country code is required")
	private String countryCode;

	@NotBlank(message = "Mobile number is required")
	private String mobileNumber;
}