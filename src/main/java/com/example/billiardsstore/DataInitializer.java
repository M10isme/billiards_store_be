package com.example.billiardsstore;

import com.example.billiardsstore.model.*;
import com.example.billiardsstore.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(ProductRepository productRepository,
                           SupplierRepository supplierRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        initUsers();
        initSuppliersAndProducts();
    }

    private void initUsers() {
        if (userRepository.count() == 0) {
            User admin = User.builder()
                    .username("admin")
                    .passwordHash(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .fullName("Quản trị viên hệ thống")
                    .email("admin@billiards.com")
                    .phoneNumber("0901-000-001")
                    .address("123 Trần Duy Hưng, Cầu Giấy, Hà Nội")
                    .active(true)
                    .build();

            User customer1 = User.builder()
                    .username("customer")
                    .passwordHash(passwordEncoder.encode("customer123"))
                    .role(Role.CUSTOMER)
                    .fullName("Nguyễn Văn An")
                    .email("customer@billiards.com")
                    .phoneNumber("0901-111-222")
                    .address("456 Nguyễn Trãi, Thanh Xuân, Hà Nội")
                    .active(true)
                    .build();

            User customer2 = User.builder()
                    .username("khachhang1")
                    .passwordHash(passwordEncoder.encode("123456"))
                    .role(Role.CUSTOMER)
                    .fullName("Trần Thị Bình")
                    .email("khachhang1@gmail.com")
                    .phoneNumber("0902-333-444")
                    .address("789 Lê Văn Lương, Thanh Xuân, Hà Nội")
                    .active(true)
                    .build();

            User customer3 = User.builder()
                    .username("khachhang2")
                    .passwordHash(passwordEncoder.encode("123456"))
                    .role(Role.CUSTOMER)
                    .fullName("Lê Minh Cường")
                    .email("khachhang2@gmail.com")
                    .phoneNumber("0903-555-666")
                    .address("321 Hoàng Quốc Việt, Cầu Giấy, Hà Nội")
                    .active(true)
                    .build();

            userRepository.saveAll(List.of(admin, customer1, customer2, customer3));
        }
    }

    private void initSuppliersAndProducts() {
        if (supplierRepository.count() == 0) {
            Supplier s1 = Supplier.builder()
                    .name("Cơ Bida Việt Nam")
                    .contactInfo("info@cobidalvn.com | 0901-111-111 | 123 Phố Huế, Hai Bà Trưng, Hà Nội")
                    .build();

            Supplier s2 = Supplier.builder()
                    .name("ProCue Vietnam")
                    .contactInfo("sales@procue.vn | 0902-222-222 | 456 Lê Lợi, Quận 1, TP.HCM")
                    .build();

            Supplier s3 = Supplier.builder()
                    .name("Elite Cue Factory")
                    .contactInfo("contact@elitecue.vn | 0903-333-333 | 789 Trần Hưng Đạo, Hoàn Kiếm, Hà Nội")
                    .build();

            Supplier s4 = Supplier.builder()
                    .name("Master Cue Workshop")
                    .contactInfo("master@cueworkshop.vn | 0904-444-444 | 321 Nguyễn Huệ, Quận 1, TP.HCM")
                    .build();

            supplierRepository.saveAll(List.of(s1, s2, s3, s4));

            if (productRepository.count() == 0) {
                List<Product> products = List.of(
                        // CƠ BIDA - Flash Sales (Giá tốt)
                        Product.builder().name("Cơ Bida Gỗ Phổ Thông")
                                .sku("CB001").description("Cơ bida gỗ phổ thông, phù hợp cho người mới chơi")
                                .price(new BigDecimal("580000"))
                                .quantityInStock(50)
                                .active(true)
                                .supplier(s1)
                                .imageUrl("https://placehold.co/400x300?text=Co+Go+Pho+Thong")
                                .categoryTag("flash-sales")
                                .build(),

                        Product.builder().name("Cơ Bida Training Basic")
                                .sku("CB002").description("Cơ bida tập luyện cơ bản, giá tiết kiệm")
                                .price(new BigDecimal("750000"))
                                .quantityInStock(40)
                                .active(true)
                                .supplier(s1)
                                .imageUrl("https://placehold.co/400x300?text=Training+Basic")
                                .categoryTag("flash-sales")
                                .build(),

                        Product.builder().name("Cơ Bida Student Special")
                                .sku("CB003").description("Cơ bida giá sinh viên, chất lượng ổn định")
                                .price(new BigDecimal("950000"))
                                .quantityInStock(35)
                                .active(true)
                                .supplier(s1)
                                .imageUrl("https://placehold.co/400x300?text=Student+Special")
                                .categoryTag("flash-sales")
                                .build(),

                        // CƠ BIDA - Best Selling (Bán chạy)
                        Product.builder().name("Cơ Bida Maple Classic")
                                .sku("CB004").description("Cơ bida gỗ maple Canada, chất lượng cao")
                                .price(new BigDecimal("2200000"))
                                .quantityInStock(25)
                                .active(true)
                                .supplier(s2)
                                .imageUrl("https://placehold.co/400x300?text=Maple+Classic")
                                .categoryTag("best-selling")
                                .build(),

                        Product.builder().name("Cơ Bida Carbon Pro")
                                .sku("CB005").description("Cơ bida carbon siêu nhẹ, độ chính xác cao")
                                .price(new BigDecimal("3200000"))
                                .quantityInStock(20)
                                .active(true)
                                .supplier(s2)
                                .imageUrl("https://placehold.co/400x300?text=Carbon+Pro")
                                .categoryTag("best-selling")
                                .build(),

                        Product.builder().name("Cơ Bida Oak Premium")
                                .sku("CB006").description("Cơ bida gỗ sồi cao cấp, cân bằng hoàn hảo")
                                .price(new BigDecimal("2800000"))
                                .quantityInStock(18)
                                .active(true)
                                .supplier(s2)
                                .imageUrl("https://placehold.co/400x300?text=Oak+Premium")
                                .categoryTag("best-selling")
                                .build(),

                        // CƠ BIDA - New Arrivals (Hàng mới về)
                        Product.builder().name("Cơ Bida Diamond Pro 2024")
                                .sku("CB007").description("Sản phẩm mới nhất 2024, công nghệ Diamond Pro")
                                .price(new BigDecimal("4800000"))
                                .quantityInStock(15)
                                .active(true)
                                .supplier(s3)
                                .imageUrl("https://placehold.co/400x300?text=Diamond+Pro")
                                .categoryTag("new-arrivals")
                                .build(),

                        Product.builder().name("Cơ Bida Titanium Elite")
                                .sku("CB008").description("Cơ bida titanium siêu bền, thiết kế hiện đại")
                                .price(new BigDecimal("5200000"))
                                .quantityInStock(12)
                                .active(true)
                                .supplier(s3)
                                .imageUrl("https://placehold.co/400x300?text=Titanium+Elite")
                                .categoryTag("new-arrivals")
                                .build(),

                        Product.builder().name("Cơ Bida Fiber Glass 2024")
                                .sku("CB009").description("Cơ bida sợi thủy tinh mới, siêu nhẹ siêu bền")
                                .price(new BigDecimal("3800000"))
                                .quantityInStock(10)
                                .active(true)
                                .supplier(s3)
                                .imageUrl("https://placehold.co/400x300?text=Fiber+Glass")
                                .categoryTag("new-arrivals")
                                .build(),

                        // CƠ BIDA - Explore (Khám phá)
                        Product.builder().name("Cơ Bida Walnut Master")
                                .sku("CB010").description("Cơ bida gỗ óc chó, tay cầm êm ái")
                                .price(new BigDecimal("1800000"))
                                .quantityInStock(22)
                                .active(true)
                                .supplier(s4)
                                .imageUrl("https://placehold.co/400x300?text=Walnut+Master")
                                .categoryTag("explore")
                                .build(),

                        Product.builder().name("Cơ Bida Ebony Luxury")
                                .sku("CB011").description("Cơ bida gỗ mun đen, sang trọng và đẳng cấp")
                                .price(new BigDecimal("6500000"))
                                .quantityInStock(8)
                                .active(true)
                                .supplier(s4)
                                .imageUrl("https://placehold.co/400x300?text=Ebony+Luxury")
                                .categoryTag("explore")
                                .build(),

                        Product.builder().name("Cơ Bida Bamboo Eco")
                                .sku("CB012").description("Cơ bida tre tự nhiên, thân thiện môi trường")
                                .price(new BigDecimal("1200000"))
                                .quantityInStock(30)
                                .active(true)
                                .supplier(s4)
                                .imageUrl("https://placehold.co/400x300?text=Bamboo+Eco")
                                .categoryTag("explore")
                                .build(),

                        Product.builder().name("Cơ Bida Professional Plus")
                                .sku("CB013").description("Cơ bida chuyên nghiệp, dành cho tay chơi giỏi")
                                .price(new BigDecimal("3500000"))
                                .quantityInStock(16)
                                .active(true)
                                .supplier(s2)
                                .imageUrl("https://placehold.co/400x300?text=Professional+Plus")
                                .categoryTag("explore")
                                .build(),

                        Product.builder().name("Cơ Bida Champion Limited")
                                .sku("CB014").description("Phiên bản giới hạn, chỉ 5 chiếc được sản xuất")
                                .price(new BigDecimal("12000000"))
                                .quantityInStock(5)
                                .active(true)
                                .supplier(s3)
                                .imageUrl("https://placehold.co/400x300?text=Champion+Limited")
                                .categoryTag("explore")
                                .build()

                );

                productRepository.saveAll(products);
            }
        }
    }
}
