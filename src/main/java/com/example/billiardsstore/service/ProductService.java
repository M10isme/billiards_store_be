package com.example.billiardsstore.service;

import com.example.billiardsstore.dto.ProductDTO;
import com.example.billiardsstore.dto.ProductResponseDTO;
import com.example.billiardsstore.model.Product;
import com.example.billiardsstore.model.Supplier;
import com.example.billiardsstore.repository.ProductRepository;
import com.example.billiardsstore.repository.SupplierRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public ProductService(ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAllOrderByUpdatedAtDesc()
                .stream()
                .map(ProductResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<Product> findByCategory(String categoryTag) {
        return productRepository.findActiveByCategoryTag(categoryTag);
    }

    public Optional<ProductResponseDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(ProductResponseDTO::fromEntity);
    }

    public List<Product> search(String query) {
        return productRepository.findActiveByNameOrDescriptionContaining(query);
    }

    // Admin methods for product management
    public ProductResponseDTO createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setSku(productDTO.getSku());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQuantityInStock(productDTO.getQuantityInStock());
        product.setImageUrl(productDTO.getImageUrl());
        product.setCategoryTag(productDTO.getCategoryTag());
        product.setActive(productDTO.isActive());
        
        // Set supplier if provided
        if (productDTO.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + productDTO.getSupplierId()));
            product.setSupplier(supplier);
        }
        
        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    public ProductResponseDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Update fields
        existingProduct.setSku(productDTO.getSku());
        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setQuantityInStock(productDTO.getQuantityInStock());
        existingProduct.setImageUrl(productDTO.getImageUrl());
        existingProduct.setActive(productDTO.isActive());
        existingProduct.setCategoryTag(productDTO.getCategoryTag());
        
        // Update supplier if provided
        if (productDTO.getSupplierId() != null) {
            Supplier supplier = supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + productDTO.getSupplierId()));
            existingProduct.setSupplier(supplier);
        } else {
            existingProduct.setSupplier(null);
        }
        
        Product savedProduct = productRepository.save(existingProduct);
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Soft delete instead of hard delete
        product.softDelete();
        productRepository.save(product);
    }

    // Method to permanently delete (chỉ dùng khi thực sự cần thiết)
    public void permanentlyDeleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    // Method to restore deleted product
    public ProductResponseDTO restoreProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        product.restore();
        Product savedProduct = productRepository.save(product);
        return ProductResponseDTO.fromEntity(savedProduct);
    }

    // Check if product can be safely deleted (không có trong đơn hàng nào)
    public boolean canSafelyDelete(Long id) {
        // TODO: Implement check for orders containing this product
        // For now, always use soft delete
        return false;
    }

    // Admin methods
    public List<ProductResponseDTO> getAllProductsForAdmin() {
        return productRepository.findAllProductsForAdmin()
                .stream()
                .map(ProductResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getDeletedProducts() {
        return productRepository.findDeletedProducts()
                .stream()
                .map(ProductResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
