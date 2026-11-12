package com.jolliebear.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.jolliebear.dao.DBConnection;
import com.jolliebear.dao.UserDAO;
import com.jolliebear.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@WebServlet("/api/admin")
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return false;
        }
        User user = (User) session.getAttribute("user");
        return "ADMIN".equalsIgnoreCase(user.getRole());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Vui lòng đăng nhập.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        
        // Both STAFF and ADMIN can view stats, but only ADMIN can view users list
        String action = request.getParameter("action");

        // Doanh thu theo khoảng thời gian: range = 7 / 30 / 60 / 90 / all
        if ("revenue".equalsIgnoreCase(action)) {
            if (!"STAFF".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Bạn không có quyền xem doanh thu.");
                out.print(gson.toJson(jsonResponse));
                return;
            }
            String range = request.getParameter("range");
            if (range == null || range.isEmpty()) range = "7";
            JsonArray series = new JsonArray();
            try (Connection conn = DBConnection.getConnection()) {
                if ("all".equalsIgnoreCase(range)) {
                    // Gom theo THÁNG từ đơn cũ nhất -> hiện tại
                    java.util.Map<String, Double> map = new java.util.HashMap<>();
                    java.time.YearMonth minYm = null;
                    try (Statement st = conn.createStatement();
                         ResultSet rs = st.executeQuery(
                            "SELECT FORMAT(created_at,'yyyy-MM') ym, SUM(total) s FROM orders " +
                            "WHERE status <> N'Đã hủy' GROUP BY FORMAT(created_at,'yyyy-MM') ORDER BY ym")) {
                        while (rs.next()) {
                            String ym = rs.getString("ym");
                            if (ym == null) continue;
                            map.put(ym, rs.getDouble("s"));
                            java.time.YearMonth y = java.time.YearMonth.parse(ym);
                            if (minYm == null) minYm = y;
                        }
                    }
                    java.time.YearMonth cur = java.time.YearMonth.now();
                    if (minYm == null) minYm = cur;
                    for (java.time.YearMonth m = minYm; !m.isAfter(cur); m = m.plusMonths(1)) {
                        JsonObject o = new JsonObject();
                        o.addProperty("date", String.format("%02d/%d", m.getMonthValue(), m.getYear()));
                        o.addProperty("amount", map.getOrDefault(m.toString(), 0.0));
                        series.add(o);
                    }
                } else {
                    int days;
                    try { days = Integer.parseInt(range); } catch (Exception e) { days = 7; }
                    if (days < 1) days = 7;
                    java.util.Map<String, Double> map = new java.util.HashMap<>();
                    try (java.sql.PreparedStatement ps = conn.prepareStatement(
                            "SELECT CAST(created_at AS DATE) d, SUM(total) s FROM orders " +
                            "WHERE status <> N'Đã hủy' AND created_at >= CAST(DATEADD(day, ?, GETDATE()) AS DATE) " +
                            "GROUP BY CAST(created_at AS DATE)")) {
                        ps.setInt(1, -(days - 1));
                        try (ResultSet rs = ps.executeQuery()) {
                            while (rs.next()) {
                                java.sql.Date d = rs.getDate("d");
                                if (d != null) map.put(d.toLocalDate().toString(), rs.getDouble("s"));
                            }
                        }
                    }
                    java.time.LocalDate today = java.time.LocalDate.now();
                    java.time.format.DateTimeFormatter lbl = java.time.format.DateTimeFormatter.ofPattern("dd/MM");
                    for (int i = days - 1; i >= 0; i--) {
                        java.time.LocalDate day = today.minusDays(i);
                        JsonObject o = new JsonObject();
                        o.addProperty("date", day.format(lbl));
                        o.addProperty("amount", map.getOrDefault(day.toString(), 0.0));
                        series.add(o);
                    }
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Lỗi CSDL: " + e.getMessage());
                out.print(gson.toJson(jsonResponse));
                out.flush();
                return;
            }
            jsonResponse.addProperty("status", "success");
            jsonResponse.add("series", series);
            out.print(gson.toJson(jsonResponse));
            out.flush();
            return;
        }

        if ("stats".equalsIgnoreCase(action)) {
            if (!"STAFF".equalsIgnoreCase(role) && !"ADMIN".equalsIgnoreCase(role)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Bạn không có quyền truy cập thống kê.");
                out.print(gson.toJson(jsonResponse));
                return;
            }
            
            // Get stats
            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement()) {
                
                int usersCount = 0;
                int menuCount = 0;
                int newsCount = 0;
                int storesCount = 0;
                int jobsCount = 0;
                int unreadContacts = 0;

                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role IN ('STAFF','ADMIN')")) {
                    if (rs.next()) usersCount = rs.getInt(1);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM menu_items")) {
                    if (rs.next()) menuCount = rs.getInt(1);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM news")) {
                    if (rs.next()) newsCount = rs.getInt(1);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM stores")) {
                    if (rs.next()) storesCount = rs.getInt(1);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM jobs")) {
                    if (rs.next()) jobsCount = rs.getInt(1);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM contacts WHERE is_read = 0")) {
                    if (rs.next()) unreadContacts = rs.getInt(1);
                }

                // --- THỐNG KÊ ĐƠN HÀNG & DOANH THU ---
                double revenue = 0;
                int ordersTotal = 0, ordersToday = 0, ordersPending = 0;
                try (ResultSet rs = stmt.executeQuery("SELECT ISNULL(SUM(total),0) FROM orders WHERE status <> N'Đã hủy'")) {
                    if (rs.next()) revenue = rs.getDouble(1);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM orders")) {
                    if (rs.next()) ordersTotal = rs.getInt(1);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM orders WHERE CAST(created_at AS DATE) = CAST(GETDATE() AS DATE)")) {
                    if (rs.next()) ordersToday = rs.getInt(1);
                }
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM orders WHERE status = N'Chờ xử lý'")) {
                    if (rs.next()) ordersPending = rs.getInt(1);
                }

                // Doanh thu 7 ngày gần nhất
                java.util.Map<String, Double> revMap = new java.util.HashMap<>();
                try (ResultSet rs = stmt.executeQuery(
                        "SELECT CAST(created_at AS DATE) d, SUM(total) s FROM orders " +
                        "WHERE status <> N'Đã hủy' AND created_at >= CAST(DATEADD(day,-6,GETDATE()) AS DATE) " +
                        "GROUP BY CAST(created_at AS DATE)")) {
                    while (rs.next()) {
                        java.sql.Date d = rs.getDate("d");
                        if (d != null) revMap.put(d.toLocalDate().toString(), rs.getDouble("s"));
                    }
                }
                JsonArray rev7 = new JsonArray();
                java.time.LocalDate today = java.time.LocalDate.now();
                java.time.format.DateTimeFormatter lbl = java.time.format.DateTimeFormatter.ofPattern("dd/MM");
                for (int i = 6; i >= 0; i--) {
                    java.time.LocalDate day = today.minusDays(i);
                    JsonObject o = new JsonObject();
                    o.addProperty("date", day.format(lbl));
                    o.addProperty("amount", revMap.getOrDefault(day.toString(), 0.0));
                    rev7.add(o);
                }

                // Số đơn theo trạng thái
                JsonObject byStatus = new JsonObject();
                try (ResultSet rs = stmt.executeQuery("SELECT status, COUNT(*) c FROM orders GROUP BY status")) {
                    while (rs.next()) byStatus.addProperty(rs.getString("status"), rs.getInt("c"));
                }

                JsonObject stats = new JsonObject();
                stats.addProperty("users", usersCount);
                stats.addProperty("menu", menuCount);
                stats.addProperty("news", newsCount);
                stats.addProperty("stores", storesCount);
                stats.addProperty("jobs", jobsCount);
                stats.addProperty("unreadContacts", unreadContacts);
                stats.addProperty("revenue", revenue);
                stats.addProperty("ordersTotal", ordersTotal);
                stats.addProperty("ordersToday", ordersToday);
                stats.addProperty("ordersPending", ordersPending);
                stats.add("revenue7days", rev7);
                stats.add("ordersByStatus", byStatus);

                jsonResponse.addProperty("status", "success");
                jsonResponse.add("stats", stats);
                out.print(gson.toJson(jsonResponse));
                
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Lỗi CSDL: " + e.getMessage());
                out.print(gson.toJson(jsonResponse));
            }
        } else {
            // ADMIN only: list all users
            if (!"ADMIN".equalsIgnoreCase(role)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Bạn không có quyền thực hiện hành động này.");
                out.print(gson.toJson(jsonResponse));
                return;
            }
            List<User> usersList = userDAO.getStaffAndAdmins();
            out.print(gson.toJson(usersList));
        }
        out.flush();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        if (!isAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Yêu cầu quyền ADMIN.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            // --- Cấp quyền NHÂN VIÊN cho 1 tài khoản đã tồn tại (theo email) ---
            JsonObject body = gson.fromJson(sb.toString(), JsonObject.class);
            if (body != null && body.has("action") && "promoteStaff".equalsIgnoreCase(body.get("action").getAsString())) {
                String targetEmail = body.has("email") ? body.get("email").getAsString().trim() : "";
                if (targetEmail.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Vui lòng nhập email.");
                    out.print(gson.toJson(jsonResponse));
                    out.flush();
                    return;
                }
                User target = userDAO.findByEmail(targetEmail);
                if (target == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Chưa có tài khoản nào dùng email này. Người đó cần đăng ký trước.");
                } else if ("ADMIN".equalsIgnoreCase(target.getRole())) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Tài khoản này là Quản trị viên, không thể đổi thành nhân viên.");
                } else if ("STAFF".equalsIgnoreCase(target.getRole())) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    jsonResponse.addProperty("status", "error");
                    jsonResponse.addProperty("message", "Tài khoản này đã là nhân viên rồi.");
                } else {
                    boolean ok = userDAO.updateUserRole(targetEmail, "STAFF");
                    if (ok) {
                        jsonResponse.addProperty("status", "success");
                        jsonResponse.addProperty("message", "Đã cấp quyền Nhân viên cho " + target.getName() + " (" + targetEmail + ").");
                    } else {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        jsonResponse.addProperty("status", "error");
                        jsonResponse.addProperty("message", "Không thể cấp quyền, vui lòng thử lại.");
                    }
                }
                out.print(gson.toJson(jsonResponse));
                out.flush();
                return;
            }

            User user = gson.fromJson(sb.toString(), User.class);
            if (user == null || user.getEmail() == null || user.getPhone() == null
                    || user.getPassword() == null || user.getName() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Dữ liệu không đầy đủ.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            boolean success = userDAO.saveUser(user);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã tạo tài khoản thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Email hoặc số điện thoại đã tồn tại.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Lỗi: " + e.getMessage());
        }
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        if (!isAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Yêu cầu quyền ADMIN.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            JsonObject dataObj = gson.fromJson(sb.toString(), JsonObject.class);
            if (dataObj == null || !dataObj.has("email") || !dataObj.has("role") 
                    || !dataObj.has("name") || !dataObj.has("phone")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Dữ liệu không hợp lệ.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            String oldEmail = dataObj.get("email").getAsString();
            String name = dataObj.get("name").getAsString();
            String phone = dataObj.get("phone").getAsString();
            String role = dataObj.get("role").getAsString();
            String password = dataObj.has("password") ? dataObj.get("password").getAsString() : "";
            
            // Build user object
            User user = new User(name, phone, oldEmail, password, role);
            if (dataObj.has("newEmail") && !dataObj.get("newEmail").getAsString().isEmpty()) {
                user.setEmail(dataObj.get("newEmail").getAsString());
            }

            boolean success = userDAO.updateUser(user, oldEmail);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã cập nhật tài khoản thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể cập nhật tài khoản.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Lỗi: " + e.getMessage());
        }
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        if (!isAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Yêu cầu quyền ADMIN.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        String email = request.getParameter("email");
        if (email == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Thiếu email tài khoản cần xóa.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        // Prevent admin from deleting themselves
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getEmail().equalsIgnoreCase(email)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Bạn không thể tự xóa tài khoản của chính mình.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        boolean success = userDAO.deleteUser(email);
        if (success) {
            jsonResponse.addProperty("status", "success");
            jsonResponse.addProperty("message", "Đã xóa tài khoản thành công!");
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Không thể xóa tài khoản.");
        }
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
