package eCommerse.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.repository.impl.ProductsRepositoryImpl;
import eCommerse.request.GetProductsReqDTO;
import eCommerse.service.ProductsService;

@Service
@Transactional
public class ProductsServiceImpl implements ProductsService {

	@Autowired
	ProductsRepositoryImpl productsRepositoryImpl;

	@Override
	public Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile[] getProductsimages) {

		Product savedProduct = productsRepositoryImpl.saveProduct(getProductsReqDTO, getProductsimages);
		return savedProduct;
	}
}
