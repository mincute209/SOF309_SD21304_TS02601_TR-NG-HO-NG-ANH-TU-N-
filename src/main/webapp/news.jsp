<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Tin Tức - Jolliebear"; String activeNav = "news"; %>
<%@ include file="common/header.jsp" %>

    <!-- NEWS (TIN TỨC) SECTION -->
    <section class="news-section" id="news">
        <div class="news-container">
            <div class="news-header">
                <span class="news-badge">TIN TỨC JOLLIEBEAR</span>
                <h2>TIN TỨC & KHUYẾN MÃI</h2>
                <p>Cập nhật những hoạt động mới nhất, khuyến mãi hấp dẫn và câu chuyện thương hiệu của chúng tôi</p>
            </div>
            <div class="news-grid" id="news-grid-container">
                <!-- Dynamically loaded news articles -->
            </div>
        </div>
    </section>

    <!-- News Details Modal -->
    <div class="details-modal-overlay" id="news-details-modal">
        <div class="details-modal-card">
            <button class="close-details-btn" id="close-news-details" aria-label="Close modal">&times;</button>
            <div class="news-details-image-wrapper">
                <img id="news-modal-img" src="" alt="News Image">
            </div>
            <div class="news-details-content">
                <span class="news-modal-date" id="news-modal-date"></span>
                <h2 id="news-modal-title"></h2>
                <div class="news-modal-text" id="news-modal-text"></div>
            </div>
        </div>
    </div>

<%@ include file="common/footer.jsp" %>
