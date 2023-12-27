package com.example.VirtualFridge;


import com.example.VirtualFridge.dataManagerImpl.PostgresUserManager;
import com.example.VirtualFridge.dataManagerImpl.PropertyFileUserManager;
import com.example.VirtualFridge.model.*;
import com.example.VirtualFridge.model.alexa.OutputSpeechRO;
import com.example.VirtualFridge.model.alexa.ResponseRO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.example.VirtualFridge.model.alexa.AlexaRO;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.VirtualFridge.dataManagerImpl.PostgresUserManager.getPostgresUserManager;
import static com.example.VirtualFridge.dataManagerImpl.PropertyFileUserManager.getPropertyFileUserManager;
import static java.lang.Integer.parseInt;

//TODO: CHange all propertyfilemanager zu postgres

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1.0")
public class MappingController {

    @PostMapping(
            path = "/user",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createUser(@RequestBody User user){
        //getPropertyFileUserManager("src/main/resources/user.properties").addUser(user);
        getPostgresUserManager().addUser(user);
        return "posted user with email " + user.getEmail();
    }


    @GetMapping("/hello")
    public String getHello(){
        return "Hello";
    }

    @GetMapping("/user/all")
    public Collection<User> getUsers(//@RequestParam(value = "email", defaultValue = "none")
                             //String email
    ){

        //UserList userList = new UserList();
        //userList.setUsers();

        return getPostgresUserManager().getAllUsers();
        //return userList;
    }

    @GetMapping("/user/email"
    )
    public User getUser(@RequestParam String email
    ){
        return getPostgresUserManager().getUser("email", email);
    }

    //TODO: Fix
    @PutMapping(path= "/user",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String putUser(@RequestBody User user
    ){
        getPostgresUserManager().putUser(user);
        return "updated User: " + user.getEmail();
    }

    @PostMapping(
            path = "/user/createtable"
    )
    @ResponseStatus(HttpStatus.OK)
    public String createUserTable() {
        Logger.getLogger("Test").log(Level.INFO, "Start Post create Table");

        final PostgresUserManager postgresUserManager =
                getPostgresUserManager();
        postgresUserManager.createTableUser();

        return "Database Table created";
    }


    @GetMapping("/user/storage/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Storage> getUserStorages(@RequestParam String OwnerID){

        return getPostgresUserManager().getStorages(parseInt(OwnerID));
    }

    @GetMapping("/storage")
    @ResponseStatus(HttpStatus.OK)
    public Storage getStorage(@RequestParam String storName, @RequestParam String email){

        return getPostgresUserManager().getStorage(storName,
                getPostgresUserManager().getUser("email", email));
    }

    @DeleteMapping(
            path="/user"
            )
    @ResponseStatus(HttpStatus.OK)
    public String deleteUser(@RequestParam String userID, @RequestParam String email, @RequestParam String password){
        //UserList userList = new UserList();
        //userList.setUsers();
        //userList.deleteUser(user);
        //getPropertyFileUserManager("src/main/resources/user.properties").storeAllUsers(userList.getUsers());
        getPostgresUserManager().deleteUser(Integer.parseInt(userID), email, password);

        return userID;
    }

    @DeleteMapping(path="/storage")
    @ResponseStatus(HttpStatus.OK)
    public String deleteStorage(@RequestParam int userID, @RequestParam int storageID){
        return getPostgresUserManager().deleteStorage(userID, storageID);
    }

    @DeleteMapping(path="/grocery")
    @ResponseStatus(HttpStatus.OK)
    public String deleteGrocery(@RequestParam int storageID, @RequestParam int groceryID){
        return getPostgresUserManager().deleteGrocery(storageID, groceryID);
    }

    @PostMapping(
            path = "/storage",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createStorage(@RequestBody Storage storage){
        //getPropertyFileUserManager("src/main/resources/user.properties").addUser(user);
        getPostgresUserManager().addStorage(storage);
        return "posted storage: " + storage.getName();
    }

    @PostMapping(
            path = "/storage/createtable"
    )
    @ResponseStatus(HttpStatus.OK)
    public String createStorageTable() {
        //Logger.getLogger("Test").log(Level.INFO, "Start Post create Table");

        final PostgresUserManager postgresUserManager =
                getPostgresUserManager();
        postgresUserManager.createTableStorage();

        return "Database Storage Table created";
    }

    @PostMapping(
            path = "/grocery",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createGrocery(@RequestParam String storName,
                                @RequestParam String ownerEmail,
                                @RequestBody Grocery grocery){
        //getPropertyFileUserManager("src/main/resources/user.properties").addUser(user);
        final PostgresUserManager PostgresManager = getPostgresUserManager();
        User owner = PostgresManager.getUser("email", ownerEmail);
        Storage storage = PostgresManager.getStorage(storName, owner);
        getPostgresUserManager().addGrocery(storage, grocery);
        return "posted grocery: " + grocery.getName() + "into Storage: " + storage.getName();
    }

    @PostMapping(
            path = "/grocery/byID",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createGroceryByID(@RequestParam String storageID,
                                @RequestBody Grocery grocery){
        getPostgresUserManager().addGrocery(Integer.parseInt(storageID), grocery);
        return "posted grocery: " + grocery.getName() + "into Storage: " + storageID;
    }

    @GetMapping("/storage/grocery/byID/all"
    )
    public Collection<Grocery> getStorageGroceriesByID(@RequestParam String storageID
    ){

        final PostgresUserManager PostgresManager = getPostgresUserManager();
        return PostgresManager.getGroceries(Integer.parseInt(storageID));
    }


    @GetMapping("/user/storage/grocery/all"
    )
    public Collection<Grocery> getStorageGroceries(@RequestParam String storName,
                                    @RequestParam String ownerEmail
    ){

        final PostgresUserManager PostgresManager = getPostgresUserManager();
        User owner = PostgresManager.getUser("email", ownerEmail);
        Storage storage = PostgresManager.getStorage(storName, owner);
        return PostgresManager.getGroceries(storage.getStorageID());
    }


    @PostMapping(
            path = "/groceries/createtable"
    )
    @ResponseStatus(HttpStatus.OK)
    public String createGroceriesTable() {
        //Logger.getLogger("Test").log(Level.INFO, "Start Post create Table");

        final PostgresUserManager postgresUserManager =
                getPostgresUserManager();
        postgresUserManager.createTableGroceries();

        return "Database Groceries Table created";
    }

    @PostMapping(
            path = "/recipe/createtable"
    )
    @ResponseStatus(HttpStatus.OK)
    public String createRecipeTable() {
        final PostgresUserManager postgresUserManager =
                getPostgresUserManager();
        //postgresUserManager.createTableUser_rel_Recipe();
        postgresUserManager.createTableIngredients();
        postgresUserManager.createTableRecipes();

        return "Database Recipes Table created";
    }

    @PostMapping(
            path = "/recipe",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createRecipe(@RequestBody Recipe recipe, @RequestParam String OwnerID){
        getPostgresUserManager().addRecipe(Integer.parseInt(OwnerID), recipe);
        return "posted recipe: " + recipe.getName();
    }

    @PostMapping(
            path = "/recipe/ingredient",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createIngredient(@RequestBody Grocery ingredient, @RequestParam String RecipeID){
        getPostgresUserManager().addIngredient(Integer.parseInt(RecipeID), ingredient);
        return "posted ingredient: " + ingredient.getName();
    }

    @PutMapping(path= "/recipe",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String putRecipe(@RequestBody Recipe recipe
    ){
        getPostgresUserManager().putRecipe(recipe);
        return "updated Rezept: " + recipe.getName();
    }

    @PutMapping(path= "/recipe/ingredient",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String putIngredient(@RequestBody Grocery ingredient
    ){
        getPostgresUserManager().putIngredient(ingredient);
        return "updated User: " + ingredient.getName();
    }

    @GetMapping("/recipe/ingredient/all"
    )
    public Collection<Grocery> getRecipeGroceries(@RequestParam String recipeID){

        final PostgresUserManager PostgresManager = getPostgresUserManager();
        return PostgresManager.getAllIngredients(Integer.parseInt(recipeID));
    }

    @GetMapping("/recipe/all"
    )
    public Collection<Recipe> getRecipes(@RequestParam String userID){

        final PostgresUserManager PostgresManager = getPostgresUserManager();
        return PostgresManager.getAllRecipes(Integer.parseInt(userID));
    }

    @GetMapping("/recipe/byID"
    )
    public Recipe getRecipeByID(@RequestParam String recipeID){

        final PostgresUserManager PostgresManager = getPostgresUserManager();
        return PostgresManager.getRecipeByID(Integer.parseInt(recipeID));
    }

    @DeleteMapping(path="/recipe")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRecipe(@RequestParam String userID ,@RequestParam String recipeID){
        return getPostgresUserManager().deleteRecipe(Integer.parseInt(userID), Integer.parseInt(recipeID));
    }

    @DeleteMapping(path="/ingredient")
    @ResponseStatus(HttpStatus.OK)
    public String deleteIngredient(@RequestParam String recipeID, @RequestParam String ingredientID){
        return getPostgresUserManager().deleteIngredient(Integer.parseInt(recipeID), Integer.parseInt(ingredientID));
    }

    @GetMapping("/storage/recipe/suggestion"
    )
    public Collection<Recipe> getRecipeSug(@RequestParam String userID, @RequestParam String storageID){

        final PostgresUserManager PostgresManager = getPostgresUserManager();
        return PostgresManager.getAllRecipeSuggestions(Integer.parseInt(userID), Integer.parseInt(storageID));
    }


    @PostMapping(
            path = "/alexa",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public AlexaRO getGroceries(@RequestBody AlexaRO alexaRO) {

        if(alexaRO.getRequest().getType().equalsIgnoreCase("LaunchRequest")){
            return prepareResponse(alexaRO, "Welcome to the Virtual Fridge", false);
        }

        if(alexaRO.getRequest().getType().equalsIgnoreCase("IntentRequest") &&
                (alexaRO.getRequest().getIntent().getName().equalsIgnoreCase("ReadGroceriesIntent"))){
            StringBuilder outText  = new StringBuilder("");

            try {
                Storage storage = getPostgresUserManager().getStorage("Lager1",
                        getPostgresUserManager().getUser("email", "klaus@mail.com"));
                storage.setIDs(106, 80);
                storage.setGroceries();
                //AtomicInteger i = new AtomicInteger(0);
                outText.append(" Storage contains: ");
                storage.getGroceries().forEach(
                        groceries -> {

                            outText.append(groceries.getName() + " with the amount: " +
                                    groceries.getAmount() + " " + groceries.getUnit() + " ");
                        }
                );
                outText.append("Thank you for using our service");
            }
            catch (Exception e){
                outText.append("Unfortunately, we cannot reach heroku. Our REST server is not responding");
            }



            return
                    prepareResponse(alexaRO, outText.toString(), true);
        }
        return prepareResponse(alexaRO, "We could not help you", true);


        //String outText = "";


        //return alexaRO;
    }

    @PostMapping(
            path = "/alexa/readRecipes",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public AlexaRO getRecipes(@RequestBody AlexaRO alexaRO) {

        if(alexaRO.getRequest().getType().equalsIgnoreCase("LaunchRequest")){
            return prepareResponse(alexaRO, "Welcome to the Virtual Fridge", false);
        }

        if(alexaRO.getRequest().getType().equalsIgnoreCase("IntentRequest") &&
                (alexaRO.getRequest().getIntent().getName().equalsIgnoreCase("ReadRecipesIntent"))){
            StringBuilder outText  = new StringBuilder("");

            try {
                List<Recipe> recipes;
                recipes = (List<Recipe>) getPostgresUserManager().getAllRecipes(106);
                outText.append("Recipe list contains: ");
                for(int i = 0; i < recipes.size(); i++){
                    outText.append(recipes.get(i).getName() + " ");

                }

                outText.append("Thank you for using our service");
            }
            catch (Exception e){
                outText.append("Unfortunately, we cannot reach heroku. Our REST server is not responding");
            }



            return
                    prepareResponse(alexaRO, outText.toString(), true);
        }
        return prepareResponse(alexaRO, "We could not help you", true);


        //String outText = "";


        //return alexaRO;
    }

    @PostMapping(
            path = "/alexa/postskill",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public AlexaRO setGroceries(@RequestBody AlexaRO alexaRO) {

        if(alexaRO.getRequest().getType().equalsIgnoreCase("LaunchRequest")){
            return prepareResponse(alexaRO, "Welcome to the Virtual Fridge", false);
        }

        if(alexaRO.getRequest().getType().equalsIgnoreCase("IntentRequest") &&
                (alexaRO.getRequest().getIntent().getName().equalsIgnoreCase("AddGroceriesIntent"))){


            StringBuilder outText  = new StringBuilder("");


            /*try {
                Storage storage = getPostgresUserManager().getStorage("Lager1",
                        getPostgresUserManager().getUser("email", "klaus@mail.com"));
                storage.setIDs(9, 1);
                storage.setGroceries();
                //AtomicInteger i = new AtomicInteger(0);
                storage.getGroceries().forEach(
                        groceries -> {
                            outText.append(" Storage contains: ");
                            outText.append(groceries.getName() + " with the amount: " +
                                    groceries.getAmount() + " " + groceries.getUnit());
                        }
                );
                outText.append("Thank you for using our service");
            }
            catch (Exception e){
                outText.append("Unfortunately, we cannot reach heroku. Our REST server is not responding");
            }*/



            return
                    prepareResponse(alexaRO, outText.toString(), true);
        }

        return prepareResponse(alexaRO, "We could not help you", true);


        //String outText = "";


        //return alexaRO;
    }


    private AlexaRO prepareResponse(AlexaRO alexaRO, String outText, boolean shouldEndSession) {

        alexaRO.setRequest(null);
        alexaRO.setSession(null);
        alexaRO.setContext(null);
        OutputSpeechRO outputSpeechRO = new OutputSpeechRO();
        outputSpeechRO.setType("PlainText");
        outputSpeechRO.setText(outText);
        ResponseRO response = new ResponseRO(outputSpeechRO, shouldEndSession);
        alexaRO.setResponse(response);
        return alexaRO;
    }

}
