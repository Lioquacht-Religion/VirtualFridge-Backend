package com.example.VirtualFridge.model.foodwarning;

public class FoodWarningRequestBody {
    private FoodWarningRequestFood food;

    public FoodWarningRequestBody() {
        this.food = new FoodWarningRequestFood();
    }

    public FoodWarningRequestBody(FoodWarningRequestFood food) {
        this.food = food;
    }

    public FoodWarningRequestFood getFood() {
        return food;
    }

    public void setFood(FoodWarningRequestFood food) {
        this.food = food;
    }

    @Override
    public String toString() {
        return "FoodWarningRequestBody{" +
                "food=" + food.toString() +
                '}';
    }
}
