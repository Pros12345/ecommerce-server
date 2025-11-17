package eCommerse.service.impl;

import eCommerse.entity.Product;
import eCommerse.repository.impl.ProductsRepository;
import eCommerse.service.ProductsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductsServiceImpl implements ProductsService {

    private final ProductsRepository productsRepository;

    public ProductsServiceImpl(ProductsRepository repository) {
        this.productsRepository = repository;
    }

    @Override
    public Product createProduct(Product product) {
        return productsRepository.save(product);
    }

    @Override
    public Product getProductById(int id) {
        return productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    @Override
    public Product updateProduct(int id, Product product) {
        Product existing = getProductById(id);
        existing.setProductName(product.getProductName());
        existing.setProductDescription(product.getProductDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        return productsRepository.save(existing);
    }

    @Override
    public void deleteProduct(int id) {
        productsRepository.deleteById(id);
    }
}
