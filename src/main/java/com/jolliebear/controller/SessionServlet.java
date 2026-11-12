package com.jolliebear.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jolliebear.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/api/session")
public class SessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();

        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            JsonObject userJson = new JsonObject();
            userJson.addProperty("name", user.getName());
            userJson.addProperty("username", user.getUsername());
            userJson.addProperty("email", user.getEmail());
            userJson.addProperty("phone", user.getPhone());
            userJson.addProperty("role", user.getRole());

            jsonResponse.addProperty("status", "success");
            jsonResponse.add("user", userJson);
        } else {
            jsonResponse.addProperty("status", "guest");
        }
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
