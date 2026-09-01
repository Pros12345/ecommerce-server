package eCommerse.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_image")
public class ProductImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// ==========================================
	// FILE NAME
	// ==========================================

	private String fileName;

	// ==========================================
	// CONTENT TYPE
	// ==========================================

	private String contentType;

	// ==========================================
	// IMAGE DATA
	// ==========================================

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "image_data")
	private byte[] imageData;

	// ==========================================
	// PRIMARY IMAGE
	// ==========================================
	//
	// true = primary image
	// false = normal image
	//
	// Only ONE image of a product should have
	// this value as true.
	// ==========================================

	@Column(name = "is_primary", nullable = false)
	private boolean primaryImage = false;

	// ==========================================
	// PRODUCT
	// ==========================================

	@ManyToOne
	@JoinColumn(name = "product_id")
	@JsonBackReference
	private Product product;

}