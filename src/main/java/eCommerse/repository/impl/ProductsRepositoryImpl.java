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

	@Override
	public Product saveProduct(GetProductsReqDTO dto, MultipartFile[] images) {

		logger.info("ProductsRepositoryImpl : saveProduct :: Started");

		Product product = new Product();

		try {

			product.setName(dto.getName());
			product.setDescription(dto.getDescription());
			product.setQuantity(dto.getQuantity());
			product.setPrice(dto.getPrice());
			product.setStatus("Active");
			Integer primaryIndex = dto.getPrimaryImageIndex();

			if (primaryIndex == null || primaryIndex < 0 || images == null || primaryIndex >= images.length) {
				primaryIndex = 0;
			}

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
					productImage.setPrimaryImage(i == primaryIndex);
					product.getImages().add(productImage);
				}
			}

			entityManager.persist(product);
			entityManager.flush();

			logger.info("Product saved successfully. ID: {}", product.getId());
			logger.info("ProductsRepositoryImpl : saveProduct :: Ended");

			return product;

		}

		catch (Exception ex) {

			logger.error("ProductsRepositoryImpl : saveProduct :: Error", ex);
			throw new RuntimeException("Unable to save product", ex);
		}
	}

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
		logger.info("ProductsRepositoryImpl : permanentlyDeleteProduct :: Ended");

	}

	@Override
	public int reduceStock(Long productId, Integer quantity) {

		logger.info("ProductsRepositoryImpl : reduceStock :: Started");

		String jpql = """
				UPDATE Product p
				SET p.quantity =
				    p.quantity - :quantity
				WHERE p.id = :productId
				AND p.quantity >= :quantity
				""";

		logger.info("ProductsRepositoryImpl : reduceStock :: Ended");

		return entityManager.createQuery(jpql).setParameter("quantity", quantity).setParameter("productId", productId)
				.executeUpdate();

	}

	@Override
	public int increaseStock(Long productId, Integer quantity) {

		logger.info("ProductsRepositoryImpl : increaseStock :: Started");

		String jpql = """
				UPDATE Product p
				SET p.quantity = p.quantity + :quantity
				WHERE p.id = :productId
				""";

		logger.info("ProductsRepositoryImpl : increaseStock :: Ended");

		return entityManager.createQuery(jpql).setParameter("quantity", quantity).setParameter("productId", productId)
				.executeUpdate();
	}

	@Override
	public Product findById(Long productId) {

		logger.info("ProductsRepositoryImpl : findById :: Started");

		return entityManager.find(Product.class, productId);

	}

}