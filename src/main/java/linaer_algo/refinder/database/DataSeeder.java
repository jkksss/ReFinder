package linaer_algo.refinder.database;

import java.sql.*;

public class DataSeeder {

    public static void seedData() {
        if (isAlreadySeeded()) {
            System.out.println("Database already seeded!");
            return;
        }
        seedFilipino();
        seedChinese();
        seedKorean();
        System.out.println("All recipes seeded successfully!");
    }

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

    private static void insertRecipe(String name, String cuisine, int cookTime, String instructions, String photoPath, String[][] ingredients) {
        String recipeSql = "INSERT INTO recipes (name, cuisine, cook_time, instructions, photo_path) VALUES (?, ?, ?, ?, ?)";
        String ingredientSql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_name, quantity_needed, unit) VALUES (?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

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
                                ipstmt.addBatch(); 
                            }
                            ipstmt.executeBatch();
                        }
                    }
                }
            }
            conn.commit(); 
            System.out.println("Seeded: " + name);

        } catch (SQLException | NumberFormatException e) {
            System.out.println("Error seeding " + name + ": " + e.getMessage());
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) {}
            }
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ex) {}
            }
        }
    }

    // Filipino Recipes
    private static void seedFilipino() {
        insertRecipe("Chicken Adobo", "Filipino", 40,
            "1. Combine the chicken pieces with soy sauce and crushed garlic in a large bowl. Mix well to coat, then marinate for at least 1 hour.\n2. Heat oil in a wide pot or deep pan over medium heat.\n3. Lift the chicken out of the marinade (reserve the liquid) and lightly pan-fry each piece for about 2 minutes per side until the skin turns golden. Remove and set aside.\n4. Using the same pot, sauté the remaining garlic until fragrant and lightly browned. Return the chicken to the pot.\n5. Pour in the reserved marinade, water, vinegar, whole peppercorns, and bay leaves.\n6. Bring to a boil — do not stir once the vinegar goes in; letting it boil uncovered for a couple of minutes mellows the sharpness.\n7. Lower the heat, cover, and simmer for 20–25 minutes, turning the chicken halfway, until fully cooked and tender.\n8. Uncover and continue cooking for another 5–10 minutes, letting the sauce reduce until it clings to the chicken. Serve hot over steamed rice.",
            "/photos/chicken_adobo.jpg",
            new String[][]{{"chicken thighs", "1", "kg"}, {"soy sauce", "120", "ml"}, {"white vinegar", "120", "ml"}, {"garlic", "6", "cloves"}, {"black peppercorns", "1", "tsp"}, {"bay leaves", "3", "pieces"}, {"water", "240", "ml"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Sinigang na Baboy", "Filipino", 60,
            "1. Place the pork ribs in a large pot with enough water to cover. Bring to a boil and simmer for 5 minutes, then skim off any foam and impurities.\n2. Add the tomatoes and onion (both halved) and continue to simmer over medium heat for 30–40 minutes, or until the pork is fork-tender.\n3. Stir in the tamarind paste and fish sauce. Taste the broth — it should be pleasantly sour and savory. Adjust as needed.\n4. Add the daikon radish and simmer for 5 minutes until just tender but not mushy.\n5. Add the string beans and cook for 3 more minutes.\n6. Turn off the heat, drop in the water spinach (kangkong), and cover the pot for 1–2 minutes so the leaves wilt. Serve immediately.",
            "/photos/sinigang_baboy.jpg",
            new String[][]{{"pork ribs", "1", "kg"}, {"tamarind paste", "3", "tbsp"}, {"tomatoes", "2", "pieces"}, {"onion", "1", "piece"}, {"fish sauce", "2", "tbsp"}, {"water", "1.5", "liters"}, {"daikon radish", "1", "cup"}, {"string beans", "1", "cup"}, {"water spinach", "2", "cups"}});

        insertRecipe("Pancit Canton", "Filipino", 30,
            "1. Heat oil in a large wok or wide pan over medium-high heat. Sauté the garlic until fragrant, then add the chicken breast strips and stir-fry until lightly browned.\n2. Add the shrimp and cook until they turn pink and curl. Remove the chicken and shrimp from the wok and set aside.\n3. In the same wok, stir-fry the carrots for 2 minutes, then add the cabbage and toss for another minute.\n4. Pour in the chicken broth, soy sauce, and oyster sauce. Bring to a boil.\n5. Add the pancit canton noodles directly to the boiling liquid.\n6. Gently toss and fold the noodles, letting them absorb the broth as they soften — about 3–4 minutes.\n7. Return the cooked chicken and shrimp to the wok and toss everything together. Season with pepper and serve.",
            "/photos/pancit_canton.jpg",
            new String[][]{{"pancit canton noodles", "250", "grams"}, {"chicken breast", "200", "grams"}, {"shrimp", "150", "grams"}, {"soy sauce", "3", "tbsp"}, {"oyster sauce", "2", "tbsp"}, {"chicken broth", "2", "cups"}, {"carrots", "1", "cup"}, {"cabbage", "2", "cups"}, {"garlic", "4", "cloves"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Beef Caldereta", "Filipino", 100,
            "1. Heat oil in a heavy pot over medium-high heat. Sauté the garlic and onion until softened and fragrant.\n2. Add the beef chunks and sear on all sides until browned, working in batches if needed.\n3. Pour in the tomato sauce and beef broth. Bring to a boil, then reduce heat to low. Cover and braise for 1 to 1.5 hours until the beef is very tender.\n4. Stir in the liver spread until fully dissolved into the sauce.\n5. Add the potatoes and carrots. Cover and simmer for 10 minutes until the vegetables are nearly fork-tender.\n6. Add the bell peppers and cook uncovered for 5 more minutes. Taste and season before serving.",
            "/photos/beef_caldereta.jpg",
            new String[][]{{"beef stew meat", "1", "kg"}, {"tomato sauce", "250", "ml"}, {"liver spread", "85", "grams"}, {"potatoes", "2", "pieces"}, {"carrots", "2", "pieces"}, {"bell peppers", "1", "piece"}, {"garlic", "4", "cloves"}, {"onion", "1", "piece"}, {"beef broth", "2", "cups"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Chicken Tinola", "Filipino", 50,
            "1. Heat oil in a pot over medium heat. Sauté the ginger first until fragrant, then add the garlic and onion.\n2. Add the chicken pieces and cook until the outside is lightly seared and no longer pink.\n3. Season with fish sauce and stir for 1–2 minutes, letting the chicken absorb the savory flavor.\n4. Pour in the water (or rice washing). Bring to a boil, then cover and reduce heat to a gentle simmer for 25–30 minutes until the chicken is tender.\n5. Add the green papaya wedges and simmer for another 8–10 minutes.\n6. Turn off the heat and stir in the chili leaves. Let them wilt for a minute and serve hot.",
            "/photos/chicken_tinola.jpg",
            new String[][]{{"chicken", "1", "kg"}, {"green papaya", "1", "piece"}, {"chili leaves", "1", "cup"}, {"ginger", "2", "tbsp"}, {"garlic", "4", "cloves"}, {"onion", "1", "piece"}, {"fish sauce", "2", "tbsp"}, {"water", "4", "cups"}, {"cooking oil", "1", "tbsp"}});

        insertRecipe("Kare-Kare", "Filipino", 180,
            "1. Place the oxtail pieces in a large pot, cover with water, and bring to a boil. Skim off the foam, then simmer for 2–2.5 hours until the meat is very tender.\n2. Reserve the braising liquid.\n3. Dissolve the annatto powder in about half a cup of the reserved broth to release its deep orange color. Pour this into the pot with the oxtail.\n4. Stir in the peanut butter gradually, adding more broth to loosen if needed, until the sauce is thick and smooth. Season lightly with salt.\n5. Add the eggplant and string beans and simmer for 5–7 minutes.\n6. Add the bok choy and cook for 2 minutes more. Serve immediately alongside a bowl of bagoong.",
            "/photos/kare_kare.jpg",
            new String[][]{{"oxtail", "1", "kg"}, {"peanut butter", "1", "cup"}, {"eggplant", "1", "piece"}, {"string beans", "1", "bundle"}, {"bok choy", "1", "bundle"}, {"annatto powder", "2", "tbsp"}, {"shrimp paste", "0.5", "cup"}, {"water", "6", "cups"}});

        insertRecipe("Lechon Kawali", "Filipino", 60,
            "1. Place the pork belly in a pot along with the garlic, bay leaves, peppercorns, and salt. Cover with water and bring to a boil.\n2. Reduce heat and simmer for 40–45 minutes until the pork is cooked through.\n3. Remove the pork and place it on a wire rack. Pat dry thoroughly with paper towels.\n4. Let it air-dry uncovered in the fridge for at least 2 hours (or overnight) for maximum crispiness.\n5. Heat a generous amount of oil in a deep pan to 180°C (350°F). Carefully lower the dried pork belly into the hot oil.\n6. Fry for 10–15 minutes until the skin is deep golden-brown and blistered. Drain on a wire rack.\n7. Let it rest for 5 minutes, chop into bite-sized pieces, and serve with vinegar dipping sauce.",
            "/photos/lechon_kawali.jpg",
            new String[][]{{"pork belly", "1", "kg"}, {"garlic", "4", "cloves"}, {"bay leaves", "2", "pieces"}, {"black peppercorns", "1", "tbsp"}, {"salt", "2", "tbsp"}, {"water", "4", "cups"}, {"cooking oil", "3", "cups"}});

        insertRecipe("Bistek Tagalog", "Filipino", 45,
            "1. Slice the beef sirloin thinly across the grain. Combine with soy sauce, calamansi juice, minced garlic, and black pepper in a bowl.\n2. Marinate for at least 30 minutes.\n3. Slice the onions into rings. Heat a little oil in a pan and cook the onion rings until translucent. Remove and set aside.\n4. Turn the heat up to medium-high. Remove the beef from the marinade (reserve the liquid) and cook the slices in a single layer until browned. Work in batches.\n5. Pour the remaining marinade into the pan. Let it come to a simmer and cook for 3–5 minutes.\n6. Arrange the cooked onion rings on top of the beef and serve immediately over steamed rice.",
            "/photos/bistek_tagalog.jpg",
            new String[][]{{"beef sirloin", "500", "grams"}, {"soy sauce", "0.25", "cup"}, {"calamansi juice", "3", "tbsp"}, {"garlic", "3", "cloves"}, {"onion", "2", "pieces"}, {"black pepper", "0.5", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Pork Barbecue", "Filipino", 25,
            "1. Whisk together the soy sauce, banana ketchup, calamansi juice, brown sugar, and minced garlic until the sugar dissolves.\n2. Add the thinly sliced pork shoulder to the marinade. Cover and refrigerate overnight.\n3. Soak bamboo skewers in water for at least 30 minutes before grilling.\n4. Thread the pork slices loosely onto the skewers, weaving them accordion-style.\n5. Grill over hot coals or a preheated grill on medium-high heat for 3–5 minutes per side, basting generously with the leftover marinade.\n6. Serve immediately with a dipping sauce of spiced vinegar and steamed rice.",
            "/photos/pork_barbecue.jpg",
            new String[][]{{"pork shoulder", "1", "kg"}, {"soy sauce", "0.5", "cup"}, {"banana ketchup", "0.5", "cup"}, {"calamansi juice", "3", "tbsp"}, {"brown sugar", "0.25", "cup"}, {"garlic", "5", "cloves"}, {"bamboo skewers", "20", "pieces"}});

        insertRecipe("Tortang Talong", "Filipino", 20,
            "1. Char the eggplants directly over a gas flame or under a broiler until the skin is completely blackened and the flesh inside is soft.\n2. Let the eggplants cool enough to handle, then peel off the charred skin. Keep the stem intact and use a fork to gently flatten the flesh.\n3. Beat the eggs in a shallow bowl and season well with salt and pepper.\n4. Gently lay a flattened eggplant into the egg mixture to coat it completely.\n5. Heat oil in a non-stick pan over medium heat. Slide the egg-coated eggplant into the pan.\n6. Cook for 2–3 minutes until the bottom is set and golden. Carefully flip and cook the other side. Serve hot.",
            "/photos/tortang_talong.jpg",
            new String[][]{{"eggplants", "4", "pieces"}, {"eggs", "3", "pieces"}, {"salt", "1", "tsp"}, {"black pepper", "0.5", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Chicken Inasal", "Filipino", 40,
            "1. Prepare the marinade: pound the lemongrass stalks to release their oils. Combine with calamansi juice, vinegar, minced garlic, grated ginger, and brown sugar.\n2. Add the chicken legs and thighs to the marinade. Cover and refrigerate for at least 3 hours.\n3. Make the annatto (achuete) basting oil by warming cooking oil with annatto seeds over low heat until the oil turns red. Strain and set aside.\n4. Preheat grill to medium heat. Remove chicken from marinade and grill for 15–20 minutes per side.\n5. Baste frequently with the annatto oil until cooked through and slightly charred.\n6. Serve with steamed rice and a dipping sauce of vinegar, garlic, and chili.",
            "/photos/chicken_inasal.jpg",
            new String[][]{{"chicken legs", "1", "kg"}, {"lemongrass", "2", "stalks"}, {"calamansi juice", "0.25", "cup"}, {"vinegar", "0.25", "cup"}, {"garlic", "6", "cloves"}, {"ginger", "2", "tbsp"}, {"brown sugar", "2", "tbsp"}, {"annatto oil", "0.25", "cup"}});

        insertRecipe("Bicol Express", "Filipino", 50,
            "1. Heat oil in a wide pan or wok over medium heat. Sauté the garlic until fragrant, then add the onion and cook until softened.\n2. Add the pork belly strips and cook until lightly browned on all sides.\n3. Stir in the bagoong (shrimp paste) and cook for 2 minutes.\n4. Pour in the coconut milk and bring to a gentle simmer. Cook uncovered for 20–25 minutes until the pork is tender.\n5. Stir in the siling labuyo (bird's eye chilies) followed by the coconut cream.\n6. Simmer for another 8–10 minutes until the sauce is thick and coats the pork. Serve over plenty of rice.",
            "/photos/bicol_express.jpg",
            new String[][]{{"pork belly", "500", "grams"}, {"coconut milk", "2", "cups"}, {"coconut cream", "1", "cup"}, {"shrimp paste", "3", "tbsp"}, {"garlic", "4", "cloves"}, {"onion", "1", "piece"}, {"bird eye chilies", "10", "pieces"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Lumpiang Shanghai", "Filipino", 30,
            "1. In a large bowl, combine the ground pork, finely shredded carrots, minced onion, and garlic. Season with soy sauce, salt, and pepper. Mix well.\n2. Lay a spring roll wrapper flat. Place about 1 tablespoon of filling in a thin horizontal line across the lower third.\n3. Fold the bottom edge over the filling, roll once, then fold in both sides snugly. Continue rolling tightly and seal the edge with beaten egg.\n4. Heat oil in a deep pan or wok to about 170°C (340°F).\n5. Fry the lumpia in batches for 3–4 minutes, turning occasionally, until golden brown and crispy.\n6. Drain on a wire rack and serve with sweet chili sauce.",
            "/photos/lumpiang_shanghai.jpg",
            new String[][]{{"ground pork", "500", "grams"}, {"carrots", "1", "cup"}, {"onion", "1", "piece"}, {"garlic", "4", "cloves"}, {"soy sauce", "1", "tbsp"}, {"spring roll wrappers", "30", "pieces"}, {"egg", "1", "piece"}, {"cooking oil", "2", "cups"}});

        insertRecipe("Pork Sisig", "Filipino", 45,
            "1. Boil the pork belly in salted water until tender, about 30 minutes. Drain and pat dry.\n2. Grill or pan-fry over high heat until the skin is crispy and deeply browned. Chop finely into small bits.\n3. Pan-fry the chicken liver over medium-high heat until cooked through. Chop finely and set aside.\n4. Heat a little oil in a cast-iron pan or sizzling plate. Sauté half of the diced onions until translucent. Add the chopped pork and liver.\n5. Season with soy sauce, calamansi juice, chopped green chilies, and black pepper. Toss and cook for 2–3 minutes.\n6. Turn off the heat. Stir in the mayonnaise and the remaining raw onions. Serve sizzling hot.",
            "/photos/pork_sisig.jpg",
            new String[][]{{"pork belly", "500", "grams"}, {"chicken liver", "150", "grams"}, {"onion", "1", "piece"}, {"calamansi juice", "3", "tbsp"}, {"soy sauce", "2", "tbsp"}, {"mayonnaise", "2", "tbsp"}, {"green chilies", "2", "pieces"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Pinakbet", "Filipino", 30,
            "1. Heat oil in a wide pot over medium heat. Sauté the garlic until fragrant, then add the tomatoes and cook until they soften and release juices.\n2. Add the pork belly strips and cook until lightly browned.\n3. Stir in the bagoong (shrimp paste) and cook for 2 minutes. Pour in the water and bring to a simmer.\n4. Add the squash (kalabasa) first and simmer for 5 minutes.\n5. Add the string beans and eggplant. Stir gently so vegetables don't break apart. Cover and cook for 5 minutes.\n6. Add the ampalaya (bitter melon) on top. Cover and cook for a final 4–5 minutes. Serve immediately.",
            "/photos/pinakbet.jpg",
            new String[][]{{"pork belly", "200", "grams"}, {"bitter melon", "1", "piece"}, {"eggplant", "1", "piece"}, {"squash", "2", "cups"}, {"string beans", "1", "bundle"}, {"shrimp paste", "2", "tbsp"}, {"tomatoes", "3", "pieces"}, {"garlic", "3", "cloves"}, {"water", "1", "cup"}});

        insertRecipe("Leche Flan", "Filipino", 45,
            "1. Make the caramel: place the white sugar in a heavy saucepan over medium-low heat. Swirl gently until it turns a rich amber color.\n2. Immediately pour the caramel into a llanera or mold, tilting to coat the bottom evenly.\n3. In a large bowl, whisk the egg yolks gently. Stir in the condensed milk, evaporated milk, and vanilla extract.\n4. Pour the custard mixture through a fine-mesh strainer directly over the hardened caramel in the mold.\n5. Cover tightly with aluminum foil and steam over medium-low heat for 30–35 minutes until just set with a slight jiggle.\n6. Cool completely and refrigerate for at least 2 hours before unmolding.",
            "/photos/leche_flan.jpg",
            new String[][]{{"egg yolks", "10", "pieces"}, {"condensed milk", "1", "can"}, {"evaporated milk", "1", "can"}, {"vanilla extract", "1", "tsp"}, {"white sugar", "0.5", "cup"}});

        insertRecipe("Turon", "Filipino", 20,
            "1. Peel the saba bananas and slice each one in half lengthwise. Roll each piece generously in brown sugar.\n2. Lay a spring roll wrapper flat. Place a coated banana slice and a few strips of sweetened langka (jackfruit) across the lower portion.\n3. Fold the lower edge over the filling, then fold in the two sides, and roll upward tightly.\n4. Moisten the last edge with water to seal completely.\n5. Heat oil in a pan over medium heat. Fry the turon for 2–3 minutes per side until golden and crisp.\n6. Transfer to a wire rack to drain. Let cool slightly before eating.",
            "/photos/turon.jpg",
            new String[][]{{"saba bananas", "6", "pieces"}, {"jackfruit", "0.5", "cup"}, {"brown sugar", "1", "cup"}, {"spring roll wrappers", "12", "pieces"}, {"cooking oil", "2", "cups"}});

        insertRecipe("Buko Pandan", "Filipino", 15,
            "1. Prepare the pandan jelly according to package directions. Once set and chilled, cut into small cubes.\n2. Cook the tapioca pearls in boiling water until fully translucent, then rinse under cold water and drain.\n3. In a large mixing bowl, combine the all-purpose cream and condensed milk. Stir until well blended and smooth.\n4. Fold in the young coconut strings (buko), pandan jelly cubes, and tapioca pearls.\n5. Mix gently until everything is evenly coated in the sweet cream.\n6. Chill in the refrigerator for at least 2 hours before serving.",
            "/photos/buko_pandan.jpg",
            new String[][]{{"young coconut strings", "2", "cups"}, {"pandan jelly cubes", "2", "cups"}, {"all purpose cream", "1", "cup"}, {"condensed milk", "0.5", "cup"}, {"tapioca pearls", "0.5", "cup"}});

        insertRecipe("Halo-Halo", "Filipino", 10,
            "1. Prepare mix-ins: sweetened red beans, kaong, nata de coco, sweetened saba banana slices, and sweetened jackfruit strips.\n2. Spoon the mix-ins into the bottom of a tall, wide glass or bowl — about 2 tablespoons of each.\n3. Pack the glass with shaved ice, mounding it generously above the rim.\n4. Pour cold evaporated milk over the shaved ice.\n5. Top with a dollop of ube halaya, a slice of leche flan, and a scoop of ube or macapuno ice cream.\n6. Serve immediately and mix thoroughly before eating.",
            "/photos/halo_halo.jpg",
            new String[][]{{"shaved ice", "2", "cups"}, {"evaporated milk", "0.5", "cup"}, {"sweetened red beans", "2", "tbsp"}, {"sweetened jackfruit", "2", "tbsp"}, {"nata de coco", "2", "tbsp"}, {"ube halaya", "1", "tbsp"}, {"ube ice cream", "1", "scoop"}});

        insertRecipe("Pancit Palabok", "Filipino", 40,
            "1. Soak the dried rice noodles in water for 10–15 minutes, then boil in salted water until just tender. Drain and arrange on a serving platter.\n2. Make the palabok sauce: whisk together the shrimp broth, annatto powder, and cornstarch in a saucepan.\n3. Bring to a simmer over medium heat, stirring continuously until thickened into an orange gravy. Season with fish sauce.\n4. In a separate pan, cook the ground pork until browned and crispy, then stir it into the sauce.\n5. Pour the hot palabok sauce generously over the plated noodles.\n6. Garnish with cooked shrimp, sliced hard-boiled eggs, crushed chicharon, tinapa flakes, and green onions. Squeeze calamansi over top before serving.",
            "/photos/pancit_palabok.jpg",
            new String[][]{{"rice noodles", "250", "grams"}, {"shrimp broth", "3", "cups"}, {"ground pork", "150", "grams"}, {"annatto powder", "1", "tbsp"}, {"cornstarch", "3", "tbsp"}, {"shrimp", "100", "grams"}, {"hard boiled eggs", "2", "pieces"}, {"chicharon", "0.5", "cup"}, {"green onions", "2", "stalks"}, {"fish sauce", "2", "tbsp"}});

        insertRecipe("Beef Tapa", "Filipino", 20,
            "1. In a bowl, combine soy sauce, calamansi juice, minced garlic, brown sugar, and black pepper. Stir until the sugar dissolves.\n2. Add the thinly sliced beef sirloin to the marinade, tossing to ensure every slice is coated.\n3. Cover and marinate overnight in the refrigerator.\n4. Heat oil in a skillet or cast-iron pan over medium-high heat.\n5. Remove the beef from the marinade and fry in batches. Cook each side for 2–3 minutes until edges char slightly and it caramelizes.\n6. Serve with garlic fried rice (sinangag) and a fried egg.",
            "/photos/beef_tapa.jpg",
            new String[][]{{"beef sirloin", "500", "grams"}, {"soy sauce", "3", "tbsp"}, {"calamansi juice", "2", "tbsp"}, {"garlic", "5", "cloves"}, {"brown sugar", "1", "tbsp"}, {"black pepper", "1", "tsp"}, {"cooking oil", "2", "tbsp"}});
    }

    // Chinese Recipes
    private static void seedChinese() {
        insertRecipe("Kung Pao Chicken", "Chinese", 25,
            "1. Dice the chicken into 1.5cm cubes. Toss with 1 tbsp soy sauce, cornstarch, Shaoxing wine, and white pepper. Marinate for 15–20 minutes.\n2. Mix the sauce: combine remaining soy sauce, black vinegar, sugar, and cornstarch. Set aside.\n3. Toast raw peanuts in a dry wok over medium heat until fragrant. Remove and set aside.\n4. Heat oil in the wok over high heat. Sear the chicken for 1 minute, then stir-fry until cooked through. Remove and set aside.\n5. Add a little more oil. Stir-fry dried chilies and Sichuan peppercorns until fragrant, then add garlic and ginger.\n6. Return the chicken to the wok. Pour in the sauce and toss quickly until thickened.\n7. Add the peanuts, toss once more, and serve with rice.",
            "/photos/kungpao_chicken.jpg",
            new String[][]{{"chicken breast", "400", "grams"}, {"soy sauce", "2", "tbsp"}, {"cornstarch", "1", "tbsp"}, {"dried red chilies", "8", "pieces"}, {"peanuts", "0.5", "cup"}, {"garlic", "3", "cloves"}, {"ginger", "1", "tbsp"}, {"black vinegar", "1", "tbsp"}, {"sugar", "1", "tbsp"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Mapo Tofu", "Chinese", 20,
            "1. Cut the soft tofu into cubes. Blanch in lightly salted boiling water for 2 minutes. Drain gently and set aside.\n2. Heat oil in a wok over medium-high heat. Stir-fry the ground pork until browned and crispy. Push to the side.\n3. Add ginger and garlic to the center and stir-fry for 30 seconds. Add doubanjiang and stir-fry for 1–2 minutes until the oil turns red.\n4. Pour in the chicken broth and soy sauce. Bring to a gentle simmer.\n5. Slide the blanched tofu cubes carefully into the sauce. Gently swirl the wok to incorporate.\n6. Simmer for 3–4 minutes, then stir in a cornstarch slurry to thicken the sauce.\n7. Top with ground Sichuan peppercorn powder and sliced green onions. Serve with rice.",
            "/photos/mapo_tofu.jpg",
            new String[][]{{"soft tofu", "400", "grams"}, {"ground pork", "150", "grams"}, {"doubanjiang", "2", "tbsp"}, {"garlic", "3", "cloves"}, {"ginger", "1", "tbsp"}, {"chicken broth", "1", "cup"}, {"soy sauce", "1", "tbsp"}, {"cornstarch", "2", "tbsp"}, {"sichuan peppercorn powder", "0.5", "tsp"}, {"green onions", "2", "stalks"}});

        insertRecipe("Beef and Broccoli", "Chinese", 25,
            "1. Slice the flank steak thinly against the grain. Toss with 1 tbsp soy sauce and cornstarch. Let sit for 15 minutes.\n2. Blanch the broccoli florets in boiling water for 90 seconds, drain, and plunge into ice water. Set aside.\n3. Whisk together the remaining soy sauce, oyster sauce, brown sugar, sesame oil, and water for the sauce.\n4. Heat a wok over high heat. Sear the beef in a single layer for 1 minute per side until browned. Remove.\n5. Sauté the garlic in the wok for 30 seconds. Pour in the sauce and bring to a boil.\n6. Return the beef and add the broccoli. Toss everything together for 1–2 minutes until coated. Serve with rice.",
            "/photos/beef_broccoli.jpg",
            new String[][]{{"flank steak", "350", "grams"}, {"broccoli florets", "3", "cups"}, {"soy sauce", "3", "tbsp"}, {"oyster sauce", "2", "tbsp"}, {"brown sugar", "1", "tbsp"}, {"cornstarch", "1", "tbsp"}, {"garlic", "3", "cloves"}, {"sesame oil", "1", "tsp"}, {"water", "0.5", "cup"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Sweet and Sour Pork", "Chinese", 35,
            "1. Cut the pork into bite-sized cubes. Season with salt and pepper, then coat generously in cornstarch.\n2. Deep-fry the pork at 175°C (350°F) in batches for 4–5 minutes until golden. Drain on a wire rack.\n3. Make the sauce: combine ketchup, white vinegar, sugar, soy sauce, and water in a pan. Bring to a simmer.\n4. Add the bell pepper chunks and pineapple pieces to the sauce. Cook for 2–3 minutes so they remain crisp.\n5. Quickly toss the crispy pork into the sweet and sour sauce, coating every piece.\n6. Serve immediately so the pork retains its crunch.",
            "/photos/sweet_sour_pork.jpg",
            new String[][]{{"pork shoulder", "400", "grams"}, {"cornstarch", "0.5", "cup"}, {"bell peppers", "1", "cup"}, {"pineapple chunks", "1", "cup"}, {"ketchup", "3", "tbsp"}, {"white vinegar", "3", "tbsp"}, {"sugar", "3", "tbsp"}, {"water", "0.5", "cup"}, {"soy sauce", "1", "tbsp"}, {"cooking oil", "2", "cups"}});

        insertRecipe("Egg Fried Rice", "Chinese", 15,
            "1. Use day-old, refrigerated rice and break up any large clumps with your fingers.\n2. Heat oil in a wok over high heat. Add the beaten eggs, scramble quickly into soft curds, and remove before fully set.\n3. Add remaining oil to the hot wok. Tip in the cold rice, spread into a single layer, and let sit undisturbed for 1 minute to crust.\n4. Toss the rice, then add soy sauce, salt, and white pepper. Toss constantly over high heat for 2 minutes.\n5. Return the scrambled eggs and sliced green onions to the wok. Toss for one final minute.\n6. Drizzle with sesame oil, toss once more, and serve.",
            "/photos/egg_fried_rice.jpg",
            new String[][]{{"cooked rice", "3", "cups"}, {"eggs", "3", "pieces"}, {"green onions", "3", "stalks"}, {"soy sauce", "1.5", "tbsp"}, {"salt", "0.5", "tsp"}, {"white pepper", "0.25", "tsp"}, {"cooking oil", "2", "tbsp"}, {"sesame oil", "1", "tsp"}});

        insertRecipe("Char Siu", "Chinese", 50,
            "1. Make the marinade: whisk together hoisin sauce, soy sauce, honey, five-spice powder, garlic, and food coloring.\n2. Cut the pork shoulder into strips. Submerge in the marinade, cover, and refrigerate overnight.\n3. Preheat the oven to 200°C (400°F). Place the pork strips on a wire rack over a roasting pan with water in the bottom.\n4. Roast for 25 minutes, flip, and baste with the remaining marinade.\n5. Roast for another 15–20 minutes until cooked through and caramelized.\n6. For the final 2–3 minutes, brush with honey and broil to char the edges. Rest 10 minutes before slicing.",
            "/photos/char_siu.jpg",
            new String[][]{{"pork shoulder", "1", "kg"}, {"hoisin sauce", "3", "tbsp"}, {"soy sauce", "2", "tbsp"}, {"honey", "3", "tbsp"}, {"chinese five spice powder", "1", "tsp"}, {"garlic", "3", "cloves"}});

        insertRecipe("Chow Mein", "Chinese", 15,
            "1. Boil the chow mein noodles until just tender. Drain, rinse under cold water, and toss with a little oil.\n2. Mix soy sauce, oyster sauce, and sesame oil in a small bowl.\n3. Heat a wok over high heat until smoking. Add oil, then stir-fry carrots for 1 minute.\n4. Add the cabbage and toss for another minute (vegetables should stay crisp).\n5. Add the noodles and bean sprouts. Toss everything together over high heat.\n6. Pour the sauce over the noodles and toss continuously for 2–3 minutes until the noodles char slightly. Serve immediately.",
            "/photos/chow_mein.jpg",
            new String[][]{{"chow mein noodles", "250", "grams"}, {"cabbage", "2", "cups"}, {"carrots", "1", "cup"}, {"bean sprouts", "1", "cup"}, {"soy sauce", "2", "tbsp"}, {"oyster sauce", "1", "tbsp"}, {"sesame oil", "1", "tsp"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Hot and Sour Soup", "Chinese", 20,
            "1. Bring the chicken broth to a boil in a medium pot.\n2. Add the silken tofu strips, sliced bamboo shoots, and rehydrated wood ear mushrooms. Simmer for 5 minutes.\n3. Stir in the soy sauce, Chinkiang black vinegar, and white pepper. Taste and adjust heat/sourness.\n4. Mix cornstarch with cold water. Slowly drizzle the slurry into the soup while stirring in circles until thickened.\n5. Turn off the heat. Beat the egg and pour it in a thin, steady stream while stirring gently to create egg ribbons.\n6. Garnish with sliced green onions and a drizzle of chili oil.",
            "/photos/hot_sour_soup.jpg",
            new String[][]{{"chicken broth", "4", "cups"}, {"firm tofu", "200", "grams"}, {"bamboo shoots", "0.5", "cup"}, {"wood ear mushrooms", "0.5", "cup"}, {"soy sauce", "2", "tbsp"}, {"black vinegar", "3", "tbsp"}, {"white pepper", "1", "tsp"}, {"cornstarch", "3", "tbsp"}, {"egg", "1", "piece"}});

        insertRecipe("Pork Dumplings", "Chinese", 30,
            "1. Mince the napa cabbage, salt lightly, let sit for 10 minutes, and squeeze out liquid. Mix with ground pork, soy sauce, sesame oil, and ginger.\n2. Place a teaspoon of filling in the center of a dumpling wrapper.\n3. Wet the edges, fold in half, and pleat to seal firmly.\n4. Heat oil in a non-stick pan over medium heat. Fry dumplings flat-side down for 2–3 minutes until bottoms are golden-brown.\n5. Pour in enough water to come 1/3 of the way up the dumplings. Cover immediately and steam for 5–6 minutes.\n6. Remove lid, let water evaporate, and cook for 1 more minute to re-crisp bottoms. Serve with dipping sauce.",
            "/photos/pork_dumpling.jpg",
            new String[][]{{"ground pork", "300", "grams"}, {"napa cabbage", "1", "cup"}, {"soy sauce", "1", "tbsp"}, {"sesame oil", "1", "tbsp"}, {"ginger", "1", "tsp"}, {"dumpling wrappers", "30", "pieces"}, {"water", "0.5", "cup"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Scallion Pancakes", "Chinese", 45,
            "1. Mix flour with boiling water to form a shaggy dough. Knead into a smooth ball, cover, and rest for 30 minutes.\n2. Divide dough into 4 pieces. Roll each piece into a very thin circle.\n3. Brush the surface with sesame oil and sprinkle evenly with salt and sliced scallions.\n4. Roll into a tight log, coil it like a snail shell, and tuck the end underneath.\n5. Flatten the coil and roll it out again into a round pancake to create flaky layers.\n6. Pan-fry in an oiled skillet over medium heat for 2–3 minutes per side until golden and crispy. Cut into wedges.",
            "/photos/scallion_pancakes.jpg",
            new String[][]{{"all purpose flour", "2", "cups"}, {"boiling water", "0.75", "cup"}, {"scallions", "1", "cup"}, {"sesame oil", "2", "tbsp"}, {"salt", "1", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Wonton Soup", "Chinese", 20,
            "1. Mince the shrimp and combine with ground pork, soy sauce, and sesame oil until the filling is cohesive.\n2. Place a small teaspoon of filling in the center of a wonton wrapper.\n3. Wet the edges, fold into a triangle, and press out air. Bring the two bottom corners together and press to seal.\n4. Bring chicken broth to a boil in a large pot. Season with salt and sesame oil.\n5. Drop wontons into the broth and cook for 5–6 minutes until they float.\n6. Add bok choy, simmer for 1 minute, and serve garnished with green onions.",
            "/photos/wonton_soup.jpg",
            new String[][]{{"ground pork", "200", "grams"}, {"shrimp", "100", "grams"}, {"soy sauce", "1", "tbsp"}, {"sesame oil", "1", "tsp"}, {"wonton wrappers", "20", "pieces"}, {"chicken broth", "4", "cups"}, {"bok choy", "2", "cups"}, {"green onions", "1", "stalk"}});

        insertRecipe("Dan Dan Noodles", "Chinese", 15,
            "1. Cook the ground pork in a hot pan with a splash of oil until browned.\n2. Add doubanjiang and soy sauce. Stir-fry until pork is crispy and fragrant. Set aside.\n3. Build the sauce in serving bowls: whisk sesame paste, chili oil, black vinegar, minced garlic, and soy sauce. Thin with hot water.\n4. Cook noodles according to package instructions. Reserve a ladle of starchy noodle water.\n5. Add drained noodles to the bowls with the sauce and a splash of noodle water. Toss vigorously.\n6. Top with the spiced pork, blanched bok choy, and green onions.",
            "/photos/dandan_noodle.jpg",
            new String[][]{{"wheat noodles", "200", "grams"}, {"ground pork", "150", "grams"}, {"doubanjiang", "1", "tbsp"}, {"soy sauce", "2", "tbsp"}, {"sesame paste", "2", "tbsp"}, {"chili oil", "2", "tbsp"}, {"black vinegar", "1", "tbsp"}, {"garlic", "2", "cloves"}, {"green onions", "2", "stalks"}});

        insertRecipe("Tomato Egg Stir-fry", "Chinese", 15,
            "1. Beat the eggs with a pinch of salt and a teaspoon of soy sauce. Cut tomatoes into large chunky wedges.\n2. Heat oil in a wok. Pour in eggs and scramble loosely into large curds. Remove while slightly underdone and set aside.\n3. Add remaining oil to the wok and sauté the white parts of the green onions for 30 seconds.\n4. Add tomatoes and cook over high heat for 3–4 minutes, pressing lightly to release juices.\n5. Season with sugar, salt, and ketchup to create a glossy sauce.\n6. Gently fold the scrambled eggs into the tomatoes for 30 seconds. Garnish with green onions.",
            "/photos/tomato_egg_stir_fry.jpg",
            new String[][]{{"tomatoes", "3", "pieces"}, {"eggs", "4", "pieces"}, {"green onions", "2", "stalks"}, {"sugar", "1", "tbsp"}, {"salt", "0.5", "tsp"}, {"ketchup", "1", "tbsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Vegetable Spring Rolls", "Chinese", 30,
            "1. Sauté shredded cabbage, carrots, and shiitake mushrooms in a wok until wilted. Season with soy sauce and sesame oil. Let cool completely.\n2. Lay a spring roll wrapper in a diamond shape and place a tablespoon of cooled filling near the bottom corner.\n3. Fold the bottom corner over the filling, fold in both side corners, and roll tightly upward.\n4. Seal the final flap with a paste of cornstarch and water.\n5. Heat oil to 175°C (350°F) in a deep pan. Fry rolls in batches for 3–4 minutes until golden and crisp.\n6. Drain on a wire rack and serve with sweet chili sauce.",
            "/photos/vegetable_spring_rolls.jpg",
            new String[][]{{"spring roll wrappers", "10", "pieces"}, {"cabbage", "2", "cups"}, {"carrots", "1", "cup"}, {"shiitake mushrooms", "0.5", "cup"}, {"soy sauce", "1", "tbsp"}, {"sesame oil", "1", "tsp"}, {"cornstarch", "1", "tsp"}, {"cooking oil", "2", "cups"}});

        insertRecipe("Hong Shao Rou", "Chinese", 70,
            "1. Cut pork belly into cubes. Blanch in boiling water for 3 minutes. Drain, rinse, and pat dry.\n2. Heat a little oil in a heavy pot over low heat. Add sugar and swirl until it melts into an amber caramel.\n3. Add the pork belly cubes carefully and toss to coat in the caramel for 1–2 minutes.\n4. Add light soy sauce, dark soy sauce, Shaoxing wine, star anise, ginger, and hot water to submerge the pork. Bring to a boil.\n5. Cover and simmer over the lowest heat for 45–60 minutes until meat is tender and sauce reduces to a thick glaze.\n6. Serve over steamed rice.",
            "/photos/hong_shou_rou.jpg",
            new String[][]{{"pork belly", "500", "grams"}, {"light soy sauce", "2", "tbsp"}, {"dark soy sauce", "1", "tbsp"}, {"brown sugar", "2", "tbsp"}, {"star anise", "2", "pieces"}, {"ginger", "4", "pieces"}, {"water", "2", "cups"}, {"cooking oil", "1", "tbsp"}});

        insertRecipe("Steamed Fish", "Chinese", 15,
            "1. Julienne the ginger and slice the scallions into long strips. Soak scallions in ice water to curl.\n2. Place the fish on a heat-proof plate. Scatter half the ginger on and under the fish.\n3. Steam for 8–10 minutes (for fillets) until the flesh flakes easily and is opaque.\n4. Carefully pour off any accumulated liquid from the plate.\n5. Lay the fresh ginger and curled scallions on top of the fish. Pour soy sauce mixed with sugar and sesame oil around the fish.\n6. Heat 2 tablespoons of oil in a pan until smoking, then pour the hot oil directly over the ginger and scallions to sizzle.",
            "/photos/steamed_fish.jpg",
            new String[][]{{"white fish fillet", "400", "grams"}, {"ginger", "3", "tbsp"}, {"scallions", "3", "stalks"}, {"soy sauce", "3", "tbsp"}, {"sugar", "1", "tsp"}, {"sesame oil", "1", "tsp"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Congee", "Chinese", 90,
            "1. Rinse the jasmine rice. Marinate it with sesame oil and salt for 10 minutes for a silkier texture.\n2. Bring chicken broth and sliced ginger to a boil in a large pot. Add the rice and stir well.\n3. Reduce heat to the lowest simmer. Cook uncovered for 60–90 minutes, stirring occasionally until it breaks down into a creamy porridge.\n4. Add more water or broth if it gets too thick.\n5. Stir in the salt and shredded poached chicken. Adjust seasoning.\n6. Serve topped with green onions, white pepper, and a drizzle of sesame oil.",
            "/photos/congee.jpg",
            new String[][]{{"jasmine rice", "1", "cup"}, {"chicken broth", "8", "cups"}, {"ginger", "3", "pieces"}, {"salt", "1", "tsp"}, {"shredded chicken", "1", "cup"}, {"green onions", "2", "stalks"}});

        insertRecipe("Egg Tarts", "Chinese", 40,
            "1. Make the custard: dissolve the sugar in hot water to make a syrup and let it cool.\n2. Gently whisk the eggs in a bowl (avoiding air bubbles), then stir in evaporated milk, vanilla, and the cooled syrup.\n3. Strain the mixture through a fine-mesh sieve to ensure a perfectly smooth custard.\n4. Press the puff pastry into a lightly greased muffin tin.\n5. Fill each shell 80% full with custard.\n6. Bake at 200°C (400°F) for 15–20 minutes until pastry is golden and custard is just set with a slight wobble. Serve warm.",
            "/photos/egg_tarts.jpg",
            new String[][]{{"puff pastry shells", "12", "pieces"}, {"hot water", "0.5", "cup"}, {"sugar", "0.3", "cup"}, {"eggs", "3", "pieces"}, {"evaporated milk", "0.5", "cup"}, {"vanilla extract", "0.5", "tsp"}});

        insertRecipe("Tangyuan", "Chinese", 25,
            "1. Gradually knead warm water into glutinous rice flour until you form a smooth, pliable dough. Cover and rest for 10 minutes.\n2. For the filling, mix black sesame paste with a small amount of softened butter to hold its shape.\n3. Pinch off a small piece of dough, flatten it in your palm, and place a teaspoon of filling in the center.\n4. Bring the edges together and roll gently into a smooth ball with no cracks.\n5. Bring water, ginger slices, and brown sugar to a boil to make the sweet soup.\n6. Drop the rice balls in and cook for 5–6 minutes until they float and are translucent. Serve hot.",
            "/photos/tangyuan.jpg",
            new String[][]{{"glutinous rice flour", "1.5", "cups"}, {"warm water", "0.5", "cup"}, {"black sesame paste", "0.5", "cup"}, {"water", "3", "cups"}, {"brown sugar", "0.25", "cup"}, {"ginger", "2", "pieces"}});

        insertRecipe("Mango Sago", "Chinese", 20,
            "1. Cook the tapioca pearls in boiling water for 12–15 minutes until fully translucent. Drain, rinse under cold water, and set aside.\n2. Peel and dice two mangoes. Blend them with coconut milk, evaporated milk, and condensed milk until completely smooth.\n3. Dice the remaining mango into small chunks.\n4. In a large bowl, combine the mango purée with the cooked tapioca pearls and diced mango pieces.\n5. Stir gently to incorporate.\n6. Refrigerate for at least 1–2 hours. Serve chilled.",
            "/photos/mango_sago.jpg",
            new String[][]{{"ripe mangoes", "3", "pieces"}, {"tapioca pearls", "0.5", "cup"}, {"coconut milk", "1", "cup"}, {"evaporated milk", "0.5", "cup"}, {"condensed milk", "3", "tbsp"}});
    }

    // Korean Recipes
    private static void seedKorean() {
        insertRecipe("Beef Bulgogi", "Korean", 15,
            "1. Slice the ribeye as thinly as possible across the grain (partially freezing helps).\n2. Make the marinade: combine soy sauce, brown sugar, sesame oil, minced garlic, grated ginger, and grated Asian pear.\n3. Add the beef, sliced onion, and green onions to the marinade. Mix well and refrigerate for at least 1 hour.\n4. Heat a large skillet or grill pan over medium-high heat with a little oil.\n5. Cook the beef in a single layer without overcrowding the pan.\n6. Cook for 2–3 minutes per side until caramelized. Garnish with sesame seeds and serve.",
            "/photos/beef_bulgogi.jpg",
            new String[][]{{"ribeye steak", "500", "grams"}, {"soy sauce", "4", "tbsp"}, {"brown sugar", "2", "tbsp"}, {"sesame oil", "1", "tbsp"}, {"garlic", "4", "cloves"}, {"asian pear", "0.5", "piece"}, {"onion", "0.5", "piece"}, {"green onions", "2", "stalks"}, {"sesame seeds", "1", "tbsp"}, {"cooking oil", "1", "tbsp"}});

        insertRecipe("Bibimbap", "Korean", 30,
            "1. Blanch the spinach, squeeze out water, and season with sesame oil and salt.\n2. Julienne and sauté the carrots and mushrooms separately in a lightly oiled pan with a pinch of salt.\n3. Cook the ground beef with soy sauce and a pinch of sugar until browned.\n4. Mix gochujang with sesame oil, sugar, and a splash of water for the sauce.\n5. Fry the eggs sunny-side up so the yolk is still runny.\n6. Add warm rice to a bowl, arrange the vegetables and beef on top, and place the egg in the center. Add gochujang sauce, mix vigorously, and eat.",
            "/photos/bibimbap.jpg",
            new String[][]{{"cooked white rice", "2", "cups"}, {"spinach", "1", "cup"}, {"carrots", "1", "cup"}, {"shiitake mushrooms", "1", "cup"}, {"ground beef", "150", "grams"}, {"eggs", "2", "pieces"}, {"gochujang", "2", "tbsp"}, {"soy sauce", "1", "tbsp"}, {"sesame oil", "2", "tbsp"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Kimchi Jjigae", "Korean", 35,
            "1. Heat sesame oil in a heavy pot over medium heat. Add the pork belly strips and cook until browned.\n2. Add well-fermented, sour kimchi and diced onion. Sauté together for 5–7 minutes.\n3. Stir in the gochugaru and gochujang.\n4. Pour in water (or anchovy broth) and bring to a boil. Reduce heat, cover, and simmer for 15 minutes.\n5. Arrange tofu slices on top and simmer for 5 more minutes.\n6. Adjust seasoning with fish sauce. Top with green onions and serve bubbling hot with rice.",
            "/photos/kimchi_jjigae.jpg",
            new String[][]{{"fermented kimchi", "2", "cups"}, {"pork belly", "200", "grams"}, {"tofu", "200", "grams"}, {"onion", "0.5", "piece"}, {"garlic", "3", "cloves"}, {"gochugaru", "1", "tbsp"}, {"gochujang", "1", "tbsp"}, {"water", "2", "cups"}, {"sesame oil", "1", "tbsp"}, {"green onions", "1", "stalk"}});

        insertRecipe("Tteokbokki", "Korean", 25,
            "1. If rice cakes are hard, soak them in warm water for 10–15 minutes until slightly softened.\n2. In a wide pan, combine anchovy broth, gochujang, gochugaru, sugar, and soy sauce. Bring to a boil.\n3. Add the rice cakes and stir gently. Cook for 5 minutes as the sauce begins to coat them.\n4. Add sliced fish cakes (eomuk) and chopped cabbage.\n5. Simmer for 5–7 minutes until the rice cakes are soft and chewy and the sauce is glossy and thick.\n6. Add hard-boiled eggs and green onions in the last minute. Serve immediately.",
            "/photos/tteokbokki.jpg",
            new String[][]{{"korean rice cakes", "400", "grams"}, {"fish cakes", "2", "sheets"}, {"cabbage", "1", "cup"}, {"gochujang", "3", "tbsp"}, {"gochugaru", "1", "tbsp"}, {"sugar", "1", "tbsp"}, {"soy sauce", "1", "tbsp"}, {"anchovy broth", "2.5", "cups"}, {"green onions", "2", "stalks"}, {"hard boiled eggs", "2", "pieces"}});

        insertRecipe("Japchae", "Korean", 40,
            "1. Boil sweet potato glass noodles for 6–7 minutes until tender. Rinse under cold water, drain, and cut into shorter lengths.\n2. Toss noodles with soy sauce, sesame oil, and sugar. Set aside.\n3. Blanch spinach, squeeze out water, and season with sesame oil.\n4. Sauté thinly sliced beef, julienned carrots, sliced onion, and shiitake mushrooms separately, seasoning each with salt.\n5. In a large bowl, combine the noodles, all cooked vegetables, and beef.\n6. Add remaining soy sauce, sugar, and sesame oil. Toss well by hand. Garnish with toasted sesame seeds.",
            "/photos/japchae.jpg",
            new String[][]{{"glass noodles", "200", "grams"}, {"beef", "100", "grams"}, {"spinach", "1", "cup"}, {"carrots", "0.5", "cup"}, {"onion", "0.5", "piece"}, {"shiitake mushrooms", "0.5", "cup"}, {"soy sauce", "4", "tbsp"}, {"sugar", "2", "tbsp"}, {"sesame oil", "2", "tbsp"}, {"sesame seeds", "1", "tbsp"}});

        insertRecipe("Kimbap", "Korean", 30,
            "1. Season warm cooked rice with sesame oil and salt. Let it cool slightly.\n2. Prepare fillings: thin omelet strips, sautéed julienned carrots, and seasoned blanched spinach.\n3. Lay a sheet of toasted seaweed (gim) on a bamboo mat, shiny-side down.\n4. Spread a thin layer of rice over the seaweed, leaving a bare strip at the top edge.\n5. Arrange a line of fillings (spinach, carrots, radish, beef, egg) across the lower third.\n6. Roll tightly over the fillings using the mat, pressing firmly to seal. Brush with sesame oil and slice into rounds.",
            "/photos/kimbap.jpg",
            new String[][]{{"sushi rice", "3", "cups"}, {"roasted seaweed", "4", "pieces"}, {"spinach", "1", "cup"}, {"carrots", "1", "cup"}, {"pickled radish", "4", "pieces"}, {"ground beef", "150", "grams"}, {"eggs", "2", "pieces"}, {"sesame oil", "2", "tbsp"}});

        insertRecipe("Haemul Pajeon", "Korean", 15,
            "1. Whisk together flour, cold water, and salt until smooth to make a crisp batter.\n2. Heat oil generously in a large non-stick pan over medium-high heat until hot.\n3. Arrange the scallions in a neat layer across the pan.\n4. Pour the batter evenly over the scallions to bind them, then distribute mixed seafood over the top.\n5. Crack an egg directly over the top and spread it slightly. Cook for 4–5 minutes until the bottom is deep golden.\n6. Flip carefully, press down with a spatula, and cook another 4–5 minutes. Serve with dipping sauce.",
            "/photos/pajeon.jpg",
            new String[][]{{"all purpose flour", "1", "cup"}, {"water", "1", "cup"}, {"scallions", "2", "cups"}, {"mixed seafood", "200", "grams"}, {"egg", "1", "piece"}, {"salt", "0.5", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Sundubu Jjigae", "Korean", 20,
            "1. Heat sesame oil in a small earthenware pot over medium heat. Add gochugaru and minced garlic, stirring for 1 minute until fragrant.\n2. Pour in the anchovy broth and bring to a boil.\n3. Add the mixed seafood (shrimp, clams, squid) and cook for 2–3 minutes.\n4. Carefully spoon the silken tofu directly into the stew in large chunks. Simmer for 3–4 minutes to heat through.\n5. Season with fish sauce. Top with green onions.\n6. Crack a raw egg into the boiling stew right before serving and let it set partially from the heat.",
            "/photos/sundubu.jpg",
            new String[][]{{"silken tofu", "400", "grams"}, {"anchovy broth", "1.5", "cups"}, {"mixed seafood", "150", "grams"}, {"gochugaru", "2", "tbsp"}, {"garlic", "3", "cloves"}, {"sesame oil", "1", "tbsp"}, {"green onions", "2", "stalks"}, {"egg", "1", "piece"}});

        insertRecipe("Galbi", "Korean", 30,
            "1. Rinse the beef short ribs in cold water and pat completely dry. Score the meat lightly.\n2. Make the marinade: blend Asian pear and onion, then mix with soy sauce, water, brown sugar, garlic, and sesame oil.\n3. Submerge the ribs in the marinade, cover, and refrigerate for at least 4 hours (overnight is best).\n4. Heat a grill or cast-iron pan over medium-high heat.\n5. Grill the ribs for 3–4 minutes per side until caramelized, slightly charred, and cooked through.\n6. Cut into pieces with scissors at the table and serve with lettuce wraps and ssamjang.",
            "/photos/galbi.jpg",
            new String[][]{{"beef short ribs", "1", "kg"}, {"soy sauce", "0.5", "cup"}, {"water", "0.5", "cup"}, {"brown sugar", "0.25", "cup"}, {"asian pear", "0.5", "piece"}, {"onion", "0.5", "piece"}, {"garlic", "4", "cloves"}, {"sesame oil", "1", "tbsp"}});

        insertRecipe("Dakgalbi", "Korean", 35,
            "1. Make the marinade: combine gochujang, soy sauce, sugar, and minced garlic.\n2. Toss the chicken thigh pieces in the marinade and let sit for at least 30 minutes.\n3. Heat oil in a large skillet over medium-high heat. Add the sweet potato pieces first and cook for 3–4 minutes.\n4. Add the marinated chicken, cabbage, and rice cakes.\n5. Stir-fry everything together for 10–15 minutes until the chicken is cooked, the rice cakes are chewy, and the sauce is caramelized.\n6. Serve directly from the pan with steamed rice.",
            "/photos/dakgalbi.jpg",
            new String[][]{{"chicken thighs", "500", "grams"}, {"cabbage", "2", "cups"}, {"sweet potato", "1", "piece"}, {"tteokbokki rice cakes", "1", "cup"}, {"gochujang", "3", "tbsp"}, {"soy sauce", "2", "tbsp"}, {"sugar", "1", "tbsp"}, {"garlic", "3", "cloves"}, {"cooking oil", "2", "tbsp"}});

        insertRecipe("Kimchi Fried Rice", "Korean", 15,
            "1. Use cold, day-old rice to prevent clumping.\n2. Heat a wok over medium-high heat. Cook bacon or SPAM until crispy and the fat has rendered. Do not drain.\n3. Add chopped kimchi and sauté in the fat for 3–4 minutes until softened.\n4. Add a few tablespoons of kimchi juice, then mix in the cold rice and gochujang.\n5. Stir-fry vigorously for 3–4 minutes until the rice takes on color and is well combined.\n6. Turn off the heat, drizzle with sesame oil, and serve topped with a fried egg and roasted seaweed.",
            "/photos/kimchi_fried_rice.jpg",
            new String[][]{{"cooked rice", "3", "cups"}, {"fermented kimchi", "1", "cup"}, {"kimchi juice", "3", "tbsp"}, {"bacon", "100", "grams"}, {"gochujang", "1", "tbsp"}, {"sesame oil", "1", "tbsp"}, {"fried egg", "1", "piece"}, {"roasted seaweed", "1", "tbsp"}});

        insertRecipe("Bossam", "Korean", 70,
            "1. In a large pot, combine water, chopped onion, garlic, ginger, doenjang, and a spoonful of instant coffee (to remove gaminess).\n2. Bring to a boil, then add the whole pork belly.\n3. Reduce heat, cover, and simmer for 60–70 minutes until very tender.\n4. Remove the pork, rest for 10 minutes, and slice thinly.\n5. Serve the sliced pork on a large plate surrounded by lettuce and perilla leaves.\n6. Wrap a piece of pork in a leaf with ssamjang, kimchi, and a slice of garlic, and eat in one bite.",
            "/photos/bossam.jpg",
            new String[][]{{"pork belly", "1", "kg"}, {"onion", "1", "piece"}, {"garlic", "6", "cloves"}, {"ginger", "3", "pieces"}, {"doenjang", "2", "tbsp"}, {"instant coffee powder", "1", "tsp"}, {"water", "8", "cups"}, {"lettuce", "1", "head"}});

        insertRecipe("Doenjang Jjigae", "Korean", 20,
            "1. Bring anchovy broth to a boil in a small pot.\n2. Dissolve the doenjang into the boiling broth by swirling it in a ladle. Do not over-stir.\n3. Add the diced potato and boil for 5 minutes.\n4. Add zucchini, tofu cubes, minced garlic, and sliced green chili.\n5. Simmer for 5–7 more minutes until all vegetables are tender.\n6. Adjust taste with doenjang or salt, garnish with green onions, and serve bubbling hot with rice.",
            "/photos/doenjang_jjigae.jpg",
            new String[][]{{"anchovy broth", "3", "cups"}, {"doenjang", "3", "tbsp"}, {"potato", "1", "piece"}, {"zucchini", "0.5", "piece"}, {"tofu", "200", "grams"}, {"garlic", "2", "cloves"}, {"green chili", "1", "piece"}, {"green onion", "1", "stalk"}});

        insertRecipe("Samgyeopsal", "Korean", 15,
            "1. Prepare the table with dipping bowls of sesame oil mixed with salt, plus lettuce leaves, ssamjang, and raw garlic cloves.\n2. Heat a grill pan or cast-iron skillet over medium-high heat. No oil needed.\n3. Lay the pork belly slices flat on the grill and cook the whole garlic cloves alongside them.\n4. Grill for 3–4 minutes per side until the fat is crispy and caramelized.\n5. Use kitchen scissors to cut the pork into bite-sized pieces directly on the grill.\n6. Wrap the pork in lettuce with garlic and ssamjang to eat.",
            "/photos/samgyeopsal.jpg",
            new String[][]{{"pork belly", "600", "grams"}, {"garlic cloves", "10", "pieces"}, {"lettuce leaves", "1", "head"}, {"sesame oil", "2", "tbsp"}, {"salt", "1", "tsp"}, {"black pepper", "0.5", "tsp"}, {"ssamjang", "3", "tbsp"}});

        insertRecipe("Gyeran-jjim", "Korean", 10,
            "1. Whisk the eggs vigorously until completely smooth. Stir in chicken broth and salt, then strain through a fine sieve.\n2. Pour into a small earthenware pot or saucepan and cook over medium-low heat.\n3. As the egg sets around the edges, scrape them toward the center every 30 seconds.\n4. When the egg is 80% set but slightly wobbly, reduce heat to the lowest setting.\n5. Cover with a lid and cook for 2 more minutes so the steam puffs the egg up.\n6. Remove from heat, drizzle with sesame oil, garnish with green onions, and serve immediately.",
            "/photos/gyeran_jjim.jpg",
            new String[][]{{"eggs", "4", "pieces"}, {"chicken broth", "1", "cup"}, {"salt", "0.5", "tsp"}, {"sesame oil", "1", "tsp"}, {"green onions", "1", "stalk"}, {"sesame seeds", "1", "tsp"}});

        insertRecipe("Naengmyeon", "Korean", 15,
            "1. Mix chilled beef broth and dongchimi (radish water kimchi) broth together. Season with salt and rice vinegar.\n2. Place in the freezer for 20–30 minutes until slushy.\n3. Cook buckwheat noodles in boiling water for 3–4 minutes until tender but chewy.\n4. Drain and vigorously rinse under ice-cold water to remove starch. Drain well.\n5. Form a neat mound of noodles in a bowl and pour the slushy broth over them.\n6. Top with Asian pear slices, julienned cucumber, and a hard-boiled egg. Serve with vinegar and mustard on the side.",
            "/photos/naengmyeon.jpg",
            new String[][]{{"buckwheat noodles", "200", "grams"}, {"beef broth", "3", "cups"}, {"dongchimi broth", "1", "cup"}, {"asian pear", "0.5", "piece"}, {"cucumber", "0.5", "piece"}, {"hard boiled egg", "1", "piece"}, {"vinegar", "2", "tbsp"}, {"korean mustard", "1", "tsp"}});

        insertRecipe("Jajangmyeon", "Korean", 30,
            "1. Heat oil in a wok over medium heat. Stir-fry the chunjang (black bean paste) for 2–3 minutes to remove bitterness, then set aside.\n2. In the remaining oil, fry the pork belly pieces until lightly browned.\n3. Add diced onion and zucchini, stir-frying for 4–5 minutes until softened.\n4. Stir the fried chunjang back in, add sugar, pour in water, and bring to a simmer.\n5. Stir in a cornstarch slurry and cook until the sauce is thick and glossy.\n6. Boil noodles, drain, and ladle the black bean sauce over them. Garnish with cucumber.",
            "/photos/jajangmyeon.jpg",
            new String[][]{{"udon noodles", "300", "grams"}, {"chunjang", "3", "tbsp"}, {"pork belly", "150", "grams"}, {"onion", "1", "piece"}, {"zucchini", "0.5", "piece"}, {"sugar", "1", "tbsp"}, {"water", "1", "cup"}, {"cornstarch", "2", "tbsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Hotteok", "Korean", 90,
            "1. Dissolve yeast in warm water. Mix with all-purpose and sweet rice flour to form a soft dough. Cover and let rise for 1 hour.\n2. Mix brown sugar, chopped walnuts, and cinnamon for the filling.\n3. Oil your hands. Pinch off a piece of dough, flatten it, and place a teaspoon of filling in the center.\n4. Seal the edges firmly into a ball.\n5. Cook seam-side down in an oiled pan for 1 minute. Use a spatula to firmly press it flat into a disc.\n6. Cook for 2–3 minutes per side until deep golden and crispy. Eat carefully as the filling will be hot.",
            "/photos/hotteok.jpg",
            new String[][]{{"all purpose flour", "1.5", "cups"}, {"sweet rice flour", "0.5", "cups"}, {"dry yeast", "1", "tsp"}, {"warm water", "1", "cup"}, {"brown sugar", "0.5", "cup"}, {"walnuts", "2", "tbsp"}, {"cinnamon powder", "1", "tsp"}, {"cooking oil", "3", "tbsp"}});

        insertRecipe("Patbingsu", "Korean", 10,
            "1. Prepare toppings: sweetened red beans, rice cake pieces (tteok), and fresh sliced fruit.\n2. Use a machine or powerful blender to shave ice into very fine, powdery snow.\n3. Pile the shaved ice generously into a large serving bowl, mounding it high.\n4. Place the sweetened red beans in the center of the mound.\n5. Arrange the rice cakes and fresh fruit around the beans.\n6. Drizzle generously with condensed milk and serve immediately, mixing as you eat.",
            "/photos/patbingsu.jpg",
            new String[][]{{"shaved ice", "3", "cups"}, {"sweetened red beans", "0.5", "cup"}, {"condensed milk", "0.25", "cup"}, {"korean rice cakes", "0.25", "cup"}, {"strawberries", "0.5", "cup"}});

        insertRecipe("Yakgwa", "Korean", 60,
            "1. Rub sesame oil into the pastry flour until it resembles fine breadcrumbs to keep the cookies tender.\n2. Mix honey, rice syrup, soju, and ginger juice. Drizzle over the flour and gently press together without kneading.\n3. Roll out dough to 8mm thickness and cut into flower shapes.\n4. Heat oil to a low 130°C (265°F). Fry the yakgwa slowly for 10–15 minutes until pale golden and floating.\n5. Make a syrup by warming honey and rice syrup.\n6. Submerge the warm yakgwa in the syrup for 2–4 hours (or overnight) to absorb the honey. Drain and cool.",
            "/photos/yakgwa.jpg",
            new String[][]{{"pastry flour", "2", "cups"}, {"sesame oil", "3", "tbsp"}, {"honey", "0.5", "cup"}, {"rice syrup", "0.5", "cup"}, {"soju", "2", "tbsp"}, {"ginger juice", "1", "tbsp"}, {"cooking oil", "3", "cups"}});
    }

    public static void main(String[] args) {
        System.out.println("Wiping old database records...");
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
             
             // THE FIX: Delete from the child tables first so they don't block the wipe!
             stmt.execute("DELETE FROM favorites");
             stmt.execute("DELETE FROM cooking_log");
             stmt.execute("DELETE FROM recipe_ingredients");
             stmt.execute("DELETE FROM recipes");
             
             System.out.println("Old data wiped successfully!");
        } catch (Exception e) {
             System.out.println("Error wiping data: " + e.getMessage());
        }

        System.out.println("Planting new seeds...");
        seedData();
        System.out.println("Done!");
    }
}