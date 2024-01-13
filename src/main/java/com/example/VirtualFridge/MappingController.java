package com.example.VirtualFridge;


import com.example.VirtualFridge.dataManagerImpl.PostgresShoppinglistManager;
import com.example.VirtualFridge.dataManagerImpl.PostgresStorageManager;
import com.example.VirtualFridge.dataManagerImpl.PostgresTableManager;
import com.example.VirtualFridge.dataManagerImpl.PostgresUserManager;
import com.example.VirtualFridge.model.*;
import com.example.VirtualFridge.model.alexa.OutputSpeechRO;
import com.example.VirtualFridge.model.alexa.ResponseRO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import com.example.VirtualFridge.model.alexa.AlexaRO;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.VirtualFridge.dataManagerImpl.PostgresRecipeManager.getPostgresRecipeManager;
import static com.example.VirtualFridge.dataManagerImpl.PostgresShoppinglistManager.getPostgresShoppinglistManager;
import static com.example.VirtualFridge.dataManagerImpl.PostgresStorageManager.getPostgresStorageManager;
import static com.example.VirtualFridge.dataManagerImpl.PostgresTableManager.getPostgresTableManager;
import static com.example.VirtualFridge.dataManagerImpl.PostgresUserManager.getPostgresUserManager;
import static java.lang.Integer.parseInt;


//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1.0")
public class MappingController {

