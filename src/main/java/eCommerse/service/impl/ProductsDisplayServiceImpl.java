package eCommerse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.repository.ProductsDisplayRepository;
import eCommerse.request.GetProductsReqDTO;
import eCommerse.service.ProductsDisplayService;

@Service
public class ProductsDisplayServiceImpl implements ProductsDisplayService {

	@Autowired
	private ProductsDisplayRepository productsDisplayRepository;

	@Override
	public Product saveProduct(GetProductsReqDTO dto, MultipartFile imageFile) {

		return productsDisplayRepository.saveProduct(dto, imageFile);
	}

	@Override
	public List<Product> getAllProducts() {

		return productsDisplayRepository.getAllProducts();
	}

}