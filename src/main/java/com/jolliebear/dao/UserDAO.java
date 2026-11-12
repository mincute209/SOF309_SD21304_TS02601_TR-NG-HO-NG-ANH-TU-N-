package com.jolliebear.dao;

import com.jolliebear.model.User;
import com.jolliebear.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Keep constructor with String parameter for compatibility with Servlet init methods
    public UserDAO(String contextPath) {
        // No longer need JSON file path, DBConnection handles SQL initialization
    }

    public UserDAO() {
    }

    public synchronized List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT name, username, phone, email, password, role FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Chỉ lấy tài khoản nội bộ (STAFF + ADMIN) cho trang quản trị thành viên
    public synchronized List<User> getStaffAndAdmins() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT name, username, phone, email, password, role FROM users WHERE role IN ('STAFF','ADMIN')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Tìm 1 tài khoản theo email (để cấp quyền nhân viên)
    public synchronized User findByEmail(String email) {
        String sql = "SELECT name, username, phone, email, password, role FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized boolean saveUser(User user) {
        // Check duplicate theo email / phone / username
        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ? OR phone = ? OR username = ?";
        String insertSql = "INSERT INTO users (name, username, phone, email, password, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            // Check duplicates
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setString(1, user.getEmail());
                psCheck.setString(2, user.getPhone());
                psCheck.setString(3, user.getUsername());
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false; // Trùng email, phone hoặc username
                    }
                }
            }

            // Insert (mật khẩu được băm trước khi lưu)
            try (PreparedStatement psInsert = conn.prepareStatement(insertSql)) {
                psInsert.setString(1, user.getName());
                psInsert.setString(2, user.getUsername());
                psInsert.setString(3, user.getPhone());
                psInsert.setString(4, user.getEmail());
                psInsert.setString(5, PasswordUtil.hash(user.getPassword()));
                psInsert.setString(6, user.getRole() != null ? user.getRole() : "CUSTOMER");
                int rows = psInsert.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized User findUser(String identifier, String password) {
        // Cho phép đăng nhập bằng email HOẶC số điện thoại HOẶC username.
        // Lấy user theo định danh rồi xác minh mật khẩu đã băm bằng Java.
        String sql = "SELECT name, username, phone, email, password, role FROM users "
                   + "WHERE (email = ? OR phone = ? OR username = ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String id = identifier.trim();
            ps.setString(1, id);
            ps.setString(2, id);
            ps.setString(3, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String stored = rs.getString("password");
                    if (PasswordUtil.verify(password, stored)) {
                        return new User(
                            rs.getString("name"),
                            rs.getString("username"),
                            rs.getString("phone"),
                            rs.getString("email"),
                            stored,
                            rs.getString("role")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Đổi mật khẩu cho tài khoản (dùng cho "Đổi mật khẩu" khi đã đăng nhập)
    public synchronized boolean updatePassword(String email, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PasswordUtil.hash(newPassword));
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Đặt lại mật khẩu khi biết đúng cả email + username (dùng cho "Quên mật khẩu")
    public synchronized boolean resetPassword(String email, String username, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE email = ? AND username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, PasswordUtil.hash(newPassword));
            ps.setString(2, email.trim());
            ps.setString(3, username.trim());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean updateUserRole(String email, String role) {
        String sql = "UPDATE users SET role = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean deleteUser(String email) {
        String sql = "DELETE FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean updateUser(User user, String oldEmail) {
        boolean changePw = user.getPassword() != null && !user.getPassword().isEmpty();
        String sql = changePw
            ? "UPDATE users SET name = ?, phone = ?, email = ?, password = ?, role = ? WHERE email = ?"
            : "UPDATE users SET name = ?, phone = ?, email = ?, role = ? WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getEmail());
            if (changePw) {
                ps.setString(4, PasswordUtil.hash(user.getPassword()));
                ps.setString(5, user.getRole());
                ps.setString(6, oldEmail);
            } else {
                ps.setString(4, user.getRole());
                ps.setString(5, oldEmail);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
