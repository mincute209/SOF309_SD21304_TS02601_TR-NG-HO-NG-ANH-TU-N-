package com.jolliebear.model;

public class Order {
    private int id;
    private String userEmail;   // email tài khoản đặt đơn (null nếu khách vãng lai)
    private String customerName;
    private String phone;
    private String address;
    private String paymentMethod;
    private String items;   // Tóm tắt món: "Gà Giòn x2, Pepsi x1"
    private double total;
    private String note;
    private String status;
    private String createdAt;

    public Order() {
        this.status = "Chờ xử lý";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
