package com.example.billiardsstore.service;

import com.example.billiardsstore.model.InventoryMovement;
import com.example.billiardsstore.model.Product;
import com.example.billiardsstore.repository.InventoryMovementRepository;
import com.example.billiardsstore.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryMovementRepository movementRepository;

    public InventoryService(ProductRepository productRepository, InventoryMovementRepository movementRepository) {
        this.productRepository = productRepository;
        this.movementRepository = movementRepository;
    }

    @Transactional
    public void adjust(Long productId, int delta, String reason) {
        Product p = productRepository.findById(productId).orElseThrow();
        int newQty = p.getQuantityInStock() + delta;
        if (newQty < 0) throw new RuntimeException("Insufficient stock");
        p.setQuantityInStock(newQty);
        productRepository.save(p);

        InventoryMovement mv = InventoryMovement.builder()
                .product(p)
                .delta(delta)
                .reason(reason)
                .createdAt(OffsetDateTime.now())
                .build();
        movementRepository.save(mv);
    }
}
