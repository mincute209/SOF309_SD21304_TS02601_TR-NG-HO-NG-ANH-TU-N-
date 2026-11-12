# ĐẶC TẢ NGHIỆP VỤ HỆ THỐNG (SOFTWARE SPECIFICATIONS)

## 1. Thông tin chung
* **Tên dự án:** Hệ thống Cửa hàng Gà rán Jolliebear (Jolliebear Fast-Food E-Commerce)
* **Công nghệ sử dụng:** Java Servlet/JSP, JDBC, SQL Server, Maven
* **Kiến trúc hệ thống:** MVC (`controller`, `dao`, `filter`, `model`, `util`)

## 2. Phân quyền người dùng
* **Khách vãng lai (Guest):** Xem danh sách sản phẩm, xem chi tiết món ăn, tìm kiếm, đăng ký tài khoản, đăng nhập.
* **Khách hàng (Customer):** Thêm món vào giỏ hàng, cập nhật số lượng, áp dụng mã giảm giá (Voucher), đặt hàng, theo dõi trạng thái đơn hàng, cập nhật hồ sơ cá nhân.
* **Quản trị viên (Admin):** Quản lý danh mục sản phẩm (Thêm/Sửa/Xóa món ăn), quản lý đơn hàng (duyệt/hủy/cập nhật trạng thái giao), quản lý mã giảm giá, xem thống kê doanh thu.

## 3. Đặc tả các chức năng cốt lõi (Core Features)

### F01: Đăng nhập & Phân quyền (Authentication & Authorization)
* **Input:** `username` (hoặc `email`), `password`.
* **Quy tắc nghiệp vụ (Business Rules):**
  * BR01: Không được bỏ trống tài khoản hoặc mật khẩu.
  * BR02: Tài khoản phải tồn tại trong CSDL và trạng thái đang hoạt động (`Active`).
  * BR03: Bộ lọc (`Filter`) tự động chặn truy cập trái phép vào các đường dẫn `/admin/*` nếu người dùng chưa đăng nhập hoặc không có quyền `Admin`.

### F02: Đăng ký tài khoản (Registration)
* **Input:** `username`, `password`, `confirmPassword`, `fullName`, `email`, `phone`.
* **Quy tắc nghiệp vụ:**
  * BR04: `username` và `email` không được trùng lặp với tài khoản đã có.
  * BR05: Mật khẩu tối thiểu 6 ký tự; `confirmPassword` phải khớp chính xác với `password`.
  * BR06: Số điện thoại phải đúng định dạng 10 chữ số, bắt đầu bằng số `0`.

### F03: Quản lý Giỏ hàng & Tính tiền (Shopping Cart)
* **Input:** `productId`, `quantity`, `discountCode` (tùy chọn).
* **Quy tắc nghiệp vụ:**
  * BR07: Số lượng thêm vào giỏ (`quantity`) phải là số nguyên dương ($1 \le quantity \le 50$) và không vượt quá số lượng tồn kho.
  * BR08: Tổng tiền hàng = $\sum (\text{Đơn giá} \times \text{Số lượng})$.
  * BR09: Nếu nhập mã giảm giá hợp lệ và còn hạn sử dụng, tổng thanh toán được trừ theo tỷ lệ/số tiền giảm của mã đó (tổng thanh toán tối thiểu là `0 VNĐ`).

### F04: Thanh toán & Đặt hàng (Checkout & Order)
* **Input:** `shippingAddress`, `receiverPhone`, `paymentMethod`.
* **Quy tắc nghiệp vụ:**
  * BR10: Giỏ hàng phải có ít nhất 1 sản phẩm mới được phép đặt hàng.
  * BR11: Địa chỉ giao hàng và số điện thoại nhận hàng không được để trống.
  * BR12: Sau khi đặt hàng thành công, hệ thống lưu đơn hàng với trạng thái `Pending` và làm rỗng giỏ hàng hiện tại.