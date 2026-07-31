package eCommerse.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.repository.ProductsDisplayRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional(readOnly = true)
public class ProductsDisplayRepositoryImpl implements ProductsDisplayRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Product> getAllProducts() {

		String jpql = "SELECT p FROM Product p";

		return entityManager.createQuery(jpql, Product.class).getResultList();
	}

	@Override
	public ProductImage getImageById(Long id) {

		return entityManager.find(ProductImage.class, id);

	}

}