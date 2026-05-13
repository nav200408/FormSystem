# Hệ Thống Quản Lý Form (FormSystem)

Đây là dự án bài tập xây dựng hệ thống quản lý Form động, cho phép người dùng tạo, quản lý các mẫu biểu mẫu và xử lý các bản ghi gửi về (submissions) một cách linh hoạt.

## Tính Năng Chính

- **Xác thực & Phân quyền**: Sử dụng JWT để quản lý phiên làm việc và phân quyền giữa Admin và User.
- **Quản lý Form & Field**: Cho phép tạo, cập nhật và xóa các form cũng như các trường dữ liệu bên trong.
- **Sắp xếp thứ tự (Reorder)**: API tối ưu để thay đổi thứ tự hiển thị của các Field trong một Form.
- **Xử lý Submission**: Nhận và lưu trữ dữ liệu người dùng gửi về, đi kèm với logic validation động dựa trên cấu hình của từng field.
- **Tài liệu API**: Tích hợp Swagger UI để dễ dàng tra cứu và thử nghiệm các endpoint.

## Công Nghệ Sử Dụng

- **Ngôn ngữ**: Java 17
- **Framework**: Spring Boot 3.x (AOP, Data JPA, Security, Validation, Web)
- **Cơ sở dữ liệu**: PostgreSQL
- **Bảo mật**: Spring Security & JWT
- **Container hóa**: Docker & Docker Compose
- **Tài liệu**: Springdoc OpenAPI (Swagger)
- **Khác**: Dotenv (quản lý biến môi trường), Maven (build tool)

> **Ghi chú về bảo mật**: Hiện tại hệ thống đang sử dụng JWT đơn giản (duy nhất Access Token) với mục đích chính là xác thực cơ bản và phân quyền. Nếu có thêm thời gian, em sẽ nâng cấp thêm:
>
> - Implement Refresh Token và lưu vào database.
> - Sử dụng Redis để quản lý Access Token Blacklist khi logout.

## Tài Liệu API

Sau khi khởi động project, anh/chị có thể truy cập tài liệu API tại:

- **Swagger UI**: [http://localhost:8080/api/swagger-ui/index.html]

## Hướng Dẫn Chạy Project

### 1. Chạy trực tiếp trên IDE (IntelliJ IDEA/Eclipse)

1.  **Cấu hình môi trường**: Tạo file `.env` tại thư mục gốc của project (tham khảo từ file `env.example`).
2.  **Khởi tạo Database**: Đảm bảo anh/chị đã có PostgreSQL chạy cục bộ và tạo database tương ứng với cấu hình trong `.env`.
3.  **Run**: Mở class `FormSystemApplication` và chọn **Run**.

### 2. Chạy bằng Docker Compose

Hệ thống đã được cấu hình sẵn Docker Compose bao gồm cả ứng dụng và cơ sở dữ liệu.

1.  **Build & Chạy**: Mở terminal tại thư mục gốc và chạy lệnh:
    ```powershell
    docker-compose up -d --build
    ```
    _(Lưu ý: File docker-compose đã bao gồm image của PostgreSQL, nếu anh/chị muốn dùng DB bên ngoài thì có thể tùy chỉnh lại)._

## Kiểm Thử

Dự án được chú trọng vào việc đảm bảo chất lượng thông qua các bộ test:

- **Unit Tests**: Bao gồm logic xử lý tại lớp Controller và Service, dto, validation middleware cho form submission.
  **em không tạo middleware cho phần validation của các api khác ngoài api submit form do các request đó được kiểm tra đơn giản hơn, và việc test vẫn dễ dàng vì các lớp dto không có dependency**

## Các Phần Đã Thực Hiện

- [x] Hoàn thành tất cả các API được yêu cầu.
- [x] Tích hợp Swagger làm API Document.
- [x] Triển khai Docker và Docker Compose cho môi trường local.
- [x] API sắp xếp thứ tự (Reorder Field).
- [x] Hệ thống Unit Test bao quát các flow chính và validation logic.
