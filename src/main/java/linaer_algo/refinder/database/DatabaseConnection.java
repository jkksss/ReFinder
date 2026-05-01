/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package linaer_algo.refinder.database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
/**
 *
 * @author Joko1
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:refinder.db";
    private static Connection connection = null;
    
    
    // Get connection of the database. basically opening the database 
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }
    
    //Create tables for the database 
    public static void initializeDatabase(){
    
        //Table for the ingredients within the inventory
        String ingredients = """
        CREATE TABLE IF NOT EXISTS ingredients (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            quantity REAL NOT NULL,
            unit TEXT NOT NULL
        );
        """;
        
        String recipes = """
        CREATE TABLE IF NOT EXISTS recipes (
            id INTEGER PRIMARY KEY AUTOINCREMENT,                              
            name TEXT NOT NULL,             
            cuisine TEXT NOT NULL,             
            cook_time INTEGER NOT NULL,             
            instructions TEXT NOT NULL,                              
            photo_path TEXT
        );                 
        """;
        
        String recipeIngredients = """
            CREATE TABLE IF NOT EXISTS recipe_ingredients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                recipe_id INTEGER NOT NULL,
                ingredient_name TEXT NOT NULL,
                quantity_needed REAL NOT NULL,
                unit TEXT NOT NULL,
                FOREIGN KEY (recipe_id) REFERENCES recipes(id)
            );
        """;

        String cookingLog = """
            CREATE TABLE IF NOT EXISTS cooking_log (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                recipe_id INTEGER NOT NULL,
                date_cooked TEXT NOT NULL,
                servings INTEGER NOT NULL,
                FOREIGN KEY (recipe_id) REFERENCES recipes(id)
            );
        """;

        String favorites = """
            CREATE TABLE IF NOT EXISTS favorites (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                recipe_id INTEGER NOT NULL,
                FOREIGN KEY (recipe_id) REFERENCES recipes(id)
            );
        """;

        String shoppingList = """
            CREATE TABLE IF NOT EXISTS shopping_list (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                ingredient_name TEXT NOT NULL,
                quantity_needed REAL NOT NULL,
                unit TEXT NOT NULL,
                is_purchased INTEGER DEFAULT 0
            );
        """;

        try (Statement stmt = getConnection().createStatement()) {
            stmt.execute(ingredients);
            stmt.execute(recipes);
            stmt.execute(recipeIngredients);
            stmt.execute(cookingLog);
            stmt.execute(favorites);
            stmt.execute(shoppingList);
            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    //Close the connection of the database 
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

}
    
    
    

