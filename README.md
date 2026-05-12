# ReFinder

## Recipe Finder System

**ReFinder** is a Java desktop application that helps users reduce food waste by matching their current kitchen inventory with an existing recipe database.

Built with the Rabin-Karp string matching algorithm, ReFinder calculates recipe match percentages, highlights missing ingredients, and supports 61 curated recipes across Filipino, Chinese, and Korean cuisine.

---

## 🚀 Key Features

- **Smart Recipe Search** - Uses Rabin-Karp text matching to compare inventory items with recipe ingredients.
- **Match Percentage** - Shows how close you are to cooking each recipe.
- **Missing Ingredients** - Displays the exact items needed to complete a recipe.
- **Inventory Management** - Add, update, delete, and view ingredients with quantities.
- **Favorites & Cookbook** - Bookmark recipes for quick access.
- **Cooking Log** - Stores meal history with date, serving size, and automatic inventory updates.
- **HD Visuals** - Recipe images are included and served from the SQLite backend.

---

## 🧰 Technology Stack

| Component | Technology |
|-----------|------------|
| Language | Java 21 (LTS) |
| UI | Java Swing + FlatLaf 3.7.1 |
| Database | SQLite 3.53.0 (JDBC) |
| Build | Apache Maven |
| Algorithm | Rabin-Karp String Matching |

---

## 📁 Project Structure

```text
ReFinder/
├── src/main/java/linaer_algo/refinder/
│   ├── ReFinder.java                 ← App entry point & UI launcher
│   ├── algorithm/
│   │   └── RabinKarp.java            ← Core matching logic
│   ├── database/
│   │   ├── DatabaseConnection.java   ← SQLite connection manager
│   │   ├── DataSeeder.java           ← Recipe + image seeding logic
│   │   └── ...                       ← DAOs for Ingredient, Recipe, CookingLog
│   ├── model/
│   │   └── ...                       ← Data models (Ingredient, Recipe, etc.)
│   └── ui/
│       └── MainFrame.java            ← Main graphical interface
├── src/main/resources/photos/        ← HD recipe images
├── refinder.db                       ← SQLite database file
├── pom.xml                           ← Maven dependencies and build config
└── README.md
```

---

## ▶️ How to Run

### Run the Executable (Recommended)
1. Place `refinder.db` in the same folder as the JAR.
2. Open a terminal in that folder.
3. Run:

```bash
java -jar ReFinder-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Run from Source (NetBeans)
1. Clone the repository:
   ```bash
git clone https://github.com/jkksss/ReFinder.git
```
2. Open the project in Apache NetBeans.
3. Right-click the project → `Clean and Build`.
4. Run the project using `F6`.

---

## 🧠 Algorithm: Rabin-Karp

ReFinder uses the Rabin-Karp string matching algorithm to compare user inventory items against recipe ingredient lists.

- **Rolling hash**: Converts text into hash values for fast matching.
- **Collision handling**: Verifies matches character-by-character only when hash values match.
- **Substring safety**: Prevents false matches such as `salt` matching `saltine`.
- **Efficiency**: Average runtime complexity is `O(n + m)`, where `n` is the text length and `m` is the pattern length.

---

## 🗄️ Database Schema

The application uses SQLite with the following primary tables:

- `ingredients` — Tracks current kitchen stock.
- `recipes` — Stores recipe names, cook times, instructions, and photo paths.
- `recipe_ingredients` — Links recipes to required ingredients.
- `cooking_log` — Saves meal history and inventory deductions.
- `favorites` — Stores user-bookmarked recipes.

---

## 👤 Team

- **Joko Roman** — System Developer
- **Institution**: Davao Del Norte State College
- **Course**: IT221 - Data Structures and Algorithms
- **Academic Year**: 2025-2026

---

## 💡 Tagline

**ReFinder — Find recipes with what you already have.**
