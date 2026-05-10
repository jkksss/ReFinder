package linaer_algo.refinder.database;

import linaer_algo.refinder.model.Recipe;
import linaer_algo.refinder.model.Ingredient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO {
    
    //Gets all recipes from the database
    public static List<Recipe> getAllRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Recipe recipe = new Recipe(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("cuisine"),
                    rs.getInt("cook_time"),
                    rs.getString("instructions"),
                    rs.getString("photo_path")
                );
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            System.out.println("Error getting recipes: " + e.getMessage());
        }

        return recipes;
    }
    
    //Gets recipe based on different cuisines available
    public static List<Recipe> getRecipesByCuisine(String cuisine){
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes WHERE LOWER(cuisine) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, cuisine);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Recipe recipe = new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cuisine"),
                        rs.getInt("cook_time"),
                        rs.getString("instructions"),
                        rs.getString("photo_path")
                    );
                    recipes.add(recipe);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting recipes by cuisine: " + e.getMessage());
        }

        return recipes;
    }
    
    //Gets the ingredients of a specific recipe
    public static List<Ingredient> getRecipeIngredients(int recipeId){
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredients WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recipeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient(
                        rs.getInt("id"),
                        rs.getString("ingredient_name"),
                        rs.getDouble("quantity_needed"),
                        rs.getString("unit")
                    );
                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting recipe ingredients: " + e.getMessage());
        }

        return ingredients;
    }
    
    //This is for the algorithm getting the name for a specific recipe
    public static List<String> getRecipeIngredientsNames(int recipeId){
        List<String> names = new ArrayList<>();
        String sql = "SELECT ingredient_name FROM recipe_ingredients WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recipeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    names.add(rs.getString("ingredient_name"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting recipe ingredient names: " + e.getMessage());
        }

        return names;
    }
    
    // Upgraded: Adds to favorites, but blocks duplicates!
    public static void addToFavorites(int recipeId){
        String checkSql = "SELECT COUNT(*) FROM favorites WHERE recipe_id = ?";
        String insertSql = "INSERT INTO favorites (recipe_id) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            
            // 1. Check if it's already favorited
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, recipeId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        System.out.println("Recipe is already in favorites!");
                        return; // Stop here, don't insert a duplicate!
                    }
                }
            }
            
            // 2. If we made it here, it's not a duplicate. Save it!
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, recipeId);
                insertStmt.executeUpdate();
                System.out.println("Recipe added to favorites!");
            }
            
        } catch (SQLException e) {
            System.out.println("Error adding to favorites: " + e.getMessage());
        }
    }
    //Removes recipe from favorites
    public static void removeFromFavorites(int recipeId){
        String sql = "DELETE FROM favorites WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.executeUpdate();
            System.out.println("Recipe removed from favorites!");
        } catch (SQLException e) {
            System.out.println("Error removing from favorites: " + e.getMessage());
        }
    }
    
    //Loads/gets what recipes are favorited
    public static List<Recipe> getFavoriteRecipes(){
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT r.* FROM recipes r " +
                     "INNER JOIN favorites f ON r.id = f.recipe_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Recipe recipe = new Recipe(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("cuisine"),
                    rs.getInt("cook_time"),
                    rs.getString("instructions"),
                    rs.getString("photo_path")
                );
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            System.out.println("Error getting favorite recipes: " + e.getMessage());
        }

        return recipes;
    }
    
    //Checks if the certain recipe is already favorited
    public static boolean isFavorite(int recipeId){
        String sql = "SELECT COUNT(*) FROM favorites WHERE recipe_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recipeId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking favorite: " + e.getMessage());
        }

        return false;
    }
    
    // Grabs a single specific recipe by its exact name
    public static Recipe getRecipeByName(String name) {
        String sql = "SELECT * FROM recipes WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("cuisine"),
                        rs.getInt("cook_time"),
                        rs.getString("instructions"),
                        rs.getString("photo_path")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting recipe by name: " + e.getMessage());
        }
        return null;
    }
}