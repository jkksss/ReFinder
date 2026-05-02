package linaer_algo.refinder.algorithm;

import java.util.ArrayList;
import java.util.List;

public class RabinKarp {
    
    // Base and MOD constants used for hashing
    private static final int BASE = 31;
    private static final int MOD = 1_000_000_007;

    // =====================
    // String Normalizer
    // =====================
    // Strips punctuation and fixes double spaces to prevent matching bugs
    private static String normalize(String input) {
        if (input == null) return "";
        return input.toLowerCase()
                    .replaceAll("[^a-z0-9\\s]", "") // Remove all punctuation
                    .replaceAll("\\s+", " ")        // Turn multiple spaces into a single space
                    .trim();                        // Remove leading/trailing spaces
    }

    // =====================
    // True Rolling Hash Search
    // =====================
    public static boolean search(String pattern, String text) {
        
        // 1. Normalize both strings to create a level playing field
        pattern = normalize(pattern);
        text = normalize(text);
        
        // 2. The Space Padding Trick (Fixes the "salt" vs "saltine" bug)
        // Adding spaces ensures we only match whole, independent words
        pattern = " " + pattern + " ";
        text = " " + text + " ";
        
        int M = pattern.length();
        int N = text.length();
        
        // Pattern must not be longer than text
        if (M > N) {
            return false;
        }

        long patternHash = 0;
        long windowHash = 0;
        long h = 1;

        // Calculate h = pow(BASE, M-1) % MOD
        for (int i = 0; i < M - 1; i++) {
            h = (h * BASE) % MOD;
        }

        // Calculate initial hash values for pattern and the first window of text
        for (int i = 0; i < M; i++) {
            patternHash = (BASE * patternHash + pattern.charAt(i)) % MOD;
            windowHash = (BASE * windowHash + text.charAt(i)) % MOD;
        }

        // Slide the pattern over the text one character at a time
        for (int i = 0; i <= N - M; i++) {
            
            // If hashes match, verify character by character to avoid collisions
            if (patternHash == windowHash) {
                if (text.substring(i, i + M).equals(pattern)) {
                    return true; // Exact word match found!
                }
            }

            // Calculate hash value for next window in O(1) time
            if (i < N - M) {
                // Remove the leading character, shift the base, add the trailing character
                windowHash = (BASE * (windowHash - text.charAt(i) * h) + text.charAt(i + M)) % MOD;

                // If windowHash becomes negative, convert it to positive (Java quirk)
                if (windowHash < 0) {
                    windowHash = (windowHash + MOD);
                }
            }
        }
        
        return false;
    }
    
    // =====================
    // Match Percentage Calculator
    // =====================
    public static double calculateMatchPercentage(
            List<String> recipeIngredients,
            List<String> inventoryIngredients) {
        
        if (recipeIngredients == null || recipeIngredients.isEmpty()) {
            return 0.0;
        }
        
        int matchCount = 0;
        
        // Check each recipe ingredient against the inventory
        for (String recipeIngredient : recipeIngredients) {
            for (String inventoryIngredient : inventoryIngredients) {
                // Bidirectional check handles situations like "chicken breast" vs "chicken"
                if (search(recipeIngredient, inventoryIngredient) ||
                    search(inventoryIngredient, recipeIngredient)) {
                    matchCount++;
                    break; // Stop once found
                }
            }
        }
        
        return ((double) matchCount / recipeIngredients.size()) * 100.0;
    }
    
    // =====================
    // Missing Ingredients Generator
    // =====================
    public static List<String> getMissingIngredients(
            List<String> recipeIngredients,
            List<String> inventoryIngredients) {
          
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
            // If not found in inventory, add to the missing list
            if (!found) {
                missing.add(recipeIngredient);
            }  
        }
        
        return missing;
    }
}