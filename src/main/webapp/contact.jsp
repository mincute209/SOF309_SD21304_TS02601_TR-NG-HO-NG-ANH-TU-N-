<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Liên Hệ - Jolliebear"; String activeNav = "contact"; %>
<%@ include file="common/header.jsp" %>

    <!-- CONTACT (LIÊN HỆ) SECTION -->
    <section class="contact-section" id="contact">
        <div class="contact-container">
            <div class="contact-header">
                <span class="contact-badge">LIÊN HỆ VỚI JOLLIEBEAR</span>
                <h2>LIÊN HỆ</h2>
                <p>Nếu bạn có thắc mắc, phản hồi hoặc muốn hợp tác, hãy gửi thông tin cho chúng tôi</p>
            </div>
            <div class="contact-content-wrapper">
                <div class="contact-info-panel">
                    <div class="contact-info-card">
                        <i class="fa-solid fa-phone"></i>
                        <div>
                            <h4>Hotline hỗ trợ 24/7</h4>
                            <p>1900 8080 (Miễn phí cuộc gọi)</p>
                        </div>
                    </div>
                    <div class="contact-info-card">
                        <i class="fa-solid fa-envelope"></i>
                        <div>
                            <h4>Email phản hồi</h4>
                            <p>feedback@jolliebear.com.vn</p>
                        </div>
                    </div>
                    <div class="contact-info-card">
                        <i class="fa-solid fa-location-dot"></i>
                        <div>
                            <h4>Trụ sở chính</h4>
                            <p>Quận 1, Thành phố Hồ Chí Minh, Việt Nam</p>
                        </div>
                    </div>
                    <div class="social-contact-wrapper">
                        <h4>Theo dõi chúng tôi</h4>
                        <div class="social-icons">
                            <a href="#"><i class="fa-brands fa-facebook-f"></i></a>
                            <a href="#"><i class="fa-brands fa-instagram"></i></a>
                            <a href="#"><i class="fa-brands fa-youtube"></i></a>
                            <a href="#"><i class="fa-brands fa-tiktok"></i></a>
                        </div>
                    </div>
                </div>
                <div class="contact-form-panel">
                    <form id="contact-form" class="premium-form">
                        <div class="form-row">
                            <div class="form-group-half">
                                <label for="contact-name">Họ và tên *</label>
                                <input type="text" id="contact-name" placeholder="Nguyễn Văn A" required>
                            </div>
                            <div class="form-group-half">
                                <label for="contact-email">Địa chỉ Email *</label>
                                <input type="email" id="contact-email" placeholder="example@email.com" required>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group-half">
                                <label for="contact-phone">Số điện thoại</label>
                                <input type="tel" id="contact-phone" placeholder="0909123456">
                            </div>
                            <div class="form-group-half">
                                <label for="contact-subject">Tiêu đề *</label>
                                <input type="text" id="contact-subject" placeholder="Góp ý dịch vụ/Hợp tác..." required>
                            </div>
                        </div>
                        <div class="form-group-full">
                            <label for="contact-message">Nội dung liên hệ *</label>
                            <textarea id="contact-message" rows="5" placeholder="Nhập tin nhắn của bạn ở đây..." required></textarea>
                        </div>
                        <button type="submit" class="contact-submit-btn">GỬI YÊU CẦU <i class="fa-regular fa-paper-plane"></i></button>
                    </form>
                </div>
            </div>
        </div>
    </section>

<%@ include file="common/footer.jsp" %>
