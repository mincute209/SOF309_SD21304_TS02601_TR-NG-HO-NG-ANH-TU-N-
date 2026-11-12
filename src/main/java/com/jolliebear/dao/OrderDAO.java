package com.jolliebear.dao;

import com.jolliebear.model.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Tạo đơn hàng mới (khách đặt từ web chính)
    public synchronized boolean createOrder(Order o) {
        String sql = "INSERT INTO orders (user_email, customer_name, phone, address, payment_method, items, total, note, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getUserEmail());
            ps.setString(2, o.getCustomerName());
            ps.setString(3, o.getPhone());
            ps.setString(4, o.getAddress());
            ps.setString(5, o.getPaymentMethod());
            ps.setString(6, o.getItems());
            ps.setDouble(7, o.getTotal());
            ps.setString(8, o.getNote());
            ps.setString(9, o.getStatus() != null ? o.getStatus() : "Chờ xử lý");
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Order mapRow(ResultSet rs, SimpleDateFormat fmt) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserEmail(rs.getString("user_email"));
        o.setCustomerName(rs.getString("customer_name"));
        o.setPhone(rs.getString("phone"));
        o.setAddress(rs.getString("address"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setItems(rs.getString("items"));
        o.setTotal(rs.getDouble("total"));
        o.setNote(rs.getString("note"));
        o.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        o.setCreatedAt(ts != null ? fmt.format(ts) : "");
        return o;
    }

    // Lấy đơn hàng của 1 khách theo email (cho trang cá nhân)
    public synchronized List<Order> getOrdersByEmail(String email) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT id, user_email, customer_name, phone, address, payment_method, items, total, note, status, created_at "
                   + "FROM orders WHERE user_email = ? ORDER BY created_at DESC";
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs, fmt));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tìm 1 đơn theo id (để kiểm tra chủ đơn + trạng thái trước khi hủy)
    public synchronized Order findById(int id) {
        String sql = "SELECT id, user_email, customer_name, phone, address, payment_method, items, total, note, status, created_at "
                   + "FROM orders WHERE id = ?";
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs, fmt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy tất cả đơn hàng (cho trang quản trị) - mới nhất lên đầu
    public synchronized List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT id, user_email, customer_name, phone, address, payment_method, items, total, note, status, created_at "
                   + "FROM orders ORDER BY created_at DESC";
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs, fmt));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Cập nhật trạng thái đơn hàng (cho quản trị)
    public synchronized boolean updateStatus(int id, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
