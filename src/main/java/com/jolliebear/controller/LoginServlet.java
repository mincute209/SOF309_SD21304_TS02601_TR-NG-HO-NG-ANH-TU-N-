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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
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
        JsonObject jsonResponse = new JsonObject();

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            JsonObject credentials = gson.fromJson(sb.toString(), JsonObject.class);
            if (credentials == null || !credentials.has("email") || !credentials.has("password")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Vui lòng nhập đầy đủ thông tin đăng nhập.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            String emailOrPhone = credentials.get("email").getAsString().trim();
            String password = credentials.get("password").getAsString();

            User user = userDAO.findUser(emailOrPhone, password);

            if (user != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);

                JsonObject userJson = new JsonObject();
                userJson.addProperty("name", user.getName());
                userJson.addProperty("email", user.getEmail());
                userJson.addProperty("phone", user.getPhone());
                userJson.addProperty("role", user.getRole());

                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đăng nhập thành công!");
                jsonResponse.add("user", userJson);
                out.print(gson.toJson(jsonResponse));
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Tài khoản hoặc mật khẩu không chính xác!");
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
