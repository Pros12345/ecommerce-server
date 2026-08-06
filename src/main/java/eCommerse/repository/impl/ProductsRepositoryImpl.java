package eCommerse.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.repository.ProductsRepository;
import eCommerse.request.GetProductsReqDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
@Transactional
public class ProductsRepositoryImpl implements ProductsRepository {

	private static final Logger logger = LoggerFactory.getLogger(ProductsRepositoryImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile[] getProductsimages) {

		logger.info("ProductsRepositoryImpl : saveProduct :: Started");

		Product product = new Product();
		try {
			product.setName(getProductsReqDTO.getName());
			product.setDescription(getProductsReqDTO.getDescription());
			product.setQuantity(getProductsReqDTO.getQuantity());
			product.setPrice(getProductsReqDTO.getPrice());
			product.setStatus("Active");

			for (MultipartFile file : getProductsimages) {
				ProductImage productImage = new ProductImage();
				productImage.setFileName(file.getOriginalFilename());
				productImage.setContentType(file.getContentType());
				productImage.setImageData(file.getBytes());
				productImage.setProduct(product);
				product.getImages().add(productImage);
			}

			entityManager.persist(product);
			logger.info("ProductsRepositoryImpl : saveProduct :: Started");

		} catch (Exception ex) {

			logger.error("ProductsRepositoryImpl : saveProduct :: error " + ex.getMessage());
		}
		return product;
	}
}