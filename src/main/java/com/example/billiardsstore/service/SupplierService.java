package com.example.billiardsstore.service;

import com.example.billiardsstore.model.Product;
import com.example.billiardsstore.model.Supplier;
import com.example.billiardsstore.repository.ProductRepository;
import com.example.billiardsstore.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;

    public SupplierService(SupplierRepository supplierRepository, 
                          ProductRepository productRepository) {
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public Supplier createSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier supplier) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
        
        existingSupplier.setName(supplier.getName());
        existingSupplier.setContactInfo(supplier.getContactInfo());
        
        return supplierRepository.save(existingSupplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        // Kiểm tra xem supplier có tồn tại không
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));

        // Tìm tất cả sản phẩm liên quan đến supplier này
        List<Product> relatedProducts = productRepository.findBySupplier(supplier);

        if (!relatedProducts.isEmpty()) {
            // Có 3 cách xử lý:
            // 1. Throw exception (không cho phép xóa)
            // 2. Set supplier của products thành null
            // 3. Xóa luôn cả products

            // Ở đây tôi chọn cách 2: Set supplier thành null
            for (Product product : relatedProducts) {
                product.setSupplier(null);
                productRepository.save(product);
            }
        }

        // Sau khi xử lý xong products, mới xóa supplier
        supplierRepository.deleteById(id);
    }

    // Method để kiểm tra xem supplier có thể xóa an toàn không
    public boolean canDeleteSupplier(Long id) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);
        if (supplier == null) {
            return false;
        }
        
        List<Product> relatedProducts = productRepository.findBySupplier(supplier);
        return relatedProducts.isEmpty();
    }

    // Method để lấy số lượng sản phẩm liên quan
    public long getRelatedProductsCount(Long supplierId) {
        Supplier supplier = supplierRepository.findById(supplierId).orElse(null);
        if (supplier == null) {
            return 0;
        }
        
        List<Product> relatedProducts = productRepository.findBySupplier(supplier);
        return relatedProducts.size();
    }
}