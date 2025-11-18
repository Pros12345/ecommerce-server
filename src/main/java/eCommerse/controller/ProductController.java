//package eCommerse.controller;
//
//import eCommerse.entity.Product;
//import eCommerse.service.impl.ProductsServiceImpl;
//import org.springframework.web.bind.annotation.*;
//import java.util.List;
//
//@RestController
//@RequestMapping("/products")
//public class ProductController {
//
//    private final ProductsServiceImpl service;
//
//    public ProductController(ProductsServiceImpl service) {
//        this.service = service;
//    }
//
//    @PostMapping
//    public Product create(@RequestBody Product product) {
//        return service.createProduct(product);
//    }
//
//    @GetMapping("/getProducts/{id}")
//    public Product getOne(@PathVariable int id) {
//        return service.getProductById(id);
//    }
//
//    @GetMapping("/getProducts")
//    public List<Product> getAll() {
//        return service.getAllProducts();
//    }
//
//    @PutMapping("/updateProducts/{id}")
//    public Product update(@PathVariable int id, @RequestBody Product product) {
//        return service.updateProduct(id, product);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public String delete(@PathVariable int id) {
//        service.deleteProduct(id);
//        return "Product deleted successfully";
//    }
//}
//
