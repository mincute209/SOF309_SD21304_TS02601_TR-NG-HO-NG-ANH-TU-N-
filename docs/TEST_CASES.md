# DANH SÁCH KỊCH BẢN KIỂM THỬ (TEST CASES)

## Nhóm 1: Chức năng Đăng nhập (Login)
| Mã TC | Tên kịch bản | Dữ liệu đầu vào (Input) | Các bước thực hiện | Kết quả mong đợi (Expected Result) | Mức độ ưu tiên |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_LOG_01** | Đăng nhập thành công với tài khoản Customer hợp lệ | `user`: `khachhang01`<br>`pass`: `123456` | 1. Mở trang Đăng nhập<br>2. Nhập user và pass hợp lệ<br>3. Bấm nút Đăng nhập | Chuyển hướng về trang chủ, hiển thị tên người dùng trên thanh menu. | High |
| **TC_LOG_02** | Đăng nhập thành công với tài khoản Admin | `user`: `admin`<br>`pass`: `admin123` | 1. Mở trang Đăng nhập<br>2. Nhập tài khoản Admin<br>3. Bấm nút Đăng nhập | Chuyển hướng vào trang Quản trị (`/admin/dashboard`). | High |
| **TC_LOG_03** | Đăng nhập thất bại khi sai mật khẩu | `user`: `khachhang01`<br>`pass`: `sai_mat_khau` | 1. Mở trang Đăng nhập<br>2. Nhập đúng user, sai pass<br>3. Bấm nút Đăng nhập | Giữ nguyên trang Đăng nhập, hiển thị thông báo lỗi *"Sai tên đăng nhập hoặc mật khẩu"*. | High |
| **TC_LOG_04** | Đăng nhập thất bại khi bỏ trống thông tin (Kiểm tra biên) | `user`: `""` (rỗng)<br>`pass`: `""` (rỗng) | 1. Mở trang Đăng nhập<br>2. Để trống cả 2 ô<br>3. Bấm nút Đăng nhập | Hệ thống chặn gửi form hoặc báo lỗi *"Vui lòng nhập đầy đủ thông tin"*. | Medium |
| **TC_LOG_05** | Kiểm tra phân quyền chặn truy cập trang Admin trái phép | Chưa đăng nhập (Guest) | 1. Truy cập trực tiếp URL `/admin/products` | Bộ lọc (`Filter`) chặn lại và chuyển hướng về trang Đăng nhập. | High |

## Nhóm 2: Chức năng Đăng ký (Register)
| Mã TC | Tên kịch bản | Dữ liệu đầu vào (Input) | Các bước thực hiện | Kết quả mong đợi (Expected Result) | Mức độ ưu tiên |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_REG_01** | Đăng ký tài khoản mới thành công | `user`: `tuan_test01`<br>`pass`: `123456`<br>`confirm`: `123456`<br>`email`: `tuan@gmail.com`<br>`phone`: `0901234567` | 1. Mở trang Đăng ký<br>2. Nhập đầy đủ thông tin hợp lệ<br>3. Bấm Đăng ký | Tạo tài khoản thành công trong CSDL, thông báo thành công và chuyển sang trang Đăng nhập. | High |
| **TC_REG_02** | Đăng ký thất bại do trùng tên đăng nhập | `user`: `admin` (đã tồn tại)<br>Các trường khác hợp lệ | 1. Mở trang Đăng ký<br>2. Nhập `username` đã tồn tại<br>3. Bấm Đăng ký | Báo lỗi *"Tên đăng nhập đã tồn tại"*, không tạo bản ghi mới. | High |
| **TC_REG_03** | Đăng ký thất bại do mật khẩu xác nhận không khớp | `pass`: `123456`<br>`confirm`: `654321` | 1. Mở trang Đăng ký<br>2. Nhập mật khẩu xác nhận khác mật khẩu gốc<br>3. Bấm Đăng ký | Báo lỗi *"Mật khẩu xác nhận không khớp"*. | Medium |
| **TC_REG_04** | Đăng ký thất bại do mật khẩu dưới 6 ký tự (Biên dưới) | `pass`: `12345` (5 ký tự) | 1. Mở trang Đăng ký<br>2. Nhập mật khẩu 5 ký tự<br>3. Bấm Đăng ký | Báo lỗi *"Mật khẩu phải có ít nhất 6 ký tự"*. | Medium |

## Nhóm 3: Chức năng Giỏ hàng & Đặt hàng (Cart & Checkout)
| Mã TC | Tên kịch bản | Dữ liệu đầu vào (Input) | Các bước thực hiện | Kết quả mong đợi (Expected Result) | Mức độ ưu tiên |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_CART_01** | Thêm sản phẩm vào giỏ hàng thành công | Món: `Gà Giòn Vui Vẻ`<br>`quantity`: `2` | 1. Chọn món ăn<br>2. Nhập số lượng `2`<br>3. Bấm Thêm vào giỏ | Biểu tượng giỏ hàng tăng lên `2`, tổng tiền tính đúng bằng `Đơn giá x 2`. | High |
| **TC_CART_02** | Kiểm tra biên số lượng sản phẩm không hợp lệ | `quantity`: `0` hoặc `-1` | 1. Tại trang chi tiết/giỏ hàng<br>2. Nhập số lượng `<= 0` | Hệ thống báo lỗi ngoại lệ/từ chối cập nhật, không cho phép số lượng âm hoặc bằng 0. | Medium |
| **TC_CART_03** | Áp dụng mã giảm giá hợp lệ | Tổng giỏ: `200,000đ`<br>Mã: `JOLLIE10` (giảm 10%) | 1. Mở giỏ hàng<br>2. Nhập mã `JOLLIE10`<br>3. Bấm Áp dụng | Tổng thanh toán cập nhật chính xác còn `180,000đ`. | Medium |
| **TC_ORD_01** | Đặt hàng thành công với thông tin giao hàng hợp lệ | Giỏ có 1 món<br>`address`: `Quận 1, TP.HCM`<br>`phone`: `0909111222` | 1. Vào trang Thanh toán<br>2. Nhập địa chỉ, SĐT<br>3. Bấm Xác nhận đặt hàng | Đơn hàng được lưu vào CSDL với trạng thái `Pending`, giỏ hàng được làm trống. | High |