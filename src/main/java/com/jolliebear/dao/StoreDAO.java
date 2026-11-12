package com.jolliebear.dao;

import com.jolliebear.model.Store;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoreDAO {

    public synchronized List<Store> getAllStores() {
        List<Store> list = new ArrayList<>();
        String sql = "SELECT id, name, address, phone, opening_hours, map_url FROM stores";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Store(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("opening_hours"),
                    rs.getString("map_url")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public synchronized boolean saveStore(Store store) {
        String sql = "INSERT INTO stores (name, address, phone, opening_hours, map_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, store.getName());
            ps.setString(2, store.getAddress());
            ps.setString(3, store.getPhone());
            ps.setString(4, store.getOpeningHours());
            ps.setString(5, store.getMapUrl());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean updateStore(Store store) {
        String sql = "UPDATE stores SET name = ?, address = ?, phone = ?, opening_hours = ?, map_url = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, store.getName());
            ps.setString(2, store.getAddress());
            ps.setString(3, store.getPhone());
            ps.setString(4, store.getOpeningHours());
            ps.setString(5, store.getMapUrl());
            ps.setInt(6, store.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean deleteStore(int id) {
        String sql = "DELETE FROM stores WHERE id = ?";
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
