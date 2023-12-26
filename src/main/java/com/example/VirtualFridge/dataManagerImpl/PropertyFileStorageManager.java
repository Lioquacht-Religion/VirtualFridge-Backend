package com.example.VirtualFridge.dataManagerImpl;

import com.example.VirtualFridge.dataManager.StorageManager;
import com.example.VirtualFridge.model.Grocery;
import com.example.VirtualFridge.model.Storage;
import com.example.VirtualFridge.model.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class PropertyFileStorageManager implements StorageManager {

    String fileName;

    static PropertyFileStorageManager propertyFileTaskManager = null;

    private PropertyFileStorageManager(String fileName) {
        this.fileName = fileName;
    }

    static public PropertyFileStorageManager getPropertyFileTaskManagerImpl(String fileName) {
        if (propertyFileTaskManager == null)
            propertyFileTaskManager = new PropertyFileStorageManager(fileName);
        return propertyFileTaskManager;
    }

    public Collection<Grocery> getAllStorages(User user){

        List<Grocery> tasks = new ArrayList<>();

        Properties properties = new Properties();

        int i = 1;

        try {
            properties.load(new FileInputStream(fileName));

            while(properties.containsKey("Task." + i + ".name")) {
                /*tasks.add(
                        new Storage(
                                properties.getProperty("Task." + i + ".name"),
                                properties.getProperty("Task." + i + ".description"),
                                Integer.parseInt(
                                        properties.getProperty("Task." + i + ".priority") )));*/

                i++;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }

        return new LinkedList<Grocery>();
    }
    @Override
    public void addGrocery(User user, Storage storage, Grocery grocery){

    }

}
