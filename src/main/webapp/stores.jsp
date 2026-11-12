<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Cửa Hàng - Jolliebear"; String activeNav = "stores"; %>
<%@ include file="common/header.jsp" %>

    <!-- STORES (CỬA HÀNG) SECTION -->
    <section class="stores-section" id="stores">
        <div class="stores-container">
            <div class="stores-header">
                <span class="store-badge">TÌM CỬA HÀNG</span>
                <h2>HỆ THỐNG CỬA HÀNG</h2>
                <p>Ghé thăm chi nhánh Jolliebear gần nhất để tận hưởng không gian ngập tràn niềm vui</p>
            </div>
            <div class="stores-content-wrapper">
                <div class="store-search-panel">
                    <div class="store-search-box">
                        <i class="fa-solid fa-magnifying-glass"></i>
                        <input type="text" id="store-search-input" placeholder="Nhập tên hoặc địa chỉ cửa hàng...">
                    </div>
                    <div class="store-list" id="store-list-container">
                        <!-- Dynamically loaded stores list -->
                    </div>
                </div>
                <div class="store-map-view">
                    <iframe id="store-map-iframe" src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3919.4853036662493!2d106.69248437590487!3d10.774092559231604!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x31752f3f501062b1%3A0x67394b9f02931102!2zMTIwIEzDqiBMYWksIELhur9uIFRow6BuaCwgUXXhuq1uIDEsIEjhu5MgQ2jDrSBNaW5oLCBWaeG7h3QN!5e0!3m2!1svi!2s!4v1689400000000!5m2!1svi!2s" width="100%" height="450" style="border:0;" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
                </div>
            </div>
        </div>
    </section>

<%@ include file="common/footer.jsp" %>
