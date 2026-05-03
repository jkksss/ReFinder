package linaer_algo.refinder.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:refinder.db";
    
    // Returns a FRESH connection every time. 
    // This pairs perfectly with the try-with-resources in your DAOs to prevent "closed connection" crashes.
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        
        // SQLite quirk: You MUST explicitly turn on foreign key constraints
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON;");
        }
        
        return conn;
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
        
        // Added ON DELETE CASCADE to prevent orphaned data
        String recipeIngredients = """
            CREATE TABLE IF NOT EXISTS recipe_ingredients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                recipe_id INTEGER NOT NULL,
                ingredient_name TEXT NOT NULL,
                quantity_needed REAL NOT NULL,
                unit TEXT NOT NULL,
                FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
            );
        """;

        // Added ON DELETE CASCADE
        String cookingLog = """
            CREATE TABLE IF NOT EXISTS cooking_log (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                recipe_id INTEGER NOT NULL,
                date_cooked TEXT NOT NULL,
                servings INTEGER NOT NULL,
                FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
            );
        """;

        // Added ON DELETE CASCADE
        String favorites = """
            CREATE TABLE IF NOT EXISTS favorites (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                recipe_id INTEGER NOT NULL,
                FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
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

        // Get a fresh connection to run the initial table creations
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
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
}