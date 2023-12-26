package com.example.VirtualFridge.dataManager;

import com.example.VirtualFridge.model.Grocery;
import com.example.VirtualFridge.model.Storage;
import com.example.VirtualFridge.model.User;

import java.util.Collection;

public interface StorageManager {
    Collection<Grocery> getAllStorages(User user);
    void addGrocery(User user, Storage storage, Grocery grocery );

    // TODO
    // removeTask, getTasksInOrder, getTaskByTaskID, ...

    // TODO
    // Make the TaskManager handling students.
}
