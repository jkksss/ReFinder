/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package linaer_algo.refinder;

import linaer_algo.refinder.algorithm.RabinKarp;
import linaer_algo.refinder.database.DatabaseConnection;
import linaer_algo.refinder.database.DataSeeder;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Joko1
 */
public class ReFinder {

     public static void main(String[] args) {
        
        // 1. System Bootup
        System.out.println("--- Booting Up ReFinder ---");
        DatabaseConnection.initializeDatabase();
        DataSeeder.seedData(); // Automatically seeds all 60 recipes if the DB is empty
        
        // 2. Test Rabin-Karp Upgrades
        System.out.println("\n--- Testing Rabin-Karp Edge Cases ---");
        
        // The Space Normalization Test (User types extra spaces and weird caps)
        System.out.println("Space Fix ('soy  sauce' in 'Soy Sauce'): " + 
            RabinKarp.search("soy  sauce", "Soy Sauce")); // Expected: true
            
        // The Substring Trap Tests (Padding Trick)
        System.out.println("Substring Trap 1 ('salt' in 'unsalted butter'): " + 
            RabinKarp.search("salt", "unsalted butter")); // Expected: false (Fixed!)
        System.out.println("Substring Trap 2 ('salt' in 'sea salt'): " + 
            RabinKarp.search("salt", "sea salt")); // Expected: true

        // 3. Simulate User Scenario
        System.out.println("\n--- Testing Adobo Recipe Match ---");
        
        // The exact ingredients we seeded for Chicken Adobo
        List<String> adoboIngredients = Arrays.asList(
            "chicken thighs", "soy sauce", "white vinegar", "garlic", 
            "black peppercorns", "bay leaves", "water", "cooking oil"
        );
        
        // The user's current fridge/pantry inventory
        // Notice: User typed "chicken" instead of "chicken thighs", and added "onions" which aren't needed.
        List<String> userInventory = Arrays.asList(
            "chicken", "soy sauce", "garlic", "water", "cooking oil", "onions"
        );

        // Calculate Match Percentage
        // (5 matched ingredients / 8 total recipe ingredients) = 62.5%
        double match = RabinKarp.calculateMatchPercentage(adoboIngredients, userInventory);
        System.out.println("Adobo Match: " + match + "%"); 

        // Generate Shopping List
        List<String> missing = RabinKarp.getMissingIngredients(adoboIngredients, userInventory);
        System.out.println("Missing to cook Adobo: " + missing); 
        // Expected: [white vinegar, black peppercorns, bay leaves]
    }
}
     

