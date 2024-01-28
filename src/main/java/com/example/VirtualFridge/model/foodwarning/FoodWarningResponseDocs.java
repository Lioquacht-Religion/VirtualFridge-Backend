package com.example.VirtualFridge.model.foodwarning;

import java.util.ArrayList;
import java.util.List;

public class FoodWarningResponseDocs {
    List<FoodWarningResponseFood> docs;

    public FoodWarningResponseDocs() {
        this.docs = docs;
    }


    public FoodWarningResponseDocs(List<FoodWarningResponseFood> docs) {
        this.docs = docs;
    }

    public List<FoodWarningResponseFood> getDocs() {
        return docs;
    }

    public void setDocs(ArrayList<FoodWarningResponseFood> docs) {
        this.docs = docs;
    }

    @Override
    public String toString() {
        return "FoodWarningResponse{" +
                "docs=" + docs +
                '}';
    }
}
