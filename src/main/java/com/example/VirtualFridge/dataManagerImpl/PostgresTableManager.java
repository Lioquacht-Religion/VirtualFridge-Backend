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

    public void createTableFood(){
        String createTable = "CREATE TABLE food (" +
                    "food_id SERIAL PRIMARY KEY, " +
                    "food_name varchar(100) NOT NULL)";

        createTable(createTable, "food");
    }

    public void createTableFoodAttributeRel(){
        String createTable = "CREATE TABLE food_attribute (" +
                    "food_id int REFERENCES food (food_id) ON UPDATE CASCADE ON DELETE CASCADE , " +
                    "attribute_id int REFERENCES attribute (attribute_id) ON UPDATE CASCADE  ON DELETE CASCADE )"
                ;

        createTable(createTable, "food_attribute");
    }

    public void createTableAttribute(){
        String createTable = "CREATE TABLE attribute (" +
                    "attribute_id SERIAL PRIMARY KEY, " +
                    "attribute_name varchar(100) NOT NULL," +
                    "unit_id int REFERENCES unit (unit_id))";

        createTable(createTable, "attribute");

    }

    public void createTableUnit(){
        String createTable = "CREATE TABLE unit (" +
                    "unit_id SERIAL PRIMARY KEY, " +
                    "unit_name varchar(30) NOT NULL)";
        createTable(createTable, "unit");
    }

    public void createTableAttrValue(){
        String createTable = "CREATE TABLE attributevalue (" +
                    "attributevalue_id SERIAL PRIMARY KEY, " +
                    "attributevalue_value varchar(30) NOT NULL, " +
                    "food_id int NOT NULL," +
                    "FOREIGN KEY (food_id) REFERENCES food(food_id) ON UPDATE CASCADE ON DELETE CASCADE," +
                    "attribute_id int NOT NULL, " +
                    "FOREIGN KEY (attribute_id) REFERENCES attribute(attribute_id) ON UPDATE CASCADE ON DELETE CASCADE )";

        createTable(createTable, "attributevalue");

    }
    public void createTableAmount(){
        String createTable = "CREATE TABLE instance (" +
                    "instance_id SERIAL PRIMARY KEY, " +
                    "instance_amount int NOT NULL, " +
                    "food_id int NOT NULL, " +
                    "FOREIGN KEY (food_id) REFERENCES food(food_id) ON UPDATE CASCADE ON DELETE CASCADE , " +
                    "storage_id int NOT NULL, " +
                    "FOREIGN KEY (storage_id) REFERENCES storages(StorageId) ON UPDATE CASCADE  ON DELETE CASCADE )";
        createTable(createTable, "amount");

    }

    private void createTable(String createTableSQL, String tableName){
        System.out.println("Starting to create new Table");
        Statement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            stmt = connection.createStatement();
            System.out.println("getting datasource");

            String dropTable = "DROP TABLE IF EXISTS " + tableName;
            stmt.executeUpdate(dropTable);

            System.out.println("creating " + tableName + " Table");

            stmt.executeUpdate(createTableSQL);
            System.out.println(tableName + " Table created");

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
                    "ticked boolean NOT NULL," +
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
