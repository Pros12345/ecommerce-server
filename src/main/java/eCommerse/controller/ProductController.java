package eCommerse.controller;

import java.io.IOException;

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

	@Autowired
	ProductsService productsService;

	@PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> addProduct(@RequestPart("product") GetProductsReqDTO getProductsReqDTO,
			@RequestPart("images") MultipartFile[] getProductsimages) throws IOException {

		productsService.saveProduct(getProductsReqDTO, getProductsimages);

		return ResponseEntity.ok("Product saved successfully");
	}

}