package eCommerse.repository.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.repository.ProductsDisplayRepository;
import eCommerse.request.GetProductsReqDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional(readOnly = true)
public class ProductsDisplayRepositoryImpl implements ProductsDisplayRepository {

	private static final Logger logger = LoggerFactory.getLogger(ProductsDisplayRepositoryImpl.class);
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Product> getAllProducts() {

		logger.info("ProductsDisplayRepositoryImpl : getAllProducts :: Started");

		String jpql = null;
		try {
			jpql = "SELECT p FROM Product p WHERE p.quantity > 0";
		} catch (Exception e) {
			logger.error("ProductsDisplayRepositoryImpl : getAllProducts :: error" + e.getMessage());
		}

		logger.info("ProductsDisplayRepositoryImpl : getAllProducts :: Ended");

		return entityManager.createQuery(jpql, Product.class).getResultList();
	}

	@Override
	public ProductImage getImageById(Long id) {

		logger.info("ProductsDisplayRepositoryImpl : getImageById :: Started");
		return entityManager.find(ProductImage.class, id);

	}

	@Override
	@Transactional
	public void deleteProduct(Long id) {

		logger.info("ProductsDisplayRepositoryImpl : deleteProduct :: Started");

		Product product = entityManager.find(Product.class, id);

		if (product == null) {
			throw new RuntimeException("Product not found");
		}

		product.setStatus("Inactive");

		entityManager.merge(product);

		logger.info("ProductsDisplayRepositoryImpl : deleteProduct :: Ended");
	}

	@Override
	@Transactional
	public void updateProduct(Long id, GetProductsReqDTO dto, MultipartFile[] newImages, List<Long> deletedImageIds)
			throws IOException {

		logger.info("ProductsDisplayRepositoryImpl : updateProduct :: Started");

		Product product = entityManager.find(Product.class, id);

		if (product == null) {
			throw new RuntimeException("Product Not Found");
		}

		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setQuantity(dto.getQuantity());
		product.setStatus("Inactive".equalsIgnoreCase(dto.getStatus()) ? "Inactive" : "Active");
		entityManager.merge(product);

		if (deletedImageIds != null) {
			for (Long imageId : deletedImageIds) {
				ProductImage image = entityManager.find(ProductImage.class, imageId);
				if (image != null) {
					entityManager.remove(image);
				}
			}
		}

		if (newImages != null) {

			for (MultipartFile file : newImages) {

				boolean exists = product.getImages().stream().anyMatch(img -> img.getFileName() != null
						&& img.getFileName().equalsIgnoreCase(file.getOriginalFilename()));

				if (exists) {
					continue;
				}

				ProductImage image = new ProductImage();
				image.setProduct(product);
				image.setFileName(file.getOriginalFilename());
				image.setImageData(file.getBytes());
				image.setContentType(file.getContentType());

				entityManager.persist(image);
			}
		}
		logger.info("ProductsDisplayRepositoryImpl : updateProduct :: Ended");
	}

	@Override
	public Product getProductById(Long id) {
		logger.info("ProductsDisplayRepositoryImpl : getProductById :: Started");
		return entityManager.find(Product.class, id);
	}
}