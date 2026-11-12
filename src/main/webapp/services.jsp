<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Dịch Vụ - Jolliebear"; String activeNav = "services"; %>
<%@ include file="common/header.jsp" %>

    <!-- SERVICES (DỊCH VỤ CỦA CHÚNG TÔI) SECTION -->
    <section class="services-section" id="services">
        <div class="services-container">
            <div class="services-header">
                <span class="service-badge">TRẢI NGHIỆM JOLLIEBEAR</span>
                <h2>DỊCH VỤ CỦA CHÚNG TÔI</h2>
                <p>Jolliebear luôn đồng hành cùng bạn với nhiều hình thức phục vụ tiện lợi nhất</p>
            </div>

            <!-- Services Grid (3x2 Layout) -->
            <div class="services-grid">
                <!-- Service 1 -->
                <div class="service-card">
                    <div class="service-icon-circle">
                        <i class="fa-solid fa-motorcycle"></i>
                    </div>
                    <h3>Giao Hàng Tận Nơi</h3>
                    <p>Đặt món yêu thích và nhận hàng ngay tại nhà chỉ trong 30 phút.</p>
                    <a href="menu.jsp" class="service-link">Đặt giao hàng <i class="fa-solid fa-arrow-right"></i></a>
                </div>

                <!-- Service 2 -->
                <div class="service-card">
                    <div class="service-icon-circle">
                        <i class="fa-solid fa-bag-shopping"></i>
                    </div>
                    <h3>Đặt Đến Lấy</h3>
                    <p>Đặt trước trên app, ghé cửa hàng lấy ngay, không cần chờ đợi.</p>
                    <a href="menu.jsp" class="service-link">Đặt đến lấy <i class="fa-solid fa-arrow-right"></i></a>
                </div>

                <!-- Service 3 -->
                <div class="service-card">
                    <div class="service-icon-circle">
                        <i class="fa-solid fa-masks-theater"></i>
                    </div>
                    <h3>Tổ Chức Tiệc & Sự Kiện</h3>
                    <p>Không gian ấm cúng, phù hợp tổ chức sinh nhật, tiệc nhóm bạn bè.</p>
                    <a href="contact.jsp" class="service-link">Liên hệ đặt tiệc <i class="fa-solid fa-arrow-right"></i></a>
                </div>

                <!-- Service 4 -->
                <div class="service-card">
                    <div class="service-icon-circle">
                        <i class="fa-solid fa-building"></i>
                    </div>
                    <h3>Đặt Suất Ăn Doanh Nghiệp</h3>
                    <p>Giải pháp suất ăn số lượng lớn cho công ty, hội nghị, sự kiện.</p>
                    <a href="contact.jsp" class="service-link">Nhận báo giá <i class="fa-solid fa-arrow-right"></i></a>
                </div>

                <!-- Service 6 -->
                <div class="service-card">
                    <div class="service-icon-circle">
                        <i class="fa-solid fa-headset"></i>
                    </div>
                    <h3>Hỗ Trợ Khách Hàng 24/7</h3>
                    <p>Đội ngũ CSKH luôn sẵn lòng giải đáp mọi thắc mắc của bạn.</p>
                    <a href="contact.jsp" class="service-link">Liên hệ hotline <i class="fa-solid fa-arrow-right"></i></a>
                </div>
            </div>
        </div>
    </section>

<%@ include file="common/footer.jsp" %>
