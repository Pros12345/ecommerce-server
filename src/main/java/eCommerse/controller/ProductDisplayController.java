package eCommerse.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eCommerse.entity.Product;
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

}