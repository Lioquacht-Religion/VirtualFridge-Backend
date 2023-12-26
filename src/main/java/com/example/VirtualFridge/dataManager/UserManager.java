package com.example.VirtualFridge.dataManager;

import com.example.VirtualFridge.model.Grocery;
import com.example.VirtualFridge.model.Storage;
import com.example.VirtualFridge.model.User;

import java.util.Collection;

public interface UserManager {
    Collection<User> getAllUsers();
    String addUser(User user);

    // TODO
    // removeTask, getTasksInOrder, getTaskByTaskID, ...

    // TODO
    // Make the TaskManager handling students.
}
