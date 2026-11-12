package com.jolliebear.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.jolliebear.dao.OrderDAO;
import com.jolliebear.dao.MenuItemDAO;
import com.jolliebear.model.MenuItem;
import com.jolliebear.model.Order;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/orders")
public class OrderServlet extends HttpServlet {
    private OrderDAO orderDAO;
    private MenuItemDAO menuDAO;

    // Giá cố định cho các combo khuyến mãi (id không nằm trong bảng menu_items)
    private static final Map<String, Double> PROMO_PRICES = new HashMap<>();
    static {
        PROMO_PRICES.put("promo-combo2", 59000.0);
        PROMO_PRICES.put("promo-spicy-b1g1", 39000.0);
    }

    @Override
    public void init() throws ServletException {
        orderDAO = new OrderDAO();
        menuDAO = new MenuItemDAO();
    }

    private boolean isStaffOrAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) return false;
        User u = (User) session.getAttribute("user");
        return "ADMIN".equalsIgnoreCase(u.getRole()) || "STAFF".equalsIgnoreCase(u.getRole());
    }

    private String readBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }

    // Đặt hàng - BẮT BUỘC đăng nhập
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        // Chỉ khách đã đăng nhập mới được đặt hàng / thanh toán
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            json.addProperty("status", "error");
            json.addProperty("message", "Vui lòng đăng nhập để đặt hàng.");
            out.print(gson.toJson(json));
            out.flush();
            return;
        }
        User currentUser = (User) session.getAttribute("user");

        try {
            String rawBody = readBody(request);
            Order order = gson.fromJson(rawBody, Order.class);
            JsonObject body = gson.fromJson(rawBody, JsonObject.class);

            if (order == null || order.getCustomerName() == null || order.getPhone() == null
                    || order.getAddress() == null || order.getPaymentMethod() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json.addProperty("status", "error");
                json.addProperty("message", "Thông tin đơn hàng chưa đầy đủ.");
                out.print(gson.toJson(json));
                out.flush();
                return;
            }

            // TÍNH LẠI tổng tiền + tóm tắt món ở SERVER (giá lấy từ DB / bảng KM, không tin client)
            double total = 0;
            StringBuilder itemsSum = new StringBuilder();
            if (body != null && body.has("cartItems") && body.get("cartItems").isJsonArray()) {
                for (JsonElement el : body.getAsJsonArray("cartItems")) {
                    JsonObject it = el.getAsJsonObject();
                    String id = it.has("id") ? it.get("id").getAsString() : "";
                    int qty = it.has("qty") ? it.get("qty").getAsInt() : 0;
                    if (qty <= 0) continue;
                    String name = it.has("name") ? it.get("name").getAsString() : "";
                    double price;
                    if (id.matches("\\d+")) {
                        MenuItem m = menuDAO.getMenuItemById(Integer.parseInt(id));
                        if (m != null) {
                            price = m.getPrice();
                            if (name.isEmpty()) name = m.getNameVi();
                        } else {
                            price = it.has("price") ? it.get("price").getAsDouble() : 0;
                        }
                    } else if (PROMO_PRICES.containsKey(id)) {
                        price = PROMO_PRICES.get(id);
                    } else {
                        price = it.has("price") ? it.get("price").getAsDouble() : 0;
                    }
                    total += price * qty;
                    if (itemsSum.length() > 0) itemsSum.append(", ");
                    itemsSum.append(name).append(" x").append(qty);
                }
            }

            if (itemsSum.length() > 0) {
                order.setItems(itemsSum.toString());
                order.setTotal(total);
            } else if (order.getItems() == null || order.getItems().trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json.addProperty("status", "error");
                json.addProperty("message", "Giỏ hàng trống, không thể đặt.");
                out.print(gson.toJson(json));
                out.flush();
                return;
            }

            order.setCustomerName(order.getCustomerName().trim());
            order.setPhone(order.getPhone().trim());
            order.setAddress(order.getAddress().trim());
            order.setStatus("Chờ xử lý");
            // Gắn đơn với tài khoản đang đăng nhập (lấy từ session, không tin client)
            order.setUserEmail(currentUser.getEmail());

            boolean ok = orderDAO.createOrder(order);
            if (ok) {
                json.addProperty("status", "success");
                json.addProperty("message", "Đặt hàng thành công! Jolliebear sẽ liên hệ giao hàng sớm nhất.");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                json.addProperty("status", "error");
                json.addProperty("message", "Không thể tạo đơn hàng, vui lòng thử lại.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json.addProperty("status", "error");
            json.addProperty("message", "Lỗi máy chủ: " + e.getMessage());
        }
        out.print(gson.toJson(json));
        out.flush();
    }

    // Xem đơn hàng:
    //  - ?mine=1  -> đơn của chính người đang đăng nhập (khách xem đơn của mình)
    //  - mặc định -> tất cả đơn (chỉ ADMIN/STAFF)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        // Khách xem đơn của chính mình
        if (request.getParameter("mine") != null) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                json.addProperty("status", "error");
                json.addProperty("message", "Vui lòng đăng nhập để xem đơn hàng.");
                out.print(gson.toJson(json));
                out.flush();
                return;
            }
            User u = (User) session.getAttribute("user");
            out.print(gson.toJson(orderDAO.getOrdersByEmail(u.getEmail())));
            out.flush();
            return;
        }

        // Quản trị xem tất cả
        if (!isStaffOrAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            json.addProperty("status", "error");
            json.addProperty("message", "Bạn không có quyền xem đơn hàng.");
            out.print(gson.toJson(json));
            out.flush();
            return;
        }

        out.print(gson.toJson(orderDAO.getAllOrders()));
        out.flush();
    }

    // Cập nhật trạng thái đơn:
    //  - ADMIN/STAFF: đổi sang bất kỳ trạng thái
    //  - Khách: chỉ HỦY đơn của chính mình khi đơn còn "Chờ xử lý"
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            json.addProperty("status", "error");
            json.addProperty("message", "Vui lòng đăng nhập.");
            out.print(gson.toJson(json));
            out.flush();
            return;
        }
        User u = (User) session.getAttribute("user");
        boolean staffAdmin = "ADMIN".equalsIgnoreCase(u.getRole()) || "STAFF".equalsIgnoreCase(u.getRole());

        try {
            JsonObject body = gson.fromJson(readBody(request), JsonObject.class);
            if (body == null || !body.has("id") || !body.has("status")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json.addProperty("status", "error");
                json.addProperty("message", "Thiếu id hoặc trạng thái.");
                out.print(gson.toJson(json));
                out.flush();
                return;
            }
            int id = body.get("id").getAsInt();
            String status = body.get("status").getAsString();

            if (staffAdmin) {
                boolean ok = orderDAO.updateStatus(id, status);
                if (ok) {
                    json.addProperty("status", "success");
                    json.addProperty("message", "Đã cập nhật trạng thái đơn hàng.");
                } else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    json.addProperty("status", "error");
                    json.addProperty("message", "Không thể cập nhật.");
                }
            } else {
                // Khách chỉ được HỦY đơn của chính mình khi còn "Chờ xử lý"
                Order target = orderDAO.findById(id);
                if (target == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    json.addProperty("status", "error");
                    json.addProperty("message", "Không tìm thấy đơn hàng.");
                } else if (target.getUserEmail() == null || !target.getUserEmail().equalsIgnoreCase(u.getEmail())) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    json.addProperty("status", "error");
                    json.addProperty("message", "Đây không phải đơn hàng của bạn.");
                } else if (!"Đã hủy".equals(status)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    json.addProperty("status", "error");
                    json.addProperty("message", "Bạn chỉ có thể hủy đơn hàng của mình.");
                } else if (!"Chờ xử lý".equals(target.getStatus()) && !"Đã xác nhận".equals(target.getStatus())) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    json.addProperty("status", "error");
                    json.addProperty("message", "Chỉ có thể hủy đơn khi đơn chưa được giao (Chờ xử lý / Đã xác nhận).");
                } else {
                    boolean ok = orderDAO.updateStatus(id, "Đã hủy");
                    if (ok) {
                        json.addProperty("status", "success");
                        json.addProperty("message", "Đã hủy đơn hàng #" + id + ".");
                    } else {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        json.addProperty("status", "error");
                        json.addProperty("message", "Không thể hủy đơn, vui lòng thử lại.");
                    }
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            json.addProperty("status", "error");
            json.addProperty("message", "Lỗi máy chủ: " + e.getMessage());
        }
        out.print(gson.toJson(json));
        out.flush();
    }
}
