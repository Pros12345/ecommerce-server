package eCommerse.repository;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import eCommerse.entity.Product;
import eCommerse.entity.ProductImage;
import eCommerse.request.GetProductsReqDTO;

public interface ProductsDisplayRepository {

	List<Product> getAllProducts();

	ProductImage getImageById(Long id);

	void deleteProduct(Long id);

	void updateProduct(Long id, GetProductsReqDTO product, MultipartFile[] newImages, List<Long> deletedImageIds)
			throws IOException;

	Product getProductById(Long id);
}