package com.example.VirtualFridge.dataManagerImpl;

import com.example.VirtualFridge.model.Grocery;
import com.example.VirtualFridge.model.Recipe;
import org.apache.commons.dbcp.BasicDataSource;

import java.sql.SQLException;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class PostgresRecipeManager {

    private BasicDataSource basicDataSource = null;
    private static PostgresRecipeManager postgresRecipeManager = null;

    private PostgresRecipeManager(){
        this.basicDataSource = DBCredentialsManager.getBasicDataSource();
    }

    public static PostgresRecipeManager getPostgresRecipeManager() {
        if(postgresRecipeManager == null){
            postgresRecipeManager = new PostgresRecipeManager();
        }
        return postgresRecipeManager;
    }

    public String addRecipe(int initUser, Recipe recipe) {
        PreparedStatement stmt = null; Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement("INSERT into recipes (name, description, Owner) VALUES (?, ?, ?);");
            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getDescription());
            stmt.setInt(3, initUser);
            stmt.executeUpdate();

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();} catch (SQLException e) {e.printStackTrace();}

        return recipe.getName();

    }

    public Collection<Recipe> getAllRecipes(int userID) {

        List<Recipe> recipes = new LinkedList<>();
        PreparedStatement stmt = null;
        Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement("SELECT * FROM recipes WHERE owner = ?");
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
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

    public Recipe getRecipeByID(int userID, int recipeID) {

        Recipe recipe = new Recipe("404", "error couldnt find recipe to update");
        PreparedStatement stmt = null;Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM recipes WHERE recipeID = ? AND owner = ?;"
            );
            stmt.setInt(1, recipeID);
            stmt.setInt(2, userID);

            ResultSet rs = stmt.executeQuery();
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
        PreparedStatement stmt = null; Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "INSERT into ingredients (name, amount, unit, partOfRecipe) VALUES (?, ?, ?, ?);"
            );
            stmt.setString(1, ingredient.getName());
            stmt.setInt(2, ingredient.getAmount());
            stmt.setString(3, ingredient.getUnit());
            stmt.setInt(4, RecipeID);
            stmt.executeUpdate();

            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();} catch (SQLException e) {e.printStackTrace();}

        return ingredient.getName();

    }

    public String putRecipe(int creatorID, Recipe recipe) {
        PreparedStatement stmt = null; Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "UPDATE recipes SET name = ?, description = ? WHERE owner = ? AND recipeid = ?;"
            );
            stmt.setString(1, recipe.getName());
            stmt.setString(2, recipe.getDescription());
            stmt.setInt(3, creatorID);
            stmt.setInt(4, recipe.getRecipeID());

            stmt.executeUpdate();
            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return "changed recipestuff: " + recipe.getName();

    }

    public String putIngredient(int creatorID, Grocery ingredient) {
        PreparedStatement stmt = null;Connection connection = null;
        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "UPDATE ingredients SET name = ?, amount = ?, unit = ? WHERE IngredientId = ?"
                    + " AND ? IN (SELECT recipeid FROM recipes WHERE owner = ?);"
            );
            stmt.setString(1, ingredient.getName());
            stmt.setInt(2, ingredient.getAmount());
            stmt.setString(3, ingredient.getUnit());
            stmt.setInt(4, ingredient.getID());
            stmt.setInt(6, creatorID);
            stmt.setInt(5, ingredient.getStoredInID());

            stmt.executeUpdate();
            stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}
        try {stmt.close();connection.close();
        } catch (SQLException e) {e.printStackTrace();}

        return "changed ingredientstuff: " + ingredient.getName();

    }

    public Collection<Grocery> getAllIngredients(int creatorID, int recipeID) {

        List<Grocery> ingredients = new LinkedList<>();
        PreparedStatement stmt = null;Connection connection = null;

        try {
            connection = basicDataSource.getConnection();
            stmt = connection.prepareStatement(
                    "SELECT * FROM ingredients WHERE partOfRecipe = ? " +
                            "AND ? IN (SELECT recipeid FROM recipes WHERE owner = ?);"
            );
            stmt.setInt(1, recipeID);
            stmt.setInt(3, creatorID);
            stmt.setInt(2, recipeID);

            ResultSet rs = stmt.executeQuery();
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

        PreparedStatement stmt = null; Connection connection = null;
        try{
            connection = basicDataSource.getConnection();

            System.out.println("delete Recipe and its ingredients");

            String deleteStorageGroc = "DELETE FROM ingredients WHERE partofrecipe = ? " +
                    "AND ? IN (SELECT recipeid FROM recipes WHERE owner = ?);";

            String deleteStorage = "DELETE FROM recipes WHERE recipeid = ? AND owner = ?;";

            stmt = connection.prepareStatement(deleteStorageGroc);
            stmt.setInt(1, recipeID);
            stmt.setInt(3, userID);
            stmt.setInt(2, recipeID);
            stmt.executeUpdate(deleteStorage);


            stmt = connection.prepareStatement(deleteStorage);
            stmt.setInt(1, recipeID);
            stmt.setInt(2, userID);
            stmt.executeUpdate(deleteStorage);


        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete recipe and its ingredients";
    }

    public String deleteIngredient(int userID, int recipeID, int ingredientID){

        PreparedStatement stmt = null; Connection connection = null;
        try{
            connection = basicDataSource.getConnection();

            System.out.println("delete ingredient from recipe");

            stmt = connection.prepareStatement(
                    "DELETE FROM ingredients WHERE partofrecipe = ? AND ingredientid = ?" +
                            " AND ? IN (SELECT recipeid FROM recipes WHERE owner = ?);"
            );
            stmt.setInt(1, recipeID);
            stmt.setInt(2, ingredientID);
            stmt.setInt(4, userID);
            stmt.setInt(3, recipeID);
            stmt.executeUpdate();

        }
        catch(SQLException e){e.printStackTrace();}

        try{stmt.close();connection.close();
        }catch(SQLException e){e.printStackTrace();}
        return "delete ingredient from recipe";
    }

}
