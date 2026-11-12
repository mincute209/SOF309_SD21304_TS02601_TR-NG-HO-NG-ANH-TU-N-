package com.jolliebear.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jolliebear.dao.MenuItemDAO;
import com.jolliebear.model.MenuItem;
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
import java.util.List;

@WebServlet("/api/menu")
public class MenuServlet extends HttpServlet {
    private MenuItemDAO menuItemDAO;

    @Override
    public void init() throws ServletException {
        menuItemDAO = new MenuItemDAO();
    }

    private boolean isStaffOrAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return false;
        }
        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        return "ADMIN".equalsIgnoreCase(role) || "STAFF".equalsIgnoreCase(role);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String idStr = request.getParameter("id");
        String category = request.getParameter("category");

        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                MenuItem item = menuItemDAO.getMenuItemById(id);
                if (item != null) {
                    out.print(gson.toJson(item));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonObject err = new JsonObject();
                    err.addProperty("status", "error");
                    err.addProperty("message", "Món ăn không tồn tại.");
                    out.print(gson.toJson(err));
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject err = new JsonObject();
                err.addProperty("status", "error");
                err.addProperty("message", "ID không hợp lệ.");
                out.print(gson.toJson(err));
            }
        } else if (category != null && !category.isEmpty() && !"All".equalsIgnoreCase(category)) {
            List<MenuItem> items = menuItemDAO.getMenuItemsByCategory(category);
            out.print(gson.toJson(items));
        } else {
            List<MenuItem> items = menuItemDAO.getAllMenuItems();
            out.print(gson.toJson(items));
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

        if (!isStaffOrAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Bạn không có quyền thêm món ăn.");
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

            MenuItem item = gson.fromJson(sb.toString(), MenuItem.class);
            if (item == null || item.getNameVi() == null || item.getNameVi().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Tên món ăn không được để trống.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            boolean success = menuItemDAO.saveMenuItem(item);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Thêm món ăn thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể lưu món ăn vào CSDL.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Lỗi xử lý dữ liệu: " + e.getMessage());
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

        if (!isStaffOrAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Bạn không có quyền chỉnh sửa món ăn.");
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

            MenuItem item = gson.fromJson(sb.toString(), MenuItem.class);
            if (item == null || item.getId() <= 0) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Dữ liệu cập nhật món ăn không hợp lệ.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            boolean success = menuItemDAO.updateMenuItem(item);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Cập nhật món ăn thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể cập nhật món ăn.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Lỗi xử lý dữ liệu: " + e.getMessage());
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

        if (!isStaffOrAdmin(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Bạn không có quyền xóa món ăn.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null || idStr.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Thiếu ID món ăn cần xóa.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            boolean success = menuItemDAO.deleteMenuItem(id);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Xóa món ăn thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể xóa món ăn.");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "ID món ăn không hợp lệ.");
        }
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
