package business;
import dao.IngredientDao;
import dao.RecipeDao;
import entity.Ingredient;

import java.util.ArrayList;

public class IngredientController {
    IngredientDao ingredientDao = new IngredientDao();

    public  boolean saveUnit(Ingredient ingredient) {
        return this.ingredientDao.saveUnit(ingredient);
    }
    
    public  int saveAmount(Ingredient ingredient){
        return this.ingredientDao.saveAmount(ingredient);
    }
    
    public boolean updateIngredient(Ingredient ingredient){
        return this.ingredientDao.updateIngredient(ingredient);
    }
    
    public  ArrayList<Ingredient> fetchIngredients() {
       return this.ingredientDao.fetchIngredient();
    }
    
    public ArrayList<Ingredient> fetchIngredientDistinct() {
        return this.ingredientDao.fetchIngredientDistinct();
    }
    
    public boolean saveIngredient(Ingredient ingredient) {
        return this.ingredientDao.saveIngredient(ingredient);
    }
}
