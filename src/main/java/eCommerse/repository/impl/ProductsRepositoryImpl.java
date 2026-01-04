package eCommerse.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.repository.ProductsRepository;
import eCommerse.request.GetProductsReqDTO;

@Repository
public class ProductsRepositoryImpl implements ProductsRepository {

	@Override
	public Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile imageFile) {

		return null;
	}

}
