package eCommerse.repository;

import org.springframework.web.multipart.MultipartFile;

import eCommerse.dto.GetProductsReqDTO;
import eCommerse.entity.Product;

public interface ProductsRepository {

	Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile[] getProductsimages);

	void permanentlyDeleteProduct(Long productId);

	int reduceStock(Long productId, Integer quantity);

	Product findById(Long productId);
}