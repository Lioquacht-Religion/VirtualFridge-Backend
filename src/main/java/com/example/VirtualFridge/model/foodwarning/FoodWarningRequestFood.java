package com.example.VirtualFridge.model.foodwarning;

import java.util.ArrayList;
import java.util.List;

public class FoodWarningRequestFood {
    int rows;
    String sort;
    int start;
    List<String> fq;

    public FoodWarningRequestFood() {
        this.rows = 10;
        this.sort = "";
        this.start = 0;
        this.fq = new ArrayList<>();
    }


    public FoodWarningRequestFood(int rows, String sort, int start, ArrayList<String> fq) {
        this.rows = rows;
        this.sort = sort;
        this.start = start;
        this.fq = fq;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public List<String> getFq() {
        return fq;
    }

    public void setFq(List<String> fq) {
        this.fq = fq;
    }

    @Override
    public String toString() {
        return "FoodWarningRequestFood{" +
                "rows=" + rows +
                ", sort='" + sort + '\'' +
                ", start=" + start +
                ", fq=" + fq +
                '}';
    }
}
