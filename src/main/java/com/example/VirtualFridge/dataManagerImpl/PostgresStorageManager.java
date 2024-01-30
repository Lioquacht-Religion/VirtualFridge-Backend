package com.example.VirtualFridge.dataManagerImpl;

import com.example.VirtualFridge.model.Grocery;
import com.example.VirtualFridge.model.Recipe;
import com.example.VirtualFridge.model.Storage;
import com.example.VirtualFridge.model.User;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PostgresStorageManager {
    BasicDataSource basicDataSource;

    static PostgresStorageManager postgresStorageManager = null;

    private PostgresStorageManager() {
        basicDataSource = DBCredentialsManager.getBasicDataSource();
    }

    public static PostgresStorageManager getPostgresStorageManager() {
        if(postgresStorageManager == null) {
            postgresStorageManager = new PostgresStorageManager();
        }
        return postgresStorageManager;
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


    public String addStorage(Storage storage, int userID) {

        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement("INSERT into storages (name, owner) VALUES ( ?, ?);");
            stmt.setString(1, storage.getName());
            stmt.setInt(2, userID);
            stmt.executeUpdate();

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return storage.getName();
    }

    public Collection<Storage> getStorages(int OwnerID) {
        Collection<Storage>  storage= new LinkedList<>();
        PreparedStatement stmt = null;Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM storages WHERE owner = ?;"
            );
            stmt.setInt(1, OwnerID);

            ResultSet rs = stmt.executeQuery();

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



    public Storage getStorage(int storID, User Owner) {

        Storage storage = new Storage("notFound", Owner);
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM storages WHERE storageid = ? AND owner = ?;"
            );
            stmt.setInt(1, storID);
            stmt.setInt(2, Owner.getID());

            ResultSet rs = stmt.executeQuery();

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

    public Storage getStorageByName(User Owner, String storName) {

        Storage storage = new Storage("notFound", Owner);
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM storages WHERE name = ? AND owner = ?;"
            );
            stmt.setString(1, storName);
            stmt.setInt(2, Owner.getID());

            ResultSet rs = stmt.executeQuery();

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

        PreparedStatement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();

            System.out.println("delete Storage and its groceries");

            String deleteStorageGroc =
                    "DELETE FROM groceries WHERE storedin = ? " +
                            "AND ? IN (SELECT storageid FROM storages WHERE owner = ?);";
            String deleteStorage =
                    "DELETE FROM storages WHERE storageid = ? AND owner = ?;";

            stmt = connection.prepareStatement(deleteStorageGroc);
            stmt.setInt(1, storageID);
            stmt.setInt(3, userID);
            stmt.setInt(2, storageID);
            stmt.executeUpdate();

            stmt = connection.prepareStatement(deleteStorage);
            stmt.setInt(1, storageID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete Storage and its groceries";
    }

    public String deleteGrocery(int ownerID, int storageID, int groceryID){

        PreparedStatement stmt = null; Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            System.out.println("delete Grocery from Storage");

            stmt = connection.prepareStatement(
                    "DELETE FROM groceries WHERE groceryid = ? " +
                            "AND ? IN (SELECT storageid FROM storages WHERE owner = ?);"
            );
            stmt.setInt(1, groceryID);
            stmt.setInt(3, ownerID);
            stmt.setInt(2, storageID);
            stmt.executeUpdate();

        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete Grocery from Storage";
    }

    public String addGrocery(int storageID, Grocery grocery) {
        PreparedStatement stmt = null; Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "INSERT into groceries (name, amount, unit, storedin) VALUES (?, ?, ?, ?);"
            );
            stmt.setString(1, grocery.getName());
            stmt.setInt(2, grocery.getAmount());
            stmt.setString(3, grocery.getUnit());
            stmt.setInt(4, storageID);

            stmt.executeUpdate();

            stmt.close();
            connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return grocery.getName();
    }

    /*
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

     */

    public Collection<Grocery> getGroceries(int userID, int storageID) {

        Collection<Grocery> groceries = new ArrayList<Grocery>();
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM groceries WHERE storedin = ? AND ? IN (SELECT storageid FROM storages WHERE owner = ?);"
            );
            stmt.setInt(1, storageID);
            stmt.setInt(3, userID);
            stmt.setInt(2, storageID);

            ResultSet rs = stmt.executeQuery();

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
        PostgresRecipeManager recipeManager = PostgresRecipeManager.getPostgresRecipeManager();
        Collection<Recipe> recipes = recipeManager.getAllRecipes(userID);
        Collection<Grocery> storGrocs = getGroceries(userID, storageID);
        Collection<Recipe> RecipeSugs = new LinkedList<Recipe>();

            for(Recipe r : recipes){
                Collection<Grocery> rIngs = recipeManager.getAllIngredients(userID, r.getRecipeID());
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





}
