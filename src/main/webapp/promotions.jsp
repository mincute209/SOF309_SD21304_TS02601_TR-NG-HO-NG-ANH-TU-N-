<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Khuyến Mãi - Jolliebear"; String activeNav = "promotions"; %>
<%@ include file="common/header.jsp" %>

    <!-- PROMOTIONS (KHUYẾN MÃI HOT) SECTION -->
    <section class="promotions-section" id="promotions">
        <div class="promotions-container">
            <div class="promotions-header">
                <span class="promo-badge">ƯU ĐÃI HÔM NAY</span>
                <h2>KHUYẾN MÃI HOT</h2>
                <p>Đừng bỏ lỡ những ưu đãi giòn rụm, tiết kiệm siêu đã dành riêng cho bạn</p>
            </div>

            <!-- Flash Sale Banner -->
            <div class="flash-sale-banner">
                <div class="flash-sale-left">
                    <div class="flash-sale-icon-circle">
                        <i class="fa-solid fa-bolt"></i>
                    </div>
                    <div class="flash-sale-text">
                        <h3>FLASH SALE CUỐI TUẦN</h3>
                        <p>Giảm ngay đến 50% cho mọi combo gà giòn</p>
                    </div>
                </div>
                <div class="flash-sale-right">
                    <div class="countdown-container">
                        <div class="countdown-box">
                            <span class="countdown-num" id="hours">52</span>
                            <span class="countdown-label">GIỜ</span>
                        </div>
                        <div class="countdown-box">
                            <span class="countdown-num" id="minutes">05</span>
                            <span class="countdown-label">PHÚT</span>
                        </div>
                        <div class="countdown-box">
                            <span class="countdown-num" id="seconds">48</span>
                            <span class="countdown-label">GIÂY</span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Promotion Cards Grid -->
            <div class="promo-cards-grid">
                <!-- Card 1: Combo Tiết Kiệm -->
                <div class="promo-card">
                    <div class="card-accent-top bg-red">
                        <i class="fa-solid fa-bucket card-illustration"></i>
                        <span class="card-badge-tag">-50%</span>
                    </div>
                    <div class="promo-card-body">
                        <span class="promo-card-category text-red">COMBO TIẾT KIỆM</span>
                        <h3>Combo Gà Giòn 2 Miếng</h3>
                        <p class="promo-card-desc">Bao gồm 2 miếng gà giòn, 1 cơm, 1 nước ngọt size vừa.</p>
                        <div class="promo-card-price-row">
                            <span class="promo-new-price">59.000đ</span>
                            <span class="promo-old-price">118.000đ</span>
                        </div>
                        <button class="promo-order-btn" data-promo-id="promo-combo2" data-promo-name="Combo Gà Giòn 2 Miếng" data-promo-price="59000">Đặt ngay</button>
                    </div>
                </div>

                <!-- Card 2: Gà Sốt Cay -->
                <div class="promo-card">
                    <div class="card-accent-top bg-orange">
                        <i class="fa-solid fa-pepper-hot card-illustration"></i>
                        <span class="card-badge-tag">MUA 1 TẶNG 1</span>
                    </div>
                    <div class="promo-card-body">
                        <span class="promo-card-category text-orange">ƯU ĐÃI THỨ 4</span>
                        <h3>Gà Sốt Cay Mua 1 Tặng 1</h3>
                        <p class="promo-card-desc">Áp dụng cho đơn đặt qua ứng dụng, thứ 4 hàng tuần.</p>
                        <div class="promo-card-price-row">
                            <span class="promo-new-price">39.000đ</span>
                            <span class="promo-old-price">78.000đ</span>
                        </div>
                        <button class="promo-order-btn" data-promo-id="promo-spicy-b1g1" data-promo-name="Combo Gà Sốt Cay (Mua 1 Tặng 1)" data-promo-price="39000">Đặt ngay</button>
                    </div>
                </div>

                <!-- Card 3: Sinh Viên -->
                <div class="promo-card">
                    <div class="card-accent-top bg-blue">
                        <i class="fa-solid fa-graduation-cap card-illustration"></i>
                        <span class="card-badge-tag font-sm">SINH VIÊN</span>
                    </div>
                    <div class="promo-card-body">
                        <span class="promo-card-category text-blue">ƯU ĐÃI HỌC SINH SV</span>
                        <h3>Giảm 20% Toàn Thực Đơn</h3>
                        <p class="promo-card-desc">Chỉ cần xuất trình thẻ học sinh, sinh viên khi thanh toán.</p>
                        <div class="promo-card-price-row">
                            <span class="promo-new-price color-red">Giảm 20%</span>
                        </div>
                        <button class="promo-order-btn text-dark" data-nav="menu.jsp">Xem thực đơn</button>
                    </div>
                </div>

                <!-- Card 4: Freeship -->
                <div class="promo-card">
                    <div class="card-accent-top bg-navy">
                        <i class="fa-solid fa-motorcycle card-illustration"></i>
                        <span class="card-badge-tag">FREESHIP</span>
                    </div>
                    <div class="promo-card-body">
                        <span class="promo-card-category text-navy">GIAO HÀNG TẬN NƠI</span>
                        <h3>Miễn Phí Giao Hàng</h3>
                        <p class="promo-card-desc">Cho đơn hàng từ 99.000đ trong bán kính 3km.</p>
                        <div class="promo-card-price-row">
                            <span class="promo-new-price color-red">0đ Ship</span>
                        </div>
                        <button class="promo-order-btn" data-nav="menu.jsp">Đặt món ngay</button>
                    </div>
                </div>
            </div>
        </div>
    </section>

<%@ include file="common/footer.jsp" %>
