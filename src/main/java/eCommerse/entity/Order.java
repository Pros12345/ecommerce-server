package eCommerse.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ==========================================
	// USER
	// ==========================================

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	// ==========================================
	// ADDRESS
	// ==========================================

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;

	// ==========================================
	// TOTAL
	// ==========================================

	@Column(nullable = false)
	private BigDecimal totalAmount;

	// ==========================================
	// PAYMENT
	// ==========================================

	@Column(nullable = false)
	private String paymentMethod;

	// ==========================================
	// ORDER STATUS
	// ==========================================

	@Column(nullable = false)
	private String orderStatus;

	// ==========================================
	// ORDER DATE
	// ==========================================

	@Column(nullable = false)
	private LocalDateTime orderDate;

	// ==========================================
	// ORDER ITEMS
	// ==========================================

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

}