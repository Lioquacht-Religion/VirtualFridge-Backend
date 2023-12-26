package com.example.VirtualFridge.model;

import java.util.Objects;

public class User {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && email.equals(user.email) && password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, password);
    }

    private String name = "Std", email = "", password = "";


    private int ID = -1;

    public User(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

   /* public User(int ID, String name, String email, String password){
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = password;
    }*/

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
