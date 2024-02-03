package com.example.VirtualFridge.model.foodwarning.storagev2;


public class Attribute {
    int id = -1;
    String name = "";
    int valueID = -1;
    String value = "";
    String unit = "";
    int foodID = -1;

    public int getValueID() {
        return valueID;
    }

    public void setValueID(int valueID) {
        this.valueID = valueID;
    }

    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public Attribute(){

    }

    public Attribute(int id, String name, String value, String unit) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.unit = unit;
    }

    public Attribute(int id, String name, int valueID, String value, String unit, int foodID) {
        this.id = id;
        this.name = name;
        this.valueID = valueID;
        this.value = value;
        this.unit = unit;
        this.foodID = foodID;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
