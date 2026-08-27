package eCommerse.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.dto.GetProductsReqDTO;
import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.repository.ProductsRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class ProductsRepositoryImpl implements ProductsRepository {

	private static final Logger logger = LoggerFactory.getLogger(ProductsRepositoryImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	// ==========================================
	// SAVE PRODUCT
	// ==========================================

	@Override
	public Product saveProduct(

			GetProductsReqDTO dto,

			MultipartFile[] images

	) {

		logger.info("ProductsRepositoryImpl : saveProduct :: Started");

		Product product = new Product();

		try {

			// ----------------------------------
			// PRODUCT DETAILS
			// ----------------------------------

			product.setName(dto.getName());

			product.setDescription(dto.getDescription());

			product.setQuantity(dto.getQuantity());

			product.setPrice(dto.getPrice());

			product.setStatus("Active");

			// ----------------------------------
			// PRIMARY IMAGE INDEX
			// ----------------------------------

			Integer primaryIndex = dto.getPrimaryImageIndex();

			/*
			 * If admin doesn't select anything, first image becomes primary.
			 */

			if (primaryIndex == null || primaryIndex < 0 || images == null || primaryIndex >= images.length) {

				primaryIndex = 0;

			}

			// ----------------------------------
			// SAVE IMAGES
			// ----------------------------------

			if (images != null && images.length > 0) {

				for (int i = 0; i < images.length; i++) {

					MultipartFile file = images[i];

					if (file == null || file.isEmpty()) {

						continue;

					}

					ProductImage productImage = new ProductImage();

					productImage.setFileName(file.getOriginalFilename());

					productImage.setContentType(file.getContentType());

					productImage.setImageData(file.getBytes());

					productImage.setProduct(product);

					// ----------------------------------
					// SET PRIMARY
					// ----------------------------------

					productImage.setPrimaryImage(i == primaryIndex);

					product.getImages().add(productImage);

				}

			}

			// ----------------------------------
			// SAVE PRODUCT
			// ----------------------------------

			entityManager.persist(product);

			entityManager.flush();

			logger.info("Product saved successfully. ID: {}", product.getId());

			return product;

		}

		catch (Exception ex) {

			logger.error("ProductsRepositoryImpl : saveProduct :: Error", ex);

			throw new RuntimeException("Unable to save product", ex);

		}

	}

	// ==========================================
	// PERMANENT DELETE
	// ==========================================

	@Override
	public void permanentlyDeleteProduct(Long productId) {

		logger.info("ProductsRepositoryImpl : permanentlyDeleteProduct :: Started");

		Product product = entityManager.find(Product.class, productId);

		if (product == null) {

			throw new RuntimeException("Product not found with id: " + productId);

		}

		entityManager.remove(product);

		entityManager.flush();

		logger.info("Product permanently deleted. ID: {}", productId);

	}

	// ==========================================
	// REDUCE STOCK
	// ==========================================

	@Override
	public int reduceStock(

			Long productId,

			Integer quantity

	) {

		String jpql = """
				UPDATE Product p
				SET p.quantity =
				    p.quantity - :quantity
				WHERE p.id = :productId
				AND p.quantity >= :quantity
				""";

		return entityManager.createQuery(jpql).setParameter("quantity", quantity).setParameter("productId", productId)
				.executeUpdate();

	}

	// ==========================================
	// FIND PRODUCT
	// ==========================================

	@Override
	public Product findById(Long productId) {

		return entityManager.find(Product.class, productId);

	}

}