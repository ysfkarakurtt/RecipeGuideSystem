package business;
import core.Helper;
import dao.RecipeDao;
import entity.Ingredient;
import entity.Recipe;

import java.sql.SQLException;
import java.util.ArrayList;

public class RecipeController {
private RecipeDao recipeDao = new RecipeDao();

 public Recipe findRecipe(String search) throws SQLException {
      return this.recipeDao.findRecipe(search);
 }

    public ArrayList<Recipe> findAllRecipe() throws SQLException {
        return this.recipeDao.findAllRecipe();
    }
    
    public ArrayList<Recipe> findAllRecipeBySearch(String search) throws SQLException {
        return this.recipeDao.findAllRecipeBySearch(search);
    }
    
    public ArrayList<Recipe> findRecipeByIngredientID(ArrayList<String> name) {
     return this.recipeDao.findRecipeByIngredientID(name);
    }
    
    public boolean saveRecipe(Recipe recipe,ArrayList<Integer> ingredient_id) {
     return this.recipeDao.RecipeSave(recipe,ingredient_id);
    }
    
    public Recipe findRecipeByID(int id) {
        return this.recipeDao.findRecipeByID(id);
    }
    
    public  ArrayList<Ingredient> findIngredientByRecipeID(int id){
            return this.recipeDao.findIngredientByRecipeID(id);
    }
    
    public boolean updateRecipe(Recipe recipe) {
        if(this.findRecipeByID(recipe.getId()) == null) {
            Helper.showMessage(recipe.getId() + " not found");
            return false;
        }
        
        return  this.recipeDao.updateRecipe(recipe);
    }
    
    public boolean deleteRecipe(int id) {
        if(this.findRecipeByID(id) == null) {
            Helper.showMessage(id + " not found");
            return false;
        }
        return  this.recipeDao.deleteRecipe(id);
    }

    public ArrayList<Recipe> filter(String search,Recipe.TYPE type,Integer rowID) {
 
        String query = "SELECT * FROM recipe";
        ArrayList<String> conditions = new ArrayList<>();

        if (search.length() > 0) {
            conditions.add("name LIKE '%" + search + "%'");
        }
        if (type != null) {
            conditions.add("category LIKE '%" + type.toString() + "%'");
        }

        if (conditions.size() > 0) {
            query += " WHERE " + String.join(" AND ", conditions);
        }

        if (rowID >= 0) {
            switch (rowID) {
                case 0:
                    break;
                case 1:
                    query += " ORDER BY preparation_time DESC";
                    break;
                case 2:
                    query += " ORDER BY preparation_time ASC";
                    break;
                case 3:        
                   query = "SELECT DISTINCT recipe_id FROM meal JOIN recipe ON meal.recipe_id = recipe.id ORDER BY meal.total_amount ASC";
                    break;
                case 4:
                    query = "SELECT DISTINCT recipe_id FROM meal JOIN recipe ON recipe.id = meal.recipe_id ORDER BY total_amount DESC";
                    break;
                default:
            }
        }

        return this.recipeDao.query(query);
}
}
