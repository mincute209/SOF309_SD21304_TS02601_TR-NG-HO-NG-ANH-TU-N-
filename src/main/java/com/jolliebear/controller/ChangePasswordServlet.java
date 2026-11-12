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
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/change-password")
public class ChangePasswordServlet extends HttpServlet {
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

        // Bắt buộc đã đăng nhập
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            json.addProperty("status", "error");
            json.addProperty("message", "Bạn cần đăng nhập để đổi mật khẩu.");
            out.print(gson.toJson(json));
            out.flush();
            return;
        }
        User current = (User) session.getAttribute("user");

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) sb.append(line);
            }
            JsonObject body = gson.fromJson(sb.toString(), JsonObject.class);

            if (body == null || !body.has("oldPassword") || !body.has("newPassword")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json.addProperty("status", "error");
                json.addProperty("message", "Vui lòng nhập đầy đủ thông tin.");
                out.print(gson.toJson(json));
                out.flush();
                return;
            }

            String oldPassword = body.get("oldPassword").getAsString();
            String newPassword = body.get("newPassword").getAsString();

            if (newPassword.length() < 6) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json.addProperty("status", "error");
                json.addProperty("message", "Mật khẩu mới phải có ít nhất 6 ký tự.");
                out.print(gson.toJson(json));
                out.flush();
                return;
            }

            // Xác minh mật khẩu cũ bằng chính email trong session
            User verified = userDAO.findUser(current.getEmail(), oldPassword);
            if (verified == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                json.addProperty("status", "error");
                json.addProperty("message", "Mật khẩu hiện tại không đúng.");
                out.print(gson.toJson(json));
                out.flush();
                return;
            }

            boolean ok = userDAO.updatePassword(current.getEmail(), newPassword);
            if (ok) {
                current.setPassword(newPassword);       // cập nhật lại session
                session.setAttribute("user", current);
                json.addProperty("status", "success");
                json.addProperty("message", "Đổi mật khẩu thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                json.addProperty("status", "error");
                json.addProperty("message", "Không thể đổi mật khẩu, vui lòng thử lại.");
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
