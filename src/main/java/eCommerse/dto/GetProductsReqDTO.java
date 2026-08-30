package eCommerse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductsReqDTO {

	private String name;

	private String description;

	private Integer quantity;

	private double price;

	private String status;

	// ==========================================
	// ADD PRODUCT
	// ==========================================
	//
	// Index of the selected primary image.
	//
	// Example:
	//
	// images:
	// 0 = front.jpg
	// 1 = back.jpg
	// 2 = side.jpg
	//
	// primaryImageIndex = 2
	//
	// side.jpg becomes primary.
	// ==========================================

	private Integer primaryImageIndex;

	// ==========================================
	// EDIT PRODUCT
	// ==========================================
	//
	// Existing image ID selected as primary.
	// ==========================================

	private Long primaryImageId;

	// ==========================================
	// EDIT PRODUCT
	// ==========================================
	//
	// Index of newly uploaded image selected
	// as primary.
	// ==========================================

	private Integer primaryNewImageIndex;

}