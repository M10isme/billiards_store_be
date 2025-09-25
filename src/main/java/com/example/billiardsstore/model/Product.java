package com.example.billiardsstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // 👈 thêm dòng này
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private int quantityInStock;
    private String imageUrl;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    @JsonIgnore  // Tạm thời ignore để tránh lazy loading issue
    private Supplier supplier;

    @Column(name = "category_tag")
    private String categoryTag = "new-arrivals"; 

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Soft delete methods
    public void softDelete() {
        this.active = false;
        this.deletedAt = LocalDateTime.now();
    }

    public void restore() {
        this.active = true;
        this.deletedAt = null;
    }

    public boolean isDeleted() {
        return !this.active || this.deletedAt != null;
    }
}
