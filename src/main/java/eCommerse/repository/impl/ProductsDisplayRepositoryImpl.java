package eCommerse.repository.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.dto.GetProductsReqDTO;
import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.repository.ProductsDisplayRepository;
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

		String jpql = """
				SELECT DISTINCT p
				FROM Product p
				LEFT JOIN FETCH p.images
				WHERE p.quantity > 0
				ORDER BY p.id DESC
				""";

		logger.info("ProductsDisplayRepositoryImpl : getAllProducts :: Ended");

		return entityManager.createQuery(jpql, Product.class).getResultList();

	}

	@Override
	public ProductImage getImageById(Long id) {

		logger.info("ProductsDisplayRepositoryImpl : deleteProduct :: Started");

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
		logger.info("Updating product ID: {}", id);

		Product product = entityManager.find(Product.class, id);
		if (product == null) {

			throw new RuntimeException("Product not found");
		}

		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setPrice(dto.getPrice());
		product.setQuantity(dto.getQuantity());
		product.setStatus("Inactive".equalsIgnoreCase(dto.getStatus()) ? "Inactive" : "Active");

		if (deletedImageIds != null && !deletedImageIds.isEmpty()) {
			for (Long imageId : deletedImageIds) {
				ProductImage image = entityManager.find(ProductImage.class, imageId);
				if (image != null && image.getProduct() != null && image.getProduct().getId().equals(id)) {
					entityManager.remove(image);
				}
			}
		}

		entityManager.flush();
		entityManager.refresh(product);

		if (newImages != null && newImages.length > 0) {

			for (MultipartFile file : newImages) {

				if (file == null || file.isEmpty()) {

					continue;

				}

				boolean exists = product.getImages().stream().anyMatch(img -> img.getFileName() != null
						&& img.getFileName().equalsIgnoreCase(file.getOriginalFilename())

				);

				if (exists) {
					continue;

				}

				ProductImage image = new ProductImage();
				image.setProduct(product);
				image.setFileName(file.getOriginalFilename());
				image.setImageData(file.getBytes());
				image.setContentType(file.getContentType());
				image.setPrimaryImage(false);
				entityManager.persist(image);
				product.getImages().add(image);

			}

		}

		entityManager.flush();
		for (ProductImage image : product.getImages()) {

			image.setPrimaryImage(false);

		}

		Long primaryImageId = dto.getPrimaryImageId();
		Integer primaryNewImageIndex = dto.getPrimaryNewImageIndex();
		boolean primarySelected = false;

		if (primaryImageId != null) {

			for (ProductImage image : product.getImages()) {

				if (image.getId() != null && image.getId().equals(primaryImageId)) {
					image.setPrimaryImage(true);
					primarySelected = true;
					break;

				}

			}

		}

		if (!primarySelected && primaryNewImageIndex != null && primaryNewImageIndex >= 0) {

			if (newImages != null && primaryNewImageIndex < newImages.length) {
				MultipartFile selectedFile = newImages[primaryNewImageIndex];
				String selectedFileName = selectedFile.getOriginalFilename();

				for (int i = product.getImages().size() - 1; i >= 0; i--) {
					ProductImage image = product.getImages().get(i);

					if (image.getFileName() != null && image.getFileName().equalsIgnoreCase(selectedFileName)) {
						image.setPrimaryImage(true);
						primarySelected = true;
						break;

					}

				}

			}

		}

		if (!primarySelected && !product.getImages().isEmpty()) {
			product.getImages().get(0).setPrimaryImage(true);
		}

		entityManager.flush();

		logger.info("Product updated successfully. ID: {}", id);
		logger.info("ProductsDisplayRepositoryImpl : updateProduct :: Ended");

	}

	@Override
	public Product getProductById(Long id) {

		logger.info("ProductsDisplayRepositoryImpl : getProductById :: Started");

		String jpql = """
				SELECT DISTINCT p
				FROM Product p
				LEFT JOIN FETCH p.images
				WHERE p.id = :id
				""";

		List<Product> products = entityManager.createQuery(jpql, Product.class).setParameter("id", id).getResultList();

		if (products.isEmpty()) {

			throw new RuntimeException("Product not found");

		}

		logger.info("ProductsDisplayRepositoryImpl : getProductById :: Ended");

		return products.get(0);

	}

}