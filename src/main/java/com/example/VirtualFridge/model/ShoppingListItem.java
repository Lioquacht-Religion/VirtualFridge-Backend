package com.example.VirtualFridge.model;

import java.util.Objects;

public class ShoppingListItem {
    private boolean ticked = false;
    private Grocery grocery =  null;

    public ShoppingListItem(boolean ticked, Grocery grocery){
        this.ticked = ticked;
        this.grocery = grocery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingListItem that = (ShoppingListItem) o;
        return ticked == that.ticked && Objects.equals(grocery, that.grocery);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticked, grocery);
    }

    public boolean isTicked() {
        return ticked;
    }

    public void setTicked(boolean ticked) {
        this.ticked = ticked;
    }

    public Grocery getGrocery() {
        return grocery;
    }

    public void setGrocery(Grocery grocery) {
        this.grocery = grocery;
    }
}
