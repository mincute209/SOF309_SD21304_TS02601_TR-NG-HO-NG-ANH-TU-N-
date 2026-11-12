<%--
    Footer dùng chung: chân trang, cookie, modal đăng nhập/đăng ký, toast, nạp JS.
    Trang cha có thể khai báo trước khi include:
        <% String pageScript = "js/cart.js"; %>   // JS riêng của trang (tùy chọn)
--%>
<% String ctxF = request.getContextPath(); %>

    <!-- FOOTER SECTION -->
    <footer class="site-footer">
        <div class="footer-container">
            <div class="footer-top">
                <div class="footer-brand">
                    <div class="footer-logo">
                        <i class="fa-solid fa-paw"></i> Jolliebear
                    </div>
                    <p class="footer-desc">Mang hương vị ngập tràn niềm vui tới mọi gia đình Việt Nam. Chất lượng hàng đầu, phục vụ tận tâm.</p>
                    <div class="social-links">
                        <a href="#" aria-label="Facebook"><i class="fa-brands fa-facebook-f"></i></a>
                        <a href="#" aria-label="Instagram"><i class="fa-brands fa-instagram"></i></a>
                        <a href="#" aria-label="Youtube"><i class="fa-brands fa-youtube"></i></a>
                        <a href="#" aria-label="TikTok"><i class="fa-brands fa-tiktok"></i></a>
                    </div>
                </div>
                <div class="footer-links-group">
                    <h4>THỰC ĐƠN</h4>
                    <ul>
                        <li><a href="<%= ctxF %>/menu.jsp">Gà Giòn Vui Vẻ</a></li>
                        <li><a href="<%= ctxF %>/menu.jsp">Gà Sốt Cay</a></li>
                        <li><a href="<%= ctxF %>/menu.jsp">Mỳ Ý Đặc Biệt</a></li>
                        <li><a href="<%= ctxF %>/menu.jsp">Kem & Tráng Miệng</a></li>
                    </ul>
                </div>
                <div class="footer-links-group">
                    <h4>LIÊN KẾT NHANH</h4>
                    <ul>
                        <li><a href="<%= ctxF %>/index.jsp">Về Jolliebear</a></li>
                        <li><a href="<%= ctxF %>/promotions.jsp">Tin Khuyến Mãi</a></li>
                        <li><a href="<%= ctxF %>/stores.jsp">Hệ Thống Cửa Hàng</a></li>
                        <li><a href="<%= ctxF %>/jobs.jsp">Cơ Hội Việc Làm</a></li>
                    </ul>
                </div>
                <div class="footer-links-group">
                    <h4>LIÊN HỆ</h4>
                    <ul>
                        <li><i class="fa-solid fa-phone"></i> Hotline: 1900 8080</li>
                        <li><i class="fa-solid fa-envelope"></i> Email: feedback@jolliebear.com.vn</li>
                        <li><i class="fa-solid fa-location-dot"></i> Trụ sở: Quận 1, TP. Hồ Chí Minh</li>
                    </ul>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2026 Jolliebear Việt Nam. Tất cả quyền được bảo lưu.</p>
                <div class="footer-bottom-links">
                    <a href="#">Chính sách bảo mật</a>
                    <a href="#">Điều khoản sử dụng</a>
                </div>
            </div>
        </div>
    </footer>

    <!-- COOKIE CONSENT BANNER -->
    <div class="cookie-consent-banner" id="cookie-banner">
        <div class="cookie-container">
            <div class="cookie-text">
                Trang web này yêu cầu sử dụng cookie để cung cấp đầy đủ tất cả các tính năng. Để biết thêm thông tin về dữ liệu có trong cookie, vui lòng xem trang <a href="#" class="cookie-policy-link">Chính sách Cookie</a> của chúng tôi. Để chấp nhận cookie từ trang web này, vui lòng nhấp vào nút Chấp nhận Cookie bên dưới.
            </div>
            <div class="cookie-actions">
                <button class="cookie-btn btn-settings" id="cookie-settings">TÙY CHỈNH CÀI ĐẶT COOKIE</button>
                <button class="cookie-btn btn-accept" id="cookie-accept">CHẤP NHẬN COOKIE</button>
                <button class="cookie-btn btn-decline" id="cookie-decline">TỪ CHỐI</button>
            </div>
        </div>
    </div>

    <!-- AUTHENTICATION MODALS -->
    <div class="auth-modal-overlay" id="auth-modal-overlay">
        <!-- Login Modal -->
        <div class="auth-modal-card" id="login-modal">
            <button class="close-modal-btn" id="close-login-btn" aria-label="Close login modal">&times;</button>
            <div class="auth-modal-header">
                <h2>ĐĂNG NHẬP</h2>
                <p>Nhập thông tin tài khoản Jolliebear của bạn</p>
            </div>
            <form id="login-form" class="auth-form">
                <div class="form-group">
                    <label for="login-email">Tên đăng nhập, Email hoặc SĐT</label>
                    <div class="form-input-wrapper">
                        <i class="fa-regular fa-user"></i>
                        <input type="text" id="login-email" placeholder="Nhập tên đăng nhập, email hoặc SĐT" required>
                    </div>
                    <span class="error-msg" id="login-email-error"></span>
                </div>
                <div class="form-group">
                    <label for="login-password">Mật khẩu</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="login-password" placeholder="Nhập mật khẩu" required>
                        <i class="fa-regular fa-eye-slash toggle-password-btn" data-target="login-password"></i>
                    </div>
                    <span class="error-msg" id="login-password-error"></span>
                </div>
                <div class="form-options">
                    <label class="checkbox-container">
                        <input type="checkbox" id="login-remember">
                        <span class="checkmark"></span>
                        <span class="checkbox-label">Ghi nhớ mật khẩu</span>
                    </label>
                    <a href="#forgot" class="forgot-link" id="forgot-trigger">Quên mật khẩu?</a>
                </div>
                <button type="submit" class="auth-submit-btn">ĐĂNG NHẬP</button>
            </form>
            <div class="auth-divider-text"><span>Hoặc đăng nhập với</span></div>
            <div class="social-login-buttons">
                <button class="social-btn google-btn" type="button"><i class="fa-brands fa-google"></i> Google</button>
                <button class="social-btn facebook-btn" type="button"><i class="fa-brands fa-facebook"></i> Facebook</button>
            </div>
            <div class="auth-footer-text">
                Chưa có tài khoản? <a href="#register" id="switch-to-register">Đăng ký ngay</a>
            </div>
        </div>

        <!-- Register Modal -->
        <div class="auth-modal-card hidden" id="register-modal">
            <button class="close-modal-btn" id="close-register-btn" aria-label="Close register modal">&times;</button>
            <div class="auth-modal-header">
                <h2>ĐĂNG KÝ TÀI KHOẢN</h2>
                <p>Trở thành thành viên Jolliebear để nhận nhiều ưu đãi</p>
            </div>
            <form id="register-form" class="auth-form">
                <div class="form-group">
                    <label for="register-name">Họ và tên</label>
                    <div class="form-input-wrapper">
                        <i class="fa-regular fa-user"></i>
                        <input type="text" id="register-name" placeholder="Nhập họ và tên" required>
                    </div>
                    <span class="error-msg" id="register-name-error"></span>
                </div>
                <div class="form-group">
                    <label for="register-username">Tên đăng nhập</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-at"></i>
                        <input type="text" id="register-username" placeholder="Đặt tên đăng nhập (vd: bear123)" required>
                    </div>
                    <span class="error-msg" id="register-username-error"></span>
                </div>
                <div class="form-group">
                    <label for="register-phone">Số điện thoại</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-phone"></i>
                        <input type="tel" id="register-phone" placeholder="Nhập số điện thoại" required>
                    </div>
                    <span class="error-msg" id="register-phone-error"></span>
                </div>
                <div class="form-group">
                    <label for="register-email">Địa chỉ Email</label>
                    <div class="form-input-wrapper">
                        <i class="fa-regular fa-envelope"></i>
                        <input type="email" id="register-email" placeholder="Nhập email" required>
                    </div>
                    <span class="error-msg" id="register-email-error"></span>
                </div>
                <div class="form-group">
                    <label for="register-password">Mật khẩu</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="register-password" placeholder="Nhập mật khẩu (tối thiểu 6 ký tự)" required>
                        <i class="fa-regular fa-eye-slash toggle-password-btn" data-target="register-password"></i>
                    </div>
                    <span class="error-msg" id="register-password-error"></span>
                </div>
                <div class="form-group">
                    <label for="register-confirm-password">Xác nhận mật khẩu</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="register-confirm-password" placeholder="Nhập lại mật khẩu" required>
                        <i class="fa-regular fa-eye-slash toggle-password-btn" data-target="register-confirm-password"></i>
                    </div>
                    <span class="error-msg" id="register-confirm-password-error"></span>
                </div>
                <div class="form-options">
                    <label class="checkbox-container">
                        <input type="checkbox" id="register-terms" required>
                        <span class="checkmark"></span>
                        <span class="checkbox-label">Tôi đồng ý với các <a href="#" class="policy-inline-link">Điều khoản dịch vụ</a> & <a href="#" class="policy-inline-link">Chính sách bảo mật</a></span>
                    </label>
                </div>
                <button type="submit" class="auth-submit-btn">ĐĂNG KÝ</button>
            </form>
            <div class="auth-footer-text">
                Đã có tài khoản? <a href="#login" id="switch-to-login">Đăng nhập</a>
            </div>
        </div>

        <!-- Forgot Password Modal -->
        <div class="auth-modal-card hidden" id="forgot-modal">
            <button class="close-modal-btn" id="close-forgot-btn" aria-label="Close">&times;</button>
            <div class="auth-modal-header">
                <h2>QUÊN MẬT KHẨU</h2>
                <p>Nhập email và tên đăng nhập để đặt lại mật khẩu ngay</p>
            </div>
            <form id="forgot-form" class="auth-form">
                <div class="form-group">
                    <label for="forgot-email">Địa chỉ Email</label>
                    <div class="form-input-wrapper">
                        <i class="fa-regular fa-envelope"></i>
                        <input type="email" id="forgot-email" placeholder="Nhập email đã đăng ký" required>
                    </div>
                    <span class="error-msg" id="forgot-email-error"></span>
                </div>
                <div class="form-group">
                    <label for="forgot-username">Tên đăng nhập</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-at"></i>
                        <input type="text" id="forgot-username" placeholder="Nhập tên đăng nhập của bạn" required>
                    </div>
                    <span class="error-msg" id="forgot-username-error"></span>
                </div>
                <div class="form-group">
                    <label for="forgot-new-password">Mật khẩu mới</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="forgot-new-password" placeholder="Nhập mật khẩu mới (tối thiểu 6 ký tự)" required>
                        <i class="fa-regular fa-eye-slash toggle-password-btn" data-target="forgot-new-password"></i>
                    </div>
                    <span class="error-msg" id="forgot-new-password-error"></span>
                </div>
                <button type="submit" class="auth-submit-btn">ĐẶT LẠI MẬT KHẨU</button>
            </form>
            <div class="auth-footer-text">
                Nhớ ra mật khẩu rồi? <a href="#login" id="forgot-switch-to-login">Đăng nhập</a>
            </div>
        </div>

        <!-- Change Password Modal (khi đã đăng nhập) -->
        <div class="auth-modal-card hidden" id="changepw-modal">
            <button class="close-modal-btn" id="close-changepw-btn" aria-label="Close">&times;</button>
            <div class="auth-modal-header">
                <h2>ĐỔI MẬT KHẨU</h2>
                <p>Nhập mật khẩu hiện tại và mật khẩu mới của bạn</p>
            </div>
            <form id="changepw-form" class="auth-form">
                <div class="form-group">
                    <label for="changepw-old">Mật khẩu hiện tại</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="changepw-old" placeholder="Nhập mật khẩu hiện tại" required>
                        <i class="fa-regular fa-eye-slash toggle-password-btn" data-target="changepw-old"></i>
                    </div>
                    <span class="error-msg" id="changepw-old-error"></span>
                </div>
                <div class="form-group">
                    <label for="changepw-new">Mật khẩu mới</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="changepw-new" placeholder="Nhập mật khẩu mới (tối thiểu 6 ký tự)" required>
                        <i class="fa-regular fa-eye-slash toggle-password-btn" data-target="changepw-new"></i>
                    </div>
                    <span class="error-msg" id="changepw-new-error"></span>
                </div>
                <div class="form-group">
                    <label for="changepw-confirm">Xác nhận mật khẩu mới</label>
                    <div class="form-input-wrapper">
                        <i class="fa-solid fa-lock"></i>
                        <input type="password" id="changepw-confirm" placeholder="Nhập lại mật khẩu mới" required>
                    </div>
                    <span class="error-msg" id="changepw-confirm-error"></span>
                </div>
                <button type="submit" class="auth-submit-btn">CẬP NHẬT MẬT KHẨU</button>
            </form>
        </div>
    </div>

    <!-- CSS cho ngăn kéo giỏ hàng (đặt ngay trong JSP, không thêm file .css) -->
    <style>
        .cart-drawer-overlay {
            position: fixed; inset: 0;
            background-color: rgba(0, 0, 0, 0.5);
            opacity: 0; visibility: hidden;
            transition: opacity var(--transition-smooth), visibility var(--transition-smooth);
            z-index: 1200;
        }
        .cart-drawer-overlay.open { opacity: 1; visibility: visible; }

        .cart-drawer {
            position: fixed; top: 0; right: 0;
            width: 400px; max-width: 90vw; height: 100%;
            background-color: var(--bg-white);
            box-shadow: -8px 0 30px rgba(0, 0, 0, 0.18);
            transform: translateX(100%);
            transition: transform var(--transition-smooth);
            z-index: 1300;
            display: flex; flex-direction: column;
        }
        .cart-drawer.open { transform: translateX(0); }

        .cart-drawer-header {
            display: flex; align-items: center; justify-content: space-between;
            padding: 20px 24px;
            background: var(--grad-red); color: var(--bg-white);
            flex-shrink: 0;
        }
        .cart-drawer-header h3 { font-size: 1.2rem; margin: 0; }
        .cart-drawer-close {
            background: none; border: none; color: var(--bg-white);
            font-size: 1.8rem; line-height: 1; cursor: pointer; padding: 0 4px;
            transition: var(--transition-fast);
        }
        .cart-drawer-close:hover { transform: scale(1.15); }

        .cart-drawer-empty {
            flex: 1; display: flex; flex-direction: column;
            align-items: center; justify-content: center; gap: 14px;
            color: var(--text-muted); text-align: center; padding: 30px;
        }
        .cart-drawer-empty i { font-size: 3.5rem; color: var(--border-color); }
        .cart-drawer-empty p { font-size: 1.05rem; font-weight: 600; }

        .cart-drawer-body { flex: 1; overflow-y: auto; padding: 12px 20px; }
        .drawer-item {
            display: flex; align-items: center; gap: 12px;
            padding: 14px 0; border-bottom: 1px solid var(--border-color);
        }
        .drawer-item-img {
            width: 60px; height: 60px; border-radius: var(--border-radius-sm);
            overflow: hidden; flex-shrink: 0; background-color: var(--bg-light);
        }
        .drawer-item-img img { width: 100%; height: 100%; object-fit: cover; }
        .drawer-item-info { flex: 1; min-width: 0; }
        .drawer-item-info h4 {
            font-size: 0.95rem; color: var(--text-dark); margin: 0 0 4px;
            white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
        }
        .drawer-item-price { font-size: 0.85rem; color: var(--primary); font-weight: 700; }
        .drawer-item-qty { display: inline-flex; align-items: center; gap: 6px; margin-top: 6px; }
        .drawer-qty-btn {
            width: 26px; height: 26px; border-radius: 50%;
            border: 1px solid var(--border-color); background-color: var(--bg-white);
            color: var(--text-dark); font-size: 0.95rem; font-weight: 700; cursor: pointer;
            display: flex; align-items: center; justify-content: center;
            transition: var(--transition-fast);
        }
        .drawer-qty-btn:hover { background-color: var(--primary); color: var(--bg-white); border-color: var(--primary); }
        .drawer-qty-value { min-width: 24px; text-align: center; font-weight: 600; font-size: 0.9rem; }
        .drawer-item-remove {
            background: none; border: none; color: var(--text-muted);
            cursor: pointer; font-size: 1rem; padding: 6px; align-self: flex-start;
            transition: var(--transition-fast);
        }
        .drawer-item-remove:hover { color: var(--primary); }

        .cart-drawer-footer {
            flex-shrink: 0; padding: 20px 24px;
            border-top: 1px solid var(--border-color);
            box-shadow: 0 -4px 16px rgba(0, 0, 0, 0.05);
        }
        .cart-drawer-total-row {
            display: flex; justify-content: space-between; align-items: center;
            font-size: 1.15rem; font-weight: 700; color: var(--text-dark); margin-bottom: 16px;
        }
        .cart-drawer-total-row span:last-child { color: var(--primary); }
        .cart-drawer-checkout {
            width: 100%; padding: 14px; border: none; border-radius: var(--border-radius-sm);
            background: var(--grad-red); color: var(--bg-white);
            font-size: 1rem; font-weight: 700; letter-spacing: 0.5px; cursor: pointer;
            transition: var(--transition-smooth); box-shadow: var(--shadow-md);
        }
        .cart-drawer-checkout:hover { transform: translateY(-2px); box-shadow: var(--shadow-lg); }
        .cart-drawer-clear {
            width: 100%; margin-top: 10px; padding: 10px;
            border: 1px solid var(--border-color); border-radius: var(--border-radius-sm);
            background-color: var(--bg-white); color: var(--text-muted);
            font-size: 0.88rem; font-weight: 600; cursor: pointer;
            transition: var(--transition-fast);
        }
        .cart-drawer-clear:hover { border-color: var(--primary); color: var(--primary); }
    </style>

    <!-- CART DRAWER (ngăn kéo giỏ hàng trượt từ phải) -->
    <div class="cart-drawer-overlay" id="cart-drawer-overlay"></div>
    <aside class="cart-drawer" id="cart-drawer" aria-label="Giỏ hàng">
        <div class="cart-drawer-header">
            <h3><i class="fa-solid fa-cart-shopping"></i> Giỏ hàng của bạn</h3>
            <button type="button" class="cart-drawer-close" id="cart-drawer-close" aria-label="Đóng">&times;</button>
        </div>

        <!-- Giỏ trống -->
        <div class="cart-drawer-empty" id="cart-drawer-empty">
            <i class="fa-solid fa-basket-shopping"></i>
            <p>Giỏ hàng đang trống</p>
            <a href="<%= ctxF %>/menu.jsp" class="btn btn-primary">Xem thực đơn</a>
        </div>

        <!-- Danh sách món -->
        <div class="cart-drawer-body" id="cart-drawer-items"></div>

        <!-- Chân: tổng tiền + đặt hàng -->
        <div class="cart-drawer-footer" id="cart-drawer-footer">
            <div class="cart-drawer-total-row">
                <span>Tổng cộng</span>
                <span id="cart-drawer-total">0 đ</span>
            </div>
            <button type="button" class="cart-drawer-checkout" id="cart-drawer-checkout">
                <i class="fa-solid fa-credit-card"></i> ĐẶT HÀNG NGAY
            </button>
            <button type="button" class="cart-drawer-clear" id="cart-drawer-clear">
                <i class="fa-solid fa-trash-can"></i> Xóa toàn bộ
            </button>
        </div>
    </aside>

    <!-- CSS cho modal đặt hàng (đặt trong JSP, không thêm file .css) -->
    <style>
        .checkout-overlay {
            position: fixed; inset: 0; z-index: 1400;
            background: rgba(0,0,0,0.55);
            display: none; align-items: center; justify-content: center;
            padding: 20px;
        }
        .checkout-overlay.show { display: flex; }
        .checkout-card {
            background: var(--bg-white); border-radius: var(--border-radius-md);
            width: 460px; max-width: 100%; max-height: 92vh; overflow-y: auto;
            padding: 28px 26px; position: relative;
            box-shadow: 0 24px 60px rgba(0,0,0,0.3);
        }
        .checkout-close {
            position: absolute; top: 14px; right: 16px;
            background: none; border: none; font-size: 1.8rem; line-height: 1;
            color: var(--text-muted); cursor: pointer;
        }
        .checkout-close:hover { color: var(--primary); }
        .checkout-steps { display: flex; align-items: center; gap: 10px; margin-bottom: 22px; }
        .cstep { font-size: 0.85rem; font-weight: 700; color: var(--text-muted); white-space: nowrap; }
        .cstep.active { color: var(--primary); }
        .cstep-line { flex: 1; height: 2px; background: var(--border-color); }
        .checkout-slide h3 { font-size: 1.25rem; color: var(--text-dark); margin-bottom: 16px; }
        .cform-group { margin-bottom: 14px; }
        .cform-group label { display: block; font-size: 0.85rem; font-weight: 600; color: var(--text-dark); margin-bottom: 6px; }
        .cform-group input, .cform-group textarea {
            width: 100%; padding: 11px 13px; border: 1px solid var(--border-color);
            border-radius: var(--border-radius-sm); font-family: inherit; font-size: 0.95rem;
            background: var(--bg-light); color: var(--text-dark);
        }
        .cform-group input:focus, .cform-group textarea:focus { outline: none; border-color: var(--primary); background: var(--bg-white); }
        .checkout-next-btn, .checkout-confirm-btn {
            width: 100%; padding: 14px; border: none; border-radius: var(--border-radius-sm);
            background: var(--grad-red); color: #fff; font-size: 1rem; font-weight: 700;
            cursor: pointer; transition: var(--transition-smooth); box-shadow: var(--shadow-md); margin-top: 8px;
        }
        .checkout-next-btn:hover, .checkout-confirm-btn:hover { transform: translateY(-2px); box-shadow: var(--shadow-lg); }
        .pay-options { display: flex; flex-direction: column; gap: 10px; margin-bottom: 18px; }
        .pay-option {
            display: flex; align-items: center; gap: 12px; padding: 13px 15px;
            border: 2px solid var(--border-color); border-radius: var(--border-radius-sm);
            cursor: pointer; font-weight: 600; color: var(--text-dark); transition: var(--transition-fast);
        }
        .pay-option:hover { border-color: var(--secondary); }
        .pay-option input { accent-color: var(--primary); width: 18px; height: 18px; }
        .pay-option:has(input:checked) { border-color: var(--primary); background: var(--primary-light); }
        .checkout-summary {
            background: var(--bg-light); border-radius: var(--border-radius-sm);
            padding: 12px 14px; margin-bottom: 12px; font-size: 0.9rem; color: var(--text-dark);
            max-height: 150px; overflow-y: auto;
        }
        .checkout-summary .csum-row { display: flex; justify-content: space-between; padding: 3px 0; }
        .checkout-total-row {
            display: flex; justify-content: space-between; align-items: center;
            font-size: 1.15rem; font-weight: 700; color: var(--text-dark); margin-bottom: 14px;
        }
        .checkout-total-row span:last-child { color: var(--primary); }
        .checkout-actions { display: flex; gap: 12px; }
        .checkout-back-btn {
            flex: 0 0 40%; padding: 14px; border: 1px solid var(--border-color);
            border-radius: var(--border-radius-sm); background: var(--bg-white);
            color: var(--text-muted); font-weight: 700; cursor: pointer;
        }
        .checkout-back-btn:hover { border-color: var(--primary); color: var(--primary); }
        .checkout-actions .checkout-confirm-btn { flex: 1; margin-top: 0; }
    </style>

    <!-- MODAL ĐẶT HÀNG (2 BƯỚC) -->
    <div class="checkout-overlay" id="checkout-overlay">
        <div class="checkout-card">
            <button type="button" class="checkout-close" id="checkout-close" aria-label="Đóng">&times;</button>
            <div class="checkout-steps">
                <span class="cstep active" id="cstep-1">1. Thông tin giao hàng</span>
                <span class="cstep-line"></span>
                <span class="cstep" id="cstep-2">2. Thanh toán</span>
            </div>

            <!-- BƯỚC 1: thông tin khách -->
            <div class="checkout-slide" id="checkout-slide-1">
                <h3>Thông tin giao hàng</h3>
                <div class="cform-group">
                    <label for="co-name">Họ và tên *</label>
                    <input type="text" id="co-name" placeholder="Nguyễn Văn A">
                    <span class="error-msg" id="co-name-error"></span>
                </div>
                <div class="cform-group">
                    <label for="co-phone">Số điện thoại *</label>
                    <input type="tel" id="co-phone" placeholder="0909xxxxxx">
                    <span class="error-msg" id="co-phone-error"></span>
                </div>
                <div class="cform-group">
                    <label for="co-address">Địa chỉ giao hàng *</label>
                    <input type="text" id="co-address" placeholder="Số nhà, đường, phường/quận...">
                    <span class="error-msg" id="co-address-error"></span>
                </div>
                <div class="cform-group">
                    <label for="co-note">Ghi chú (tùy chọn)</label>
                    <textarea id="co-note" rows="2" placeholder="Ví dụ: giao giờ hành chính, ít cay..."></textarea>
                </div>
                <button type="button" class="checkout-next-btn" id="checkout-next">Tiếp tục <i class="fa-solid fa-arrow-right"></i></button>
            </div>

            <!-- BƯỚC 2: thanh toán -->
            <div class="checkout-slide hidden" id="checkout-slide-2">
                <h3>Phương thức thanh toán</h3>
                <div class="pay-options">
                    <label class="pay-option"><input type="radio" name="pay-method" value="Tiền mặt khi nhận hàng (COD)" checked> <i class="fa-solid fa-money-bill-wave"></i> Tiền mặt khi nhận hàng (COD)</label>
                    <label class="pay-option"><input type="radio" name="pay-method" value="Chuyển khoản ngân hàng"> <i class="fa-solid fa-building-columns"></i> Chuyển khoản ngân hàng</label>
                    <label class="pay-option"><input type="radio" name="pay-method" value="Ví MoMo"> <i class="fa-solid fa-wallet"></i> Ví MoMo</label>
                    <label class="pay-option"><input type="radio" name="pay-method" value="Thẻ tín dụng/ghi nợ"> <i class="fa-regular fa-credit-card"></i> Thẻ tín dụng/ghi nợ</label>
                </div>
                <div class="checkout-summary" id="checkout-summary"></div>
                <div class="checkout-total-row">
                    <span>Tổng cộng</span>
                    <span id="checkout-total">0 đ</span>
                </div>
                <div class="checkout-actions">
                    <button type="button" class="checkout-back-btn" id="checkout-back"><i class="fa-solid fa-arrow-left"></i> Quay lại</button>
                    <button type="button" class="checkout-confirm-btn" id="checkout-confirm"><i class="fa-solid fa-check"></i> Đặt hàng</button>
                </div>
            </div>
        </div>
    </div>

    <!-- TOAST NOTIFICATION CONTAINER -->
    <div class="toast-container" id="toast-container"></div>

    <!-- Translations & Custom JS -->
    <script src="<%= ctxF %>/js/translations.js"></script>
    <script src="<%= ctxF %>/js/main.js"></script>
<% if (pageScript != null) { %>
    <script src="<%= ctxF %>/<%= pageScript %>"></script>
<% } %>
</body>
</html>
