package eCommerse.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.request.GetProductsReqDTO;
import eCommerse.service.ProductsDisplayService;

@RestController
@RequestMapping("/api")
public class ProductDisplayController {

	private static final Logger logger = LoggerFactory.getLogger(ProductDisplayController.class);
	@Autowired
	private ProductsDisplayService productsDisplayService;

	@GetMapping("/productsDisplay")
	public ResponseEntity<List<Product>> getAllProducts() {

		logger.info("ProductDisplayController : getAllProducts :: Started");
		List<Product> products = productsDisplayService.getAllProducts();
		logger.info("ProductDisplayController : getAllProducts :: Ended");

		return ResponseEntity.ok(products);
	}

	@GetMapping("/images/{id}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long id) {

		logger.info("ProductDisplayController : getImage :: Started");

		ProductImage image = productsDisplayService.getImageById(id);
		logger.info("ProductDisplayController : getImage :: Ended");

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getContentType()))
				.body(image.getImageData());
	}

	@DeleteMapping("/products/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Long id) {

		logger.info("ProductDisplayController : deleteProduct :: Started");

		productsDisplayService.deleteProduct(id);

		logger.info("ProductDisplayController : deleteProduct :: Ended");

		return ResponseEntity.ok("Product deleted successfully");
	}

	@PutMapping(value = "/products/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> updateProduct(

			@PathVariable Long id,

			@RequestPart("product") GetProductsReqDTO product,

			@RequestPart(value = "newImages", required = false) MultipartFile[] newImages,

			@RequestPart(value = "deletedImageIds", required = false) List<Long> deletedImageIds

	) throws IOException {

		logger.info("ProductController : updateProduct :: Started");

		productsDisplayService.updateProduct(id, product, newImages, deletedImageIds);

		logger.info("ProductDisplayController : updateProduct :: Ended");

		return ResponseEntity.ok("Product Updated Successfully");
	}

	@GetMapping("/products/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {

	    logger.info("ProductDisplayController : getProductById :: Started");

	    Product product = productsDisplayService.getProductById(id);

	    logger.info("ProductDisplayController : getProductById :: Ended");

	    return ResponseEntity.ok(product);
	}
}