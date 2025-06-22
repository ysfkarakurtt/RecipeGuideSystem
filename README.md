# Recipe Guide System

## Description

* Recipe Guide is a smart desktop application designed to help users manage recipes and available ingredients effectively. It offers features like recipe suggestion based on available materials, dynamic search, and detailed cost analysis of missing ingredients.
---

## Features

* **Recipe Management**: Add, edit, delete, and list recipes.
* **Material Management**: Add and manage ingredients with quantity, units, and unit cost.
* **Smart Recipe Suggestions**: Suggest dishes that can be prepared based on available ingredients.
* **Dynamic Search**: Search recipes by name or ingredients.
* **Filtering & Sorting**:

  * Filter by recipe category, preparation time, number of ingredients, or total cost.
  * Sort by cost or preparation time.
* **Visual Feedback**:

  * Highlight recipes that are fully matchable with user's pantry.
  * Show missing items and estimated additional cost.
* **Validation**:

  * Prevent duplicate recipe entries.

---

## Technologies Used

* **Frontend / UI**: Java Swing
* **Backend**: MySQL
* **Database Design**: Normalized relational schema with at least 3 core tables:

  * `Recipes`
  * `Ingredients`
  * `RecipeIngredients` (many-to-many)

---

## How It Works

1. User adds ingredients they currently have at home.
2. Application suggests recipes that can be fully or partially made.
3. For partial matches, application lists missing items and their estimated cost.
4. Recipes can be browsed, filtered, and searched.
5. Each recipe displays preparation time, category, instructions, and total cost.

---

## Requirements

* At least **5 recipe categories**
* Minimum **10 recipes per category**
* Minimum **3 recipes that require identical ingredients**
* GUI must allow category-based filtering and sorting by time/cost
* Matching percentage calculation based on available ingredients

---

## Sample Screenshots

<p align="center">
<img align="center" alt="coding" width="600" src="https://github.com/user-attachments/assets/9d5d11b1-2349-44aa-8240-a4dfe56ad454">
</p>

---



