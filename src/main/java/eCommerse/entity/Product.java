package eCommerse.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "description", length = 1000)
	private String description;

	private Integer quantity;

	@Column(name = "price")
	private double price;

	@Column(name = "status", nullable = false)
	private String status = "Active";

	// ==========================================
	// PRODUCT IMAGES
	// ==========================================
	//
	// Primary image will always come first.
	//
	// primaryImage DESC:
	// true → first
	// false → after
	//
	// id ASC:
	// maintains a stable order for the
	// remaining images.
	// ==========================================

	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ProductImage> images = new ArrayList<>();

}