<%--
    Header dùng chung cho toàn bộ trang JSP của Jolliebear.
    Trang cha PHẢI khai báo trước khi include:
        <% String pageTitle = "..."; String activeNav = "menu"; %>
    activeNav nhận 1 trong: home | menu | promotions | services | news | stores | contact | jobs
--%>
<%
    if (pageTitle == null) pageTitle = "Jolliebear - Giao Hàng Tận Nơi & Thực Đơn Thơm Ngon";
    if (activeNav == null) activeNav = "home";
    String ctx = request.getContextPath();
    String pageScript = null; // Trang con có thể gán trước khi include footer.jsp
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= pageTitle %></title>
    <!-- Google Fonts: Outfit -->
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <!-- Font Awesome for Icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="<%= ctx %>/css/style.css">
</head>
<body>

    <!-- TOP UTILITY BAR -->
    <div class="utility-bar">
        <div class="utility-container">
            <div class="utility-left">
                <span data-i18n="welcome_msg">Chào mừng bạn đến với Jolliebear!</span>
            </div>
            <div class="utility-right">
                <div class="language-selector">
                    <span class="lang-item active" id="lang-vi">
                        <span class="flag-icon vi-flag"></span> VN
                    </span>
                    <span class="lang-item" id="lang-en">
                        <span class="flag-icon en-flag"></span> EN
                    </span>
                </div>
                <div class="auth-btn-group" id="auth-header-actions">
                    <!-- Guest (Logged Out) State -->
                    <div class="auth-logged-out">
                        <i class="fa-regular fa-user"></i>
                        <a href="#register" class="auth-link" id="register-trigger" data-i18n="btn_register">ĐĂNG KÝ</a>
                        <span class="auth-divider">/</span>
                        <a href="#login" class="auth-link" id="login-trigger" data-i18n="btn_login">ĐĂNG NHẬP</a>
                    </div>
                    <!-- User (Logged In) State -->
                    <div class="auth-logged-in hidden">
                        <i class="fa-solid fa-circle-user"></i>
                        <span class="user-welcome" id="user-display-name">Chào, Khách!</span>
                        <a href="<%= ctx %>/dashboard.jsp" class="auth-link hidden" id="dashboard-link" style="margin-left: 10px; color: #ffc107; font-weight: 600;" data-i18n="btn_admin_portal"><i class="fa-solid fa-gauge"></i> QUẢN TRỊ</a>
                        <span class="auth-divider">/</span>
                        <a href="<%= ctx %>/profile.jsp" class="auth-link" id="profile-link"><i class="fa-solid fa-user"></i> TÀI KHOẢN</a>
                        <span class="auth-divider">/</span>
                        <a href="#changepw" class="auth-link" id="changepw-trigger"><i class="fa-solid fa-key"></i> ĐỔI MK</a>
                        <span class="auth-divider">/</span>
                        <a href="#logout" class="auth-link" id="logout-trigger" data-i18n="btn_logout">ĐĂNG XUẤT</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- MAIN HEADER & NAVIGATION -->
    <header class="main-header">
        <div class="header-container">
            <!-- Brand Logo -->
            <a href="<%= ctx %>/index.jsp" class="logo-area">
                <div class="logo-circle">
                    <i class="fa-solid fa-paw logo-icon"></i>
                </div>
                <div class="logo-text">
                    <span class="logo-brand">Jolliebear</span>
                    <span class="logo-slogan">I'm lovin' it</span>
                </div>
            </a>

            <!-- Navigation Links -->
            <nav class="navbar">
                <ul class="nav-menu">
                    <li class="nav-item"><a href="<%= ctx %>/index.jsp" class="nav-link <%= "home".equals(activeNav) ? "active" : "" %>" data-i18n="nav_home">TRANG CHỦ</a></li>
                    <li class="nav-item"><a href="<%= ctx %>/menu.jsp" class="nav-link <%= "menu".equals(activeNav) ? "active" : "" %>" data-i18n="nav_menu">THỰC ĐƠN</a></li>
                    <li class="nav-item"><a href="<%= ctx %>/promotions.jsp" class="nav-link <%= "promotions".equals(activeNav) ? "active" : "" %>" data-i18n="nav_promotions">KHUYẾN MÃI</a></li>
                    <li class="nav-item"><a href="<%= ctx %>/services.jsp" class="nav-link <%= "services".equals(activeNav) ? "active" : "" %>" data-i18n="nav_services">DỊCH VỤ</a></li>
                    <li class="nav-item"><a href="<%= ctx %>/news.jsp" class="nav-link <%= "news".equals(activeNav) ? "active" : "" %>" data-i18n="nav_news">TIN TỨC</a></li>
                    <li class="nav-item"><a href="<%= ctx %>/stores.jsp" class="nav-link <%= "stores".equals(activeNav) ? "active" : "" %>" data-i18n="nav_stores">CỬA HÀNG</a></li>
                    <li class="nav-item"><a href="<%= ctx %>/contact.jsp" class="nav-link <%= "contact".equals(activeNav) ? "active" : "" %>" data-i18n="nav_contact">LIÊN HỆ</a></li>
                    <li class="nav-item"><a href="<%= ctx %>/jobs.jsp" class="nav-link <%= "jobs".equals(activeNav) ? "active" : "" %>" data-i18n="nav_jobs">TUYỂN DỤNG</a></li>
                </ul>
            </nav>

            <!-- Cart + Mobile Toggle -->
            <div class="header-actions">
                <button type="button" class="cart-icon-btn" id="cart-toggle-btn" aria-label="Giỏ hàng" title="Giỏ hàng">
                    <i class="fa-solid fa-cart-shopping"></i>
                    <span class="cart-count-badge" id="cart-count">0</span>
                </button>
                <button class="mobile-nav-toggle" aria-label="Toggle navigation">
                    <i class="fa-solid fa-bars"></i>
                </button>
            </div>
        </div>
    </header>
