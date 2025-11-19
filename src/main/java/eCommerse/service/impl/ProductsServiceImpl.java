package eCommerse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.repository.impl.ProductsRepositoryImpl;
import eCommerse.request.GetProductsReqDTO;
import eCommerse.service.ProductsService;

@Service
public class ProductsServiceImpl implements ProductsService {

	@Autowired
	ProductsRepositoryImpl productsRepositoryImpl;

	@Override
	public Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile imageFile) {

		Product savedProduct = productsRepositoryImpl.saveProduct(getProductsReqDTO, imageFile);
		return savedProduct;
	}
}
