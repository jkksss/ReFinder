# 🍴 ReFinder — Recipe Finder System
### Powered by the Rabin-Karp String Matching Algorithm

---

## 📌 Project Overview
ReFinder is a Java-based desktop application that helps users find recipes based on the ingredients they currently have in their inventory. Using the **Rabin-Karp algorithm**, the system efficiently searches and matches ingredients from the user's inventory against recipe databases, providing real-time match percentages and missing ingredient suggestions.

This project was developed as a Final Project for **IT221 - Data Structures and Algorithms** at **Davao Del Norte State College, Institute of Computing**.

---

## 🎯 Features
- 🔍 **Smart Recipe Search** — Uses Rabin-Karp algorithm to match inventory ingredients against recipes
- 📊 **Match Percentage** — Shows how many ingredients you have for each recipe
- 🛒 **Missing Ingredients** — Tells you exactly what you're missing for a recipe
- 🧺 **Inventory Management** — Add, update, and remove ingredients from your inventory
- ⭐ **Favorites** — Bookmark recipes you love for quick access
- 📋 **Cooking Log** — Track your cooking history with date and serving size
- ⚠️ **Low Stock Warning** — Get notified when ingredients are running low
- 🌏 **60 Pre-loaded Recipes** — 20 Filipino, 20 Chinese, and 20 Korean recipes

---

## 🛠️ Tech Stack
| Component | Technology |
|---|---|
| Language | Java 21 (LTS) |
| GUI Framework | Java Swing + FlatLaf 3.7.1 |
| Database | SQLite 3.53.0 |
| Build Tool | Apache Maven |
| IDE | Apache NetBeans 28 |
| Algorithm | Rabin-Karp String Matching |

---

## 📁 Project Structure
```
ReFinder/
├── src/main/java/linaer_algo/refinder/
│   ├── ReFinder.java                  ← Entry point
│   ├── algorithm/
│   │   └── RabinKarp.java             ← Core algorithm
│   ├── database/
│   │   ├── DatabaseConnection.java    ← SQLite connection
│   │   ├── DataSeeder.java            ← 60 pre-loaded recipes
│   │   ├── IngredientDAO.java         ← Inventory DB operations
│   │   ├── RecipeDAO.java             ← Recipe DB operations
│   │   └── CookingLogDAO.java         ← History DB operations
│   ├── model/
│   │   ├── Ingredient.java            ← Ingredient model
│   │   ├── Recipe.java                ← Recipe model
│   │   └── CookingLog.java            ← Cooking log model
│   └── ui/
│       └── MainFrame.java             ← Main GUI window
├── photos/                            ← Recipe photos
├── refinder.db                        ← SQLite database (auto-generated)
├── pom.xml                            ← Maven dependencies
└── README.md
```

---

## ⚙️ How to Run

### Prerequisites
- Java 21 LTS installed
- Apache NetBeans 28
- Maven (included with NetBeans)

### Steps
1. Clone the repository:
```bash
git clone https://github.com/jkksss/ReFinder.git
```
2. Open the project in **Apache NetBeans**
3. Right click the project → **Clean and Build**
4. Press **F6** to run
5. The database and all 60 recipes will be automatically generated on first run ✅

---

## 🗄️ Database Schema
```
ingredients         ← User's inventory
recipes             ← All 60 pre-loaded recipes
recipe_ingredients  ← Ingredients needed per recipe
cooking_log         ← User's cooking history
favorites           ← User's favorited recipes
shopping_list       ← Missing ingredients list
```

---

## 🧠 Algorithm — Rabin-Karp
The Rabin-Karp algorithm is used to match ingredient names from the user's inventory against recipe ingredient lists.

### How It Works
1. Converts ingredient strings into hash numbers using a rolling hash formula
2. Slides a window across the text comparing hashes
3. Verifies character-by-character when hashes match
4. Uses space padding to prevent false matches (e.g. "salt" ≠ "saltine")
5. Uses ASCII values to handle spaces in multi-word ingredients

### Time Complexity
| Case | Complexity |
|---|---|
| Average | O(n + m) |
| Worst | O(nm) |

Where `n` = length of text, `m` = length of pattern

---

## 🍽️ Pre-loaded Recipes

### 🇵🇭 Filipino (20)
Chicken Adobo, Sinigang na Baboy, Pancit Canton, Beef Caldereta, Chicken Tinola, Kare-Kare, Lechon Kawali, Bistek Tagalog, Pork Barbecue, Tortang Talong, Chicken Inasal, Bicol Express, Lumpiang Shanghai, Pork Sisig, Pinakbet, Leche Flan, Turon, Buko Pandan, Halo-Halo, Mango Float, Pancit Palabok, Beef Tapa

### 🇨🇳 Chinese (20)
Kung Pao Chicken, Mapo Tofu, Beef and Broccoli, Sweet and Sour Pork, Egg Fried Rice, Char Siu, Chow Mein, Hot and Sour Soup, Pork Dumplings, Scallion Pancakes, Wonton Soup, Dan Dan Noodles, Tomato Egg Stir-fry, Vegetable Spring Rolls, Hong Shao Rou, Steamed Fish, Congee, Egg Tarts, Tangyuan, Mango Sago

### 🇰🇷 Korean (20)
Beef Bulgogi, Bibimbap, Kimchi Jjigae, Tteokbokki, Japchae, Kimbap, Haemul Pajeon, Sundubu Jjigae, Galbi, Dakgalbi, Kimchi Fried Rice, Bossam, Doenjang Jjigae, Samgyeopsal, Gyeran-jjim, Naengmyeon, Jajangmyeon, Hotteok, Patbingsu, Yakgwa

---

## 👥 Team
| Role | Responsibility |
|---|---|
| System Developer | Java application, algorithm, database |
| Manuscript Lead | IEEE paper writing |
| Video Lead | Recording and editing |

**Institution:** Davao Del Norte State College
**Course:** IT221 - Data Structures and Algorithms
**Academic Year:** 2025-2026

---

## 📄 License
This project was created for academic purposes only.

---

*ReFinder — Find recipes with what you have!* 🍴
