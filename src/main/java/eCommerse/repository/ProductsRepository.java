package eCommerse.repository;

import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.request.GetProductsReqDTO;

public interface ProductsRepository {

	Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile[] getProductsimages);

	void permanentlyDeleteProduct(Long productId);

	int reduceStock(Long productId, Integer quantity);

	Product findById(Long productId);
}