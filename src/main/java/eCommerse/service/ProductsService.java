package eCommerse.service;

import org.springframework.web.multipart.MultipartFile;

import eCommerse.dto.GetProductsReqDTO;
import eCommerse.entity.Product;

public interface ProductsService {

	Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile[] getProductsimages);

	void permanentlyDeleteProduct(Long productId);
}