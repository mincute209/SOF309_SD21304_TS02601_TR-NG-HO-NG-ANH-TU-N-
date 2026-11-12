package com.jolliebear.model;

public class Store {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String openingHours;
    private String mapUrl;

    public Store() {
    }

    public Store(int id, String name, String address, String phone, String openingHours, String mapUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.openingHours = openingHours;
        this.mapUrl = mapUrl;
    }

    public Store(String name, String address, String phone, String openingHours, String mapUrl) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.openingHours = openingHours;
        this.mapUrl = mapUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }
}
