package com.example.VirtualFridge.dataManagerImpl;

import com.example.VirtualFridge.model.Grocery;
import com.example.VirtualFridge.model.ShoppingList;
import com.example.VirtualFridge.model.ShoppingListItem;
import com.example.VirtualFridge.model.User;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

public class PostgresShoppinglistManager {
    BasicDataSource basicDataSource;

    static PostgresShoppinglistManager postgresShoppinglistManager = null;

    private PostgresShoppinglistManager() {
        basicDataSource = DBCredentialsManager.getBasicDataSource();
    }

    public static PostgresShoppinglistManager getPostgresShoppinglistManager() {
        if(postgresShoppinglistManager == null) {
            postgresShoppinglistManager = new PostgresShoppinglistManager();
        }
        return postgresShoppinglistManager;
    }


    public String addShoppingList(ShoppingList shoppingList, int userID) {

        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement("INSERT into shoppinglists (name, owner) VALUES ( ?, ?);");
            stmt.setString(1, shoppingList.getName());
            stmt.setInt(2, userID);
            stmt.executeUpdate();

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return shoppingList.getName();
    }

    public Collection<ShoppingList> getShoppingLists(int OwnerID) {
        Collection<ShoppingList>  shoppingLists = new LinkedList<>();
        PreparedStatement stmt = null;Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM shoppinglists WHERE owner = ?;"
            );
            stmt.setInt(1, OwnerID);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                ShoppingList l_stor = new ShoppingList(
                        rs.getInt("shoppinglistid"),
                        OwnerID,
                        rs.getString("name")
                );
                shoppingLists.add(l_stor);
            }
        } catch (SQLException e) {e.printStackTrace();}

        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        return shoppingLists;
    }



    public String deleteShoppingList(int userID, int shoppingListID){

        PreparedStatement stmt = null;
        Connection connection = null;
        try{
            connection = basicDataSource.getConnection();

            System.out.println("delete Shoppinglist and its items");

            String deleteStorageGroc =
                    "DELETE FROM shoppingitems WHERE itemoflist = ? " +
                            "AND ? IN (SELECT shoppinglistid FROM shoppinglists WHERE owner = ?);";
            String deleteStorage =
                    "DELETE FROM shoppinglists WHERE shoppinglistid = ? AND owner = ?;";

            stmt = connection.prepareStatement(deleteStorageGroc);
            stmt.setInt(1, shoppingListID);
            stmt.setInt(3, userID);
            stmt.setInt(2, shoppingListID);
            stmt.executeUpdate();

            stmt = connection.prepareStatement(deleteStorage);
            stmt.setInt(1, shoppingListID);
            stmt.setInt(2, userID);
            stmt.executeUpdate();
        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete Shoppinglist and its items";
    }

    public String deleteItem(int ownerID, int shoppinglistID, int itemID){

        PreparedStatement stmt = null; Connection connection = null;
        try{
            connection = basicDataSource.getConnection();
            System.out.println("delete item from Shoppinglist");

            stmt = connection.prepareStatement(
                    "DELETE FROM shoppingitems WHERE shoppingitemid = ? " +
                            "AND ? IN (SELECT shoppinglistid FROM shoppinglists WHERE owner = ?);"
            );
            stmt.setInt(1, itemID);
            stmt.setInt(3, ownerID);
            stmt.setInt(2, shoppinglistID);
            stmt.executeUpdate();

        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete Item from Shoppinglist";
    }

    public String addItem(int shoppingListID, ShoppingListItem item) {
        PreparedStatement stmt = null; Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "INSERT into shoppingitems (name, amount, unit, ticked, itemoflist) VALUES (?, ?, ?, ?, ?);"
            );
            Grocery grocery = item.getGrocery();
            stmt.setString(1, grocery.getName());
            stmt.setInt(2, grocery.getAmount());
            stmt.setString(3, grocery.getUnit());
            stmt.setBoolean(4, item.isTicked());
            stmt.setInt(5, shoppingListID);

            stmt.executeUpdate();

            stmt.close();
            connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return item.getGrocery().getName();
    }


    public Collection<ShoppingListItem> getListItems(int userID, int shoppingListID) {

        Collection<ShoppingListItem> items = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM shoppingitems WHERE itemoflist = ? " +
                            "AND ? IN (SELECT shoppinglistid FROM shoppinglists WHERE owner = ?);"
            );
            stmt.setInt(1, shoppingListID);
            stmt.setInt(3, userID);
            stmt.setInt(2, shoppingListID);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                 Grocery l_groc = new Grocery(
                        rs.getString("name"),
                        rs.getString("unit"),
                        rs.getInt("amount")
                );
                 l_groc.setIDs(rs.getInt("shoppingitemid"), shoppingListID);
                 items.add(
                         new ShoppingListItem(
                                 rs.getBoolean("ticked"),
                                 l_groc
                         )
                 );
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


        return items;
    }

    public String setItemTicked(int userID, int shoppinglistID, int itemID, boolean ticked){
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "UPDATE shoppingitems SET ticked = ? WHERE shoppingitemid = ? AND itemoflist = ?" +
                            "AND ? IN (SELECT shoppinglistid FROM shoppinglists WHERE owner = ?);"
            );
            stmt.setBoolean(1, ticked );
            stmt.setInt(2, itemID );
            stmt.setInt(3, shoppinglistID);
            stmt.setInt(4, shoppinglistID);
            stmt.setInt(5, userID);

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

        return "set item tick to " + ticked;


    }

}
