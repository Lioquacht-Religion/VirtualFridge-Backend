package com.example.VirtualFridge.model;

import java.util.Collection;
import java.util.LinkedList;

import static com.example.VirtualFridge.dataManagerImpl.PostgresUserManager.getPostgresUserManager;

public class Storage {
    private String name = "Standard-Lager";
    private User Owner;


    private int ownerID = -1;


    private int storageID = -1;
    private Collection<Grocery> Groceries = new LinkedList<Grocery>();

    public Storage(String name, User Owner){
        this.name = name; this.Owner = Owner;
    }

    /*public Storage(int ownerID, int storageID, String name, User Owner){
        this.ownerID = ownerID; this.storageID = storageID; this.name = name; this.Owner = Owner;
    }*/

    public void setIDs(int ownerID, int storageID){
        this.ownerID = ownerID; this.storageID = storageID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getStorageID() {
        return storageID;
    }

    public void setStorageID(int storageID) {
        this.storageID = storageID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public User getOwner(){
        return Owner;
    }

    public Collection<Grocery> getGroceries() {
        return Groceries;
    }

    public void setGroceries() {
       Groceries = getPostgresUserManager().getGroceries(getStorageID());
    }

}
