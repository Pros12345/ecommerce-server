package eCommerse.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.dto.GetProductsReqDTO;
import eCommerse.entity.Product;
import eCommerse.repository.impl.ProductsRepositoryImpl;
import eCommerse.service.ProductsService;

@Service
@Transactional
public class ProductsServiceImpl implements ProductsService {

	private static final Logger logger = LoggerFactory.getLogger(ProductsServiceImpl.class);

	@Autowired
	ProductsRepositoryImpl productsRepositoryImpl;

	@Override
	public Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile[] getProductsimages) {

		logger.info("ProductsServiceImpl : saveProduct :: Started");

		Product savedProduct = productsRepositoryImpl.saveProduct(getProductsReqDTO, getProductsimages);

		logger.info("ProductsServiceImpl : saveProduct :: Ended");

		return savedProduct;
	}

	@Override
	public void permanentlyDeleteProduct(Long productId) {

		logger.info("ProductsServiceImpl : permanentlyDeleteProduct :: Started");

		productsRepositoryImpl.permanentlyDeleteProduct(productId);

		logger.info("ProductsServiceImpl : permanentlyDeleteProduct :: Ended");
	}
}