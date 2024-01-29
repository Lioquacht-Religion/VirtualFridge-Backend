package com.example.VirtualFridge.model.foodwarning;

import java.util.ArrayList;

public class Product {
    String manufacturer;
    ArrayList<String> imageUrls;

    public Product() {
        this.manufacturer = "";
        this.imageUrls = new ArrayList<>();
    }

    public Product(String manufacturer, ArrayList<String> imageUrls) {
        this.manufacturer = manufacturer;
        this.imageUrls = imageUrls;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
