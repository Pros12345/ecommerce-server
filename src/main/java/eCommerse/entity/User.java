package eCommerse.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Include
	private Long id;

	@NotBlank(message = "First name is required")
	@Column(name = "first_name")
	@ToString.Include
	private String firstName;

	@Email(message = "Invalid email address")
	@NotBlank(message = "Email is required")
	@Column(unique = true, nullable = false)
	@ToString.Include
	private String email;

	@NotBlank(message = "Country code is required")
	@Column(name = "country_code", nullable = false)
	@ToString.Include
	private String countryCode;

	@NotBlank(message = "Mobile number is required")
	@Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must contain exactly 10 digits")
	@Column(name = "mobile_number", unique = true, nullable = false)
	@ToString.Include
	private String mobileNumber;

	@NotBlank
	@Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Address> addresses = new ArrayList<>();
}