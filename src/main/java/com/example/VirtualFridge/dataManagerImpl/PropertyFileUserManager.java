package com.example.VirtualFridge.dataManagerImpl;

import com.example.VirtualFridge.dataManager.UserManager;
import com.example.VirtualFridge.model.User;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class PropertyFileUserManager implements UserManager {
    String fileName;

    static PropertyFileUserManager propertyFileUserManager = null;

    private PropertyFileUserManager(String fileName) {
        this.fileName = fileName;
    }

    static public PropertyFileUserManager getPropertyFileUserManager(String fileName) {
        if (propertyFileUserManager == null)
            propertyFileUserManager = new PropertyFileUserManager(fileName);
        return propertyFileUserManager;
    }

    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        Properties properties = new Properties();

        int i = 1;

        try {
            properties.load(new FileInputStream(fileName));

            while(properties.containsKey("User." + i + ".name")) {
                users.add(
                        new User(
                                properties.getProperty("User." + i + ".name"),
                                properties.getProperty("User." + i + ".email"),
                                properties.getProperty("User." + i + ".password") ));

                i++;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public String addUser(User user){
        Collection<User> users = getAllUsers();
        users.add(user);
        storeAllUsers(users);
        return user.getEmail();
    }

    //@Override
    public void deleteUser(User user){
    }



    public void storeAllUsers(Collection<User> users) {


        // I am ignoring the student and storing all tasks to the file

        Properties properties = new Properties();

        AtomicInteger i = new AtomicInteger(0);
        users.forEach(
                user -> {
                    properties.setProperty("User." + i.incrementAndGet() + ".name", user.getName());
                    properties.setProperty("User." + i.get() + ".email", user.getEmail());
                    properties.setProperty("User." + i.get() + ".password", ""+user.getPassword());
                }

        );
        try{
            properties.store(new FileOutputStream(fileName), "store data to file");

        }
        catch(IOException e){
            e.printStackTrace();
        }

    }

}
