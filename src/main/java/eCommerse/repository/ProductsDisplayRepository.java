package eCommerse.repository;

import java.util.List;

import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;

public interface ProductsDisplayRepository {

	List<Product> getAllProducts();

	ProductImage getImageById(Long id);

}