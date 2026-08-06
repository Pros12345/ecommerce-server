package eCommerse.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.request.GetProductsReqDTO;
import eCommerse.service.ProductsService;

@RestController
@RequestMapping("/api")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
	@Autowired
	ProductsService productsService;

	@PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> addProduct(@RequestPart("product") GetProductsReqDTO getProductsReqDTO,
			@RequestPart("images") MultipartFile[] getProductsimages) throws IOException {

		logger.info("ProductController : addProduct :: Started");
		productsService.saveProduct(getProductsReqDTO, getProductsimages);
		logger.info("ProductController : addProduct :: Ended");
		return ResponseEntity.ok("Product saved successfully");
	}

}