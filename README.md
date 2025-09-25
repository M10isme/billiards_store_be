# Billiards Store Backend (Spring Boot 3.5.5 + PostgreSQL)

## Yêu cầu
- Java 17
- Maven 3.9+
- PostgreSQL đang chạy local

## Cấu hình
Sửa `src/main/resources/application.yml` cho đúng user/password PostgreSQL của bạn:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/billiards_store
    username: postgres
    password: postgres
```

## Chạy dự án
```bash
mvn spring-boot:run
```
Mở Swagger UI: `http://localhost:8080/swagger-ui.html`

## Tài khoản mẫu
- admin / admin123 (ROLE_ADMIN)
- staff / staff123 (ROLE_STAFF)

## Luồng thử nhanh
1. **Login**: `POST /api/auth/login` body:
```json
{"username":"admin","password":"admin123"}
```
Copy `accessToken` trả về để gắn vào header `Authorization: Bearer <token>` cho các API cần auth.

2. **Products**
- `GET /api/products`
- `POST /api/products` (ADMIN/STAFF)
```json
{
  "sku":"CUE-NEW-01","name":"Co moi","description":"desc","price":1200000,
  "quantityInStock":5,"supplierId":1,"active":true
}
```
- Điều chỉnh kho: `POST /api/products/{id}/adjust?delta=5&reason=IMPORT`

3. **Orders**
- Tạo đơn: `POST /api/orders`
```json
{"customerId":1,"items":[{"productId":1,"quantity":2}]}
```
- Thanh toán: `POST /api/orders/{id}/pay`

## Ghi chú
- Password được mã hoá Bcrypt.
- JWT cấu hình trong `app.jwt.*` của `application.yml`.
- DDL tự động `spring.jpa.hibernate.ddl-auto=update`.
