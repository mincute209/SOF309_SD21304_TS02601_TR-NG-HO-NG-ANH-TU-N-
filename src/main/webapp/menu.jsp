<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Thực Đơn - Jolliebear"; String activeNav = "menu"; %>
<%@ include file="common/header.jsp" %>

    <!-- DYNAMIC MENU PRODUCT LISTING -->
    <section class="featured-section" id="menu">
        <div class="dynamic-menu-container container" style="margin-top: 40px;">
            <div class="section-title text-center">
                <h2 data-i18n="menu_section_title">THỰC ĐƠN JOLLIEBEAR</h2>
                <p data-i18n="menu_section_subtitle">Khám phá danh sách các món ăn ngon khó cưỡng được chế biến từ nguyên liệu tươi ngon nhất.</p>
            </div>

            <div class="menu-filter-controls">
                <div class="menu-search-box">
                    <i class="fa-solid fa-magnifying-glass"></i>
                    <input type="text" id="food-search-input" placeholder="Tìm kiếm món ăn yêu thích..." data-i18n="search_food_placeholder">
                </div>

                <div class="menu-categories-tabs">
                    <button class="food-cat-tab active" data-category="All" data-i18n="cat_all">Tất cả</button>
                    <button class="food-cat-tab" data-category="GaGion" data-i18n="cat_gagion">Gà Giòn</button>
                    <button class="food-cat-tab" data-category="MyY" data-i18n="cat_myy">Mỳ Ý</button>
                    <button class="food-cat-tab" data-category="Burger" data-i18n="cat_burger">Burger</button>
                    <button class="food-cat-tab" data-category="Combo" data-i18n="cat_combo">Combo Tiết Kiệm</button>
                    <button class="food-cat-tab" data-category="NuocTrangMieng" data-i18n="cat_nuoc">Nước & Tráng Miệng</button>
                </div>
            </div>

            <div class="food-products-grid" id="food-items-container">
                <!-- Dynamically populated by main.js -->
            </div>
        </div>
    </section>

<%@ include file="common/footer.jsp" %>
