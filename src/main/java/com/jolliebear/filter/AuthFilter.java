package com.jolliebear.filter;

import com.jolliebear.model.User;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Bảo vệ trang quản trị ở phía SERVER: chỉ ADMIN/STAFF (đã đăng nhập) mới vào
 * được dashboard.jsp. Không còn phụ thuộc mỗi JavaScript.
 */
@WebFilter(urlPatterns = {"/dashboard.jsp"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        boolean allowed = user != null
                && ("ADMIN".equalsIgnoreCase(user.getRole()) || "STAFF".equalsIgnoreCase(user.getRole()));

        if (allowed) {
            chain.doFilter(request, response);
        } else {
            // Chưa đăng nhập hoặc là khách hàng -> đá về trang chủ
            res.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }
}
