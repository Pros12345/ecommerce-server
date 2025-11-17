package eCommerse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class User {

	@Id // Correct import from jakarta.persistence
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ToString.Include
	private Long id;

	@NotBlank
	@Column(name = "first_name")
	@ToString.Include
	private String firstName;

	@Email
	@NotBlank
	@Column(unique = true)
	@ToString.Include
	private String email;

	@NotBlank
	@Size(min = 8, message = "Password must be at least 8 characters long")
	private String password;

    @OneToMany
    private Order order;

}
