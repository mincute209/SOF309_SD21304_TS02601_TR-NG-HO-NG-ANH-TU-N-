package com.jolliebear.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jolliebear.dao.UserDAO;
import com.jolliebear.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    /**
     * Hàm xử lý nghiệp vụ kiểm tra dữ liệu đăng ký (thuần Java để Unit Test Y3)
     */
    public static void validateRegistration(User user) {
        if (user == null || user.getName() == null || user.getUsername() == null
                || user.getPhone() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("Dữ liệu đăng ký không hợp lệ.");
        }

        String name = user.getName().trim();
        String username = user.getUsername().trim();
        String phone = user.getPhone().trim();
        String email = user.getEmail().trim();
        String password = user.getPassword();

        if (name.isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }
        if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) {
            throw new IllegalArgumentException("Tên đăng nhập phải từ 3-20 ký tự, chỉ gồm chữ, số hoặc gạch dưới.");
        }
        if (!phone.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0.");
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Định dạng email không hợp lệ.");
        }
        if (password.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            User user = gson.fromJson(sb.toString(), User.class);

            try {
                validateRegistration(user);
            } catch (IllegalArgumentException ex) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", ex.getMessage());
                out.print(gson.toJson(jsonResponse));
                return;
            }

            user.setName(user.getName().trim());
            user.setUsername(user.getUsername().trim());
            user.setPhone(user.getPhone().trim());
            user.setEmail(user.getEmail().trim());

            boolean success = userDAO.saveUser(user);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đăng ký tài khoản thành công!");
                out.print(gson.toJson(jsonResponse));
            } else {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Tên đăng nhập, Email hoặc Số điện thoại đã tồn tại!");
                out.print(gson.toJson(jsonResponse));
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Lỗi máy chủ: " + e.getMessage());
            out.print(gson.toJson(jsonResponse));
        } finally {
            out.flush();
        }
    }
}