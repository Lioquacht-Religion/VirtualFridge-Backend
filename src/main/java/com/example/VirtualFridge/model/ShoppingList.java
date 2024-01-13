package com.example.VirtualFridge.model;

public class ShoppingList {
    int shoppingListId = -1;
    int ownerId = -1;
    String name = "";

    public ShoppingList(int id, int ownerId, String name){
        this.shoppingListId = id;
        this.ownerId = ownerId;
        this.name = name;
    }

    public int getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(int shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
