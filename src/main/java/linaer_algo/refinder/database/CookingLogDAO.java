package linaer_algo.refinder.database;

import linaer_algo.refinder.model.CookingLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CookingLogDAO {
    
    //Adds a new entry in the cooking logs
    public static void addCookingLog(CookingLog log){
        String sql = "INSERT INTO cooking_log (recipe_id, date_cooked, servings) VALUES (?, ?, ?)";
        
        // Wrapped Connection in the try block
         try (Connection conn = DatabaseConnection.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, log.getRecipeId());
            pstmt.setString(2, log.getDateCooked());
            pstmt.setInt(3, log.getServings());
            pstmt.executeUpdate();
            System.out.println("Cooking log added: " + log.getRecipeName());
        } catch (SQLException e) {
            System.out.println("Error adding cooking log: " + e.getMessage());
        }
    }
    
    //Gets all cooking logs
    public static List<CookingLog> getAllCookingLogs(){
        List<CookingLog> logs = new ArrayList<>();
        String sql = "SELECT cl.*, r.name as recipe_name FROM cooking_log cl " +
                    "INNER JOIN recipes r ON cl.recipe_id = r.id " +
                    "ORDER BY cl.id DESC";
        
         try (Connection conn = DatabaseConnection.getConnection();
              Statement stmt = conn.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CookingLog log = new CookingLog(
                    rs.getInt("id"),
                    rs.getInt("recipe_id"),
                    rs.getString("recipe_name"),
                    rs.getString("date_cooked"),
                    rs.getInt("servings")
                );
                logs.add(log);
            }
        } catch (SQLException e) {
            System.out.println("Error getting cooking logs: " + e.getMessage());
        }

        return logs;
    }
    
    //Delete a log from the entry
    public static void deleteCookingLog(int id){
        String sql = "DELETE FROM cooking_log WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Cooking log deleted!");
        } catch (SQLException e) {
            System.out.println("Error deleting cooking log: " + e.getMessage());
        }
    }
    
    //Get cooking logs using id
    public static List<CookingLog> getLogsByRecipe(int recipeId) {
        List<CookingLog> logs = new ArrayList<>();
        String sql = "SELECT cl.*, r.name as recipe_name FROM cooking_log cl " +
                     "INNER JOIN recipes r ON cl.recipe_id = r.id " +
                     "WHERE cl.recipe_id = ? ORDER BY cl.id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, recipeId);
            
            // Wrapped ResultSet in its own try block to prevent memory leak
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    CookingLog log = new CookingLog(
                        rs.getInt("id"),
                        rs.getInt("recipe_id"),
                        rs.getString("recipe_name"),
                        rs.getString("date_cooked"),
                        rs.getInt("servings")
                    );
                    logs.add(log);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error getting logs by recipe: " + e.getMessage());
        }

        return logs;
    }
}