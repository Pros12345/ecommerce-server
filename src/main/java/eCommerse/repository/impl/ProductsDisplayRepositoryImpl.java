package eCommerse.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.repository.ProductsDisplayRepository;
import eCommerse.request.GetProductsReqDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class ProductsDisplayRepositoryImpl implements ProductsDisplayRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Product> getAllProducts() {

		String jpql = "SELECT p FROM Product p";

		return entityManager.createQuery(jpql, Product.class).getResultList();
	}

	@Override
	public Product saveProduct(GetProductsReqDTO dto, MultipartFile imageFile) {
		return null;
	}

}