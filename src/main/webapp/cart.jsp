<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Giỏ Hàng - Jolliebear"; String activeNav = ""; %>
<%@ include file="common/header.jsp" %>

    <!-- CART (GIỎ HÀNG) SECTION -->
    <section class="cart-section" id="cart">
        <div class="cart-container">
            <div class="cart-page-header">
                <span class="cart-badge">ĐƠN HÀNG CỦA BẠN</span>
                <h2><i class="fa-solid fa-cart-shopping"></i> GIỎ HÀNG</h2>
                <p>Kiểm tra lại các món ăn ngon bạn đã chọn trước khi đặt hàng nhé!</p>
            </div>

            <!-- Trạng thái giỏ trống -->
            <div class="cart-empty" id="cart-empty" style="display:none;">
                <i class="fa-solid fa-basket-shopping"></i>
                <h3>Giỏ hàng của bạn đang trống</h3>
                <p>Hãy khám phá thực đơn thơm ngon của Jolliebear và thêm món yêu thích!</p>
                <a href="menu.jsp" class="btn btn-primary">Xem thực đơn ngay <i class="fa-solid fa-arrow-right"></i></a>
            </div>

            <!-- Nội dung giỏ hàng -->
            <div class="cart-content" id="cart-content" style="display:none;">
                <div class="cart-items-panel">
                    <div class="cart-items-head">
                        <span>Món ăn</span>
                        <span>Số lượng</span>
                        <span>Thành tiền</span>
                        <span></span>
                    </div>
                    <div class="cart-items-list" id="cart-items-list">
                        <!-- Rendered by cart.js -->
                    </div>
                </div>

                <div class="cart-summary-panel">
                    <h3>Tóm tắt đơn hàng</h3>
                    <div class="cart-summary-row">
                        <span>Tạm tính</span>
                        <span id="cart-subtotal">0 đ</span>
                    </div>
                    <div class="cart-summary-row">
                        <span>Phí giao hàng</span>
                        <span id="cart-shipping">Miễn phí</span>
                    </div>
                    <div class="cart-summary-divider"></div>
                    <div class="cart-summary-row cart-summary-total">
                        <span>Tổng cộng</span>
                        <span id="cart-total">0 đ</span>
                    </div>
                    <button class="cart-checkout-btn" id="cart-checkout-btn">
                        <i class="fa-solid fa-credit-card"></i> ĐẶT HÀNG NGAY
                    </button>
                    <button class="cart-clear-btn" id="cart-clear-btn">
                        <i class="fa-solid fa-trash-can"></i> Xóa toàn bộ giỏ hàng
                    </button>
                    <a href="menu.jsp" class="cart-continue-link">
                        <i class="fa-solid fa-arrow-left"></i> Tiếp tục chọn món
                    </a>
                </div>
            </div>
        </div>
    </section>

<% pageScript = "js/cart.js"; %>
<%@ include file="common/footer.jsp" %>
