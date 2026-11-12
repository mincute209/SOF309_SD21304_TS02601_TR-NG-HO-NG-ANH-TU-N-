package com.jolliebear.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jolliebear.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Quên mật khẩu (đơn giản cho môi trường học):
 * người dùng nhập ĐÚNG cả email + tên đăng nhập -> được đặt lại mật khẩu ngay,
 * không cần gửi email thật.
 */
@WebServlet("/api/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) sb.append(line);
            }
            JsonObject body = gson.fromJson(sb.toString(), JsonObject.class);

            if (body == null || !body.has("email") || !body.has("username") || !body.has("newPassword")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json.addProperty("status", "error");
                json.addProperty("message", "Vui lòng nhập đầy đủ email, tên đăng nhập và mật khẩu mới.");
                out.print(gson.toJson(json));
                out.flush();
                return;
            }

            String email = body.get("email").getAsString().trim();
            String username = body.get("username").getAsString().trim();
            String newPassword = body.get("newPassword").getAsString();

            if (newPassword.length() < 6) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json.addProperty("status", "error");
                json.addProperty("message", "Mật khẩu mới phải có ít nhất 6 ký tự.");
                out.print(gson.toJson(json));
                out.flush();
                return;
            }

            boolean ok = userDAO.resetPassword(email, username, newPassword);
            if (ok) {
                json.addProperty("status", "success");
                json.addProperty("message", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập lại.");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                json.addProperty("status", "error");
                json.addProperty("message", "Email và tên đăng nhập không khớp với tài khoản nào.");
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
