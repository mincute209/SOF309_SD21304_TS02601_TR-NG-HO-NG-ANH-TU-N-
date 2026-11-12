# KẾ HOẠCH KIỂM THỬ (TEST PLAN) - DỰ ÁN JOLLIEBEAR

## 1. Mục tiêu kiểm thử
* Đảm bảo các luồng nghiệp vụ chính của website bán thức ăn nhanh **Jolliebear** hoạt động chính xác theo đặc tả.
* Kiểm soát chất lượng mã nguồn thông qua Unit Test (JUnit 5) cho tầng xử lý logic và UI Automation Test (Selenium WebDriver) cho giao diện trình duyệt.
* Tích hợp tự động hóa kiểm thử liên tục (CI/CD) thông qua GitHub Actions.

## 2. Phạm vi kiểm thử (Scope)
* **Trong phạm vi (In-Scope):**
  * Chức năng Đăng nhập, Đăng ký và Phân quyền truy cập (`Filter`).
  * Chức năng Giỏ hàng (Thêm, sửa số lượng, xóa món, áp dụng mã giảm giá).
  * Chức năng Đặt hàng (Checkout) và Quản lý sản phẩm (Admin CRUD).
* **Ngoài phạm vi (Out-of-Scope):**
  * Kiểm thử hiệu năng chịu tải lớn (Load/Stress Testing).
  * Cổng thanh toán bên thứ ba thực tế.

## 3. Chiến lược kiểm thử (Test Strategy)
| Cấp độ kiểm thử | Công cụ / Framework | Đối tượng áp dụng | Tiêu chí đạt (Pass Criteria) |
| :--- | :--- | :--- | :--- |
| **Unit Testing** | JUnit 5 (`@ParameterizedTest`, `assertThrows`) | Các lớp xử lý nghiệp vụ, Validate dữ liệu, Tính toán giỏ hàng | 100% test case pass khi chạy `mvn test` |
| **UI Automation Testing** | Selenium WebDriver 4 + Page Object Model (POM) | Luồng Đăng nhập, Tìm kiếm món ăn, Thêm vào giỏ & Đặt hàng | Kịch bản chạy ổn định trên trình duyệt, tự chụp ảnh khi fail |
| **CI/CD Pipeline** | GitHub Actions (`test.yml`) | Toàn bộ mã nguồn khi thực hiện `git push` | Workflow báo xanh (Success) trên GitHub |

## 4. Môi trường kiểm thử
* **Ngôn ngữ & JDK:** Java SE 17
* **Quản lý thư viện & Build:** Apache Maven
* **Web Server:** Apache Tomcat 10+ (Jakarta EE)
* **Cơ sở dữ liệu:** Microsoft SQL Server
* **Trình duyệt kiểm thử:** Google Chrome (Headless mode trên CI/CD)