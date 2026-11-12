package com.jolliebear.dao;

import com.jolliebear.model.MenuItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAO {

    public synchronized List<MenuItem> getAllMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT id, name_vi, name_en, category, price, description_vi, description_en, image_url, is_available FROM menu_items ORDER BY id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                items.add(new MenuItem(
                    rs.getInt("id"),
                    rs.getString("name_vi"),
                    rs.getString("name_en"),
                    rs.getString("category"),
                    rs.getDouble("price"),
                    rs.getString("description_vi"),
                    rs.getString("description_en"),
                    rs.getString("image_url"),
                    rs.getBoolean("is_available")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public synchronized List<MenuItem> getMenuItemsByCategory(String category) {
        List<MenuItem> items = new ArrayList<>();
        String sql = "SELECT id, name_vi, name_en, category, price, description_vi, description_en, image_url, is_available FROM menu_items WHERE category = ? ORDER BY id DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name_vi"),
                        rs.getString("name_en"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("description_vi"),
                        rs.getString("description_en"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_available")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public synchronized MenuItem getMenuItemById(int id) {
        String sql = "SELECT id, name_vi, name_en, category, price, description_vi, description_en, image_url, is_available FROM menu_items WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new MenuItem(
                        rs.getInt("id"),
                        rs.getString("name_vi"),
                        rs.getString("name_en"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("description_vi"),
                        rs.getString("description_en"),
                        rs.getString("image_url"),
                        rs.getBoolean("is_available")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized boolean saveMenuItem(MenuItem item) {
        String sql = "INSERT INTO menu_items (name_vi, name_en, category, price, description_vi, description_en, image_url, is_available) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, item.getNameVi());
            ps.setString(2, item.getNameEn());
            ps.setString(3, item.getCategory());
            ps.setDouble(4, item.getPrice());
            ps.setString(5, item.getDescriptionVi());
            ps.setString(6, item.getDescriptionEn());
            ps.setString(7, item.getImageUrl());
            ps.setBoolean(8, item.isAvailable());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean updateMenuItem(MenuItem item) {
        String sql = "UPDATE menu_items SET name_vi = ?, name_en = ?, category = ?, price = ?, description_vi = ?, description_en = ?, image_url = ?, is_available = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, item.getNameVi());
            ps.setString(2, item.getNameEn());
            ps.setString(3, item.getCategory());
            ps.setDouble(4, item.getPrice());
            ps.setString(5, item.getDescriptionVi());
            ps.setString(6, item.getDescriptionEn());
            ps.setString(7, item.getImageUrl());
            ps.setBoolean(8, item.isAvailable());
            ps.setInt(9, item.getId());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean deleteMenuItem(int id) {
        String sql = "DELETE FROM menu_items WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
