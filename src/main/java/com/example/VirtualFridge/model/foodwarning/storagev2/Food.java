package com.example.VirtualFridge.model.foodwarning.storagev2;

import java.util.ArrayList;
import java.util.List;

public class Food {
    int id = -1;
    String name = "";
    int amount = 0;
    //maybe add Barcode?
    List<Attribute> Attributes = new ArrayList<>();

    public Food(){
        this.id = -1;
        this.name = "";
        this.amount = 0;
        this.Attributes = new ArrayList<>();
    }

    public Food(int id, String name, int amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.Attributes = new ArrayList<>();
    }

    public Food(int id, String name, int amount, List<Attribute> attributes) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.Attributes = attributes;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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

    public List<Attribute> getAttributes() {
        return Attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        Attributes = attributes;
    }
}
