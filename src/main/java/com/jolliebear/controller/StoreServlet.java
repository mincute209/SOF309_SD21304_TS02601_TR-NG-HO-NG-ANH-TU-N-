package com.jolliebear.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jolliebear.dao.StoreDAO;
import com.jolliebear.model.Store;
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

@WebServlet("/api/stores")
public class StoreServlet extends HttpServlet {
    private StoreDAO storeDAO;

    @Override
    public void init() throws ServletException {
        storeDAO = new StoreDAO();
    }

    private boolean isAuthorized(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            return false;
        }
        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        return "STAFF".equalsIgnoreCase(role) || "ADMIN".equalsIgnoreCase(role);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        List<Store> list = storeDAO.getAllStores();
        out.print(gson.toJson(list));
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

        if (!isAuthorized(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Bạn không có quyền thực hiện hành động này.");
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

            Store store = gson.fromJson(sb.toString(), Store.class);
            if (store == null || store.getName() == null || store.getAddress() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Dữ liệu không đầy đủ.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            boolean success = storeDAO.saveStore(store);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã thêm cửa hàng thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể lưu cửa hàng vào cơ sở dữ liệu.");
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

        if (!isAuthorized(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Bạn không có quyền thực hiện hành động này.");
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

            Store store = gson.fromJson(sb.toString(), Store.class);
            if (store == null || store.getId() <= 0 || store.getName() == null || store.getAddress() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Dữ liệu không hợp lệ.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            boolean success = storeDAO.updateStore(store);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã cập nhật cửa hàng thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể cập nhật cửa hàng.");
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

        if (!isAuthorized(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Bạn không có quyền thực hiện hành động này.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        String idStr = request.getParameter("id");
        if (idStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "Thiếu tham số ID.");
            out.print(gson.toJson(jsonResponse));
            return;
        }

        try {
            int id = Integer.parseInt(idStr);
            boolean success = storeDAO.deleteStore(id);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã xóa cửa hàng thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể xóa cửa hàng.");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("status", "error");
            jsonResponse.addProperty("message", "ID không hợp lệ.");
        }
        out.print(gson.toJson(jsonResponse));
        out.flush();
    }
}
