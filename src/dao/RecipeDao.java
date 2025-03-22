package dao;
import core.Helper;
import core.db;
import entity.Ingredient;
import entity.Recipe;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;

public class RecipeDao {
    private Connection connection;

    public RecipeDao() {
        this.connection = db.getInstance();
    }

    public Recipe findRecipe(String search) throws SQLException {
        Recipe recipe = null;
        String query = "SELECT * FROM recipe WHERE name LIKE ? ";
        PreparedStatement pr = this.connection.prepareStatement(query);
        pr.setString(1, "%" + search + "%");
        ResultSet rs = pr.executeQuery();
        while (rs.next()) {
            recipe = this.matchRecipe(rs);
        }
        
        return recipe;
    }

    public boolean RecipeSave(Recipe recipe, ArrayList<Integer> ingredientIds) {
        String query = "INSERT INTO recipe (name, category, preparation_time, instructions) VALUES(?,?,?,?)";
        String query2 = "INSERT INTO meal (recipe_id, ingredients_id) VALUES(?,?)";
        String query3 = "SELECT * FROM recipe WHERE name = ?";

        try {
            PreparedStatement pr3 = this.connection.prepareStatement(query3);
            pr3.setString(1, recipe.getName());
            ResultSet rs3 = pr3.executeQuery();

            if (!rs3.next()) {
                PreparedStatement pr = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                pr.setString(1, recipe.getName());
                pr.setString(2, recipe.getCategory().toString());
                pr.setString(3, recipe.getPreparation_time());
                pr.setString(4, recipe.getInstructions());

                int affectedRows = pr.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = pr.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            recipe.setId(generatedKeys.getInt(1));
                        }
                    }
                } else {
                    throw new SQLException("Recipe kaydı eklenemedi, ID alınamadı.");
                }

                for (int ingredientId : ingredientIds) {
                    PreparedStatement pr2 = this.connection.prepareStatement(query2);
                    pr2.setInt(1, recipe.getId());
                    pr2.setInt(2, ingredientId);
                    int result2 = pr2.executeUpdate();
                    
                    if (result2 <= 0) {
                        throw new SQLException("Ingredient ID " + ingredientId + " için meal kaydı eklenemedi.");
                    }
                }
 
