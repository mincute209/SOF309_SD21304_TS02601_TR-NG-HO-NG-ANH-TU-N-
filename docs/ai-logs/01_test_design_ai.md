# Nhật Ký Sử Dụng AI Thiết Kế Unit Test - Tuần 2 (SOF309)

## 1. Thông tin chung
- **Sinh viên thực hiện:** Trương Hoàng Anh Tuấn - TS02601
- **Chức năng kiểm thử:** Đăng ký tài khoản (`RegisterServlet` - Jolliebear)
- **Công cụ sử dụng:** JUnit 5 (`@ParameterizedTest`, `@CsvSource`, `assertThrows`)

## 2. Prompt 1: Phân tích biên (BVA) và thiết kế bộ dữ liệu kiểm thử Form đăng ký
### Prompt gửi AI:
> "Phân tích hàm xử lý đăng ký tài khoản trong `RegisterServlet.java` (kiểm tra độ dài username từ 3-20 ký tự, định dạng email, số điện thoại 10 số, mật khẩu tối thiểu 6 ký tự). Hãy thiết kế bộ dữ liệu kiểm thử theo kỹ thuật Phân tích giá trị biên (BVA) và Phân vùng tương đương để dùng với `@ParameterizedTest` và `@CsvSource` trong JUnit 5."

### Kết quả & Tinh chỉnh:
- AI đề xuất các ca kiểm thử biên dưới (2 ký tự - Fail, 3 ký tự - Pass) và biên trên (20 ký tự - Pass, 21 ký tự - Fail).
- Sinh viên đã kiểm tra lại điều kiện regex thực tế trong `RegisterServlet` và điều chỉnh dữ liệu `@CsvSource` cho khớp với thông báo lỗi của hệ thống.

## 3. Prompt 2: Kiểm thử ngoại lệ nghiệp vụ (`assertThrows`) và mã hóa mật khẩu
### Prompt gửi AI:
> "Viết các phương thức JUnit 5 sử dụng `assertThrows()` để bắt ngoại lệ nghiệp vụ khi đối tượng `User` truyền vào bị null hoặc thiếu trường bắt buộc, đồng thời kiểm tra hàm băm mật khẩu SHA-256 trong `PasswordUtil`."

### Kết quả & Tinh chỉnh:
- Hoàn thiện file `RegisterControllerTest.java` tại thư mục `src/test/java/com/poly/sof309/controllers/`[cite: 14, 23].
- Chạy lệnh `mvn test` đạt kết quả 14/14 test cases Pass (0 Errors, 0 Failures).