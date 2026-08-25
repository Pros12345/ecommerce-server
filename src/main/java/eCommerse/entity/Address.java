package eCommerse.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// =====================================================
	// USER
	// =====================================================

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// =====================================================
	// ADDRESS DETAILS
	// =====================================================

	@Column(name = "full_name", nullable = false)
	private String fullName;

	@Column(name = "mobile_number", nullable = false)
	private String mobileNumber;

	@Column(name = "address_line1", nullable = false)
	private String addressLine1;

	@Column(name = "address_line2")
	private String addressLine2;

	@Column(nullable = false)
	private String city;

	@Column(nullable = false)
	private String state;

	@Column(nullable = false)
	private String pincode;

	private String landmark;

	@Column(name = "address_type")
	private String addressType;

	// =====================================================
	// AUDIT FIELDS
	// =====================================================

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	// =====================================================
	// CREATE
	// =====================================================

	@PrePersist
	protected void onCreate() {

		createdAt = LocalDateTime.now();

		updatedAt = LocalDateTime.now();
	}

	// =====================================================
	// UPDATE
	// =====================================================

	@PreUpdate
	protected void onUpdate() {

		updatedAt = LocalDateTime.now();
	}
}