package eCommerse.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.request.GetProductsReqDTO;

public interface ProductsDisplayService {

	Product saveProduct(GetProductsReqDTO dto, MultipartFile imageFile);

	List<Product> getAllProducts();

}