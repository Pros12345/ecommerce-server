package eCommerse.service;

import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.request.GetProductsReqDTO;

public interface ProductsService {

	Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile[] getProductsimages);

	void permanentlyDeleteProduct(Long productId);
}