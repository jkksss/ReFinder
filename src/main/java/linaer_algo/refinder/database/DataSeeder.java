package linaer_algo.refinder.database;

import java.sql.*;

public class DataSeeder {

    // Main seeder method
    public static void seedData() {
        if (isAlreadySeeded()) {
            System.out.println("Database already seeded!");
            return;
        }
        seedFilipino();
        seedChinese();
        seedKorean();
        System.out.println("All 60 recipes seeded successfully!");
    }


    // Check if already seeded
    private static boolean isAlreadySeeded() {
        String sql = "SELECT COUNT(*) FROM recipes";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.out.println("Error checking seed: " + e.getMessage());
        }
        return false;
    }


    // Insert a recipe and its ingredients (OPTIMIZED)
    private static void insertRecipe(String name, String cuisine,
            int cookTime, String instructions, String photoPath,
            String[][] ingredients) {
        
        String recipeSql = "INSERT INTO recipes (name, cuisine, cook_time, instructions, photo_path) VALUES (?, ?, ?, ?, ?)";
        String ingredientSql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_name, quantity_needed, unit) VALUES (?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction

            try (PreparedStatement pstmt = conn.prepareStatement(recipeSql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, name);
                pstmt.setString(2, cuisine);
                pstmt.setInt(3, cookTime);
                pstmt.setString(4, instructions);
                pstmt.setString(5, photoPath);
                pstmt.executeUpdate();

                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        int recipeId = keys.getInt(1);

                        try (PreparedStatement ipstmt = conn.prepareStatement(ingredientSql)) {
                            for (String[] ingredient : ingredients) {
                                ipstmt.setInt(1, recipeId);
                                ipstmt.setString(2, ingredient[0]);
                                ipstmt.setDouble(3, Double.parseDouble(ingredient[1]));
                                ipstmt.setString(4, ingredient[2]);
                                ipstmt.addBatch(); // Batch processing
                            }
                            ipstmt.executeBatch();
                        }
                    }
                }
            }
            conn.commit(); // Commit if successful
            System.out.println("Seeded: " + name);

        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error seeding " + name + ": " + e.getMessage());
            if (conn != null) {
                try {
                    System.out.println("Rolling back transaction for " + name);
                    conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Rollback failed: " + ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.out.println("Failed to reset auto-commit: " + ex.getMessage());
                }
            }
        }
    }

    // Filipino Recipes (20)
    private static void seedFilipino() {
        insertRecipe("Chicken Adobo", "Filipino", 40,
            "1. Combine chicken, soy sauce, vinegar, garlic, peppercorns, and bay leaves in a pot. Marinate for 30 minutes.\n2. Bring the pot to a boil over medium heat. Lower the heat and simmer for 20 minutes.\n3. Remove the chicken pieces from the sauce. Heat oil in a pan and brown the chicken on all sides.\n4. Pour the remaining sauce into the pan with the chicken and simmer for another 10 minutes until the sauce thickens.",
            "photos/chicken_adobo.jpg",
            new String[][]{{"chicken thighs", "1", "kg"}, {"soy sauce", "120", "ml"}, {"white vinegar", "120", "ml"}, {"garlic", "6", "cloves"}, {"black peppercorns", "1", "tsp"}, {"bay leaves", "3", "pieces"}, {"water", "240", "ml"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Sinigang na Baboy", "Filipino", 60,
            "1. Boil the water in a large pot. Add the pork ribs, tomatoes, and onion. Simmer for 45 minutes until the pork is tender.\n2. Stir in the tamarind paste and fish sauce.\n3. Add the daikon radish and simmer for 5 minutes.\n4. Add the string beans and cook for another 3 minutes.\n5. Turn off the heat, stir in the water spinach, and let it wilt before serving.",
            "photos/sinigang.jpg",
            new String[][]{{"pork ribs", "1", "kg"}, {"tamarind paste", "3", "tbsp"}, {"tomatoes", "2", "pieces"}, {"onion", "1", "piece"}, {"fish sauce", "2", "tbsp"}, {"water", "1.5", "liters"}, {"daikon radish", "1", "cup"}, {"string beans", "1", "cup"}, {"water spinach", "2", "cups"}});

        insertRecipe("Pancit Canton", "Filipino", 30,
            "1. Heat oil in a wok. Sauté the garlic until fragrant, then add the chicken breast and cook until lightly browned.\n2. Add the shrimp and cook until they turn pink. Remove the meat and shrimp and set aside.\n3. In the same wok, add the carrots and cabbage. Stir-fry for 3 minutes.\n4. Pour in the chicken broth, soy sauce, and oyster sauce. Bring to a boil.\n5. Add the noodles, gently tossing until they absorb the liquid. Toss the meat and shrimp back in before serving.",
            "photos/pancit_canton.jpg",
            new String[][]{{"pancit canton noodles", "250", "grams"}, {"chicken breast", "200", "grams"}, {"shrimp", "150", "grams"}, {"soy sauce", "3", "tbsp"}, {"oyster sauce", "2", "tbsp"}, {"chicken broth", "2", "cups"}, {"carrots", "1", "cup"}, {"cabbage", "2", "cups"}, {"garlic", "4", "cloves"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Beef Caldereta", "Filipino", 100,
            "1. Heat oil in a pot. Sauté garlic and onion until softened.\n2. Add the beef and brown on all sides.\n3. Pour in the beef broth and tomato sauce. Bring to a boil, then cover and simmer for 1 to 1.5 hours until beef is tender.\n4. Stir in the liver spread until well combined.\n5. Add the potatoes and carrots. Simmer for 10 minutes.\n6. Add the bell peppers and cook for a final 5 minutes.",
            "photos/caldereta.jpg",
            new String[][]{{"beef stew meat", "1", "kg"}, {"tomato sauce", "250", "ml"}, {"liver spread", "85", "grams"}, {"potatoes", "2", "pieces"}, {"carrots", "2", "pieces"}, {"bell peppers", "1", "piece"}, {"garlic", "4", "cloves"}, {"onion", "1", "piece"}, {"beef broth", "2", "cups"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Chicken Tinola", "Filipino", 50,
            "1. Heat oil in a pot and sauté ginger, garlic, and onion until fragrant.\n2. Add the chicken pieces and cook until the outside turns light brown.\n3. Season with fish sauce and stir for 2 minutes.\n4. Pour in the rice washing or water. Bring to a boil, cover, and simmer for 30 minutes.\n5. Add the green papaya and simmer for another 10 minutes until tender.\n6. Turn off the heat and stir in the chili leaves.",
            "photos/tinola.jpg",
            new String[][]{{"chicken", "1", "kg"}, {"green papaya", "1", "piece"}, {"chili leaves", "1", "cup"}, {"ginger", "2", "tbsp"}, {"garlic", "4", "cloves"}, {"onion", "1", "piece"}, {"fish sauce", "2", "tbsp"}, {"water", "4", "cups"}, {"cooking oil", "1", "tbsp"}});

        insertRecipe("Kare-Kare", "Filipino", 180,
            "1. Boil oxtail in water for 2-3 hours until very tender.\n2. Dissolve annatto powder in a little water and add to the pot along with the peanut butter. Stir until the sauce thickens.\n3. Add the eggplant and string beans, cooking for 5 minutes.\n4. Stir in the bok choy and cook for another 2 minutes.\n5. Serve hot with shrimp paste on the side.",
            "photos/kare_kare.jpg",
            new String[][]{{"oxtail", "1", "kg"}, {"peanut butter", "1", "cup"}, {"eggplant", "1", "piece"}, {"string beans", "1", "bundle"}, {"bok choy", "1", "bundle"}, {"annatto powder", "2", "tbsp"}, {"shrimp paste", "0.5", "cup"}, {"water", "6", "cups"}});

        insertRecipe("Lechon Kawali", "Filipino", 60,
            "1. Place pork belly, garlic, bay leaves, peppercorns, salt, and water in a pot. Bring to a boil and simmer for 45 minutes until tender.\n2. Remove the pork from the pot and let it air dry completely.\n3. Heat oil in a deep pan. Deep fry the dried pork belly over medium heat until golden and crispy.\n4. Remove from oil, drain on paper towels, and chop into bite-sized pieces.",
            "photos/lechon_kawali.jpg",
            new String[][]{{"pork belly", "1", "kg"}, {"garlic", "4", "cloves"}, {"bay leaves", "2", "pieces"}, {"black peppercorns", "1", "tbsp"}, {"salt", "2", "tbsp"}, {"water", "4", "cups"}, {"cooking oil", "3", "cups"}});

        insertRecipe("Bistek Tagalog", "Filipino", 45,
            "1. Marinate beef in soy sauce, calamansi juice, garlic, and pepper for 30 minutes.\n2. Heat oil in a pan and lightly fry the onion rings until translucent. Remove and set aside.\n3. In the same pan, fry the beef slices for 1-2 minutes per side.\n4. Pour the remaining marinade into the pan and let it simmer for 5 minutes.\n5. Top the beef with the cooked onion rings before serving.",
            "photos/bistek.jpg",
            new String[][]{{"beef sirloin", "500", "grams"}, {"soy sauce", "0.25", "cup"}, {"calamansi juice", "3", "tbsp"}, {"garlic", "3", "cloves"}, {"onion", "2", "pieces"}, {"black pepper", "0.5", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Pork Barbecue", "Filipino", 25,
            "1. In a bowl, mix soy sauce, banana ketchup, calamansi juice, brown sugar, and garlic.\n2. Add the pork slices to the marinade, cover, and refrigerate overnight.\n3. Thread the marinated pork slices onto the bamboo skewers.\n4. Grill the pork over hot coals for 3-5 minutes per side, basting with leftover marinade.\n5. Serve immediately with vinegar dip.",
            "photos/pork_bbq.jpg",
            new String[][]{{"pork shoulder", "1", "kg"}, {"soy sauce", "0.5", "cup"}, {"banana ketchup", "0.5", "cup"}, {"calamansi juice", "3", "tbsp"}, {"brown sugar", "0.25", "cup"}, {"garlic", "5", "cloves"}, {"bamboo skewers", "20", "pieces"}});

        insertRecipe("Tortang Talong", "Filipino", 20,
            "1. Roast or grill the eggplants until the skin is charred and the flesh is soft. Let cool, then peel off the skin, keeping the stem intact.\n2. Flatten the eggplant flesh with a fork.\n3. Beat the eggs in a bowl and season with salt and pepper.\n4. Heat oil in a pan. Dip each flattened eggplant into the beaten egg.\n5. Fry the eggplant for 2-3 minutes per side until golden brown.",
            "photos/tortang_talong.jpg",
            new String[][]{{"eggplants", "4", "pieces"}, {"eggs", "3", "pieces"}, {"salt", "1", "tsp"}, {"black pepper", "0.5", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Chicken Inasal", "Filipino", 40,
            "1. Mix lemongrass, calamansi juice, vinegar, garlic, ginger, and brown sugar in a bowl.\n2. Marinate the chicken in the mixture for at least 3 hours or overnight.\n3. Preheat a grill over medium heat.\n4. Grill the chicken for 15-20 minutes per side, basting frequently with annatto oil until fully cooked and slightly charred.",
            "photos/inasal.jpg",
            new String[][]{{"chicken legs", "1", "kg"}, {"lemongrass", "2", "stalks"}, {"calamansi juice", "0.25", "cup"}, {"vinegar", "0.25", "cup"}, {"garlic", "6", "cloves"}, {"ginger", "2", "tbsp"}, {"brown sugar", "2", "tbsp"}, {"annatto oil", "0.25", "cup"}});

        insertRecipe("Bicol Express", "Filipino", 50,
            "1. Heat oil in a pan and sauté garlic and onion until fragrant.\n2. Add the pork belly and cook until browned.\n3. Stir in the shrimp paste and cook for 2 minutes.\n4. Pour in the coconut milk and simmer for 30 minutes until the pork is tender.\n5. Add the chilies and coconut cream. Simmer for another 10 minutes until the sauce thickens.",
            "photos/bicol_express.jpg",
            new String[][]{{"pork belly", "500", "grams"}, {"coconut milk", "2", "cups"}, {"coconut cream", "1", "cup"}, {"shrimp paste", "3", "tbsp"}, {"garlic", "4", "cloves"}, {"onion", "1", "piece"}, {"bird eye chilies", "10", "pieces"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Lumpiang Shanghai", "Filipino", 30,
            "1. Mix ground pork, carrots, onion, garlic, and soy sauce in a bowl.\n2. Place a tablespoon of the filling in a thin line on a spring roll wrapper.\n3. Roll the wrapper tightly around the filling, sealing the edge with a dab of beaten egg.\n4. Heat oil in a pan over medium heat. Deep fry the rolls until golden brown and crispy.\n5. Drain on paper towels and serve with sweet chili sauce.",
            "photos/lumpia.jpg",
            new String[][]{{"ground pork", "500", "grams"}, {"carrots", "1", "cup"}, {"onion", "1", "piece"}, {"garlic", "4", "cloves"}, {"soy sauce", "1", "tbsp"}, {"spring roll wrappers", "30", "pieces"}, {"egg", "1", "piece"}, {"cooking oil", "2", "cups"}});

        insertRecipe("Pork Sisig", "Filipino", 45,
            "1. Boil pork belly until tender, then grill or pan-fry until crispy. Chop into fine pieces.\n2. Pan-fry the chicken liver until cooked, then chop finely.\n3. Heat oil in a pan and sauté half of the onions until translucent. Add the chopped pork and liver.\n4. Season with soy sauce, calamansi juice, and chilies. Stir well for 3 minutes.\n5. Turn off the heat and stir in the mayonnaise and remaining raw onions.",
            "photos/sisig.jpg",
            new String[][]{{"pork belly", "500", "grams"}, {"chicken liver", "150", "grams"}, {"onion", "1", "piece"}, {"calamansi juice", "3", "tbsp"}, {"soy sauce", "2", "tbsp"}, {"mayonnaise", "2", "tbsp"}, {"green chilies", "2", "pieces"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Pinakbet", "Filipino", 30,
            "1. Sauté garlic and tomatoes in a pot until soft.\n2. Add the pork belly and cook until browned.\n3. Stir in the shrimp paste and cook for 2 minutes. Pour in the water and bring to a simmer.\n4. Add the squash and simmer for 5 minutes.\n5. Add the string beans, eggplant, and bitter melon. Cover and cook for 10 minutes.",
            "photos/pinakbet.jpg",
            new String[][]{{"pork belly", "200", "grams"}, {"bitter melon", "1", "piece"}, {"eggplant", "1", "piece"}, {"squash", "2", "cups"}, {"string beans", "1", "bundle"}, {"shrimp paste", "2", "tbsp"}, {"tomatoes", "3", "pieces"}, {"garlic", "3", "cloves"}, {"water", "1", "cup"}});

        insertRecipe("Leche Flan", "Filipino", 45,
            "1. Melt the sugar in a saucepan over low heat until it turns into a golden caramel. Pour into a mold and let it harden.\n2. In a bowl, gently whisk the egg yolks.\n3. Slowly pour in the condensed milk, evaporated milk, and vanilla extract. Stir gently.\n4. Strain the mixture into the mold over the caramel. Cover with aluminum foil.\n5. Steam for 30-35 minutes until set. Chill before serving.",
            "photos/leche_flan.jpg",
            new String[][]{{"egg yolks", "10", "pieces"}, {"condensed milk", "1", "can"}, {"evaporated milk", "1", "can"}, {"vanilla extract", "1", "tsp"}, {"white sugar", "0.5", "cup"}});

        insertRecipe("Turon", "Filipino", 20,
            "1. Roll each banana slice in brown sugar until coated.\n2. Place a coated banana and a few strips of jackfruit on a spring roll wrapper.\n3. Fold the sides inward and roll tightly. Wet the edge with water to seal.\n4. Heat oil in a pan over medium heat. Fry the rolls until golden brown.\n5. Drain on a wire rack and let cool slightly before eating.",
            "photos/turon.jpg",
            new String[][]{{"saba bananas", "6", "pieces"}, {"jackfruit", "0.5", "cup"}, {"brown sugar", "1", "cup"}, {"spring roll wrappers", "12", "pieces"}, {"cooking oil", "2", "cups"}});

        insertRecipe("Buko Pandan", "Filipino", 15,
            "1. In a large mixing bowl, combine the all-purpose cream and condensed milk.\n2. Fold in the young coconut strings, pandan jelly cubes, and cooked tapioca pearls.\n3. Mix well until everything is evenly coated in the sweet cream.\n4. Chill in the refrigerator for at least 2 hours before serving.",
            "photos/buko_pandan.jpg",
            new String[][]{{"young coconut strings", "2", "cups"}, {"pandan jelly cubes", "2", "cups"}, {"all purpose cream", "1", "cup"}, {"condensed milk", "0.5", "cup"}, {"tapioca pearls", "0.5", "cup"}});

        insertRecipe("Halo-Halo", "Filipino", 10,
            "1. Place the sweet beans, jackfruit, plantains, and nata de coco at the bottom of a tall glass.\n2. Fill the glass with shaved ice, pressing down gently.\n3. Pour the evaporated milk over the ice.\n4. Top with ube halaya, a slice of leche flan, and a scoop of ube ice cream.\n5. Mix everything together before eating.",
            "photos/halo_halo.jpg",
            new String[][]{{"shaved ice", "2", "cups"}, {"evaporated milk", "0.5", "cup"}, {"sweetened red beans", "2", "tbsp"}, {"sweetened jackfruit", "2", "tbsp"}, {"nata de coco", "2", "tbsp"}, {"ube halaya", "1", "tbsp"}, {"ube ice cream", "1", "scoop"}});

        insertRecipe("Pancit Palabok", "Filipino", 40,
            "1. Soak the rice noodles in water for 15 minutes, then boil until tender. Drain and place on a serving platter.\n2. In a saucepan, dissolve annatto powder and cornstarch in the shrimp broth.\n3. Bring the mixture to a simmer over medium heat, stirring continuously until the sauce thickens. Season with fish sauce.\n4. Sauté the ground pork in a separate pan until browned, then mix it into the thickened sauce.\n5. Pour the hot sauce generously over the cooked noodles.\n6. Garnish with cooked shrimp, sliced hard-boiled eggs, crushed chicharon, and chopped green onions.",
            "photos/pancit_palabok.jpg",
            new String[][]{{"rice noodles", "250", "grams"}, {"shrimp broth", "3", "cups"}, {"ground pork", "150", "grams"}, {"annatto powder", "1", "tbsp"}, {"cornstarch", "3", "tbsp"}, {"shrimp", "100", "grams"}, {"hard boiled eggs", "2", "pieces"}, {"chicharon", "0.5", "cup"}, {"green onions", "2", "stalks"}, {"fish sauce", "2", "tbsp"}});

        insertRecipe("Beef Tapa", "Filipino", 20,
            "1. In a bowl, combine the soy sauce, calamansi juice, minced garlic, brown sugar, and black pepper.\n2. Add the thinly sliced beef to the mixture, ensuring every piece is coated. Marinate overnight.\n3. Heat the cooking oil in a skillet over medium-high heat.\n4. Remove the beef from the marinade and fry in batches.\n5. Cook each side for 2-3 minutes until the edges are slightly crispy and caramelized.",
            "photos/beef_tapa.jpg",
            new String[][]{{"beef sirloin", "500", "grams"}, {"soy sauce", "3", "tbsp"}, {"calamansi juice", "2", "tbsp"}, {"garlic", "5", "cloves"}, {"brown sugar", "1", "tbsp"}, {"black pepper", "1", "tsp"}, {"cooking oil", "2", "tbsp"}});
    }

    // Chinese Recipes (20)
    private static void seedChinese() {
        insertRecipe("Kung Pao Chicken", "Chinese", 25,
            "1. Mix the chicken with 1 tbsp of soy sauce and the cornstarch. Marinate for 15 minutes.\n2. Mix the remaining soy sauce, black vinegar, and sugar for the sauce.\n3. Heat oil in a wok. Add the chicken and stir-fry until cooked. Remove and set aside.\n4. Add dried chilies, garlic, and ginger. Stir-fry for 30 seconds.\n5. Return the chicken to the wok, pour in the sauce, and toss well.\n6. Stir in the peanuts right before serving.",
            "photos/kung_pao.jpg",
            new String[][]{{"chicken breast", "400", "grams"}, {"soy sauce", "2", "tbsp"}, {"cornstarch", "1", "tbsp"}, {"dried red chilies", "8", "pieces"}, {"peanuts", "0.5", "cup"}, {"garlic", "3", "cloves"}, {"ginger", "1", "tbsp"}, {"black vinegar", "1", "tbsp"}, {"sugar", "1", "tbsp"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Mapo Tofu", "Chinese", 20,
            "1. Blanch the tofu cubes in boiling water for 2 minutes. Drain and set aside.\n2. Heat oil in a wok. Add the ground pork and stir-fry until crispy.\n3. Add the garlic, ginger, and doubanjiang. Stir-fry for 1 minute.\n4. Pour in the chicken broth and soy sauce. Bring to a simmer.\n5. Gently slide the tofu into the sauce. Simmer for 3-5 minutes.\n6. Stir in the cornstarch slurry to thicken. Top with Sichuan peppercorn powder and green onions.",
            "photos/mapo_tofu.jpg",
            new String[][]{{"soft tofu", "400", "grams"}, {"ground pork", "150", "grams"}, {"doubanjiang", "2", "tbsp"}, {"garlic", "3", "cloves"}, {"ginger", "1", "tbsp"}, {"chicken broth", "1", "cup"}, {"soy sauce", "1", "tbsp"}, {"cornstarch", "2", "tbsp"}, {"sichuan peppercorn powder", "0.5", "tsp"}, {"green onions", "2", "stalks"}});

        insertRecipe("Beef and Broccoli", "Chinese", 25,
            "1. Toss the beef slices with 1 tbsp of soy sauce and the cornstarch. Set aside.\n2. Blanch the broccoli florets in boiling water for 2 minutes, then plunge into cold water.\n3. Whisk together the remaining soy sauce, oyster sauce, brown sugar, sesame oil, and water.\n4. Heat oil in a wok. Stir-fry the beef until browned. Remove and set aside.\n5. Add garlic to the wok and sauté for 30 seconds.\n6. Pour in the sauce, return the beef, and add the broccoli. Toss until coated.",
            "photos/beef_broccoli.jpg",
            new String[][]{{"flank steak", "350", "grams"}, {"broccoli florets", "3", "cups"}, {"soy sauce", "3", "tbsp"}, {"oyster sauce", "2", "tbsp"}, {"brown sugar", "1", "tbsp"}, {"cornstarch", "1", "tbsp"}, {"garlic", "3", "cloves"}, {"sesame oil", "1", "tsp"}, {"water", "0.5", "cup"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Sweet and Sour Pork", "Chinese", 35,
            "1. Coat the pork cubes thoroughly in cornstarch.\n2. Deep fry the pork for 5-6 minutes until golden and crispy. Drain.\n3. In a separate pan, combine ketchup, vinegar, sugar, water, and soy sauce. Bring to a simmer.\n4. Add the bell peppers and pineapple chunks to the sauce and cook for 2 minutes.\n5. Toss the fried pork into the sauce, mixing quickly to coat.",
            "photos/sweet_sour_pork.jpg",
            new String[][]{{"pork shoulder", "400", "grams"}, {"cornstarch", "0.5", "cup"}, {"bell peppers", "1", "cup"}, {"pineapple chunks", "1", "cup"}, {"ketchup", "3", "tbsp"}, {"white vinegar", "3", "tbsp"}, {"sugar", "3", "tbsp"}, {"water", "0.5", "cup"}, {"soy sauce", "1", "tbsp"}, {"cooking oil", "2", "cups"}});

        insertRecipe("Egg Fried Rice", "Chinese", 15,
            "1. Heat 1 tbsp of oil in a wok. Pour in the beaten eggs and scramble quickly. Remove and set aside.\n2. Add the remaining oil. Toss in the day-old rice, breaking up any clumps. Stir-fry for 3 minutes.\n3. Sprinkle the soy sauce, salt, and white pepper over the rice. Mix well.\n4. Add the scrambled eggs back in along with the green onions. Toss for another minute.\n5. Drizzle with sesame oil, toss one last time, and serve.",
            "photos/fried_rice.jpg",
            new String[][]{{"cooked rice", "3", "cups"}, {"eggs", "3", "pieces"}, {"green onions", "3", "stalks"}, {"soy sauce", "1.5", "tbsp"}, {"salt", "0.5", "tsp"}, {"white pepper", "0.25", "tsp"}, {"cooking oil", "2", "tbsp"}, {"sesame oil", "1", "tsp"}});

        insertRecipe("Char Siu", "Chinese", 50,
            "1. Combine hoisin sauce, soy sauce, honey, five-spice powder, garlic, and food coloring.\n2. Marinate the pork in the mixture overnight.\n3. Preheat oven to 200°C. Place the pork on a wire rack over a roasting pan with a little water.\n4. Roast for 30 minutes. Turn, baste with remaining marinade, and roast for another 20 minutes.\n5. Let it rest before slicing.",
            "photos/char_siu.jpg",
            new String[][]{{"pork shoulder", "1", "kg"}, {"hoisin sauce", "3", "tbsp"}, {"soy sauce", "2", "tbsp"}, {"honey", "3", "tbsp"}, {"chinese five spice powder", "1", "tsp"}, {"garlic", "3", "cloves"}});

        insertRecipe("Chow Mein", "Chinese", 15,
            "1. Boil the noodles until just tender, drain, and set aside.\n2. Mix soy sauce, oyster sauce, and sesame oil in a small bowl.\n3. Heat oil in a wok over high heat. Add the cabbage and carrots, stir-frying for 2 minutes.\n4. Add the noodles and bean sprouts to the wok.\n5. Pour the sauce over the noodles and toss continuously for 2-3 minutes.",
            "photos/chow_mein.jpg",
            new String[][]{{"chow mein noodles", "250", "grams"}, {"cabbage", "2", "cups"}, {"carrots", "1", "cup"}, {"bean sprouts", "1", "cup"}, {"soy sauce", "2", "tbsp"}, {"oyster sauce", "1", "tbsp"}, {"sesame oil", "1", "tsp"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Hot and Sour Soup", "Chinese", 20,
            "1. Bring the chicken broth to a boil.\n2. Add the tofu, bamboo shoots, and mushrooms. Simmer for 5 minutes.\n3. Stir in the soy sauce, black vinegar, and white pepper.\n4. Slowly stir in the cornstarch slurry to thicken the soup.\n5. Turn off the heat and slowly drizzle in the beaten egg while stirring to form ribbons.",
            "photos/hot_sour_soup.jpg",
            new String[][]{{"chicken broth", "4", "cups"}, {"firm tofu", "200", "grams"}, {"bamboo shoots", "0.5", "cup"}, {"wood ear mushrooms", "0.5", "cup"}, {"soy sauce", "2", "tbsp"}, {"black vinegar", "3", "tbsp"}, {"white pepper", "1", "tsp"}, {"cornstarch", "3", "tbsp"}, {"egg", "1", "piece"}});

        insertRecipe("Pork Dumplings", "Chinese", 30,
            "1. Squeeze excess water from the minced cabbage. Mix with ground pork, soy sauce, sesame oil, and ginger.\n2. Place a small spoonful of filling in the center of a wrapper. Wet the edges, fold in half, and pleat to seal.\n3. Heat oil in a pan. Place the dumplings flat-side down and pan-fry for 2 minutes until golden.\n4. Pour water into the pan and immediately cover. Steam for 5-6 minutes until water evaporates.\n5. Remove lid and cook for 1 more minute to crisp the bottoms.",
            "photos/dumplings.jpg",
            new String[][]{{"ground pork", "300", "grams"}, {"napa cabbage", "1", "cup"}, {"soy sauce", "1", "tbsp"}, {"sesame oil", "1", "tbsp"}, {"ginger", "1", "tsp"}, {"dumpling wrappers", "30", "pieces"}, {"water", "0.5", "cup"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Scallion Pancakes", "Chinese", 45,
            "1. Mix flour and boiling water until a shaggy dough forms. Knead into a smooth ball and rest for 30 minutes.\n2. Divide the dough into 4 pieces. Roll each piece into a thin circle.\n3. Brush with sesame oil, sprinkle with salt and scallions. Roll into a log, then coil like a snail.\n4. Flatten the coil and roll it out again into a pancake.\n5. Heat oil in a pan and pan-fry each pancake for 2-3 minutes per side until crispy.",
            "photos/scallion_pancakes.jpg",
            new String[][]{{"all purpose flour", "2", "cups"}, {"boiling water", "0.75", "cup"}, {"scallions", "1", "cup"}, {"sesame oil", "2", "tbsp"}, {"salt", "1", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Wonton Soup", "Chinese", 20,
            "1. Mix ground pork, minced shrimp, soy sauce, and sesame oil in a bowl.\n2. Place a small spoonful of filling in the center of a wonton wrapper. Fold into a triangle and press the two bottom corners together.\n3. Bring the chicken broth to a boil.\n4. Drop the wontons into the boiling broth and cook for 5 minutes until they float.\n5. Add the bok choy and simmer for 1 more minute. Garnish with green onions.",
            "photos/wonton_soup.jpg",
            new String[][]{{"ground pork", "200", "grams"}, {"shrimp", "100", "grams"}, {"soy sauce", "1", "tbsp"}, {"sesame oil", "1", "tsp"}, {"wonton wrappers", "20", "pieces"}, {"chicken broth", "4", "cups"}, {"bok choy", "2", "cups"}, {"green onions", "1", "stalk"}});

        insertRecipe("Dan Dan Noodles", "Chinese", 15,
            "1. Stir-fry ground pork with doubanjiang and 1 tbsp soy sauce until crispy. Set aside.\n2. In the serving bowls, whisk together the sesame paste, chili oil, black vinegar, garlic, and remaining soy sauce.\n3. Boil the noodles according to package instructions. Drain, reserving a splash of noodle water.\n4. Add the noodles and a little noodle water to the bowls with the sauce. Toss well.\n5. Top with the crispy pork and chopped green onions.",
            "photos/dan_dan.jpg",
            new String[][]{{"wheat noodles", "200", "grams"}, {"ground pork", "150", "grams"}, {"doubanjiang", "1", "tbsp"}, {"soy sauce", "2", "tbsp"}, {"sesame paste", "2", "tbsp"}, {"chili oil", "2", "tbsp"}, {"black vinegar", "1", "tbsp"}, {"garlic", "2", "cloves"}, {"green onions", "2", "stalks"}});

        insertRecipe("Tomato Egg Stir-fry", "Chinese", 15,
            "1. Beat the eggs with a pinch of salt. Heat 1 tbsp of oil in a wok, scramble the eggs loosely, then remove.\n2. Heat the remaining oil. Sauté the white parts of the green onions for 30 seconds.\n3. Add the tomatoes and cook until they break down and release their juices.\n4. Stir in the sugar, salt, and ketchup.\n5. Add the scrambled eggs back in and mix gently. Garnish with the green parts of the onions.",
            "photos/tomato_egg.jpg",
            new String[][]{{"tomatoes", "3", "pieces"}, {"eggs", "4", "pieces"}, {"green onions", "2", "stalks"}, {"sugar", "1", "tbsp"}, {"salt", "0.5", "tsp"}, {"ketchup", "1", "tbsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Vegetable Spring Rolls", "Chinese", 30,
            "1. Sauté cabbage, carrots, and mushrooms until wilted. Season with soy sauce and sesame oil. Let cool.\n2. Place a wrapper in a diamond shape, add a spoonful of filling near the bottom corner.\n3. Roll up halfway, fold the sides in, and continue rolling tightly. Seal with cornstarch slurry.\n4. Heat oil to 180°C and deep fry the rolls until golden and crispy.\n5. Drain on paper towels and serve hot.",
            "photos/veg_spring_rolls.jpg",
            new String[][]{{"spring roll wrappers", "10", "pieces"}, {"cabbage", "2", "cups"}, {"carrots", "1", "cup"}, {"shiitake mushrooms", "0.5", "cup"}, {"soy sauce", "1", "tbsp"}, {"sesame oil", "1", "tsp"}, {"cornstarch", "1", "tsp"}, {"cooking oil", "2", "cups"}});

        insertRecipe("Hong Shao Rou", "Chinese", 70,
            "1. Blanch the pork belly cubes in boiling water for 3 minutes. Drain and dry.\n2. Heat oil in a wok over low heat. Add sugar and stir until it melts and turns amber.\n3. Add the pork belly and coat evenly in the caramelized sugar.\n4. Stir in the light soy sauce, dark soy sauce, star anise, ginger, and water. Bring to a boil.\n5. Reduce heat, cover, and simmer for 45-60 minutes until the pork is tender and the sauce is thick.",
            "photos/hong_shao_rou.jpg",
            new String[][]{{"pork belly", "500", "grams"}, {"light soy sauce", "2", "tbsp"}, {"dark soy sauce", "1", "tbsp"}, {"brown sugar", "2", "tbsp"}, {"star anise", "2", "pieces"}, {"ginger", "4", "pieces"}, {"water", "2", "cups"}, {"cooking oil", "1", "tbsp"}});

        insertRecipe("Steamed Fish", "Chinese", 15,
            "1. Place the fish fillet on a heat-proof plate. Top with half of the julienned ginger.\n2. Steam the fish over boiling water for 8-10 minutes until cooked through and flaky.\n3. Carefully pour off any accumulated liquid. Top with remaining ginger and scallions.\n4. Mix soy sauce, sugar, and sesame oil. Pour around the fish.\n5. Heat cooking oil until smoking hot. Pour the hot oil over the ginger and scallions.",
            "photos/steamed_fish.jpg",
            new String[][]{{"white fish fillet", "400", "grams"}, {"ginger", "3", "tbsp"}, {"scallions", "3", "stalks"}, {"soy sauce", "3", "tbsp"}, {"sugar", "1", "tsp"}, {"sesame oil", "1", "tsp"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Congee", "Chinese", 90,
            "1. Bring the broth to a boil in a large pot. Add the rice and ginger slices.\n2. Reduce the heat to a low simmer. Cook for 1 to 1.5 hours until it reaches a porridge consistency.\n3. Stir in the salt and shredded chicken.\n4. Serve hot, garnished with chopped green onions.",
            "photos/congee.jpg",
            new String[][]{{"jasmine rice", "1", "cup"}, {"chicken broth", "8", "cups"}, {"ginger", "3", "pieces"}, {"salt", "1", "tsp"}, {"shredded chicken", "1", "cup"}, {"green onions", "2", "stalks"}});

        insertRecipe("Egg Tarts", "Chinese", 40,
            "1. Dissolve the sugar in the hot water and let the syrup cool completely.\n2. Whisk the eggs. Stir in the evaporated milk, vanilla extract, and cooled syrup.\n3. Strain the egg mixture through a fine sieve to ensure a smooth custard.\n4. Press the puff pastry shells into a muffin tin. Pour the custard into the shells until 80% full.\n5. Bake at 200°C for 15-20 minutes until the pastry is golden and the custard is just set.",
            "photos/egg_tarts.jpg",
            new String[][]{{"puff pastry shells", "12", "pieces"}, {"hot water", "0.5", "cup"}, {"sugar", "0.3", "cup"}, {"eggs", "3", "pieces"}, {"evaporated milk", "0.5", "cup"}, {"vanilla extract", "0.5", "tsp"}});

        insertRecipe("Tangyuan", "Chinese", 25,
            "1. Gradually add warm water to the glutinous rice flour, kneading until smooth.\n2. Pinch off small pieces of dough, flatten, place sesame paste in the center, and roll into a ball.\n3. In a pot, bring water, ginger, and brown sugar to a boil to make the sweet soup.\n4. Gently drop the rice balls into the boiling soup.\n5. Cook for 5-6 minutes until the rice balls float to the surface. Serve hot.",
            "photos/tangyuan.jpg",
            new String[][]{{"glutinous rice flour", "1.5", "cups"}, {"warm water", "0.5", "cup"}, {"black sesame paste", "0.5", "cup"}, {"water", "3", "cups"}, {"brown sugar", "0.25", "cup"}, {"ginger", "2", "pieces"}});

        insertRecipe("Mango Sago", "Chinese", 20,
            "1. Boil the tapioca pearls in water for 15 minutes until translucent. Rinse and drain well.\n2. Blend half of the mango cubes with the coconut milk, evaporated milk, and condensed milk until smooth.\n3. In a large bowl, mix the mango purée with the cooked tapioca pearls.\n4. Stir in the remaining diced mango chunks.\n5. Chill in the refrigerator for at least 1 hour before serving.",
            "photos/mango_sago.jpg",
            new String[][]{{"ripe mangoes", "3", "pieces"}, {"tapioca pearls", "0.5", "cup"}, {"coconut milk", "1", "cup"}, {"evaporated milk", "0.5", "cup"}, {"condensed milk", "3", "tbsp"}});
    }

    // Korean Recipes (20)
    private static void seedKorean() {
        insertRecipe("Beef Bulgogi", "Korean", 15,
            "1. In a bowl, mix soy sauce, brown sugar, sesame oil, garlic, and grated Asian pear.\n2. Add the beef, onion, and green onions to the marinade. Refrigerate for at least 1 hour.\n3. Heat oil in a large skillet over medium-high heat.\n4. Add the marinated beef in a single layer. Cook for 2-3 minutes per side until caramelized.\n5. Garnish with sesame seeds before serving.",
            "photos/bulgogi.jpg",
            new String[][]{{"ribeye steak", "500", "grams"}, {"soy sauce", "4", "tbsp"}, {"brown sugar", "2", "tbsp"}, {"sesame oil", "1", "tbsp"}, {"garlic", "4", "cloves"}, {"asian pear", "0.5", "piece"}, {"onion", "0.5", "piece"}, {"green onions", "2", "stalks"}, {"sesame seeds", "1", "tbsp"}, {"cooking oil", "1", "tbsp"}});

        insertRecipe("Bibimbap", "Korean", 30,
            "1. Blanch the spinach, drain well, and toss with a drop of sesame oil and a pinch of salt.\n2. Sauté the carrots and mushrooms separately in a lightly oiled pan.\n3. Cook the ground beef in the pan with the soy sauce until browned.\n4. Fry the eggs sunny-side up.\n5. Assemble the bowls: place rice at the bottom, arrange the vegetables and beef on top, place the fried egg in the center. Top with gochujang and sesame oil.",
            "photos/bibimbap.jpg",
            new String[][]{{"cooked white rice", "2", "cups"}, {"spinach", "1", "cup"}, {"carrots", "1", "cup"}, {"shiitake mushrooms", "1", "cup"}, {"ground beef", "150", "grams"}, {"eggs", "2", "pieces"}, {"gochujang", "2", "tbsp"}, {"soy sauce", "1", "tbsp"}, {"sesame oil", "2", "tbsp"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Kimchi Jjigae", "Korean", 35,
            "1. In a pot, heat the sesame oil over medium heat. Add the pork belly and cook until browned.\n2. Add the kimchi, onion, and garlic to the pot. Sauté for 5 minutes until the kimchi is soft.\n3. Stir in the gochugaru and gochujang.\n4. Pour in the water or broth and bring to a boil. Reduce heat and simmer for 15 minutes.\n5. Arrange the tofu slices on top and simmer for 5 more minutes.\n6. Garnish with green onions before serving.",
            "photos/kimchi_jjigae.jpg",
            new String[][]{{"fermented kimchi", "2", "cups"}, {"pork belly", "200", "grams"}, {"tofu", "200", "grams"}, {"onion", "0.5", "piece"}, {"garlic", "3", "cloves"}, {"gochugaru", "1", "tbsp"}, {"gochujang", "1", "tbsp"}, {"water", "2", "cups"}, {"sesame oil", "1", "tbsp"}, {"green onions", "1", "stalk"}});

        insertRecipe("Tteokbokki", "Korean", 25,
            "1. Soak the rice cakes in warm water for 10 minutes if they are hard.\n2. In a wide pan, mix the gochujang, gochugaru, sugar, soy sauce, and broth. Bring to a boil.\n3. Add the rice cakes and stir gently. Cook for 5 minutes.\n4. Add the fish cakes and chopped cabbage. Continue to simmer for 5-7 minutes until the sauce thickens.\n5. Add the hard-boiled eggs and green onions, simmering for 1 more minute.",
            "photos/tteokbokki.jpg",
            new String[][]{{"korean rice cakes", "400", "grams"}, {"fish cakes", "2", "sheets"}, {"cabbage", "1", "cup"}, {"gochujang", "3", "tbsp"}, {"gochugaru", "1", "tbsp"}, {"sugar", "1", "tbsp"}, {"soy sauce", "1", "tbsp"}, {"anchovy broth", "2.5", "cups"}, {"green onions", "2", "stalks"}, {"hard boiled eggs", "2", "pieces"}});

        insertRecipe("Japchae", "Korean", 40,
            "1. Boil the sweet potato noodles for 6-7 minutes. Rinse under cold water, drain, and cut with scissors.\n2. Blanch the spinach, squeeze out excess water, and toss with a drop of sesame oil.\n3. Stir-fry the beef, carrots, onion, and mushrooms separately until cooked.\n4. In a large mixing bowl, combine the noodles, cooked vegetables, and beef.\n5. Add the soy sauce, sugar, and sesame oil. Toss everything together.\n6. Sprinkle with sesame seeds before serving.",
            "photos/japchae.jpg",
            new String[][]{{"glass noodles", "200", "grams"}, {"beef", "100", "grams"}, {"spinach", "1", "cup"}, {"carrots", "0.5", "cup"}, {"onion", "0.5", "piece"}, {"shiitake mushrooms", "0.5", "cup"}, {"soy sauce", "4", "tbsp"}, {"sugar", "2", "tbsp"}, {"sesame oil", "2", "tbsp"}, {"sesame seeds", "1", "tbsp"}});

        insertRecipe("Kimbap", "Korean", 30,
            "1. Season the warm rice with sesame oil and a pinch of salt. Cut the egg omelet into strips.\n2. Lay a sheet of seaweed on a bamboo mat. Spread an even, thin layer of rice over the seaweed.\n3. Arrange a row of spinach, carrots, radish, beef, and egg across the lower third of the rice.\n4. Roll the seaweed tightly from the bottom up, using the mat to press it firmly.\n5. Brush the roll with a little sesame oil and slice into bite-sized pieces.",
            "photos/kimbap.jpg",
            new String[][]{{"sushi rice", "3", "cups"}, {"roasted seaweed", "4", "pieces"}, {"spinach", "1", "cup"}, {"carrots", "1", "cup"}, {"pickled radish", "4", "pieces"}, {"ground beef", "150", "grams"}, {"eggs", "2", "pieces"}, {"sesame oil", "2", "tbsp"}});

        insertRecipe("Haemul Pajeon", "Korean", 15,
            "1. In a bowl, whisk together the flour, water, and salt to make a smooth batter.\n2. Heat oil in a large non-stick pan over medium-high heat.\n3. Lay the scallions in the pan in an even layer. Pour the batter over the scallions.\n4. Distribute the mixed seafood evenly on top of the batter. Crack the egg over the top.\n5. Cook for 4-5 minutes until the bottom is crispy, then flip carefully and cook for another 4 minutes.",
            "photos/pajeon.jpg",
            new String[][]{{"all purpose flour", "1", "cup"}, {"water", "1", "cup"}, {"scallions", "2", "cups"}, {"mixed seafood", "200", "grams"}, {"egg", "1", "piece"}, {"salt", "0.5", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Sundubu Jjigae", "Korean", 20,
            "1. Heat sesame oil in a pot over medium heat. Sauté the gochugaru and garlic for 1 minute.\n2. Pour in the anchovy broth and bring to a boil.\n3. Add the seafood and cook for 3 minutes until cooked through.\n4. Gently slide the silken tofu into the pot. Break it into large chunks. Simmer for 3 minutes.\n5. Top with green onions and crack a raw egg into the boiling stew right before serving.",
            "photos/sundubu.jpg",
            new String[][]{{"silken tofu", "400", "grams"}, {"anchovy broth", "1.5", "cups"}, {"mixed seafood", "150", "grams"}, {"gochugaru", "2", "tbsp"}, {"garlic", "3", "cloves"}, {"sesame oil", "1", "tbsp"}, {"green onions", "2", "stalks"}, {"egg", "1", "piece"}});

        insertRecipe("Galbi", "Korean", 30,
            "1. Rinse the beef short ribs in cold water and pat dry.\n2. Mix soy sauce, water, brown sugar, grated pear, grated onion, garlic, and sesame oil.\n3. Pour the marinade over the ribs. Marinate in the fridge for at least 4 hours.\n4. Preheat a grill or heavy pan over medium-high heat.\n5. Grill the ribs for 3-4 minutes per side until caramelized and cooked through.",
            "photos/galbi.jpg",
            new String[][]{{"beef short ribs", "1", "kg"}, {"soy sauce", "0.5", "cup"}, {"water", "0.5", "cup"}, {"brown sugar", "0.25", "cup"}, {"asian pear", "0.5", "piece"}, {"onion", "0.5", "piece"}, {"garlic", "4", "cloves"}, {"sesame oil", "1", "tbsp"}});

        insertRecipe("Dakgalbi", "Korean", 35,
            "1. Mix the gochujang, soy sauce, sugar, and garlic. Marinate the chicken for 30 minutes.\n2. Heat oil in a large skillet. Add the sweet potatoes and cook for 3 minutes.\n3. Add the marinated chicken, cabbage, and rice cakes to the pan.\n4. Stir-fry everything together for 10-15 minutes until the chicken is fully cooked.\n5. Serve hot directly from the pan.",
            "photos/dakgalbi.jpg",
            new String[][]{{"chicken thighs", "500", "grams"}, {"cabbage", "2", "cups"}, {"sweet potato", "1", "piece"}, {"tteokbokki rice cakes", "1", "cup"}, {"gochujang", "3", "tbsp"}, {"soy sauce", "2", "tbsp"}, {"sugar", "1", "tbsp"}, {"garlic", "3", "cloves"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Kimchi Fried Rice", "Korean", 15,
            "1. Heat a pan over medium heat and cook the bacon or SPAM until crispy.\n2. Add the chopped kimchi and sauté for 3 minutes.\n3. Add the rice, gochujang, and kimchi juice. Stir-fry until well combined and heated through.\n4. Turn off the heat and drizzle with sesame oil.\n5. Serve topped with a fried egg and crumbled seaweed.",
            "photos/kimchi_fried_rice.jpg",
            new String[][]{{"cooked rice", "3", "cups"}, {"fermented kimchi", "1", "cup"}, {"kimchi juice", "3", "tbsp"}, {"bacon", "100", "grams"}, {"gochujang", "1", "tbsp"}, {"sesame oil", "1", "tbsp"}, {"fried egg", "1", "piece"}, {"roasted seaweed", "1", "tbsp"}});

        insertRecipe("Bossam", "Korean", 70,
            "1. In a large pot, bring water, onion, garlic, ginger, doenjang, and coffee powder to a boil.\n2. Submerge the pork belly in the boiling liquid.\n3. Reduce heat to medium-low, cover, and simmer for 1 hour until the pork is fully tender.\n4. Remove the pork, let it rest for 10 minutes, and slice thinly.\n5. Serve with lettuce leaves to wrap, alongside kimchi and dipping sauces.",
            "photos/bossam.jpg",
            new String[][]{{"pork belly", "1", "kg"}, {"onion", "1", "piece"}, {"garlic", "6", "cloves"}, {"ginger", "3", "pieces"}, {"doenjang", "2", "tbsp"}, {"instant coffee powder", "1", "tsp"}, {"water", "8", "cups"}, {"lettuce", "1", "head"}});

        insertRecipe("Doenjang Jjigae", "Korean", 20,
            "1. Bring the broth to a boil in a small pot.\n2. Dissolve the doenjang into the broth.\n3. Add the potatoes and boil for 5 minutes.\n4. Add the zucchini, tofu, garlic, and green chili. Simmer for another 5-7 minutes.\n5. Top with green onions and serve boiling hot with a side of rice.",
            "photos/doenjang_jjigae.jpg",
            new String[][]{{"anchovy broth", "3", "cups"}, {"doenjang", "3", "tbsp"}, {"potato", "1", "piece"}, {"zucchini", "0.5", "piece"}, {"tofu", "200", "grams"}, {"garlic", "2", "cloves"}, {"green chili", "1", "piece"}, {"green onion", "1", "stalk"}});

        insertRecipe("Samgyeopsal", "Korean", 15,
            "1. Mix sesame oil, salt, and pepper in small individual dipping bowls.\n2. Heat a grill pan or electric griddle over medium-high heat.\n3. Grill the pork belly slices and whole garlic cloves until the meat is browned and crispy.\n4. Cut the cooked pork into bite-sized pieces with scissors right on the grill.\n5. Place a piece of pork and garlic on a lettuce leaf, add a dab of ssamjang, wrap, and eat.",
            "photos/samgyeopsal.jpg",
            new String[][]{{"pork belly", "600", "grams"}, {"garlic cloves", "10", "pieces"}, {"lettuce leaves", "1", "head"}, {"sesame oil", "2", "tbsp"}, {"salt", "1", "tsp"}, {"black pepper", "0.5", "tsp"}, {"ssamjang", "3", "tbsp"}});

        insertRecipe("Gyeran-jjim", "Korean", 10,
            "1. Vigorously whisk the eggs in a bowl until completely smooth. Stir in the chicken broth and salt.\n2. Pour the mixture into a small heat-safe pot.\n3. Cook over medium-low heat. As the egg cooks around the edges, gently scrape it with a spoon.\n4. When it's about 80% set, cover with a lid and cook for 2 more minutes until puffed up.\n5. Remove from heat, drizzle with sesame oil, and top with green onions and sesame seeds.",
            "photos/gyeran_jjim.jpg",
            new String[][]{{"eggs", "4", "pieces"}, {"chicken broth", "1", "cup"}, {"salt", "0.5", "tsp"}, {"sesame oil", "1", "tsp"}, {"green onions", "1", "stalk"}, {"sesame seeds", "1", "tsp"}});

        insertRecipe("Naengmyeon", "Korean", 15,
            "1. Mix the chilled beef broth and dongchimi broth together. Keep in the freezer until slushy.\n2. Boil the buckwheat noodles for 3-4 minutes. Rinse under ice-cold water. Drain well.\n3. Place a mound of noodles in a large serving bowl. Pour the slushy broth over them.\n4. Top the noodles with the pear, cucumber, and half a hard-boiled egg.\n5. Serve with vinegar and mustard on the side.",
            "photos/naengmyeon.jpg",
            new String[][]{{"buckwheat noodles", "200", "grams"}, {"beef broth", "3", "cups"}, {"dongchimi broth", "1", "cup"}, {"asian pear", "0.5", "piece"}, {"cucumber", "0.5", "piece"}, {"hard boiled egg", "1", "piece"}, {"vinegar", "2", "tbsp"}, {"korean mustard", "1", "tsp"}});

        insertRecipe("Jajangmyeon", "Korean", 30,
            "1. Heat oil in a wok and fry the black bean paste over medium heat for 3 minutes. Remove the paste and set aside.\n2. Fry the pork belly in the oil until cooked. Add the onion and zucchini, stir-frying for 5 minutes.\n3. Stir the fried paste back into the wok along with the sugar. Mix well.\n4. Pour in the water, bring to a simmer, and stir in the cornstarch slurry to thicken the sauce.\n5. Boil the noodles, drain, and serve topped with the black bean sauce.",
            "photos/jajangmyeon.jpg",
            new String[][]{{"udon noodles", "300", "grams"}, {"chunjang", "3", "tbsp"}, {"pork belly", "150", "grams"}, {"onion", "1", "piece"}, {"zucchini", "0.5", "piece"}, {"sugar", "1", "tbsp"}, {"water", "1", "cup"}, {"cornstarch", "2", "tbsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Hotteok", "Korean", 90,
            "1. Dissolve yeast in warm water. Mix with flour and sweet rice flour to form a sticky dough. Let rise for 1 hour.\n2. Mix the brown sugar, nuts, and cinnamon for the filling.\n3. Oil your hands. Take a handful of dough, flatten it, and place a spoonful of filling in the center. Seal into a ball.\n4. Heat oil in a pan over medium-low heat. Place the dough ball in the pan and press it completely flat.\n5. Fry for 2-3 minutes per side until deeply golden and crispy.",
            "photos/hotteok.jpg",
            new String[][]{{"all purpose flour", "1.5", "cups"}, {"sweet rice flour", "0.5", "cups"}, {"dry yeast", "1", "tsp"}, {"warm water", "1", "cup"}, {"brown sugar", "0.5", "cup"}, {"walnuts", "2", "tbsp"}, {"cinnamon powder", "1", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Patbingsu", "Korean", 10,
            "1. Prepare the shaved ice and place it in a large serving bowl.\n2. Top the center of the ice mound with the sweetened red beans.\n3. Arrange the chopped rice cakes and fresh fruit around the beans.\n4. Drizzle the condensed milk generously over the entire bowl.\n5. Mix well before eating, or eat layer by layer.",
            "photos/patbingsu.jpg",
            new String[][]{{"shaved ice", "3", "cups"}, {"sweetened red beans", "0.5", "cup"}, {"condensed milk", "0.25", "cup"}, {"korean rice cakes", "0.25", "cup"}, {"strawberries", "0.5", "cup"}});

        insertRecipe("Yakgwa", "Korean", 60,
            "1. Rub the sesame oil into the flour until crumbly.\n2. Mix honey, rice syrup, soju, and ginger juice. Add to the flour and knead gently to form a dough.\n3. Roll the dough flat and cut into flower shapes.\n4. Fry the pastries in low-heat oil until they float and turn golden brown. Drain.\n5. Make a syrup by simmering the remaining honey and rice syrup. Soak the fried pastries in the syrup for a few hours.",
            "photos/yakgwa.jpg",
            new String[][]{{"pastry flour", "2", "cups"}, {"sesame oil", "3", "tbsp"}, {"honey", "0.5", "cup"}, {"rice syrup", "0.5", "cup"}, {"soju", "2", "tbsp"}, {"ginger juice", "1", "tbsp"}, {"cooking oil", "3", "cups"}});
    }
}