    @PostMapping(
            path = "/user/register",
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

    @GetMapping(path= "/user/authenticated",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public User getUserAuthenticated(
            @AuthenticationPrincipal User user
    ){
        User l_user = new User(user.getName(), user.getEmail(), "");
        l_user.setID(user.getID());
        return l_user;
    }

    @GetMapping("/user/email"
    )
    public User getUser(
            @AuthenticationPrincipal User user
            //@RequestParam String email
    ){
        return getPostgresUserManager().getUser(user.getEmail());
    }

    //TODO: change endpoint in frontend
    @PutMapping(path= "/user/update",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String putUser(
            @AuthenticationPrincipal User user,
            @RequestBody User updateUserData
    ){
        getPostgresUserManager().putUser(updateUserData, user);
        return "updated User: " + user.getEmail();
    }

    @DeleteMapping(
            path="/user/delete"
            )
    @ResponseStatus(HttpStatus.OK)
    public String deleteUser(
            @AuthenticationPrincipal User user,
            @RequestParam String userID, @RequestParam String email, @RequestParam String password){
        //UserList userList = new UserList();
        //userList.setUsers();
        //userList.deleteUser(user);
        //getPropertyFileUserManager("src/main/resources/user.properties").storeAllUsers(userList.getUsers());
        getPostgresUserManager().deleteUser(user.getID(), user.getEmail(), user.getPassword());

        return userID;
    }


    @PostMapping(
            path = "/user/createtable"
    )
    @ResponseStatus(HttpStatus.OK)
    public String createUserTable() {
        Logger.getLogger("Test").log(Level.INFO, "Start Post create Table");

        final PostgresTableManager postgresTableManager =
                getPostgresTableManager();
        postgresTableManager.createTableUser();

        return "Database Table created";
    }


    @GetMapping("/user/storage/all")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Storage> getUserStorages(
            @AuthenticationPrincipal User user
            //@RequestParam String OwnerID
    ){
        return getPostgresStorageManager().getStorages(user.getID());
    }

    @GetMapping("/storage")
    @ResponseStatus(HttpStatus.OK)
    public Storage getStorage(
            @AuthenticationPrincipal User user,
            @RequestParam int storID){
        return getPostgresStorageManager().getStorage(storID, user);
    }


    @DeleteMapping(path="/storage")
    @ResponseStatus(HttpStatus.OK)
    public String deleteStorage(@AuthenticationPrincipal User user, @RequestParam int storageID){
        return getPostgresStorageManager().deleteStorage(user.getID(), storageID);
    }

    @DeleteMapping(path="/grocery")
    @ResponseStatus(HttpStatus.OK)
    public String deleteGrocery(@AuthenticationPrincipal User user, @RequestParam int storageID, @RequestParam int groceryID){
        return getPostgresStorageManager().deleteGrocery(user.getID(), storageID, groceryID);
    }

    @PostMapping(
            path = "/storage",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createStorage(
            @AuthenticationPrincipal User user,
            @RequestBody Storage storage
    ){
        //getPropertyFileUserManager("src/main/resources/user.properties").addUser(user);
        getPostgresStorageManager().addStorage(storage, user.getID());
        return "posted storage: " + storage.getName();
    }

    @PostMapping(
            path = "/storage/createtable"
    )
    @ResponseStatus(HttpStatus.OK)
    public String createStorageTable() {
        //Logger.getLogger("Test").log(Level.INFO, "Start Post create Table");
        getPostgresTableManager().createTableStorage();
        return "Database Storage Table created";
    }

    @PostMapping(
            path = "/grocery",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createGrocery(
            @AuthenticationPrincipal User user,
            @RequestParam int storID,
            @RequestParam String ownerEmail,
            @RequestBody Grocery grocery){
        //getPropertyFileUserManager("src/main/resources/user.properties").addUser(user);
        final PostgresStorageManager StorageManager = getPostgresStorageManager();
        Storage storage = StorageManager.getStorage(storID, user);
        StorageManager.addGrocery(storID, grocery);
        return "posted grocery: " + grocery.getName() + "into Storage: " + storage.getName();
    }

    @PostMapping(
            path = "/grocery/byID",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createGroceryByID(@RequestParam int storageID,
                                @RequestBody Grocery grocery){
        getPostgresStorageManager().addGrocery(storageID, grocery);
        return "posted grocery: " + grocery.getName() + "into Storage: " + storageID;
    }

    @GetMapping("/storage/grocery/byID/all")
    public Collection<Grocery> getStorageGroceriesByID(
            @AuthenticationPrincipal User user,
            @RequestParam int storageID
    ){
        return getPostgresStorageManager().getGroceries(user.getID(), storageID);
    }


    @GetMapping("/user/storage/grocery/all"
    )
    public Collection<Grocery> getStorageGroceries(
            @AuthenticationPrincipal User user,
            @RequestParam int storID
    ){

        return getPostgresStorageManager().getGroceries(user.getID(), storID);
    }


    @PostMapping(
            path = "/groceries/createtable"
    )
    @ResponseStatus(HttpStatus.OK)
    public String createGroceriesTable() {

        getPostgresTableManager().createTableGroceries();

        return "Database Groceries Table created";
    }

    @GetMapping("/recipe/createtable")
    //@ResponseStatus(HttpStatus.OK)
    public String createRecipeTable(@AuthenticationPrincipal User user) {
        final PostgresTableManager postgresTableManager = getPostgresTableManager();
        postgresTableManager.createTableRecipes();
        postgresTableManager.createTableIngredients();

        return "Database Recipes Table created by " + user.getEmail();
    }

    @PostMapping(
            path = "/recipe",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    //@ResponseStatus(HttpStatus.OK)
    public String createRecipe(
            @AuthenticationPrincipal User user,
            @RequestBody Recipe recipe
    ){
        getPostgresRecipeManager().addRecipe(user.getID(), recipe);
        return "posted recipe: " + recipe.getName();
    }

    @PostMapping(
            path = "/recipe/ingredient",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String createIngredient(@RequestBody Grocery ingredient, @RequestParam int RecipeID){
        getPostgresRecipeManager().addIngredient(RecipeID, ingredient);
        return "posted ingredient: " + ingredient.getName();
    }

    @PutMapping(path= "/recipe",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String putRecipe(
            @AuthenticationPrincipal User user,
            @RequestBody Recipe recipe
    ){
        getPostgresRecipeManager().putRecipe(user.getID(), recipe);
        return "updated Rezept: " + recipe.getName();
    }

    @PutMapping(path= "/recipe/ingredient",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @ResponseStatus(HttpStatus.OK)
    public String putIngredient(
            @AuthenticationPrincipal User user,
            @RequestBody Grocery ingredient
    ){
        getPostgresRecipeManager().putIngredient(user.getID(), ingredient);
        return "updated User: " + ingredient.getName();
    }

    @GetMapping("/recipe/ingredient/all"
    )
    public Collection<Grocery> getRecipeGroceries(
            @AuthenticationPrincipal User user,
            @RequestParam int recipeID
    ){
        return getPostgresRecipeManager().getAllIngredients(user.getID(), recipeID);
    }

    @GetMapping("/recipe/all"
    )
    public Collection<Recipe> getRecipes(@AuthenticationPrincipal User user){
        return getPostgresRecipeManager().getAllRecipes(user.getID());
    }

    @GetMapping("/recipe/byID"
    )
    public Recipe getRecipeByID(
            @AuthenticationPrincipal User user,
            @RequestParam int recipeID
    ){
        return getPostgresRecipeManager().getRecipeByID(user.getID(), recipeID);
    }

    @DeleteMapping(path="/recipe")
    @ResponseStatus(HttpStatus.OK)
    public String deleteRecipe(
            @AuthenticationPrincipal User user,
            @RequestParam int recipeID
    ){
        return getPostgresRecipeManager().deleteRecipe(user.getID(), recipeID);
    }

    @DeleteMapping(path="/ingredient")
    @ResponseStatus(HttpStatus.OK)
    public String deleteIngredient(
            @AuthenticationPrincipal User user,
            @RequestParam int recipeID,
            @RequestParam int ingredientID
    ){
        return getPostgresRecipeManager().deleteIngredient(user.getID(), recipeID, ingredientID);
    }

    @GetMapping("/storage/recipe/suggestion")
    public Collection<Recipe> getRecipeSug(@RequestParam String userID, @RequestParam String storageID){
        return getPostgresStorageManager().getAllRecipeSuggestions(Integer.parseInt(userID), Integer.parseInt(storageID));
    }

    @GetMapping(
            path="/shoppinglist/all",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Collection<ShoppingList> getShoppingLists(
            @AuthenticationPrincipal User user
    ){
        return getPostgresShoppinglistManager().getShoppingLists(user.getID());
    }

    @GetMapping(
            path="/shoppinglist/item/all",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public Collection<ShoppingListItem> getShoppingListItems(
            @AuthenticationPrincipal User user,
            @RequestParam int shoppingListId
    ){
        return getPostgresShoppinglistManager().getListItems(user.getID(), shoppingListId);
    }


    @PostMapping(
            path="/shoppinglist/add",
            consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public String postShoppingList(
            @AuthenticationPrincipal User user,
            @RequestParam ShoppingList shoppingList
    ){
        return getPostgresShoppinglistManager().addShoppingList(shoppingList, user.getID());
    }

    @PostMapping(
            path="/shoppinglist/item/add",
            consumes= {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    public String postShoppingListItem(
            @AuthenticationPrincipal User user,
            @RequestParam int shoppingListId,
            @RequestParam ShoppingListItem item
    ){
        return getPostgresShoppinglistManager().addItem(shoppingListId, item);
    }

    @DeleteMapping("/shoppinglist")
    public String deleteShoppingList(
            @AuthenticationPrincipal User user,
            @RequestParam int shoppingListId
    ){
        return getPostgresShoppinglistManager().deleteShoppingList(user.getID(), shoppingListId);
    }

    @DeleteMapping("/shoppinglist/item")
    public String deleteShoppingListItem(
            @AuthenticationPrincipal User user,
            @RequestParam int shoppingListId,
            @RequestParam int itemId
    ){
        return getPostgresShoppinglistManager().deleteItem(user.getID(), shoppingListId, itemId);
    }

    @PutMapping("/shoppinglist/item/ticked")
    public String putShoppingListItemTicked(
            @AuthenticationPrincipal User user,
            @RequestParam int shoppingListId,
            @RequestParam int itemId,
            @RequestParam boolean ticked
    ){
        return getPostgresShoppinglistManager().setItemTicked(user.getID(), shoppingListId, itemId, ticked);
    }

    @GetMapping("/shoppinglist/createtable")
    public String createShoppingListTable(){
        getPostgresTableManager().createTableShoppinglist();
        getPostgresTableManager().createTableShoppinglistItem();
        return "created shoppinglisttable";
    }




    /*
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
                Storage storage = getPostgresStorageManager().getStorage("Lager1",
                        getPostgresUserManager().getUser("klaus@mail.com"));
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
    */

/*
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
*/

    /*
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

            return prepareResponse(alexaRO, outText.toString(), true);
        }

        return prepareResponse(alexaRO, "We could not help you", true);


        //String outText = "";


        //return alexaRO;
    }*/


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
