package com.example.VirtualFridge.model;

import java.util.Collection;
import java.util.LinkedList;

public class Recipe {
    public Collection<Grocery> getIngredients() {
        return Ingredients;
    }

    public void setIngredients(Collection<Grocery> ingredients) {
        Ingredients = ingredients;
    }

    private Collection<Grocery> Ingredients = new LinkedList<Grocery>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name = "Standardrezept";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description = "Standard Text";

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    private int recipeID = -1;

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {
        this.authorID = authorID;
    }

    private int authorID = -1;

    private User Author;

    //Consider adding author attribute

    public Recipe(String name, String description){
        this.name = name; this.description= description;
    }

}
