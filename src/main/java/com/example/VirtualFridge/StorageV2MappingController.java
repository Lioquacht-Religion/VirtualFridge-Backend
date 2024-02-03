package com.example.VirtualFridge;


import com.example.VirtualFridge.dataManagerImpl.PostgresStorageV2Manager;
import com.example.VirtualFridge.model.foodwarning.storagev2.Food;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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




}
