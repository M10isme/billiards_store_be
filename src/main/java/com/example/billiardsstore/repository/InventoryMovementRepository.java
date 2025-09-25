package com.example.billiardsstore.repository;

import com.example.billiardsstore.model.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {}
