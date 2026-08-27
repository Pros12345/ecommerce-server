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

	// ==========================================
	// GET ALL PRODUCTS
	// ==========================================

	@Override
	public List<Product> getAllProducts() {

		String jpql = """
				SELECT DISTINCT p
				FROM Product p
				LEFT JOIN FETCH p.images
				WHERE p.quantity > 0
				ORDER BY p.id DESC
				""";

		return entityManager.createQuery(jpql, Product.class).getResultList();

	}

	// ==========================================
	// GET IMAGE
	// ==========================================

	@Override
	public ProductImage getImageById(Long id) {

		return entityManager.find(ProductImage.class, id);

	}

	// ==========================================
	// DELETE PRODUCT
	// ==========================================

	@Override
	@Transactional
	public void deleteProduct(Long id) {

		Product product = entityManager.find(Product.class, id);

		if (product == null) {

			throw new RuntimeException("Product not found");

		}

		product.setStatus("Inactive");

		entityManager.merge(product);

	}

	// ==========================================
	// UPDATE PRODUCT
	// ==========================================

	@Override
	@Transactional
	public void updateProduct(

			Long id,

			GetProductsReqDTO dto,

			MultipartFile[] newImages,

			List<Long> deletedImageIds

	) throws IOException {

		logger.info("Updating product ID: {}", id);

		// ======================================
		// FIND PRODUCT
		// ======================================

		Product product = entityManager.find(Product.class, id);

		if (product == null) {

			throw new RuntimeException("Product not found");

		}

		// ======================================
		// PRODUCT DETAILS
		// ======================================

		product.setName(dto.getName());

		product.setDescription(dto.getDescription());

		product.setPrice(dto.getPrice());

		product.setQuantity(dto.getQuantity());

		product.setStatus("Inactive".equalsIgnoreCase(dto.getStatus()) ? "Inactive" : "Active");

		// ======================================
		// DELETE SELECTED IMAGES
		// ======================================

		if (deletedImageIds != null && !deletedImageIds.isEmpty()) {

			for (Long imageId : deletedImageIds) {

				ProductImage image = entityManager.find(ProductImage.class, imageId);

				if (image != null && image.getProduct() != null && image.getProduct().getId().equals(id)) {

					entityManager.remove(image);

				}

			}

		}

		entityManager.flush();

		// ======================================
		// REFRESH IMAGE LIST
		// ======================================

		entityManager.refresh(product);

		// ======================================
		// NEW IMAGES
		// ======================================

		if (newImages != null && newImages.length > 0) {

			for (MultipartFile file : newImages) {

				if (file == null || file.isEmpty()) {

					continue;

				}

				// --------------------------------
				// PREVENT DUPLICATE FILE NAME
				// --------------------------------

				boolean exists = product.getImages().stream().anyMatch(

						img -> img.getFileName() != null
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

				/*
				 * Add it to the product collection so it is available below.
				 */

				product.getImages().add(image);

			}

		}

		entityManager.flush();

		// ======================================
		// RESET PRIMARY IMAGE
		// ======================================

		for (ProductImage image : product.getImages()) {

			image.setPrimaryImage(false);

		}

		// ======================================
		// SELECT PRIMARY IMAGE
		// ======================================

		Long primaryImageId = dto.getPrimaryImageId();

		Integer primaryNewImageIndex = dto.getPrimaryNewImageIndex();

		boolean primarySelected = false;

		// ======================================
		// OPTION 1
		// EXISTING IMAGE
		// ======================================

		if (primaryImageId != null) {

			for (ProductImage image : product.getImages()) {

				if (image.getId() != null && image.getId().equals(primaryImageId)) {

					image.setPrimaryImage(true);

					primarySelected = true;

					break;

				}

			}

		}

		// ======================================
		// OPTION 2
		// NEW IMAGE
		// ======================================

		if (!primarySelected && primaryNewImageIndex != null && primaryNewImageIndex >= 0) {

			/*
			 * The Angular index refers only to the uploaded newImages array.
			 *
			 * Find the corresponding newly persisted image by comparing file order/name.
			 */

			if (newImages != null && primaryNewImageIndex < newImages.length) {

				MultipartFile selectedFile = newImages[primaryNewImageIndex];

				String selectedFileName = selectedFile.getOriginalFilename();

				/*
				 * Search from the end because newly uploaded images are added after existing
				 * images.
				 */

				for (int i = product.getImages().size() - 1;

						i >= 0;

						i--) {

					ProductImage image = product.getImages().get(i);

					if (image.getFileName() != null && image.getFileName().equalsIgnoreCase(selectedFileName)) {

						image.setPrimaryImage(true);

						primarySelected = true;

						break;

					}

				}

			}

		}

		// ======================================
		// FALLBACK PRIMARY IMAGE
		// ======================================

		if (!primarySelected && !product.getImages().isEmpty()) {

			product.getImages().get(0).setPrimaryImage(true);

		}

		entityManager.flush();

		logger.info("Product updated successfully. ID: {}", id);

	}

	// ==========================================
	// GET PRODUCT BY ID
	// ==========================================

	@Override
	public Product getProductById(Long id) {

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

		return products.get(0);

	}

}