                return true;

            } else {
                	Helper.showMessage("There is already this recipe name");
                	return  false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Recipe> findAllRecipe() throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        String query = "SELECT * FROM recipe";
        PreparedStatement pr = this.connection.prepareStatement(query);
        ResultSet rs = pr.executeQuery();

        while (rs.next()) {
            recipes.add(this.matchRecipe(rs));
        }
            return recipes;
    }

    public ArrayList<Recipe> findAllRecipeBySearch(String search) throws SQLException {
        ArrayList<Recipe> recipes = new ArrayList<>();
        String query = "SELECT * FROM recipe WHERE name LIKE ? ";
        PreparedStatement pr = this.connection.prepareStatement(query);
        pr.setString(1, "%" + search + "%");
        ResultSet rs = pr.executeQuery();

        while (rs.next()) {
            recipes.add(this.matchRecipe(rs));
        }
        
        if(recipes.size() == 0)
            return null;
        
        return recipes;
    }

    public  Recipe  matchRecipe (ResultSet rs) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setId(rs.getInt("id"));
        recipe.setName(rs.getString("name"));
        recipe.setCategory(Recipe.fromDatabaseValue(rs.getString("category")));
        recipe.setPreparation_time(rs.getString("preparation_time"));
        recipe.setInstructions(rs.getString("instructions"));
        return recipe;
    }

    public  Ingredient  matchIngredient (ResultSet rs) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setId(rs.getInt("id"));
        ingredient.setName(rs.getString("name"));
       // ingredient.setTotal_amount(Recipe.fromDatabaseValue(rs.getString("category")));
        //ingredient.setTotal_amount(Integer.parseInt(rs.getString("total_amount")));
        //ingredient.setUnit_price(rs.getString("instructions"));
        return ingredient;
    }
    
    public boolean updateRecipe(Recipe recipe)
    {
        String query="UPDATE recipe SET name = ?, category = ?, preparation_time = ?, instructions = ? WHERE id = ?";
        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setString(1, recipe.getName());
            pr.setString(2, recipe.getCategory().toString());
            pr.setString(3, recipe.getPreparation_time());
            pr.setString(4, recipe.getInstructions());
            pr.setInt(5, recipe.getId());
            return pr.executeUpdate() !=-1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return true;
    }

    public boolean deleteRecipe(int id)
    {
        String query="DELETE  FROM recipe WHERE id = ?";
        try {
            PreparedStatement pr =this.connection.prepareStatement(query);
            pr.setInt(1, id);
            return pr.executeUpdate() !=-1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public ArrayList<Recipe> query(String query)
    {
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            ResultSet rs = this.connection.createStatement().executeQuery(query);
            while (rs.next()) {
                recipes.add(this.matchRecipe(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }
    
    public Recipe findRecipeByID(int id) {
        Recipe recipe = null;

        String query = "SELECT * FROM recipe WHERE id=?";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            if (rs.next()) {
                recipe = this.matchRecipe(rs); 
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipe;
    }

    public ArrayList<Recipe> findRecipeByIngredientID(ArrayList<String> names) {

        ArrayList<Recipe> recipes = new ArrayList<>();
        String ingredientQuery = "SELECT id FROM ingredient WHERE name LIKE ?";
        String recipeQuery = "SELECT recipe_id FROM meal WHERE ingredients_id = ?";

        try {
            	HashSet<Integer> commonRecipeIds = null;

            for (String name : names) {
                PreparedStatement pr = this.connection.prepareStatement(ingredientQuery);
                pr.setString(1, name);
                ResultSet rs = pr.executeQuery();
                HashSet<Integer> currentIngredientRecipeIds = new HashSet<>();

                while (rs.next()) {
                    int ingredientId = rs.getInt("id");
                    PreparedStatement pr2 = this.connection.prepareStatement(recipeQuery);
                    pr2.setInt(1, ingredientId);
                    ResultSet rs2 = pr2.executeQuery();

                    while (rs2.next()) {
                        currentIngredientRecipeIds.add(rs2.getInt("recipe_id"));
                    }
                }

                if (commonRecipeIds == null) {
                    commonRecipeIds = currentIngredientRecipeIds;
                } else {
                    commonRecipeIds.retainAll(currentIngredientRecipeIds);
                }
            }

            if (commonRecipeIds != null) {
                for (int recipeId : commonRecipeIds) {
                    Recipe recipe = findRecipeByID(recipeId);
                    recipes.add(recipe);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
    }

    public ArrayList<Ingredient> findIngredientByRecipeID(int id) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String query = "SELECT * FROM recipe WHERE id=?";
        String query2 = "SELECT ingredients_id FROM meal WHERE recipe_id=?";
        String query3 = "SELECT * FROM ingredient WHERE id=?";

        try {
            PreparedStatement pr = this.connection.prepareStatement(query);
            pr.setInt(1, id);
            ResultSet rs = pr.executeQuery();
            Recipe recipe = null;
            if (rs.next()) {
                recipe = this.matchRecipe(rs);
            }

            PreparedStatement pr2 = this.connection.prepareStatement(query2);
            pr2.setInt(1, id);
            ResultSet rs2 = pr2.executeQuery();
            
            while (rs2.next()) {
                int resultIngredientID = rs2.getInt("ingredients_id");
                PreparedStatement pr3 = this.connection.prepareStatement(query3);
                pr3.setInt(1, resultIngredientID); 
                ResultSet rs3 = pr3.executeQuery();
                if (rs3.next()) {
                    Ingredient ingredient = this.matchIngredient(rs3); 
                    ingredients.add(ingredient); 
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

}
