package com.example.billiardsstore.controller;

import com.example.billiardsstore.model.Supplier;
import com.example.billiardsstore.service.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/suppliers")
@SecurityRequirement(name = "bearerAuth")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> all() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getById(@PathVariable("id") Long id) {
        return supplierService.getSupplierById(id)
                .map(supplier -> ResponseEntity.ok(supplier))
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Supplier> create(@RequestBody Supplier supplier) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(supplierService.createSupplier(supplier));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> update(@PathVariable("id") Long id, @RequestBody Supplier supplier) {
        try {
            Supplier updatedSupplier = supplierService.updateSupplier(id, supplier);
            return ResponseEntity.ok(updatedSupplier);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // API để kiểm tra thông tin trước khi xóa
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/delete-info")
    public ResponseEntity<Map<String, Object>> getDeleteInfo(@PathVariable("id") Long id) {
        try {
            long relatedProductsCount = supplierService.getRelatedProductsCount(id);
            boolean canDelete = relatedProductsCount == 0;
            
            return ResponseEntity.ok(Map.of(
                "canDelete", canDelete,
                "relatedProductsCount", relatedProductsCount,
                "message", canDelete 
                    ? "Có thể xóa nhà cung cấp này an toàn"
                    : "Nhà cung cấp này có " + relatedProductsCount + " sản phẩm liên quan. Xóa sẽ gỡ bỏ liên kết supplier khỏi các sản phẩm này."
            ));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            long relatedProductsCount = supplierService.getRelatedProductsCount(id);
            supplierService.deleteSupplier(id);
            
            if (relatedProductsCount > 0) {
                return ResponseEntity.ok(Map.of(
                    "message", "Đã xóa nhà cung cấp thành công. " + relatedProductsCount + " sản phẩm đã được gỡ bỏ liên kết supplier.",
                    "affectedProducts", relatedProductsCount
                ));
            } else {
                return ResponseEntity.ok(Map.of(
                    "message", "Đã xóa nhà cung cấp thành công."
                ));
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
