package eCommerse.repository;

import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.request.GetProductsReqDTO;

public interface ProductsRepository {

	public Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile imageFile);

}
