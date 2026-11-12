<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Jolliebear - Giao Hàng Tận Nơi & Thực Đơn Thơm Ngon"; String activeNav = "home"; %>
<%@ include file="common/header.jsp" %>

    <!-- MAIN HERO CAROUSEL -->
    <section class="hero-carousel" id="home">
        <div class="carousel-track-container">
            <ul class="carousel-track">
                <!-- Slide 1 -->
                <li class="carousel-slide current-slide">
                    <div class="slide-content bg-gradient-1">
                        <div class="slide-inner">
                            <div class="slide-text">
                                <span class="badge">BÁN CHẠY NHẤT</span>
                                <h2>Gà Giòn Vui Vẻ</h2>
                                <p>Giòn rụm từng miếng, thơm ngon đậm đà chuẩn vị Jolliebear. Thử ngay hôm nay!</p>
                                <div class="price-tag">
                                    <span class="price">35.000đ</span> <span class="unit">/ Phần</span>
                                </div>
                                <a href="menu.jsp" class="btn btn-primary">Đặt hàng ngay <i class="fa-solid fa-arrow-right"></i></a>
                            </div>
                            <div class="slide-graphic">
                                <div class="food-plate chicken-plate">
                                    <span class="graphic-shadow"></span>
                                    <i class="fa-solid fa-drumstick-bite graphic-food-icon animate-float"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
                <!-- Slide 2 -->
                <li class="carousel-slide">
                    <div class="slide-content bg-gradient-2">
                        <div class="slide-inner">
                            <div class="slide-text">
                                <span class="badge">MỚI RA MẮT</span>
                                <h2>Mỳ Ý Sốt Bò Bằm</h2>
                                <p>Sốt bò bằm chua ngọt đậm đà hòa quyện cùng phô mai bào béo ngậy khó cưỡng.</p>
                                <div class="price-tag">
                                    <span class="price">145.000đ</span> <span class="old-price">180.000đ</span>
                                </div>
                                <a href="menu.jsp" class="btn btn-primary">Khám phá ngay <i class="fa-solid fa-arrow-right"></i></a>
                            </div>
                            <div class="slide-graphic">
                                <div class="food-plate spaghetti-plate">
                                    <span class="graphic-shadow"></span>
                                    <i class="fa-solid fa-bowl-food graphic-food-icon animate-float"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
                <!-- Slide 3 -->
                <li class="carousel-slide">
                    <div class="slide-content bg-gradient-3">
                        <div class="slide-inner">
                            <div class="slide-text">
                                <span class="badge">ƯU ĐÃI KHỦNG</span>
                                <h2>Combo Gà Sốt Cay</h2>
                                <p>Vị cay nồng nàn, ngọt dịu đặc trưng thấm đượm trong từng thớ thịt gà mọng nước.</p>
                                <div class="price-tag">
                                    <span class="price">145.000đ</span> <span class="old-price">178.000đ</span>
                                </div>
                                <a href="menu.jsp" class="btn btn-primary">Nhận ưu đãi <i class="fa-solid fa-arrow-right"></i></a>
                            </div>
                            <div class="slide-graphic">
                                <div class="food-plate spicy-plate">
                                    <span class="graphic-shadow"></span>
                                    <i class="fa-solid fa-fire graphic-food-icon animate-float"></i>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
            </ul>
        </div>

        <!-- Carousel Indicators -->
        <div class="carousel-nav">
            <button class="carousel-indicator current-slide-indicator" aria-label="Slide 1"></button>
            <button class="carousel-indicator" aria-label="Slide 2"></button>
            <button class="carousel-indicator" aria-label="Slide 3"></button>
        </div>
    </section>

    <!-- SERVICE SELECTOR & SEARCH BAR -->
    <section class="order-section">
        <div class="order-container">
            <!-- Toggle buttons -->
            <div class="order-toggle-wrapper">
                <button class="toggle-btn active" id="delivery-toggle">
                    <i class="fa-solid fa-motorcycle"></i> GIAO HÀNG TẬN NƠI
                </button>
                <button class="toggle-btn" id="pickup-toggle">
                    <i class="fa-solid fa-store"></i> ĐẶT ĐẾN LẤY
                </button>
            </div>

            <!-- Address Input Box -->
            <div class="address-search-box">
                <div class="input-wrapper">
                    <i class="fa-solid fa-location-dot input-loc-icon"></i>
                    <input type="text" id="address-input" placeholder="Nhập địa chỉ giao hàng của bạn..." aria-label="Nhập địa chỉ giao hàng">
                    <button class="search-btn" aria-label="Tìm kiếm địa chỉ">
                        <i class="fa-solid fa-magnifying-glass"></i>
                    </button>
                </div>
            </div>
        </div>
    </section>

    <!-- "ĂN GÌ HÔM NAY" FEATURED SECTION -->
    <section class="featured-section">
        <div class="featured-container">
            <!-- Left Promo Card -->
            <div class="promo-brand-card">
                <div class="promo-content">
                    <div class="brand-mascot-wrapper">
                        <i class="fa-solid fa-paw mascot-icon"></i>
                    </div>
                    <h2>ĂN GÌ HÔM NAY</h2>
                    <p>Thực đơn Jolliebear đa dạng và phong phú, có rất nhiều sự lựa chọn cho bạn, gia đình và bạn bè.</p>
                </div>
            </div>

            <!-- Food Category Cards (Wood Textured Backgrounds) -->
            <div class="category-cards-grid">
                <!-- Card 1: Gà Giòn -->
                <div class="category-card wood-card">
                    <div class="card-image-wrapper">
                        <div class="food-mock-img chicken-mock">
                            <i class="fa-solid fa-drumstick-bite food-mock-icon"></i>
                        </div>
                    </div>
                    <div class="card-info">
                        <h3>Gà Giòn Vui Vẻ</h3>
                        <p>Thơm giòn rụm bên ngoài, mọng nước bên trong.</p>
                        <a href="menu.jsp" class="card-link">Xem thực đơn <i class="fa-solid fa-chevron-right"></i></a>
                    </div>
                </div>

                <!-- Card 2: Gà Sốt Cay -->
                <div class="category-card wood-card">
                    <div class="card-image-wrapper">
                        <div class="food-mock-img spicy-mock">
                            <i class="fa-solid fa-fire-flame-curved food-mock-icon"></i>
                        </div>
                    </div>
                    <div class="card-info">
                        <h3>Gà Sốt Cay & Ngọt</h3>
                        <p>Sốt đặc trưng thấm sâu cay tê kích thích vị giác.</p>
                        <a href="menu.jsp" class="card-link">Xem thực đơn <i class="fa-solid fa-chevron-right"></i></a>
                    </div>
                </div>

                <!-- Card 3: Mỳ Ý -->
                <div class="category-card wood-card">
                    <div class="card-image-wrapper">
                        <div class="food-mock-img spaghetti-mock">
                            <i class="fa-solid fa-bowl-food food-mock-icon"></i>
                        </div>
                    </div>
                    <div class="card-info">
                        <h3>Mỳ Ý Sốt Bò Bằm</h3>
                        <p>Mỳ dai mềm kết hợp sốt thịt băm đậm đà.</p>
                        <a href="menu.jsp" class="card-link">Xem thực đơn <i class="fa-solid fa-chevron-right"></i></a>
                    </div>
                </div>

                <!-- Card 4: Kem & Tráng Miệng -->
                <div class="category-card wood-card">
                    <div class="card-image-wrapper">
                        <div class="food-mock-img dessert-mock">
                            <i class="fa-solid fa-ice-cream food-mock-icon"></i>
                        </div>
                    </div>
                    <div class="card-info">
                        <h3>Tráng Miệng Ngọt Ngào</h3>
                        <p>Kem tươi mát lạnh, giải nhiệt ngày hè.</p>
                        <a href="menu.jsp" class="card-link">Xem thực đơn <i class="fa-solid fa-chevron-right"></i></a>
                    </div>
                </div>
            </div>
        </div>
    </section>

<%@ include file="common/footer.jsp" %>
