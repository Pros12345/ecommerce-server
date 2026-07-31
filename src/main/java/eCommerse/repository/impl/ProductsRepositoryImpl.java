package eCommerse.repository.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.repository.ProductsRepository;
import eCommerse.request.GetProductsReqDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class ProductsRepositoryImpl implements ProductsRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile imageFile) {

		Product product = new Product();

		product.setName(getProductsReqDTO.getName());
		product.setDescription(getProductsReqDTO.getDescription());
		product.setQuantity(getProductsReqDTO.getQuantity());

		String originalName = imageFile.getOriginalFilename();
		String uniqueName = UUID.randomUUID() + "_" + originalName;

		product.setImageOriginalName(originalName);
		product.setImageUniqueName(uniqueName);

		product.setPrice(getProductsReqDTO.getPrice());

		entityManager.persist(product);

		return product;
	}
}
