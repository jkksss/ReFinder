/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package linaer_algo.refinder.database;

import linaer_algo.refinder.model.Ingredient;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joko1
 */
public class IngredientDAO {
        
        //Inserts ingredients into users inventory
        public static void addIngredient(Ingredient ingredient){
            String sql = "INSERT INTO ingredients (name, quantity, unit) VALUES (?, ?, ?)";
            
            try(PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)){
                pstmt.setString(1, ingredient.getName());
                pstmt.setDouble(2, ingredient.getQuantity());
                pstmt.setString(3, ingredient.getUnit());
                pstmt.executeUpdate();
                System.out.println("Ingredient added: "+ ingredient.getName());
            } catch (SQLException e){
                System.out.println("Error adding ingredient: " + e.getMessage());
            }
        }
         
        //Shows ingredients when user opens their inventory
        public static List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM ingredients";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ingredient ingredient = new Ingredient(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("quantity"),
                    rs.getString("unit")
                );
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            System.out.println("Error getting ingredients: " + e.getMessage());
        }

        return ingredients;
    }
        
        //Updates ingredients quantity in the inventory 
        public static  void updateQuantity(int id, double newQuantity){
            String sql = "UPDATE ingredients SET quantity = ? WHERE id = ?";
            
            try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setDouble(1, newQuantity);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Ingredient quantity updated!");
        } catch (SQLException e) {
            System.out.println("Error updating ingredient: " + e.getMessage());
            }  
        }
        
        //Deletes ingredients when users uses them or remove them
        public static void deleteIngredient(int id){
            String sql = "DELETE FROM ingredients WHERE id = ?";
            
            try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Ingredient deleted!");
        } catch (SQLException e) {
            System.out.println("Error deleting ingredient: " + e.getMessage());
            }
        }
        
        //Removes quantity of ingredients after user cooks a recipe
        public static void deductIngredient(String name, double amountToDeduct){
            String sql = "UPDATE ingredients SET quantity = quantity - ? WHERE LOWER(name) = LOWER(?)";
            
            try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setDouble(1, amountToDeduct);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
            System.out.println("Deducted " + amountToDeduct + " from " + name);
        } catch (SQLException e) {
            System.out.println("Error deducting ingredient: " + e.getMessage());
            }
        }
        
        //Uses Rabin-Karp to  get the names of the ingredients
        public static List<String> getIngredientNames() {
        List<String> names = new ArrayList<>();
        String sql = "SELECT name FROM ingredients";

        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Error getting ingredient names: " + e.getMessage());
        }

        return names;
    }
        
        //Checks if the ingredients already existed in the inventory
        public static boolean ingredientExists(String name) {
        String sql = "SELECT COUNT(*) FROM ingredients WHERE LOWER(name) = LOWER(?)";

        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking ingredient: " + e.getMessage());
        }

        return false;
    }
}
