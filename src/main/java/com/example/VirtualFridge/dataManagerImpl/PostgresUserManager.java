package com.example.VirtualFridge.dataManagerImpl;

import com.example.VirtualFridge.dataManager.UserManager;
import com.example.VirtualFridge.model.Grocery;
import com.example.VirtualFridge.model.Recipe;
import com.example.VirtualFridge.model.Storage;
import com.example.VirtualFridge.model.User;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostgresUserManager implements UserManager, UserDetailsService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.getUser(username);
    }

    String databaseURL = "";
    String username = "";
    String password = "";
    BasicDataSource basicDataSource;

    void getDBLoginData(String filename){
        //reading dbinfo from file
        try(BufferedReader br = new BufferedReader(new FileReader(filename))){
            for(String line; (line = br.readLine()) != null;){
                String[] varval = line.split("=", 2);
                if(varval.length == 2){
                    String var = varval[0];
                    String val = varval[1];
                    if(var.equals("url")){
                        this.databaseURL = val;
                    }
                    else if(var.equals("user")){
                        this.username = val;
                    }
                    else if(var.equals("password")){
                        this.password = val;
                    }
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }

    }

    static PostgresUserManager postgresUserManager = null;

    private PostgresUserManager() {
        basicDataSource = DBCredentialsManager.getBasicDataSource();
        /*
        this.getDBLoginData("src/main/resources/.dblogininfo");
        //basicDataSource.setDriverClassName("org.postgresql.Driver");
        basicDataSource.setUrl(databaseURL);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        //basicDataSource.setDriverClassName("org.postgresql.Driver");
         */
    }

    static public PostgresUserManager getPostgresUserManager() {
        if (postgresUserManager == null)
            postgresUserManager = new PostgresUserManager();
        return postgresUserManager;
    }


    @Override
    public Collection<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        Statement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                User l_u = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                l_u.setID(rs.getInt("id"));
                users.add(l_u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return users;
    }

    //@Override
    public User getUser(String email) {

        User r_user = new User("notFound", "404", "BOB");
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM users WHERE email = ? ");
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                r_user = new User(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                r_user.setID(rs.getInt("id"));
            }
            else{ r_user = new User("notFound", "404", "BOB");}

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return r_user;
    }

    @Override
    public String addUser(User user) {

        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            //stmt = connection.createStatement();

            stmt = connection.prepareStatement(
                    "INSERT into users (name, email, password) VALUES ( ?, ?, ?)"
            );
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3,
                    new BCryptPasswordEncoder().encode(
                    user.getPassword())
            );
           /*
           old, not secure code, because of risk from SQL injection
            String udapteSQL =
                    "INSERT into users (name, email, password) VALUES (" +
                    "'" + user.getName() + "', " +
                    "'" + user.getEmail() + "', " +
                    "'" + user.getPassword() + "')";

            stmt.executeUpdate(udapteSQL);
           */

            //stmt.executeQuery();
            stmt.executeUpdate();

            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user.getEmail();

    }

    public String putUser(User updateUserData, User oldUserData) {

        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "UPDATE users SET name = ?, email = ?, password = ? WHERE id = ?;"
            );
            stmt.setString(1, updateUserData.getName() );
            stmt.setString(2, oldUserData.getEmail() );
            stmt.setString(3,
                    new BCryptPasswordEncoder().encode(
                    updateUserData.getPassword())
            );
            stmt.setInt(4, oldUserData.getID());

            stmt.executeUpdate();

            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "changed userstuff: " + updateUserData.getEmail();

    }

    public void deleteUser(int userID, String email, String password){

        PreparedStatement stmt = null; Connection connection = null;
        try{
            connection = basicDataSource.getConnection();

            String deleteGroceries =
                    "DELETE FROM groceries WHERE storedin IN  (SELECT storageid FROM storages WHERE owner = ?);";
            String deleteStorages =
                    "DELETE FROM storages WHERE owner = ?;";

            String deleteIngredients =
                    "DELETE FROM ingredients WHERE partofrecipe IN  (SELECT recipeid FROM recipes WHERE owner = ?);";
            String deleteRecipes =
                    "DELETE FROM recipes WHERE owner = ?;";

            System.out.println("delete User");
            String deleteUser =
                    "DELETE FROM users WHERE id = ?;";


            stmt = connection.prepareStatement(
                    deleteGroceries + deleteStorages
                    + deleteIngredients + deleteRecipes
                    + deleteUser
            );
            stmt.setInt(1, userID);
            stmt.setInt(2, userID);

            stmt.setInt(3, userID);
            stmt.setInt(4, userID);

            stmt.setInt(5, userID);
            stmt.setInt(6, userID);
            stmt.setInt(7, userID);


            stmt.executeUpdate(deleteGroceries);
            stmt.executeUpdate(deleteStorages);
            stmt.executeUpdate(deleteIngredients);
            stmt.executeUpdate(deleteRecipes);
            stmt.executeUpdate(deleteUser);
            System.out.println("user deleted");

        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
    }

    /*
    public void createTableUser() {

        // Be carefull: It deletes data if table already exists.
        //
        System.out.println("Starting to create new user Table");
        Statement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            System.out.println("getting datasource");

            String dropTable = "DROP TABLE IF EXISTS users";
            stmt.executeUpdate(dropTable);

            System.out.println("creating user Table");

            String createTable = "CREATE TABLE users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "name varchar(100) NOT NULL, " +
                    "email varchar(258) NOT NULL, " +
                    "password varchar(100) NOT NULL)";
            stmt.executeUpdate(createTable);
            System.out.println("user Table created");

        }
        catch(SQLException e){
            e.printStackTrace();
        }

        try{
            stmt.close();
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }


    }

    public void createTableStorage() {

        // Be carefull: It deletes data if table already exists.
        //
        System.out.println("Starting to create new storage Table");
        Statement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            System.out.println("getting datasource");

            String dropTable = "DROP TABLE IF EXISTS storages";
            stmt.executeUpdate(dropTable);

            System.out.println("creating storage Table");

            String createTable = "CREATE TABLE storages (" +
                    "StorageId SERIAL PRIMARY KEY, " +
                    "name varchar(100) NOT NULL, " +
                    "Owner int NOT NULL, " +
                    "FOREIGN KEY (Owner) REFERENCES users(id))";
            stmt.executeUpdate(createTable);
            System.out.println("storage Table created");

        }
        catch(SQLException e){
            e.printStackTrace();
        }

        try{
            stmt.close();
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }


    }

    public void createTableGroceries() {

        // Be carefull: It deletes data if table already exists.
        //
        System.out.println("Starting to create new groceries Table");
        Statement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            System.out.println("getting datasource");

            String dropTable = "DROP TABLE IF EXISTS groceries";
            stmt.executeUpdate(dropTable);

            System.out.println("creating groceries Table");

            String createTable = "CREATE TABLE groceries (" +
                    "GroceryId SERIAL PRIMARY KEY, " +
                    "name varchar(100) NOT NULL, " +
                    "amount int, " +
                    "unit varchar(50)," +
                    "StoredIn int NOT NULL, " +
                    "FOREIGN KEY (StoredIn) REFERENCES storages(StorageId))";
            stmt.executeUpdate(createTable);
            System.out.println("groceries Table created");

        }
        catch(SQLException e){
            e.printStackTrace();
        }

        try{
            stmt.close();
            connection.close();
        }catch(SQLException e){
            e.printStackTrace();
        }


    }

    public void createTableRecipes() {
        System.out.println("Starting to create new Recipe Table");
        Statement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String dropTable = "DROP TABLE IF EXISTS recipes";
            stmt.executeUpdate(dropTable);
            String createTable = "CREATE TABLE recipes (" +
                    "RecipeId SERIAL PRIMARY KEY, " +
                    "name varchar(100) NOT NULL, " +
                    "description varchar(5000) NOT NULL, " +
                    "Owner int NOT NULL, " +
                    "FOREIGN KEY (Owner) REFERENCES users(id))";
            stmt.executeUpdate(createTable);
            System.out.println("recipe Table created");

        }
        catch(SQLException e){e.printStackTrace();}
        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
    }

    public void createTableIngredients() {
        System.out.println("Starting to create new Ingredient Table");
        Statement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String dropTable = "DROP TABLE IF EXISTS ingredients";
            stmt.executeUpdate(dropTable);
            String createTable = "CREATE TABLE ingredients (" +
                    "IngredientId SERIAL PRIMARY KEY, " +
                    "name varchar(100) NOT NULL, " +
                    "amount int, " +
                    "unit varchar(50)," +
                    "partOfRecipe int NOT NULL, " +
                    "FOREIGN KEY (partOfRecipe) REFERENCES recipes(RecipeId))";
            stmt.executeUpdate(createTable);
            System.out.println("ingredient Table created");

        }
        catch(SQLException e){e.printStackTrace();}
        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
    }



    public String addRecipe(int initUser, Recipe recipe) {
        Statement stmt = null; Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String udapteSQL = "INSERT into recipes (name, description, Owner) VALUES (" +
                    "'" + recipe.getName() + "', '" +
                    recipe.getDescription() + "', " +
                    initUser +")";
            stmt.executeUpdate(udapteSQL);

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();} catch (SQLException e) {e.printStackTrace();}

        return recipe.getName();

    }

    public Collection<Recipe> getAllRecipes(int userID) {

        List<Recipe> recipes = new LinkedList<>();
        Statement stmt = null;Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM recipes WHERE Owner = " + userID);
            while (rs.next()) {
                Recipe l_rec = new Recipe(
                        rs.getString("name"),
                        rs.getString("description")
                );
                l_rec.setRecipeID(rs.getInt("RecipeId"));
                l_rec.setAuthorID(rs.getInt("Owner"));
                recipes.add(l_rec);
            }
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return recipes;
    }

    public Recipe getRecipeByID(int recipeID) {

        Recipe recipe = new Recipe("404", "error couldnt find recipe to update");
        Statement stmt = null;Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM recipes WHERE recipeID = " + recipeID);
            if (rs.next()) {
                recipe = new Recipe(
                        rs.getString("name"),
                        rs.getString("description")
                );
                recipe.setRecipeID(rs.getInt("RecipeId"));
                recipe.setAuthorID(rs.getInt("Owner"));
            }
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return recipe;
    }

    public String addIngredient(int RecipeID, Grocery ingredient) {
        Statement stmt = null; Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String udapteSQL = "INSERT into ingredients (name, amount, unit, partOfRecipe) VALUES (" +
                    "'" + ingredient.getName() + "', " +
                    ingredient.getAmount() + ", " +
                    "'" + ingredient.getUnit() + "', " +
                    RecipeID +")";
            stmt.executeUpdate(udapteSQL);

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();} catch (SQLException e) {e.printStackTrace();}

        return ingredient.getName();

    }

    public String putRecipe(Recipe recipe) {
        Statement stmt = null;Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String udapteSQL =
                    "UPDATE recipes " +
                            "SET name = '" + recipe.getName() + "', description = '" + recipe.getDescription() +
                            "' WHERE recipeid = " + recipe.getRecipeID();

            stmt.executeUpdate(udapteSQL);
            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return "changed recipestuff: " + recipe.getName();

    }

    public String putIngredient(Grocery ingredient) {
        Statement stmt = null;Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String udapteSQL =
                    "UPDATE ingredients " +
                            "SET name = '" + ingredient.getName() + "', amount = " + ingredient.getAmount() +", unit ='"
                            + ingredient.getUnit() + "' " +
                            "WHERE IngredientId = " + ingredient.getID();

            stmt.executeUpdate(udapteSQL);
            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return "changed ingredientstuff: " + ingredient.getName();

    }

    public Collection<Grocery> getAllIngredients(int recipeID) {

        List<Grocery> ingredients = new LinkedList<>();
        Statement stmt = null;Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM ingredients WHERE partOfRecipe =" + recipeID);
            while (rs.next()) {
                Grocery l_ing = new Grocery(
                        rs.getString("name"),
                        rs.getString("unit"),
                        rs.getInt("amount")
                );
                l_ing.setID(rs.getInt("ingredientid"));
                ingredients.add(l_ing);
            }
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return ingredients;
    }


    public String deleteRecipe(int userID, int recipeID){

        Statement stmt = null; Connection connection = null;
        try{
            connection = basicDataSource.getConnection(); stmt = connection.createStatement();

            System.out.println("delete Recipe and its ingredients");

            String deleteStorageGroc = "DELETE FROM ingredients WHERE " +
                    "partofrecipe = " + recipeID;
            stmt.executeUpdate(deleteStorageGroc);

            String deleteStorage = "DELETE FROM recipes WHERE " +
                    "recipeid = " + recipeID + " AND owner = " + userID;
            stmt.executeUpdate(deleteStorage);


        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete recipe and its ingredients";
    }

    public String deleteIngredient(int storageID, int groceryID){

        Statement stmt = null; Connection connection = null;
        try{
            connection = basicDataSource.getConnection(); stmt = connection.createStatement();

            System.out.println("delete ingredient from recipe");

            String deleteStorageGroc = "DELETE FROM ingredients WHERE " +
                    "partofrecipe = " + storageID + "AND ingredientid = " + groceryID;
            stmt.executeUpdate(deleteStorageGroc);

        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete ingredient from recipe";
    }




    public String addStorage(Storage storage) {

        Statement stmt = null;
        Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String getStorOwnerId = "(SELECT id FROM users WHERE email = '" + storage.getOwner().getEmail() + "')";
            String udapteSQL = "INSERT into storages (name, Owner) VALUES (" +
                    "'" + storage.getName() + "', " +
                    storage.getOwnerID() + ")";

            stmt.executeUpdate(udapteSQL);

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return storage.getName();
    }

    public Collection<Storage> getStorages(int OwnerID) {
        Collection<Storage>  storage= new LinkedList<>();
        Statement stmt = null;Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String getStorages =
                    "SELECT * FROM storages WHERE owner = " + OwnerID;

            ResultSet rs = stmt.executeQuery(getStorages);

            while(rs.next()) {
                Storage l_stor = new Storage(
                        rs.getString("name"),
                        new User("", "", ""));
                l_stor.setIDs( OwnerID, rs.getInt("storageid") );
                storage.add(l_stor);
            }
        } catch (SQLException e) {e.printStackTrace();}

        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        return storage;
    }



    public Storage getStorage(String storName, User Owner) {

        Storage storage = new Storage("notFound", Owner);
        Statement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String getStorOwnerId = "(SELECT id FROM users WHERE email = '" + Owner.getEmail() + "')";
            String getUser =
                    "SELECT * FROM storages WHERE name = '" + storName + "' AND owner = " + getStorOwnerId;

            ResultSet rs = stmt.executeQuery(getUser);

            if(rs.next()) {
                storage = new Storage(
                        rs.getString("name"),
                        Owner
                );
                storage.setIDs(  rs.getInt("owner"),
                        rs.getInt("storageid") );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return storage;
    }

    public String deleteStorage(int userID, int storageID){

        Statement stmt = null; Connection connection = null;
        try{
            connection = basicDataSource.getConnection(); stmt = connection.createStatement();

            System.out.println("delete Storage and its groceries");

            String deleteStorageGroc = "DELETE FROM groceries WHERE " +
                    "storedin = " + storageID;
            stmt.executeUpdate(deleteStorageGroc);

            String deleteStorage = "DELETE FROM storages WHERE " +
                    "storageid = " + storageID + " AND owner = " + userID;
            stmt.executeUpdate(deleteStorage);


        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete Storage and its groceries";
    }

    public String deleteGrocery(int storageID, int groceryID){

        Statement stmt = null; Connection connection = null;
        try{
            connection = basicDataSource.getConnection(); stmt = connection.createStatement();

            System.out.println("delete Grocery from Storage");

            String deleteStorageGroc = "DELETE FROM groceries WHERE " +
                    "storedin = " + storageID + "AND groceryid = " + groceryID + ";";
            stmt.executeUpdate(deleteStorageGroc);

        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete Grocery from Storage";
    }

    public String addGrocery(int storageID, Grocery grocery) {
        Statement stmt = null; Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String udapteSQL = "INSERT into groceries (name, amount, unit, storedin) VALUES (" +
                    "'" + grocery.getName() + "', " +
                    "" + grocery.getAmount() + ", " +
                    "'" + grocery.getUnit() + "', " +
                    "" + storageID  + ")";

            stmt.executeUpdate(udapteSQL);

            stmt.close();
            connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return grocery.getName();
    }

    public String addGrocery(Storage storage, Grocery grocery) {
        Statement stmt = null; Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            String getStorOwnerId = "(SELECT id FROM users WHERE email = '" + storage.getOwner().getEmail() + "')";
            String udapteSQL = "INSERT into groceries (name, amount, unit, storedin) VALUES (" +
                    "'" + grocery.getName() + "', " +
                    "'" + grocery.getAmount() + "', " +
                    "'" + grocery.getUnit() + "', " +
                    "'" + storage.getStorageID()  + "')";

            stmt.executeUpdate(udapteSQL);

            stmt.close();
            connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return storage.getName();
    }

    public Collection<Grocery> getGroceries(int storageID) {

        Collection<Grocery> groceries = new ArrayList<Grocery>();
        Statement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            //String getStorOwnerId = "(SELECT id FROM users WHERE email = '" + Owner.getEmail() + "')";
            String getGroceries =
                    "SELECT * FROM groceries WHERE storedin = '" + storageID + "'";

            ResultSet rs = stmt.executeQuery(getGroceries);

            while(rs.next()) {
                 Grocery l_groc = new Grocery(
                        rs.getString("name"),
                        rs.getString("unit"),
                        rs.getInt("amount")
                );
                 l_groc.setIDs(rs.getInt("groceryid"), storageID);
                 groceries.add(l_groc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return groceries;
    }


    public Collection<Recipe> getAllRecipeSuggestions(int userID, int storageID) {
        Collection<Recipe> recipes = getAllRecipes(userID);
        Collection<Grocery> storGrocs = getGroceries(storageID);
        Collection<Recipe> RecipeSugs = new LinkedList<Recipe>();

            for(Recipe r : recipes){
                Collection<Grocery> rIngs = getAllIngredients(r.getRecipeID());
                boolean hasAllIng = true;
                for(Grocery ing : rIngs){
                    boolean hasIng = false;
                    for(Grocery stGroc : storGrocs){
                        if(stGroc.equals(ing) && stGroc.getAmount() >= ing.getAmount()){
                            hasAllIng = true; break; //Ingredient found, break out of storage, search next ing
                        }
                        else hasAllIng = false;
                    }
                    if(!hasAllIng) break; //Ingredient not found, move to next recipe
                }
                if(hasAllIng){ r.setIngredients(rIngs); RecipeSugs.add(r); }
            }
        return RecipeSugs;
    }

    */



}


