/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package linaer_algo.refinder;

import linaer_algo.refinder.algorithm.RabinKarp;
import linaer_algo.refinder.database.DatabaseConnection;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Joko1
 */
public class ReFinder {

     public static void main(String[] args) {
        DatabaseConnection.initializeDatabase();
        
        //Lets test the algorithm real quick
        
         System.out.println("Testing Rabin Karp");
         System.out.println(RabinKarp.search("garlic", "garlic cloves"));  // true
        System.out.println(RabinKarp.search("chicken", "beef"));          // false
        System.out.println(RabinKarp.search("onion", "red onion"));       // true

        // =====================
        // Test Match Percentage
        // =====================
        System.out.println("\n--- Testing Match Percentage ---");
        List<String> recipeIngredients = Arrays.asList(
            "chicken", "garlic", "vinegar", "soy sauce", "onion"
        );
        List<String> inventoryIngredients = Arrays.asList(
            "garlic", "chicken", "onion"
        );
        double match = RabinKarp.calculateMatchPercentage(
            recipeIngredients, inventoryIngredients
        );
        System.out.println("Match: " + match + "%"); // Should print 60.0%

        // =====================
        // Test Missing Ingredients
        // =====================
        System.out.println("\n--- Testing Missing Ingredients ---");
        List<String> missing = RabinKarp.getMissingIngredients(
            recipeIngredients, inventoryIngredients
        );
        System.out.println("Missing: " + missing); // Should print [vinegar, soy sauce]
    }
}
     

