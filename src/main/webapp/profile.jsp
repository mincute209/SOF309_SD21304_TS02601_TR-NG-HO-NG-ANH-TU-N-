<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Tài khoản của tôi - Jolliebear"; String activeNav = ""; %>
<%@ include file="common/header.jsp" %>

    <style>
        .profile-info-card {
            background: var(--bg-white); border-radius: var(--border-radius-md);
            box-shadow: var(--shadow-sm); padding: 24px; margin-bottom: 30px;
            display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 18px;
        }
        .profile-field label { display: block; font-size: 0.78rem; text-transform: uppercase; letter-spacing: 0.5px; color: var(--text-muted); margin-bottom: 4px; font-weight: 700; }
        .profile-field span { font-size: 1.05rem; color: var(--text-dark); font-weight: 600; }
        .profile-orders-title { font-size: 1.3rem; color: var(--text-dark); margin-bottom: 16px; }
        .porder-card {
            background: var(--bg-white); border-radius: var(--border-radius-md);
            box-shadow: var(--shadow-sm); padding: 20px 22px; margin-bottom: 16px;
        }
        .porder-head { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; margin-bottom: 12px; padding-bottom: 12px; border-bottom: 1px solid var(--border-color); }
        .porder-id { font-weight: 800; color: var(--text-dark); }
        .porder-date { font-size: 0.85rem; color: var(--text-muted); }
        .porder-status { padding: 5px 14px; border-radius: 20px; font-size: 0.8rem; font-weight: 700; }
        .st-cho     { background: #fff4d6; color: #b7791f; }
        .st-xacnhan { background: #d4f5f9; color: #0e7490; }
        .st-giao    { background: #d6ecff; color: #1e6fb8; }
        .st-hoan  { background: #d8f5e3; color: #1f9254; }
        .st-huy   { background: #ffe0e0; color: #d13333; }
        .porder-items { color: var(--text-dark); margin-bottom: 8px; }
        .porder-note { font-size: 0.85rem; color: var(--text-muted); font-style: italic; margin-bottom: 8px; }
        .porder-foot { display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 10px; }
        .porder-total { font-weight: 800; color: var(--primary); font-size: 1.1rem; }
        .porder-pay { font-size: 0.85rem; color: var(--text-muted); }
        .porder-cancel-btn {
            padding: 9px 16px; border: 1px solid var(--primary); border-radius: var(--border-radius-sm);
            background: var(--bg-white); color: var(--primary); font-weight: 700; cursor: pointer; transition: var(--transition-fast);
        }
        .porder-cancel-btn:hover { background: var(--primary); color: #fff; }
    </style>

    <section class="cart-section" id="profile">
        <div class="cart-container">
            <div class="cart-page-header">
                <span class="cart-badge">TÀI KHOẢN CỦA TÔI</span>
                <h2><i class="fa-solid fa-user"></i> HỒ SƠ &amp; ĐƠN HÀNG</h2>
                <p>Xem thông tin tài khoản và theo dõi các đơn hàng bạn đã đặt</p>
            </div>

            <!-- Chưa đăng nhập -->
            <div class="cart-empty" id="profile-guest" style="display:none;">
                <i class="fa-solid fa-user-lock"></i>
                <h3>Bạn chưa đăng nhập</h3>
                <p>Vui lòng đăng nhập để xem hồ sơ và các đơn hàng của bạn.</p>
                <button class="btn btn-primary" id="profile-login-btn">Đăng nhập ngay</button>
            </div>

            <!-- Đã đăng nhập -->
            <div id="profile-content" style="display:none;">
                <div class="profile-info-card">
                    <div class="profile-field"><label>Họ và tên</label><span id="pf-name">—</span></div>
                    <div class="profile-field"><label>Tên đăng nhập</label><span id="pf-username">—</span></div>
                    <div class="profile-field"><label>Email</label><span id="pf-email">—</span></div>
                    <div class="profile-field"><label>Số điện thoại</label><span id="pf-phone">—</span></div>
                </div>

                <div style="display:flex; align-items:center; justify-content:space-between; gap:12px; flex-wrap:wrap; margin-bottom:16px;">
                    <h3 class="profile-orders-title" style="margin:0;"><i class="fa-solid fa-receipt"></i> Đơn hàng của tôi</h3>
                    <button id="orders-refresh-btn" class="cart-clear-btn" style="width:auto; margin:0; padding:9px 16px;">
                        <i class="fa-solid fa-rotate"></i> Làm mới
                    </button>
                </div>
                <p id="orders-auto-note" style="font-size:0.8rem; color:var(--text-muted); margin-bottom:14px;">
                    <i class="fa-solid fa-circle-info"></i> Trạng thái đơn tự động cập nhật mỗi 15 giây.
                </p>
                <div id="profile-orders">
                    <!-- Rendered by profile.js -->
                </div>
            </div>
        </div>
    </section>

<% pageScript = "js/profile.js"; %>
<%@ include file="common/footer.jsp" %>
