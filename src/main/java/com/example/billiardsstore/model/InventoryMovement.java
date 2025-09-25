package com.example.billiardsstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "inventory_movements")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryMovement {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private int delta; // positive for inbound, negative for outbound

    @Column(nullable = false, length = 100)
    private String reason; // "IMPORT", "ADJUST", "ORDER"

    @Column(nullable = false)
    private OffsetDateTime createdAt;
}
