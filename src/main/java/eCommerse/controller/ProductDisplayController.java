package eCommerse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.service.ProductsDisplayService;

@RestController
@RequestMapping("/api")
public class ProductDisplayController {

	@Autowired
	private ProductsDisplayService productsDisplayService;

	@GetMapping("/productsDisplay")
	public ResponseEntity<List<Product>> getAllProducts() {

		List<Product> products = productsDisplayService.getAllProducts();

		return ResponseEntity.ok(products);
	}

	@GetMapping("/images/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id) {

		ProductImage image = productsDisplayService.getImageById(id);

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getContentType()))
				.body(image.getImageData());
	}

}