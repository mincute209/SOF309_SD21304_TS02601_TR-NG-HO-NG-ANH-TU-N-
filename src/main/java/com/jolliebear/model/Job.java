package com.jolliebear.model;

import java.sql.Date;
import java.sql.Timestamp;

public class Job {
    private int id;
    private String title;
    private String department;
    private String location;
    private String salary;
    private String description;
    private String requirements;
    private Date deadline;
    private Timestamp createdAt;

    public Job() {
    }

    public Job(int id, String title, String department, String location, String salary, String description, String requirements, Date deadline, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.department = department;
        this.location = location;
        this.salary = salary;
        this.description = description;
        this.requirements = requirements;
        this.deadline = deadline;
        this.createdAt = createdAt;
    }

    public Job(String title, String department, String location, String salary, String description, String requirements, Date deadline) {
        this.title = title;
        this.department = department;
        this.location = location;
        this.salary = salary;
        this.description = description;
        this.requirements = requirements;
        this.deadline = deadline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
