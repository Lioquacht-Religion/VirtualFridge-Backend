package com.example.VirtualFridge.dataManagerImpl;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;

public class PostgresTableManager {

    private BasicDataSource basicDataSource = DBCredentialsManager.getBasicDataSource();
    private static PostgresTableManager postgresTableManager = null;

    private  PostgresTableManager() {
        basicDataSource = DBCredentialsManager.getBasicDataSource();
    }

    public static PostgresTableManager getPostgresTableManager(){
        if(postgresTableManager == null){
            postgresTableManager = new PostgresTableManager();
        }
        return postgresTableManager;

    }

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

    public void createTableShoppinglist() {

        // Be carefull: It deletes data if table already exists.
        //
        System.out.println("Starting to create new storage Table");
        Statement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            System.out.println("getting datasource");

            String dropTable = "DROP TABLE IF EXISTS shoppinglists";
            stmt.executeUpdate(dropTable);

            System.out.println("creating shoppinglists Table");

            String createTable = "CREATE TABLE shoppinglists (" +
                    "ShoppingListId SERIAL PRIMARY KEY, " +
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

    public void createTableShoppinglistItem() {

        // Be carefull: It deletes data if table already exists.
        //
        System.out.println("Starting to create new storage Table");
        Statement stmt = null;
        Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            System.out.println("getting datasource");

            String dropTable = "DROP TABLE IF EXISTS shoppingitems";
            stmt.executeUpdate(dropTable);

            System.out.println("creating shoppinglistitems Table");

            String createTable = "CREATE TABLE shoppingitems (" +
                    "ShoppingItemId SERIAL PRIMARY KEY, " +
                    "name varchar(100) NOT NULL, " +
                    "amount int, " +
                    "unit varchar(50)," +
                    "ticked boolean NOT NULL" +
                    "itemOfList int NOT NULL, " +
                    "FOREIGN KEY (itemOfList) REFERENCES shoppinglists(ShoppingListId))";
            stmt.executeUpdate(createTable);
            System.out.println("storage Table created");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }





}
