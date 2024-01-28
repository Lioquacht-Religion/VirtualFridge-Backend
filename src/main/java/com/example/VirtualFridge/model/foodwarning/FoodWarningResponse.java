package com.example.VirtualFridge.model.foodwarning;

public class FoodWarningResponse {
    FoodWarningResponseDocs response;

    public FoodWarningResponse() {
        this.response = new FoodWarningResponseDocs();
    }

    public FoodWarningResponse(FoodWarningResponseDocs docs) {
        this.response = docs;
    }

    public FoodWarningResponseDocs getResponse() {
        return response;
    }

    public void setResponse(FoodWarningResponseDocs response) {
        this.response = response;
    }
}
