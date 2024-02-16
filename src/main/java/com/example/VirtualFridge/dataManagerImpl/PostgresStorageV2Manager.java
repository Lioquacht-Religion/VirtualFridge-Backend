package com.example.VirtualFridge.dataManagerImpl;

import com.example.VirtualFridge.model.foodwarning.storagev2.Attribute;
import com.example.VirtualFridge.model.foodwarning.storagev2.Food;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PostgresStorageV2Manager {

    private BasicDataSource basicDataSource = DBCredentialsManager.getBasicDataSource();
    private static PostgresStorageV2Manager postgresStorageV2Manager = null;

    private  PostgresStorageV2Manager() {
        basicDataSource = DBCredentialsManager.getBasicDataSource();
    }

    public static PostgresStorageV2Manager getPostgresStorageV2Manager(){
        if(postgresStorageV2Manager == null){
            postgresStorageV2Manager = new PostgresStorageV2Manager();
        }
        return postgresStorageV2Manager;

    }

//TODO: still needs to be reworked with checking for if attributes already exist
    public String addFood(Food food) {

        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            connection = basicDataSource.getConnection();

            stmt = connection.prepareStatement("INSERT into food (food_name) VALUES ( ?);");
            stmt.setString(1, food.getName());
            stmt.executeUpdate();

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        try {
            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return food.getName();
    }

    public String addAttributesAndValuesToFood(Food food) {

        for(Attribute a : food.getAttributes()) {
                this.addNewAttribute(a);
                Attribute l_a = this.getAttributeByName(a.getName());
                l_a.setValue(a.getValue());

                Attribute existingAttrValue = getAttributeValueOfFood(food, l_a);
                if(existingAttrValue != null) {
                    existingAttrValue.setValue(a.getValue());
                    this.updateAttributeValueOfFood(food, existingAttrValue);
                }
                else {
                    this.addValueOfAttributeToFood(food, l_a);
                }
        }
        return food.getName();
    }


    //TODO: add checking for if user is owner

    public List<Food> getInstancesOfFoodInStorage(int OwnerID, int storageID) {
        List<Food>  foods = new ArrayList<>();
        PreparedStatement stmt = null;Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM instance WHERE storage = ?;"
            );
            stmt.setInt(1, storageID);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Food food = this.getFood(rs.getInt("food_id"));
                food.setAmount(rs.getInt("instance_amount"));
                foods.add(food);
            }
        } catch (SQLException e) {e.printStackTrace();}

        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        return foods;
    }

    public Food getFood(int foodID) {

        Food food = null;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM food WHERE food_id = ?;"
            );
            stmt.setInt(1, foodID);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                food = new Food(
                        foodID,
                        rs.getString("food_name"),
                        0
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


        return food;
    }

    public List<Food> getAllFoods() {

        List<Food> foods = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM food;"
            );
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                Food food = new Food(
                        rs.getInt("food_id"),
                        rs.getString("food_name"),
                        0
                );
                foods.add(food);

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


        return foods;
    }


    public Food getFoodWithAttributes(int foodID) {

        Food food = null;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM food WHERE food_id = ?;"
            );
            stmt.setInt(1, foodID);

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                food = new Food(
                        foodID,
                        rs.getString("food_name"),
                        0
                );
                food.setAttributes(this.getAttributesOfFood(foodID));
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


        return food;
    }

    public List<Attribute> getAttributesOfFood(int foodID) {

        List<Attribute> attributes = new ArrayList<>();
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM attributevalue WHERE food_id = ?;"
            );
            stmt.setInt(1, foodID);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                Attribute attribute = this.getAttribute(rs.getInt("attribute_id"));
                attribute.setValue(rs.getString("attributevalue_value"));
                attribute.setValueID(rs.getInt("attributevalue_id"));
                attribute.setFoodID(foodID);
                attributes.add(attribute);

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

        return attributes;
    }


    public String addValueOfAttributeToFood(Food food, Attribute attribute) {

        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            connection = basicDataSource.getConnection();

            stmt = connection.prepareStatement(
                    "INSERT into attributevalue (attributevalue_value, food_id, attribute_id) VALUES ( ?, ?, ? );"
            );
            System.out.println(attribute.getName() +" : "+ attribute.getValue());
            stmt.setString(1, attribute.getValue());
            stmt.setInt(2, food.getId());
            stmt.setInt(3, attribute.getId());
            stmt.executeUpdate();

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        try {
            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return "attribute " + attribute.getName() + " with value: " + attribute.getValue() + " was added";
    }

    public Attribute getAttributeValueOfFood(Food food, Attribute attribute){
        Attribute ret_a = null;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM attributevalue WHERE food_id = ? AND attribute_id = ?;"
            );
            stmt.setInt(1, food.getId());
            stmt.setInt(2, attribute.getId());

            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                ret_a = this.getAttribute(rs.getInt("attribute_id"));
                attribute.setValue(rs.getString("attributevalue_value"));
                attribute.setValueID(rs.getInt("attributevalue_id"));
                attribute.setFoodID(food.getId());
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

        return ret_a;

    }

       public void updateAttributeValueOfFood(Food food, Attribute attribute){
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "UPDATE attributevalue SET attributevalue_value = ? " +
                            "WHERE food_id = ? AND attribute_id = ?;"
            );

            stmt.setString(1, attribute.getValue());
            stmt.setInt(2, food.getId());
            stmt.setInt(3, attribute.getId());

            stmt.executeUpdate();

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

    public String addNewAttribute(Attribute attribute) {

        if(this.getAttributeByName(attribute.getName()) != null){
            return "attribute with that name already exists";
        }

        PreparedStatement stmt = null;
        Connection connection = null;
        try {
            connection = basicDataSource.getConnection();

            stmt = connection.prepareStatement("INSERT into attribute (attribute_name) VALUES ( ?);");
            stmt.setString(1, attribute.getName());
            stmt.executeUpdate();

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        try {
            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return "attribute " + attribute.getName() + "was added";
    }


    public Attribute getAttributeByName(String attrName) {

        Attribute attribute = null;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM attribute WHERE attribute_name = ?;"
            );
            stmt.setString(1, attrName);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                attribute = new Attribute(
                        rs.getInt("attribute_id"),
                        rs.getString("attribute_name"),
                        "",
                        "no_unit"
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


        return attribute;
    }

    public Attribute getAttribute(int attributeID) {

        Attribute attribute = null;
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM attribute WHERE attribute_id = ?;"
            );
            stmt.setInt(1, attributeID);
            ResultSet rs = stmt.executeQuery();

            if(rs.next()) {
                attribute = new Attribute(
                        rs.getInt("attribute_id"),
                        rs.getString("attribute_name"),
                        "",
                        "no_unit"
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


        return attribute;
    }


    /*
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
*/

}
