package linaer_algo.refinder.algorithm;
import java.util.ArrayList;
import java.util.List;

public class RabinKarp {
    
    // Base and MOD constants used for hashing
    // BASE is a prime number used as multiplier
    // MOD is a large prime to prevent hash overflow
    private static final int BASE = 31;
    private static final int MOD = 1_000_000_007;
    
    // Hash function, converts a string into a unique hash number
    private static long computeHash(String text) {
        long hash = 0;
        long power = 1;
        for (int i = 0; i < text.length(); i++) {
            hash = (hash + (text.charAt(i) + 1) * power) % MOD;
            power = (power * BASE) % MOD;
        }
        return hash;
    }

    // Computes hash for a specific window inside the text
    private static long computeWindowHash(String text, int start, int length) {
        long hash = 0;
        long power = 1;
        for (int i = start; i < start + length; i++) {
            hash = (hash + (text.charAt(i) + 1) * power) % MOD;
            power = (power * BASE) % MOD;
        }
        return hash;
    }
    
    // Search method, checks if the pattern exists inside the text
    // Returns true if found, false if not
    public static boolean search(String pattern, String text) {
        
        // Convert both to lowercase for case-insensitive search
        pattern = pattern.toLowerCase().trim();
        text = text.toLowerCase().trim();
        
        int patternLength = pattern.length();
        int textLength = text.length();
        
        // Pattern must not be longer than text
        // because it would be impossible to find it
        if (patternLength > textLength) {
            return false;
        }

        // Compute hash of the pattern
        long patternHash = computeHash(pattern);
        
        // Slide the window across the text
        for (int i = 0; i <= textLength - patternLength; i++) {

            // Compute hash of current window
            long windowHash = computeWindowHash(text, i, patternLength);
            
            // If hashes match, verify character by character
            if (patternHash == windowHash) {
                if (text.substring(i, i + patternLength).equals(pattern)) {
                    return true; // Match found!
                }
            }
        }
        
        // No match found after checking all windows
        return false;
    }
    
    // Calculates how many recipe ingredients match the users inventory
    // Returns a percentage from 0.0 to 100.0
    public static double calculateMatchPercentage(
            List<String> recipeIngredients,
            List<String> inventoryIngredients) {
        
        // Return 0 if recipe has no ingredients
        if (recipeIngredients == null || recipeIngredients.isEmpty()) {
            return 0.0;
        }
        
        // Counter for matched ingredients
        int matchCount = 0;
        
        // Check each recipe ingredient against the inventory using Rabin-Karp
        for (String recipeIngredient : recipeIngredients) {
            for (String inventoryIngredient : inventoryIngredients) {
                if (search(recipeIngredient, inventoryIngredient) ||
                    search(inventoryIngredient, recipeIngredient)) {
                    matchCount++;
                    break; // Stop once found, no need to keep searching
                }
            }
        }
        
        // Calculate and return the match percentage
        return ((double) matchCount / recipeIngredients.size()) * 100.0;
    }
    
    // Gets the list of ingredients the user is missing for a recipe
    // Used for the shopping list feature
    public static List<String> getMissingIngredients(
            List<String> recipeIngredients,
            List<String> inventoryIngredients) {
          
        // List to store missing ingredients
        List<String> missing = new ArrayList<>();
        
        // Check each recipe ingredient against inventory
        for (String recipeIngredient : recipeIngredients) {
            boolean found = false;
            for (String inventoryIngredient : inventoryIngredients) {
                if (search(recipeIngredient, inventoryIngredient) ||
                    search(inventoryIngredient, recipeIngredient)) {
                    found = true;
                    break; // Found it, stop searching
                }
            }
            // If not found in inventory, add to missing list
            if (!found) {
                missing.add(recipeIngredient);
            }  
        }
        
        // Return the complete list of missing ingredients
        return missing;
    }
}