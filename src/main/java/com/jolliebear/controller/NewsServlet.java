package com.jolliebear.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jolliebear.dao.NewsDAO;
import com.jolliebear.model.News;
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

@WebServlet("/api/news")
public class NewsServlet extends HttpServlet {
    private NewsDAO newsDAO;

    @Override
    public void init() throws ServletException {
        newsDAO = new NewsDAO();
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

        String idStr = request.getParameter("id");
        if (idStr != null) {
            try {
                int id = Integer.parseInt(idStr);
                News news = newsDAO.getNewsById(id);
                if (news != null) {
                    out.print(gson.toJson(news));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonObject err = new JsonObject();
                    err.addProperty("message", "Không tìm thấy bài viết");
                    out.print(gson.toJson(err));
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject err = new JsonObject();
                err.addProperty("message", "ID không hợp lệ");
                out.print(gson.toJson(err));
            }
        } else {
            List<News> list = newsDAO.getAllNews();
            out.print(gson.toJson(list));
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

            News news = gson.fromJson(sb.toString(), News.class);
            if (news == null || news.getTitle() == null || news.getContent() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Dữ liệu không đầy đủ.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            boolean success = newsDAO.saveNews(news);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã thêm bài viết thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể lưu bài viết vào cơ sở dữ liệu.");
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

            News news = gson.fromJson(sb.toString(), News.class);
            if (news == null || news.getId() <= 0 || news.getTitle() == null || news.getContent() == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Dữ liệu không hợp lệ hoặc thiếu ID.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            boolean success = newsDAO.updateNews(news);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã cập nhật bài viết thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể cập nhật bài viết.");
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
            boolean success = newsDAO.deleteNews(id);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã xóa bài viết thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể xóa bài viết.");
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
