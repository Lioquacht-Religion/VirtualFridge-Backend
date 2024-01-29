package com.example.VirtualFridge.model.foodwarning;

import java.util.ArrayList;
import java.util.List;

public class FoodWarningResponseFood {
    String publishedDate;
    String title;
    String warning;
    Product product;
    List<String> affectedStates;

    public FoodWarningResponseFood() {
        this.publishedDate = "";
        this.title = "";
        this.warning = "";
        this.product = new Product();
        this.affectedStates = new ArrayList<>();
    }


    public FoodWarningResponseFood(String date, String productName, String warning, Product product, List<String> affectedStates) {
        this.publishedDate = date;
        this.title = productName;
        this.warning = warning;
        this.product = product;
        this.affectedStates = affectedStates;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    @Override
    public String toString() {
        return "FoodWarningResponseFood{" +
                "date='" + publishedDate + '\'' +
                ", productName='" + title + '\'' +
                ", warning='" + warning + '\'' +
                ", product=" + product +
                ", affectedStates=" + affectedStates +
                '}';
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public List<String> getAffectedStates() {
        return affectedStates;
    }

    public void setAffectedStates(ArrayList<String> affectedStates) {
        this.affectedStates = affectedStates;
    }
}
