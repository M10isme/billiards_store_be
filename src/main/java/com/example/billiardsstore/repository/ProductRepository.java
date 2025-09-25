package com.example.billiardsstore.repository;

import com.example.billiardsstore.model.Product;
import com.example.billiardsstore.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryTag(String categoryTag);
    List<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
    
    // Method to get all ACTIVE products sorted by updatedAt descending (newest first)
    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.updatedAt DESC")
    List<Product> findAllOrderByUpdatedAtDesc();
    
    // Method to get all ACTIVE products by category
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.categoryTag = :categoryTag")
    List<Product> findActiveByCategoryTag(@Param("categoryTag") String categoryTag);
    
    // Method to search ACTIVE products only
    @Query("SELECT p FROM Product p WHERE p.active = true AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Product> findActiveByNameOrDescriptionContaining(@Param("query") String query);
    
    // Method to find products by supplier (including inactive for admin purposes)
    List<Product> findBySupplier(Supplier supplier);
    
    // Admin methods to get all products (including inactive)
    @Query("SELECT p FROM Product p ORDER BY p.updatedAt DESC")
    List<Product> findAllProductsForAdmin();
    
    // Find deleted products
    @Query("SELECT p FROM Product p WHERE p.active = false ORDER BY p.deletedAt DESC")
    List<Product> findDeletedProducts();
}
