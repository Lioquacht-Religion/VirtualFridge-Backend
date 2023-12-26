package com.example.VirtualFridge.model;

import com.example.VirtualFridge.dataManagerImpl.PostgresUserManager;
import com.example.VirtualFridge.dataManagerImpl.PropertyFileUserManager;

import java.util.Collection;
import java.util.List;

public class UserList {
    private Collection<User> users;

    public Collection<User> getUsers() {
        return users;
    }



    public void setUsers() {
        //PropertyFileUserManager UserManager = PropertyFileUserManager.getPropertyFileUserManager("src/main/resources/user.properties");
        PostgresUserManager UserManager = PostgresUserManager.getPostgresUserManager();
        users = UserManager.getAllUsers();
    }

    public void deleteUser(User user){
        if(users instanceof List){
            List<User> l_uList = (List<User>) users;
            for(int i = l_uList.size()-1; i > 0; i--){
                if(l_uList.get(i).getEmail().equals( user.getEmail() )){
                    System.out.println("User to delete found");
                    l_uList.remove(i);
                    break;
                }
            }
        }


    }
}
