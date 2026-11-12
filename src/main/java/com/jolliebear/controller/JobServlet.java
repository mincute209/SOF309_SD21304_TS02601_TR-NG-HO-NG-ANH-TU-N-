package com.jolliebear.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jolliebear.dao.JobDAO;
import com.jolliebear.model.Job;
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
import java.sql.Date;
import java.util.List;

@WebServlet("/api/jobs")
public class JobServlet extends HttpServlet {
    private JobDAO jobDAO;

    @Override
    public void init() throws ServletException {
        jobDAO = new JobDAO();
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
                Job job = jobDAO.getJobById(id);
                if (job != null) {
                    out.print(gson.toJson(job));
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonObject err = new JsonObject();
                    err.addProperty("message", "Không tìm thấy tin tuyển dụng");
                    out.print(gson.toJson(err));
                }
            } catch (NumberFormatException e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                JsonObject err = new JsonObject();
                err.addProperty("message", "ID không hợp lệ");
                out.print(gson.toJson(err));
            }
        } else {
            List<Job> list = jobDAO.getAllJobs();
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

            // Custom parse to handle SQL Date safely
            JsonObject jobObj = gson.fromJson(sb.toString(), JsonObject.class);
            if (jobObj == null || !jobObj.has("title") || !jobObj.has("department") 
                    || !jobObj.has("location") || !jobObj.has("description") || !jobObj.has("requirements")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Dữ liệu không đầy đủ.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            Job job = new Job();
            job.setTitle(jobObj.get("title").getAsString());
            job.setDepartment(jobObj.get("department").getAsString());
            job.setLocation(jobObj.get("location").getAsString());
            job.setSalary(jobObj.has("salary") ? jobObj.get("salary").getAsString() : "Thỏa thuận");
            job.setDescription(jobObj.get("description").getAsString());
            job.setRequirements(jobObj.get("requirements").getAsString());
            
            if (jobObj.has("deadline") && !jobObj.get("deadline").getAsString().isEmpty()) {
                job.setDeadline(Date.valueOf(jobObj.get("deadline").getAsString()));
            }

            boolean success = jobDAO.saveJob(job);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã thêm tin tuyển dụng thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể lưu tin tuyển dụng.");
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

            JsonObject jobObj = gson.fromJson(sb.toString(), JsonObject.class);
            if (jobObj == null || !jobObj.has("id") || !jobObj.has("title") 
                    || !jobObj.has("department") || !jobObj.has("location")) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Dữ liệu cập nhật không hợp lệ.");
                out.print(gson.toJson(jsonResponse));
                return;
            }

            Job job = new Job();
            job.setId(jobObj.get("id").getAsInt());
            job.setTitle(jobObj.get("title").getAsString());
            job.setDepartment(jobObj.get("department").getAsString());
            job.setLocation(jobObj.get("location").getAsString());
            job.setSalary(jobObj.has("salary") ? jobObj.get("salary").getAsString() : "Thỏa thuận");
            job.setDescription(jobObj.get("description").getAsString());
            job.setRequirements(jobObj.get("requirements").getAsString());
            
            if (jobObj.has("deadline") && !jobObj.get("deadline").getAsString().isEmpty()) {
                job.setDeadline(Date.valueOf(jobObj.get("deadline").getAsString()));
            }

            boolean success = jobDAO.updateJob(job);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã cập nhật tin tuyển dụng thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể cập nhật tin tuyển dụng.");
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
            boolean success = jobDAO.deleteJob(id);
            if (success) {
                jsonResponse.addProperty("status", "success");
                jsonResponse.addProperty("message", "Đã xóa tin tuyển dụng thành công!");
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                jsonResponse.addProperty("status", "error");
                jsonResponse.addProperty("message", "Không thể xóa tin tuyển dụng.");
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
