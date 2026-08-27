package eCommerse.service.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.dto.GetProductsReqDTO;
import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.repository.ProductsDisplayRepository;
import eCommerse.service.ProductsDisplayService;

@Service
public class ProductsDisplayServiceImpl implements ProductsDisplayService {

	private static final Logger logger = LoggerFactory.getLogger(ProductsDisplayServiceImpl.class);

	@Autowired
	private ProductsDisplayRepository productsDisplayRepository;

	@Override
	public List<Product> getAllProducts() {

		logger.info("ProductsDisplayServiceImpl : getAllProducts :: Started");
		return productsDisplayRepository.getAllProducts();

	}

	@Override
	public ProductImage getImageById(Long id) {

		logger.info("ProductsDisplayServiceImpl : getImageById :: Started");
		return productsDisplayRepository.getImageById(id);

	}

	@Override
	public void deleteProduct(Long id) {

		logger.info("ProductsDisplayServiceImpl : deleteProduct :: Started");

		productsDisplayRepository.deleteProduct(id);

		logger.info("ProductsDisplayServiceImpl : deleteProduct :: Ended");
	}

	@Override
	public void updateProduct(Long id, GetProductsReqDTO product, MultipartFile[] newImages, List<Long> deletedImageIds)
			throws IOException {

		logger.info("ProductsDisplayServiceImpl : updateProduct :: Started");

		productsDisplayRepository.updateProduct(id, product, newImages, deletedImageIds);

		logger.info("ProductsDisplayServiceImpl : updateProduct :: Ended");
	}

	@Override
	public Product getProductById(Long id) {

		logger.info("ProductsDisplayServiceImpl : getProductById :: Started");

		return productsDisplayRepository.getProductById(id);
	}
}