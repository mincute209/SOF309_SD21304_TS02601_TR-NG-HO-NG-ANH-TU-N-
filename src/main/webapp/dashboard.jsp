<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Jolliebear - Trang Quản Trị Hệ Thống</title>
    <!-- Google Fonts: Outfit -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS for Dashboard -->
    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>

    <div class="dashboard-wrapper">
        <!-- SIDEBAR -->
        <aside class="sidebar">
            <div class="sidebar-brand">
                <a href="index.jsp" class="logo-area">
                    <div class="logo-circle">
                        <i class="fa-solid fa-paw logo-icon"></i>
                    </div>
                    <div class="logo-text">
                        <span class="logo-brand">Jolliebear</span>
                        <span class="logo-slogan">Hệ thống quản trị</span>
                    </div>
                </a>
            </div>

            <nav class="sidebar-nav">
                <ul>
                    <li>
                        <a href="#overview" class="nav-item active" id="tab-overview">
                            <i class="fa-solid fa-chart-pie"></i>
                            <span data-i18n="db_overview">Tổng quan</span>
                        </a>
                    </li>
                    <li>
                        <a href="#orders" class="nav-item" id="tab-orders">
                            <i class="fa-solid fa-receipt"></i>
                            <span>Đơn hàng</span>
                        </a>
                    </li>
                    <li>
                        <a href="#menu" class="nav-item" id="tab-menu">
                            <i class="fa-solid fa-utensils"></i>
                            <span data-i18n="db_menu_mgmt">Quản lý thực đơn</span>
                        </a>
                    </li>
                    <li>
                        <a href="#news" class="nav-item" id="tab-news">
                            <i class="fa-solid fa-newspaper"></i>
                            <span data-i18n="db_news_mgmt">Quản lý tin tức</span>
                        </a>
                    </li>
                    <li>
                        <a href="#stores" class="nav-item" id="tab-stores">
                            <i class="fa-solid fa-store"></i>
                            <span data-i18n="db_stores_mgmt">Hệ thống cửa hàng</span>
                        </a>
                    </li>
                    <li>
                        <a href="#jobs" class="nav-item" id="tab-jobs">
                            <i class="fa-solid fa-briefcase"></i>
                            <span data-i18n="db_jobs_mgmt">Tin tuyển dụng</span>
                        </a>
                    </li>
                    <li>
                        <a href="#contacts" class="nav-item" id="tab-contacts">
                            <i class="fa-solid fa-envelope"></i>
                            <span data-i18n="db_contacts_mgmt">Ý kiến liên hệ</span>
                            <span class="badge badge-warning hidden" id="unread-messages-count">0</span>
                        </a>
                    </li>
                    <li class="admin-only hidden">
                        <a href="#users" class="nav-item" id="tab-users">
                            <i class="fa-solid fa-users"></i>
                            <span data-i18n="db_users_mgmt">Tài khoản thành viên</span>
                        </a>
                    </li>
                </ul>
            </nav>

            <div class="sidebar-footer">
                <div class="user-profile">
                    <div class="avatar-circle">
                        <i class="fa-solid fa-circle-user"></i>
                    </div>
                    <div class="user-info">
                        <span class="user-name" id="profile-name">Đang tải...</span>
                        <span class="user-role" id="profile-role">...</span>
                    </div>
                </div>
                <a href="#logout" class="logout-btn" id="logout-trigger">
                    <i class="fa-solid fa-right-from-bracket"></i> Đăng xuất
                </a>
            </div>
        </aside>

        <!-- MAIN CONTENT AREA -->
        <main class="main-content">
            <header class="content-header">
                <h1 id="page-title" data-i18n="db_overview">Tổng quan</h1>
                <div class="header-actions" style="display: flex; align-items: center; gap: 20px;">
                    <div class="language-selector" style="display: flex; gap: 8px; background: rgba(255,255,255,0.05); padding: 4px 8px; border-radius: 8px; border: 1px solid var(--border-color);">
                        <span class="lang-item active" id="lang-vi" style="cursor: pointer; padding: 4px 8px; border-radius: 6px; font-size: 0.8rem; font-weight: 700;">VN</span>
                        <span class="lang-item" id="lang-en" style="cursor: pointer; padding: 4px 8px; border-radius: 6px; font-size: 0.8rem; font-weight: 700;">EN</span>
                    </div>
                    <span class="current-time"><i class="fa-regular fa-clock"></i> <span id="clock-display"></span></span>
                </div>
            </header>

            <div class="content-body">
                <!-- SECTION 1: OVERVIEW -->
                <section class="tab-content" id="content-overview">
                    <div class="stats-grid" style="grid-template-columns: repeat(5, 1fr);">
                        <div class="stat-card">
                            <div class="stat-icon bg-red">
                                <i class="fa-solid fa-users"></i>
                            </div>
                            <div class="stat-details">
                                <h3 id="stat-users">0</h3>
                                <p data-i18n="db_stat_members">Thành viên</p>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon bg-yellow">
                                <i class="fa-solid fa-utensils"></i>
                            </div>
                            <div class="stat-details">
                                <h3 id="stat-menu">0</h3>
                                <p data-i18n="db_stat_menu">Món ăn thực đơn</p>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon bg-green">
                                <i class="fa-solid fa-newspaper"></i>
                            </div>
                            <div class="stat-details">
                                <h3 id="stat-news">0</h3>
                                <p data-i18n="db_stat_news">Bài viết tin tức</p>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon bg-blue">
                                <i class="fa-solid fa-store"></i>
                            </div>
                            <div class="stat-details">
                                <h3 id="stat-stores">0</h3>
                                <p data-i18n="db_stat_stores">Chi nhánh cửa hàng</p>
                            </div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon bg-red">
                                <i class="fa-solid fa-briefcase"></i>
                            </div>
                            <div class="stat-details">
                                <h3 id="stat-jobs">0</h3>
                                <p data-i18n="db_stat_jobs">Tin tuyển dụng</p>
                            </div>
                        </div>
                    </div>

                    <!-- HÀNG THẺ DOANH THU / ĐƠN HÀNG -->
                    <div class="stats-grid" style="grid-template-columns: repeat(4, 1fr); margin-top: 20px;">
                        <div class="stat-card">
                            <div class="stat-icon bg-green"><i class="fa-solid fa-sack-dollar"></i></div>
                            <div class="stat-details"><h3 id="stat-revenue">0đ</h3><p>Tổng doanh thu</p></div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon bg-blue"><i class="fa-solid fa-receipt"></i></div>
                            <div class="stat-details"><h3 id="stat-orders-total">0</h3><p>Tổng đơn hàng</p></div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon bg-yellow"><i class="fa-solid fa-calendar-day"></i></div>
                            <div class="stat-details"><h3 id="stat-orders-today">0</h3><p>Đơn hôm nay</p></div>
                        </div>
                        <div class="stat-card">
                            <div class="stat-icon bg-red"><i class="fa-solid fa-clock"></i></div>
                            <div class="stat-details"><h3 id="stat-orders-pending">0</h3><p>Đơn chờ xử lý</p></div>
                        </div>
                    </div>

                    <!-- BIỂU ĐỒ -->
                    <div class="dashboard-charts" style="display:grid; grid-template-columns: 1.5fr 1fr; gap:20px; margin-top:24px;">
                        <div style="background:rgba(255,255,255,0.03); border:1px solid rgba(255,255,255,0.08); border-radius:14px; padding:20px;">
                            <div style="display:flex; align-items:center; justify-content:space-between; gap:12px; margin-bottom:14px; flex-wrap:wrap;">
                                <h3 style="color:#fff; margin:0;"><i class="fa-solid fa-chart-column" style="color:var(--secondary);"></i> Doanh thu</h3>
                                <select id="revenue-range" style="padding:7px 12px; border-radius:8px; border:1px solid rgba(255,255,255,0.15); background:rgba(0,0,0,0.3); color:#fff; font-weight:600; cursor:pointer;">
                                    <option value="7">7 ngày gần nhất</option>
                                    <option value="30">30 ngày gần nhất</option>
                                    <option value="60">60 ngày gần nhất</option>
                                    <option value="90">90 ngày gần nhất</option>
                                    <option value="all">Tất cả (theo tháng)</option>
                                </select>
                            </div>
                            <canvas id="revenueChart" height="150"></canvas>
                        </div>
                        <div style="background:rgba(255,255,255,0.03); border:1px solid rgba(255,255,255,0.08); border-radius:14px; padding:20px;">
                            <h3 style="color:#fff; margin-bottom:14px;"><i class="fa-solid fa-chart-pie" style="color:var(--secondary);"></i> Đơn theo trạng thái</h3>
                            <canvas id="statusChart" height="150"></canvas>
                        </div>
                    </div>

                    <div class="dashboard-info-row" style="margin-top: 32px;">
                        <div class="info-card">
                            <h2>Chào mừng trở lại!</h2>
                            <p>Đây là hệ thống quản trị nội bộ của Jolliebear. Bạn có thể cập nhật thông tin thực đơn, tin tức khuyến mãi, hệ thống cửa hàng và thông báo tuyển dụng hiển thị trực tiếp lên website chính.</p>
                            <div class="info-action-links" style="margin-top: 20px;">
                                <a href="index.jsp" target="_blank" class="btn btn-secondary"><i class="fa-solid fa-globe"></i> Xem website chính</a>
                            </div>
                        </div>
                    </div>
                </section>

                <!-- SECTION: MENU MANAGEMENT -->
                <section class="tab-content hidden" id="content-menu">
                    <div class="table-actions-row">
                        <div class="search-box">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            <input type="text" id="menu-search" placeholder="Tìm kiếm món ăn...">
                        </div>
                        <button class="btn btn-primary" id="add-menu-btn"><i class="fa-solid fa-plus"></i> <span data-i18n="db_btn_add_new">Thêm mới món ăn</span></button>
                    </div>
                    <div class="table-wrapper">
                        <table class="premium-table">
                            <thead>
                                <tr>
                                    <th width="80" data-i18n="db_tbl_img">Ảnh</th>
                                    <th data-i18n="db_tbl_name">Tên món ăn</th>
                                    <th width="150" data-i18n="db_tbl_category">Danh mục</th>
                                    <th width="140" data-i18n="db_tbl_price">Giá bán</th>
                                    <th width="120" data-i18n="db_tbl_status">Trạng thái</th>
                                    <th width="140" data-i18n="db_tbl_actions">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="menu-table-body">
                                <!-- Loaded dynamically -->
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- SECTION 2: NEWS MANAGEMENT -->
                <section class="tab-content hidden" id="content-news">
                    <div class="table-actions-row">
                        <div class="search-box">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            <input type="text" id="news-search" placeholder="Tìm kiếm bài viết...">
                        </div>
                        <button class="btn btn-primary" id="add-news-btn"><i class="fa-solid fa-plus"></i> Thêm bài viết mới</button>
                    </div>
                    <div class="table-wrapper">
                        <table class="premium-table">
                            <thead>
                                <tr>
                                    <th width="80">Ảnh</th>
                                    <th>Tiêu đề</th>
                                    <th>Tóm tắt</th>
                                    <th width="150">Ngày tạo</th>
                                    <th width="140">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="news-table-body">
                                <!-- Loaded dynamically -->
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- SECTION 3: STORES MANAGEMENT -->
                <section class="tab-content hidden" id="content-stores">
                    <div class="table-actions-row">
                        <div class="search-box">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            <input type="text" id="stores-search" placeholder="Tìm kiếm cửa hàng...">
                        </div>
                        <button class="btn btn-primary" id="add-store-btn"><i class="fa-solid fa-plus"></i> Thêm cửa hàng mới</button>
                    </div>
                    <div class="table-wrapper">
                        <table class="premium-table">
                            <thead>
                                <tr>
                                    <th>Tên cửa hàng</th>
                                    <th>Địa chỉ</th>
                                    <th width="150">Điện thoại</th>
                                    <th width="150">Giờ mở cửa</th>
                                    <th width="140">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="stores-table-body">
                                <!-- Loaded dynamically -->
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- SECTION 4: JOBS MANAGEMENT -->
                <section class="tab-content hidden" id="content-jobs">
                    <div class="table-actions-row">
                        <div class="search-box">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            <input type="text" id="jobs-search" placeholder="Tìm kiếm vị trí tuyển dụng...">
                        </div>
                        <button class="btn btn-primary" id="add-job-btn"><i class="fa-solid fa-plus"></i> Đăng tin tuyển dụng mới</button>
                    </div>
                    <div class="table-wrapper">
                        <table class="premium-table">
                            <thead>
                                <tr>
                                    <th>Vị trí tuyển dụng</th>
                                    <th>Bộ phận</th>
                                    <th>Địa điểm</th>
                                    <th>Mức lương</th>
                                    <th width="150">Hạn nộp</th>
                                    <th width="140">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="jobs-table-body">
                                <!-- Loaded dynamically -->
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- SECTION 5: CONTACT MESSAGES -->
                <section class="tab-content hidden" id="content-contacts">
                    <div class="table-actions-row">
                        <div class="search-box">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            <input type="text" id="contacts-search" placeholder="Tìm kiếm ý kiến khách hàng...">
                        </div>
                    </div>
                    <div class="table-wrapper">
                        <table class="premium-table">
                            <thead>
                                <tr>
                                    <th width="60">Trạng thái</th>
                                    <th>Người gửi</th>
                                    <th>Email / SĐT</th>
                                    <th>Tiêu đề</th>
                                    <th width="150">Ngày gửi</th>
                                    <th width="140">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="contacts-table-body">
                                <!-- Loaded dynamically -->
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- SECTION: ORDERS (đơn hàng khách đặt) -->
                <section class="tab-content hidden" id="content-orders">
                    <div class="table-actions-row">
                        <div class="search-box">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            <input type="text" id="orders-search" placeholder="Tìm theo tên khách, SĐT, trạng thái...">
                        </div>
                    </div>
                    <div class="table-wrapper">
                        <table class="premium-table">
                            <thead>
                                <tr>
                                    <th>Mã</th>
                                    <th>Khách hàng</th>
                                    <th>SĐT</th>
                                    <th>Địa chỉ</th>
                                    <th>Món đã đặt</th>
                                    <th>Tổng tiền</th>
                                    <th>Thanh toán</th>
                                    <th>Trạng thái</th>
                                    <th>Thời gian</th>
                                </tr>
                            </thead>
                            <tbody id="orders-table-body">
                                <!-- Loaded dynamically -->
                            </tbody>
                        </table>
                    </div>
                </section>

                <!-- SECTION 6: USERS MANAGEMENT (ADMIN ONLY) -->
                <section class="tab-content hidden" id="content-users">
                    <!-- Cấp quyền nhân viên bằng email (chỉ ADMIN) -->
                    <div class="promote-staff-box" style="display:flex; gap:12px; align-items:center; flex-wrap:wrap; background:rgba(255,255,255,0.03); border:1px solid rgba(255,255,255,0.08); border-radius:12px; padding:16px 18px; margin-bottom:18px;">
                        <div style="flex:1; min-width:220px;">
                            <strong style="color:var(--text-white,#fff);"><i class="fa-solid fa-user-shield"></i> Thêm nhân viên bằng email</strong>
                            <p style="font-size:0.8rem; color:rgba(255,255,255,0.5); margin-top:4px;">Nhập email của một tài khoản đã đăng ký để cấp quyền Nhân viên (truy cập trang quản trị).</p>
                        </div>
                        <input type="email" id="promote-email" placeholder="email@nguoidung.com" style="flex:1; min-width:220px; padding:10px 14px; border-radius:8px; border:1px solid rgba(255,255,255,0.15); background:rgba(0,0,0,0.25); color:#fff;">
                        <button class="btn btn-primary" id="promote-staff-btn"><i class="fa-solid fa-user-plus"></i> Cấp quyền nhân viên</button>
                    </div>

                    <div class="table-actions-row">
                        <div class="search-box">
                            <i class="fa-solid fa-magnifying-glass"></i>
                            <input type="text" id="users-search" placeholder="Tìm kiếm thành viên...">
                        </div>
                        <button class="btn btn-primary" id="add-user-btn"><i class="fa-solid fa-plus"></i> Tạo tài khoản mới</button>
                    </div>
                    <div class="table-wrapper">
                        <table class="premium-table">
                            <thead>
                                <tr>
                                    <th>Họ và tên</th>
                                    <th>Số điện thoại</th>
                                    <th>Email</th>
                                    <th>Vai trò</th>
                                    <th width="140">Thao tác</th>
                                </tr>
                            </thead>
                            <tbody id="users-table-body">
                                <!-- Loaded dynamically -->
                            </tbody>
                        </table>
                    </div>
                </section>
            </div>
        </main>
    </div>

    <!-- ==========================================================
         DASHBOARD MODALS
         ========================================================== -->

    <!-- 1. NEWS MODAL (ADD / EDIT) -->
    <div class="modal-overlay" id="news-modal">
        <div class="modal-card">
            <div class="modal-header">
                <h2 id="news-modal-title-text">Thêm bài viết mới</h2>
                <button class="close-modal-btn" id="close-news-modal">&times;</button>
            </div>
            <form id="news-form" class="modal-form">
                <input type="hidden" id="news-id">
                <div class="form-group">
                    <label for="news-title">Tiêu đề bài viết *</label>
                    <input type="text" id="news-title" required placeholder="Nhập tiêu đề...">
                </div>
                <div class="form-group">
                    <label for="news-summary">Tóm tắt ngắn *</label>
                    <textarea id="news-summary" rows="2" required placeholder="Nhập tóm tắt hiển thị bên ngoài trang chủ..."></textarea>
                </div>
                <div class="form-group">
                    <label for="news-content">Nội dung chi tiết *</label>
                    <textarea id="news-content" rows="6" required placeholder="Nhập nội dung chi tiết bài viết..."></textarea>
                </div>
                <div class="form-group">
                    <label for="news-image">Đường dẫn ảnh bìa (Image URL)</label>
                    <input type="text" id="news-image" placeholder="https://example.com/image.jpg">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" id="cancel-news-modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu lại</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 2. STORE MODAL (ADD / EDIT) -->
    <div class="modal-overlay" id="store-modal">
        <div class="modal-card">
            <div class="modal-header">
                <h2 id="store-modal-title-text">Thêm cửa hàng mới</h2>
                <button class="close-modal-btn" id="close-store-modal">&times;</button>
            </div>
            <form id="store-form" class="modal-form">
                <input type="hidden" id="store-id">
                <div class="form-group">
                    <label for="store-name">Tên cửa hàng *</label>
                    <input type="text" id="store-name" required placeholder="Ví dụ: Jolliebear Bến Thành">
                </div>
                <div class="form-group">
                    <label for="store-address">Địa chỉ *</label>
                    <input type="text" id="store-address" required placeholder="Ví dụ: 120 Lê Lai, Phường Bến Thành, Quận 1">
                </div>
                <div class="form-group">
                    <label for="store-phone">Điện thoại liên hệ</label>
                    <input type="text" id="store-phone" placeholder="Ví dụ: 02838241234">
                </div>
                <div class="form-group">
                    <label for="store-hours">Giờ mở cửa</label>
                    <input type="text" id="store-hours" placeholder="Ví dụ: 09:00 - 22:30" value="09:00 - 22:00">
                </div>
                <div class="form-group">
                    <label for="store-map">Link bản đồ Google Map (Chỉ lấy URL trong thẻ src iframe)</label>
                    <input type="text" id="store-map" placeholder="https://www.google.com/maps/embed?...">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" id="cancel-store-modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu lại</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 3. JOB MODAL (ADD / EDIT) -->
    <div class="modal-overlay" id="job-modal">
        <div class="modal-card">
            <div class="modal-header">
                <h2 id="job-modal-title-text">Đăng tin tuyển dụng mới</h2>
                <button class="close-modal-btn" id="close-job-modal">&times;</button>
            </div>
            <form id="job-form" class="modal-form">
                <input type="hidden" id="job-id">
                <div class="form-group">
                    <label for="job-title">Vị trí tuyển dụng *</label>
                    <input type="text" id="job-title" required placeholder="Ví dụ: Quản Lý Cửa Hàng">
                </div>
                <div class="form-row-2">
                    <div class="form-group">
                        <label for="job-dept">Bộ phận *</label>
                        <select id="job-dept" required>
                            <option value="Vận Hành Cửa Hàng">Vận Hành Cửa Hàng</option>
                            <option value="Quản Lý">Quản Lý</option>
                            <option value="Chế Biến">Chế Biến</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="job-loc">Địa điểm làm việc *</label>
                        <input type="text" id="job-loc" required placeholder="Ví dụ: TP. Hồ Chí Minh">
                    </div>
                </div>
                <div class="form-row-2">
                    <div class="form-group">
                        <label for="job-salary">Mức lương</label>
                        <input type="text" id="job-salary" placeholder="Ví dụ: 15.000.000đ - 18.000.000đ" value="Thỏa thuận">
                    </div>
                    <div class="form-group">
                        <label for="job-deadline">Hạn nộp hồ sơ *</label>
                        <input type="date" id="job-deadline" required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="job-desc">Mô tả công việc *</label>
                    <textarea id="job-desc" rows="4" required placeholder="Nhập mô tả các đầu việc chính..."></textarea>
                </div>
                <div class="form-group">
                    <label for="job-reqs">Yêu cầu ứng viên *</label>
                    <textarea id="job-reqs" rows="4" required placeholder="Nhập yêu cầu kinh nghiệm, độ tuổi, kỹ năng..."></textarea>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" id="cancel-job-modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu lại</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 4. CONTACT MESSAGE MODAL (VIEW DETAIL) -->
    <div class="modal-overlay" id="contact-modal">
        <div class="modal-card">
            <div class="modal-header">
                <h2>Chi tiết thư liên hệ</h2>
                <button class="close-modal-btn" id="close-contact-modal">&times;</button>
            </div>
            <div class="message-details-body">
                <div class="meta-row">
                    <span class="meta-label">Người gửi:</span>
                    <span class="meta-value" id="msg-name"></span>
                </div>
                <div class="meta-row">
                    <span class="meta-label">Email:</span>
                    <span class="meta-value" id="msg-email"></span>
                </div>
                <div class="meta-row">
                    <span class="meta-label">Điện thoại:</span>
                    <span class="meta-value" id="msg-phone"></span>
                </div>
                <div class="meta-row">
                    <span class="meta-label">Ngày gửi:</span>
                    <span class="meta-value" id="msg-date"></span>
                </div>
                <div class="meta-row">
                    <span class="meta-label">Tiêu đề:</span>
                    <span class="meta-value font-bold" id="msg-subject"></span>
                </div>
                <div class="msg-content-box">
                    <p class="meta-label" style="margin-bottom: 8px;">Nội dung tin nhắn:</p>
                    <div id="msg-text" class="msg-text-content"></div>
                </div>
            </div>
            <div class="modal-footer" style="margin-top: 24px;">
                <button type="button" class="btn btn-secondary" id="close-contact-modal-btn">Đóng</button>
                <button type="button" class="btn btn-primary" id="mark-read-btn">Đánh dấu đã đọc</button>
            </div>
        </div>
    </div>

    <!-- 5. USER MODAL (ADD / EDIT) (ADMIN ONLY) -->
    <div class="modal-overlay" id="user-modal">
        <div class="modal-card">
            <div class="modal-header">
                <h2 id="user-modal-title-text">Thêm tài khoản mới</h2>
                <button class="close-modal-btn" id="close-user-modal">&times;</button>
            </div>
            <form id="user-form" class="modal-form">
                <input type="hidden" id="user-is-edit" value="false">
                <input type="hidden" id="user-old-email">
                <div class="form-group">
                    <label for="user-name">Họ và tên *</label>
                    <input type="text" id="user-name" required placeholder="Nhập họ và tên...">
                </div>
                <div class="form-row-2">
                    <div class="form-group">
                        <label for="user-phone">Số điện thoại *</label>
                        <input type="tel" id="user-phone" required placeholder="0909xxxxxx">
                    </div>
                    <div class="form-group">
                        <label for="user-email">Địa chỉ Email *</label>
                        <input type="email" id="user-email" required placeholder="username@jolliebear.com">
                    </div>
                </div>
                <div class="form-row-2">
                    <div class="form-group">
                        <label for="user-password" id="user-password-label">Mật khẩu *</label>
                        <input type="password" id="user-password" required placeholder="Nhập mật khẩu...">
                        <span id="user-password-tip" style="font-size: 0.75rem; color: rgba(255,255,255,0.4); display: none;">Để trống nếu giữ nguyên mật khẩu cũ</span>
                    </div>
                    <div class="form-group">
                        <label for="user-role">Vai trò quản trị *</label>
                        <select id="user-role" required>
                            <option value="CUSTOMER">CUSTOMER (Khách hàng)</option>
                            <option value="STAFF">STAFF (Nhân viên)</option>
                            <option value="ADMIN">ADMIN (Quản trị tối cao)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" id="cancel-user-modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu lại</button>
                </div>
            </form>
        </div>
    </div>

    <!-- 6. MENU ITEM MODAL (ADD / EDIT) -->
    <div class="modal-overlay" id="menu-item-modal">
        <div class="modal-card">
            <div class="modal-header">
                <h2 id="menu-modal-title-text">Thêm món ăn mới</h2>
                <button class="close-modal-btn" id="close-menu-modal">&times;</button>
            </div>
            <form id="menu-item-form" class="modal-form">
                <input type="hidden" id="menu-item-id">
                <div class="form-row-2">
                    <div class="form-group">
                        <label for="menu-item-name-vi">Tên Tiếng Việt *</label>
                        <input type="text" id="menu-item-name-vi" required placeholder="Ví dụ: Gà Giòn Vui Vẻ">
                    </div>
                    <div class="form-group">
                        <label for="menu-item-name-en">Tên Tiếng Anh *</label>
                        <input type="text" id="menu-item-name-en" required placeholder="Example: Crispy Chickenjoy">
                    </div>
                </div>
                <div class="form-row-2">
                    <div class="form-group">
                        <label for="menu-item-cat">Danh mục *</label>
                        <select id="menu-item-cat" required>
                            <option value="GaGion">Gà Giòn</option>
                            <option value="MyY">Mỳ Ý</option>
                            <option value="Burger">Burger</option>
                            <option value="Combo">Combo Tiết Kiệm</option>
                            <option value="NuocTrangMieng">Nước & Tráng Miệng</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label for="menu-item-price">Giá bán (VND) *</label>
                        <input type="number" id="menu-item-price" required placeholder="35000" min="0">
                    </div>
                </div>
                <div class="form-group">
                    <label for="menu-item-desc-vi">Mô tả Tiếng Việt</label>
                    <textarea id="menu-item-desc-vi" rows="2" placeholder="Nhập mô tả Tiếng Việt..."></textarea>
                </div>
                <div class="form-group">
                    <label for="menu-item-desc-en">Mô tả Tiếng Anh</label>
                    <textarea id="menu-item-desc-en" rows="2" placeholder="Enter English description..."></textarea>
                </div>
                <div class="form-group">
                    <label for="menu-item-image">Đường dẫn ảnh sản phẩm (Image URL)</label>
                    <input type="text" id="menu-item-image" placeholder="https://example.com/food.jpg">
                </div>
                <div class="form-group">
                    <label for="menu-item-avail">Trạng thái phục vụ</label>
                    <select id="menu-item-avail">
                        <option value="true">Đang phục vụ (Available)</option>
                        <option value="false">Tạm ngưng (Unavailable)</option>
                    </select>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" id="cancel-menu-modal">Hủy</button>
                    <button type="submit" class="btn btn-primary">Lưu lại</button>
                </div>
            </form>
        </div>
    </div>

    <!-- TOAST NOTIFICATION CONTAINER -->
    <div class="toast-container" id="toast-container"></div>

    <!-- Translations & Custom Dashboard Logic -->
    <script src="js/translations.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.1/dist/chart.umd.min.js"></script>
    <script src="js/dashboard.js"></script>
</body>
</html>
