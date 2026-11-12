<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% String pageTitle = "Tuyển Dụng - Jolliebear"; String activeNav = "jobs"; %>
<%@ include file="common/header.jsp" %>

    <!-- JOBS (TUYỂN DỤNG) SECTION -->
    <section class="jobs-section" id="jobs">
        <div class="jobs-container">
            <div class="jobs-header">
                <span class="jobs-badge">CƠ HỘI VIỆC LÀM</span>
                <h2>TUYỂN DỤNG</h2>
                <p>Gia nhập đại gia đình Jolliebear để cùng nhau lan tỏa niềm vui và phát triển sự nghiệp</p>
            </div>
            <div class="jobs-filter-bar">
                <div class="filter-group">
                    <label for="filter-location"><i class="fa-solid fa-location-dot"></i> Địa điểm</label>
                    <select id="filter-location">
                        <option value="all">Tất cả địa điểm</option>
                        <option value="TP. Hồ Chí Minh">TP. Hồ Chí Minh</option>
                        <option value="Hà Nội">Hà Nội</option>
                    </select>
                </div>
                <div class="filter-group">
                    <label for="filter-dept"><i class="fa-solid fa-briefcase"></i> Bộ phận</label>
                    <select id="filter-dept">
                        <option value="all">Tất cả bộ phận</option>
                        <option value="Vận Hành Cửa Hàng">Vận Hành Cửa Hàng</option>
                        <option value="Quản Lý">Quản Lý</option>
                        <option value="Chế Biến">Chế Biến</option>
                    </select>
                </div>
            </div>
            <div class="jobs-grid" id="jobs-grid-container">
                <!-- Dynamically loaded job postings -->
            </div>
        </div>
    </section>

    <!-- Job Details Modal -->
    <div class="details-modal-overlay" id="job-details-modal">
        <div class="details-modal-card">
            <button class="close-details-btn" id="close-job-details" aria-label="Close modal">&times;</button>
            <div class="job-details-content">
                <div class="job-details-header">
                    <span class="job-modal-dept" id="job-modal-dept"></span>
                    <h2 id="job-modal-title"></h2>
                    <div class="job-modal-meta">
                        <span><i class="fa-solid fa-location-dot"></i> Địa điểm: <span id="job-modal-loc"></span></span>
                        <span><i class="fa-solid fa-money-bill-wave"></i> Lương: <span id="job-modal-salary"></span></span>
                        <span><i class="fa-solid fa-calendar-days"></i> Hạn nộp: <span id="job-modal-deadline"></span></span>
                    </div>
                </div>
                <div class="job-modal-body">
                    <h3>Mô tả công việc</h3>
                    <div id="job-modal-desc"></div>
                    <h3 style="margin-top: 20px;">Yêu cầu công việc</h3>
                    <div id="job-modal-reqs"></div>
                </div>
                <div class="job-modal-footer" style="margin-top: 25px; display: flex; justify-content: flex-end;">
                    <button class="btn btn-primary" id="apply-job-btn">Ứng tuyển ngay <i class="fa-solid fa-paper-plane"></i></button>
                </div>
            </div>
        </div>
    </div>

<%@ include file="common/footer.jsp" %>
