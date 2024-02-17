package com.example.VirtualFridge;


import com.example.VirtualFridge.dataManagerImpl.PostgresStorageV2Manager;
import com.example.VirtualFridge.model.User;
import com.example.VirtualFridge.model.foodwarning.storagev2.Food;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1.1/storagev2")
public class StorageV2MappingController {

    @PostMapping(
            path="/food",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public Food postFood(
            @RequestBody Food food
    ) {
        PostgresStorageV2Manager.getPostgresStorageV2Manager()
                .addFood(food);
        return food;
    }

    @PostMapping(
            path="/food/attributes",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public Food postFoodAttributes(
            @RequestBody Food food
    ) {
        PostgresStorageV2Manager.getPostgresStorageV2Manager()
                .addAttributesAndValuesToFood(food);
        return food;
    }

    @GetMapping(
            path="/food",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public Food getFood(@RequestParam int foodID) {
        return PostgresStorageV2Manager.getPostgresStorageV2Manager()
                .getFood(foodID);
    }

    @GetMapping(
            path="/food/all",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public List<Food> getAllFoods() {
        return PostgresStorageV2Manager.getPostgresStorageV2Manager()
                .getAllFoods();
    }

    @GetMapping(
            path="/food/attributes",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public Food getFoodWithAttributes(@RequestParam int foodID) {
        return PostgresStorageV2Manager.getPostgresStorageV2Manager()
                .getFoodWithAttributes(foodID);
    }

    @GetMapping(
            path="/food/instances",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public Collection<Food> getInstancesOfFoodInStorage(
            @AuthenticationPrincipal User user,
            @RequestParam int storagev2ID) {
        return PostgresStorageV2Manager.getPostgresStorageV2Manager()
                .getInstancesOfFoodInStorage(user.getID(), storagev2ID);
    }

    @PostMapping(
            path="/food/instances",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public Food addInstanceOfFoodToStorage(
            @AuthenticationPrincipal User user,
            @RequestParam int storagev2ID,
            @RequestBody Food foodInstance
    ) {
        return PostgresStorageV2Manager.getPostgresStorageV2Manager()
                .addInstanceOfFoodToStorage(user.getID(), storagev2ID, foodInstance);
    }





}
