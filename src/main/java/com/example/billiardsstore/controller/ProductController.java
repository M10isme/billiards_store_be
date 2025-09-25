package com.example.billiardsstore.controller;

import com.example.billiardsstore.dto.ProductDTO;
import com.example.billiardsstore.dto.ProductResponseDTO;
import com.example.billiardsstore.model.Product;
import com.example.billiardsstore.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable("id") Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Admin endpoints for product management
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductResponseDTO savedProduct = productService.createProduct(productDTO);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable("id") Long id, @RequestBody ProductDTO productDTO) {
        ProductResponseDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Sản phẩm đã được xóa (soft delete). Dữ liệu lịch sử đơn hàng được bảo toàn.");
    }

    // Admin endpoint to restore deleted product
    @PostMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> restoreProduct(@PathVariable("id") Long id) {
        ProductResponseDTO restoredProduct = productService.restoreProduct(id);
        return ResponseEntity.ok(restoredProduct);
    }

    // Admin endpoint to get all products (including deleted)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductResponseDTO> getAllProductsForAdmin() {
        return productService.getAllProductsForAdmin();
    }

    // Admin endpoint to get deleted products
    @GetMapping("/admin/deleted")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductResponseDTO> getDeletedProducts() {
        return productService.getDeletedProducts();
    }

    // Permanent delete (chỉ dùng khi thực sự cần thiết)
    @DeleteMapping("/{id}/permanent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> permanentlyDeleteProduct(@PathVariable("id") Long id) {
        productService.permanentlyDeleteProduct(id);
        return ResponseEntity.ok("Sản phẩm đã được xóa vĩnh viễn khỏi hệ thống.");
    }

    @GetMapping("/flash-sales")
    public List<Product> getFlashSales() {
        return productService.findByCategory("flash-sales");
    }

    @GetMapping("/best-selling")
    public List<Product> getBestSelling() {
        return productService.findByCategory("best-selling");
    }

    @GetMapping("/new-arrivals")
    public List<Product> getNewArrivals() {
        return productService.findByCategory("new-arrivals");
    }

    @GetMapping("/explore")
    public List<Product> getExplore() {
        return productService.findByCategory("explore");
    }

    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam("q") String query) {
        return productService.search(query);
    }
}

