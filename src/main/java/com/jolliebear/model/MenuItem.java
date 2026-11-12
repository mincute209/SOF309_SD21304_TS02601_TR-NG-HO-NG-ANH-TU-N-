package com.jolliebear.model;

public class MenuItem {
    private int id;
    private String nameVi;
    private String nameEn;
    private String category;
    private double price;
    private String descriptionVi;
    private String descriptionEn;
    private String imageUrl;
    private boolean available;

    public MenuItem() {
    }

    public MenuItem(int id, String nameVi, String nameEn, String category, double price, 
                    String descriptionVi, String descriptionEn, String imageUrl, boolean available) {
        this.id = id;
        this.nameVi = nameVi;
        this.nameEn = nameEn;
        this.category = category;
        this.price = price;
        this.descriptionVi = descriptionVi;
        this.descriptionEn = descriptionEn;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    public MenuItem(String nameVi, String nameEn, String category, double price, 
                    String descriptionVi, String descriptionEn, String imageUrl, boolean available) {
        this.nameVi = nameVi;
        this.nameEn = nameEn;
        this.category = category;
        this.price = price;
        this.descriptionVi = descriptionVi;
        this.descriptionEn = descriptionEn;
        this.imageUrl = imageUrl;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameVi() {
        return nameVi;
    }

    public void setNameVi(String nameVi) {
        this.nameVi = nameVi;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescriptionVi() {
        return descriptionVi;
    }

    public void setDescriptionVi(String descriptionVi) {
        this.descriptionVi = descriptionVi;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
