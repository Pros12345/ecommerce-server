package eCommerse.repository.impl;

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

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public Product saveProduct(GetProductsReqDTO getProductsReqDTO, MultipartFile[] getProductsimages) {

		try {

			Product product = new Product();

			product.setName(getProductsReqDTO.getName());
			product.setDescription(getProductsReqDTO.getDescription());
			product.setQuantity(getProductsReqDTO.getQuantity());
			product.setPrice(getProductsReqDTO.getPrice());

			for (MultipartFile file : getProductsimages) {

				ProductImage productImage = new ProductImage();

				productImage.setFileName(file.getOriginalFilename());

				productImage.setContentType(file.getContentType());

				productImage.setImageData(file.getBytes());

				productImage.setProduct(product);

				product.getImages().add(productImage);

			}

			entityManager.persist(product);

			return product;

		} catch (Exception ex) {

			throw new RuntimeException(ex);

		}

	}

}