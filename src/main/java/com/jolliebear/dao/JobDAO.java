package com.jolliebear.dao;

import com.jolliebear.model.Job;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobDAO {

    public synchronized List<Job> getAllJobs() {
        List<Job> list = new ArrayList<>();
        String sql = "SELECT id, title, department, location, salary, description, requirements, deadline, created_at FROM jobs ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Job(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("department"),
                    rs.getString("location"),
                    rs.getString("salary"),
                    rs.getString("description"),
                    rs.getString("requirements"),
                    rs.getDate("deadline"),
                    rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public synchronized Job getJobById(int id) {
        String sql = "SELECT id, title, department, location, salary, description, requirements, deadline, created_at FROM jobs WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Job(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("department"),
                        rs.getString("location"),
                        rs.getString("salary"),
                        rs.getString("description"),
                        rs.getString("requirements"),
                        rs.getDate("deadline"),
                        rs.getTimestamp("created_at")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized boolean saveJob(Job job) {
        String sql = "INSERT INTO jobs (title, department, location, salary, description, requirements, deadline) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, job.getTitle());
            ps.setString(2, job.getDepartment());
            ps.setString(3, job.getLocation());
            ps.setString(4, job.getSalary());
            ps.setString(5, job.getDescription());
            ps.setString(6, job.getRequirements());
            ps.setDate(7, job.getDeadline());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean updateJob(Job job) {
        String sql = "UPDATE jobs SET title = ?, department = ?, location = ?, salary = ?, description = ?, requirements = ?, deadline = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, job.getTitle());
            ps.setString(2, job.getDepartment());
            ps.setString(3, job.getLocation());
            ps.setString(4, job.getSalary());
            ps.setString(5, job.getDescription());
            ps.setString(6, job.getRequirements());
            ps.setDate(7, job.getDeadline());
            ps.setInt(8, job.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean deleteJob(int id) {
        String sql = "DELETE FROM jobs WHERE id = ?";
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
