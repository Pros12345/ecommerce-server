package eCommerse.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.repository.ProductsDisplayRepository;
import eCommerse.service.ProductsDisplayService;

@Service
public class ProductsDisplayServiceImpl implements ProductsDisplayService {

	@Autowired
	private ProductsDisplayRepository repository;

	@Override
	public List<Product> getAllProducts() {

		return repository.getAllProducts();

	}

	@Override
	public ProductImage getImageById(Long id) {

		return repository.getImageById(id);

	}

}