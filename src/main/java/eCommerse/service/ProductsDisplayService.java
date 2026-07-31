package eCommerse.service;

import java.util.List;

import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;

public interface ProductsDisplayService {

	List<Product> getAllProducts();

	ProductImage getImageById(Long id);

}